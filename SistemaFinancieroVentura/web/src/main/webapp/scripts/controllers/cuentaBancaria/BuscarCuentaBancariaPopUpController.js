define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("BuscarCuentaBancariaPopUpController", ["$scope","$timeout","$modalInstance","$state","focus", "CuentaBancariaService","VariablesService",
        function($scope,$timeout, $modalInstance, $state,focus, CuentaBancariaService, VariablesService) {

            $scope.focusElements = {
                filterText: 'focusFilterText'
            };
            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                $timeout(function() {
                    focus($scope.focusElements.filterText);
                }, 100);
            };
            $scope.setInitialFocus();

            $scope.tipoCuentasBancarias = [];
            $scope.tipoPersonas = VariablesService.getTipoPersonas();;
            $scope.tipoEstadoCuenta = [];
            $scope.tipoEstadoCuenta.push(VariablesService.getEstadoBancarioActivo());
            $scope.tipoCuentasBancarias.push(VariablesService.getAhorro());
            $scope.tipoCuentasBancarias.push(VariablesService.getCorriente());
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
                $scope.pagingOptions.currentPage = 1;
                CuentaBancariaService.getCuentasBancariasView($scope.tipoCuentasBancarias, $scope.tipoPersonas, $scope.tipoEstadoCuenta,$scope.getDesde(), $scope.getHasta()).then(function(data){
                    $scope.cuentasList = data;
                    $scope.setPagingData($scope.cuentasList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                });
                CuentaBancariaService.count().then(function(data){
                    $scope.totalServerItems = data;
                });
            };
            $scope.getPagedDataInitial();

            $scope.getPagedDataSearched = function () {
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
                $scope.setInitialFocus();
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
                    {field:"tipoCuenta", displayName:'TIPO CTA', width:90},
                    {field:"numeroCuenta", displayName:'NUMERO CUENTA', width:120},
                    {field:"tipoDocumento", displayName:'TIPO DOC', width:75},
                    {field:"numeroDocumento", displayName:'Nº DOCUMENTO', width:100},
                    {field:"socio", displayName:'SOCIO'},
                    {field:"moneda.simbolo", displayName:'MONEDA', width:60},
                    {field:"estadoCuenta", displayName:'ESTADO', width:70},
                    {displayName: 'Select', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="selectCuenta(row.entity)"><span class="glyphicon glyphicon-share"></span>Select</button></div>',  width:70}
                ]
            };

            $scope.updateGridLayout = function(){
                gridLayoutPlugin.updateGridLayout();
            }
            setTimeout(function () {
                $scope.updateGridLayout();
            }, 500);

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
