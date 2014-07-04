define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("BuscarCuentaBancariaController", ["$scope", "$state","focus","CuentaBancariaService","VariablesService",
        function($scope, $state,focus, CuentaBancariaService, VariablesService) {

            $scope.focusElements = {
                filterText: 'focusFilterText'
            };
            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus($scope.focusElements.filterText);
            };
            $scope.setInitialFocus();

            //variables para busqueda de cuentas
            $scope.tipoCuentasBancarias = VariablesService.getTipoCuentasBancarias();
            $scope.tipoPersonas = VariablesService.getTipoPersonas();
            $scope.tipoEstadoCuenta = VariablesService.getEstadosCuentaBancaria();

            $scope.nuevo = function(){
                $state.transitionTo("app.socio.crearCuentaBancaria");
            };

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
                CuentaBancariaService.getCuentasBancariasView($scope.tipoCuentasBancarias, $scope.tipoPersonas, $scope.tipoEstadoCuenta, $scope.getDesde(), $scope.getHasta()).then(function(data){
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
                    CuentaBancariaService.findByFilterTextView(ft, $scope.tipoCuentasBancarias, $scope.tipoPersonas, $scope.tipoEstadoCuenta, $scope.getDesde(), $scope.getHasta()).then(function (data){
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
                        });
                    } else {
                        CuentaBancariaService.getCuentasBancariasView($scope.tipoCuentasBancarias, $scope.tipoPersonas, $scope.tipoEstadoCuenta, $scope.getDesde(), $scope.getHasta()).then(function(data){
                            $scope.cuentasList = data;
                            $scope.setPagingData($scope.cuentasList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                    }
                },true);

            $scope.gridOptions = {
                data: 'cuentasList',
                multiSelect: false,
                enablePaging: true,
                showFooter: true,
                totalServerItems: 'totalServerItems',
                pagingOptions: $scope.pagingOptions,
                filterOptions: $scope.filterOptions,
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

            $scope.editCuenta = function(row){
                $state.transitionTo("app.socio.editarCuentaBancaria", { id: row.id });
            };
        }]);
});