define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("BuscarCuentaBancariaPopUpController", ["$scope","$modalInstance", "$state","focus", "CuentaBancariaService","VariablesService",
        function($scope, $modalInstance, $state,focus, CuentaBancariaService, VariablesService) {

            $scope.tipoCuentasBancarias = VariablesService.getTipoCuentasBancarias();;
            $scope.tipoPersonas = VariablesService.getTipoPersonas();;
            $scope.tipoEstadoCuenta = [];
            $scope.tipoEstadoCuenta.push(VariablesService.getEstadoBancarioActivo());

            //configurar tabla
            $scope.cuentasList = [];

            $scope.filterOptions = {
                filterText: "",
                useExternalFilter: true
            };
            $scope.totalServerItems = 0;
            $scope.pagingOptions = {
                pageSizes: [10, 20, 40],
                pageSize: 10,
                currentPage: 1
            };
            $scope.setPagingData = function(data, page, pageSize){
                $scope.cuentasList = data;
                if (!$scope.$$phase) {
                    $scope.$apply();
                }
            };
            $scope.getDesde = function(){
                return ($scope.pagingOptions.pageSize*$scope.pagingOptions.currentPage)-$scope.pagingOptions.pageSize;
            };
            $scope.getHasta = function(){
                return $scope.pagingOptions.pageSize;
            };

            $scope.getPagedDataInitial = function () {
                setTimeout(function () {
                    $scope.pagingOptions.currentPage = 1;
                    CuentaBancariaService.getCuentasBancariasView($scope.tipoCuentasBancarias, $scope.tipoPersonas, $scope.tipoEstadoCuenta,$scope.getDesde(), $scope.getHasta()).then(function(data){
                        $scope.cuentasList = data;
                        $scope.setPagingData($scope.cuentasList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                    });
                    CuentaBancariaService.count().then(function(data){
                        $scope.totalServerItems = data;
                    });
                }, 100);
            };
            $scope.getPagedDataInitial();

            $scope.getPagedDataSearched = function () {
                setTimeout(function () {
                    if ($scope.filterOptions.filterText) {
                        var ft = $scope.filterOptions.filterText.toUpperCase();
                        CuentaBancariaService.findByFilterTextView(ft, $scope.tipoCuentasBancarias, $scope.tipoPersonas, $scope.tipoEstadoCuenta,$scope.getDesde(), $scope.getHasta()).then(function (data){
                            $scope.cuentasList = data;
                            $scope.setPagingData($scope.cuentasList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                        CuentaBancariaService.count().then(function(data){
                            $scope.totalServerItems = data;
                        });
                    } else {
                        $scope.getPagedDataInitial();
                    }
                }, 100);
            };

            $scope.$watch(
                function () {
                    return {
                        currentPage: $scope.pagingOptions.currentPage,
                        pageSize: $scope.pagingOptions.pageSize
                    };
                },
                function (newVal, oldVal) {
                    if (newVal.pageSize !== oldVal.pageSize) {
                        $scope.pagingOptions.currentPage = 1;
                    }
                    if ($scope.filterOptions.filterText) {
                        var ft = $scope.filterOptions.filterText.toUpperCase();
                        CuentaBancariaService.findByFilterTextView(ft, $scope.tipoCuentasBancarias, $scope.tipoPersonas, $scope.tipoEstadoCuenta, $scope.getDesde(), $scope.getHasta()).then(function (data){
                            $scope.cuentasList = data;
                            $scope.setPagingData($scope.cuentasList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                            $scope.cuentasListAuxiliar = angular.copy(data);
                        });
                    } else {
                        CuentaBancariaService.getCuentasBancariasView($scope.tipoCuentasBancarias, $scope.tipoPersonas, $scope.tipoEstadoCuenta, $scope.getDesde(), $scope.getHasta()).then(function(data){
                            $scope.cuentasList = data;
                            $scope.setPagingData($scope.cuentasList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                    }
                },true);

            var gridLayoutPlugin = new ngGridLayoutPlugin();
            $scope.gridOptions = {
                data: 'cuentasList',
                selectedItems: [],
                multiSelect: false,
                enablePaging: true,
                showFooter: true,
                totalServerItems: 'totalServerItems',
                pagingOptions: $scope.pagingOptions,
                filterOptions: $scope.filterOptions,
                plugins: [gridLayoutPlugin],
                columnDefs: [
                    {field:"tipoCuenta", displayName:'TIPO CTA.', width:80},
                    {field:"numeroCuenta", displayName:'NUMERO CUENTA', width:135},
                    {field:"tipoDocumento", displayName:'T.DOC.', width:60},
                    {field:"numeroDocumento", displayName:'NUMERO',width:100},
                    {field:"socio", displayName:'SOCIO',width:220},
                    {field:"moneda.denominacion", displayName:'MONEDA'},
                    {field:"estadoCuenta", displayName:'ESTADO',width:80},
                    {displayName: 'Select', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="selectCuenta(row.entity)"><span class="glyphicon glyphicon-share"></span>Select</button></div>'}
                ]
            };

            $scope.updateGridLayout = function(){
                gridLayoutPlugin.updateGridLayout();
            }
            setTimeout(function () {
                $scope.updateGridLayout();
                angular.element(document.querySelector('#txtBuscarCuenta')).focus();
            }, 100);

            $scope.selectCuenta = function(row){
                $scope.cuentaSelected = row;
                $scope.ok();
            }

            $scope.ok = function () {
                var cta = $scope.gridOptions.selectedItems[0];
                if(cta !== undefined && cta !== null){
                    $scope.cuentaSelected = cta;
                }
                if ($scope.cuentaSelected !== undefined && $scope.cuentaSelected !== null) {
                    $modalInstance.close($scope.cuentaSelected);
                }
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        }]);
});