define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller("BuscarTransaccionController", ["$scope", "$state", "ngProgress","focus", "CuentaBancariaService","VariablesService",
        function($scope, $state, ngProgress,focus, CuentaBancariaService, VariablesService) {

            //variables para busqueda de cuentas
            $scope.tipoCuentasBancarias = VariablesService.getTipoCuentasBancarias();
            $scope.tipoPersonas = VariablesService.getTipoPersonas();
            $scope.tipoEstadoCuenta = VariablesService.getEstadosCuentaBancaria();

            ngProgress.color("#2d6ca2");
            focus("firstFocus");

            $scope.nuevo = function(){
                $state.transitionTo("app.socio.crearCuentaBancaria");
            }

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
            }
            $scope.getHasta = function(){
                return ($scope.pagingOptions.pageSize*$scope.pagingOptions.currentPage);
            }
            $scope.getPagedDataInitial = function () {
                setTimeout(function () {
                    $scope.pagingOptions.currentPage = 1;
                    CuentaBancariaService.getCuentasBancariasView($scope.getDesde(), $scope.getHasta(), $scope.tipoCuentasBancarias, $scope.tipoPersonas, $scope.tipoEstadoCuenta).then(function(data){
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
                        CuentaBancariaService.findByFilterTextView(ft, $scope.getDesde(), $scope.getHasta(), $scope.tipoCuentasBancarias, $scope.tipoPersonas, $scope.tipoEstadoCuenta).then(function (data){
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
                        CuentaBancariaService.findByFilterTextView(ft, $scope.getDesde(), $scope.getHasta(), $scope.tipoCuentasBancarias, $scope.tipoPersonas, $scope.tipoEstadoCuenta).then(function (data){
                            $scope.cuentasList = data;
                            $scope.setPagingData($scope.cuentasList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                    } else {
                        CuentaBancariaService.getCuentasBancariasView($scope.getDesde(), $scope.getHasta(), $scope.tipoCuentasBancarias, $scope.tipoPersonas, $scope.tipoEstadoCuenta).then(function(data){
                            $scope.cuentasList = data;
                            $scope.setPagingData($scope.cuentasList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                    }
                },true);

            var gridLayoutPlugin = new ngGridLayoutPlugin();
            $scope.gridOptions = {
                data: 'cuentasList',
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
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="editCuenta(row.entity)"><span class="glyphicon glyphicon-share"></span>Editar</button></div>'}
                ]
            };
            $scope.updateGridLayout = function(){
                gridLayoutPlugin.updateGridLayout();
            }

            $scope.editCuenta = function(row){
                $state.transitionTo("app.socio.editarCuentaBancaria", { id: row.id });
            }
        }]);
});