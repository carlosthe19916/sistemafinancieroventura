define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller("BuscarTransaccionHistorialController", ["$scope", "$state", "ngProgress","focus", "CajaSessionService",
        function($scope, $state, ngProgress,focus, CajaSessionService) {

            $scope.focusElements = {
                filterText: 'focusFilterText'
            };
            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus($scope.focusElements.filterText);
            };
            $scope.setInitialFocus();

            $scope.nuevo = function(){
                $state.transitionTo("app.socio.crearCuentaBancaria");
            }

            $scope.historialList = [];
            $scope.historialListFilter = [];

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
                var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
                $scope.historialListFilter = pagedData;
                $scope.totalServerItems = data.length;
                if (!$scope.$$phase) {
                    $scope.$apply();
                }
            };
            $scope.getPagedDataInitial = function () {
                $scope.pagingOptions.currentPage = 1;
                CajaSessionService.getHistorialTransaccion().then(function(data){
                    $scope.historialList = data;
                    $scope.setPagingData($scope.historialList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                });
            };
            $scope.getPagedDataInitial();
            $scope.getPagedDataAsync = function (pageSize, page, searchText) {
                var data;
                if (searchText) {
                    var ft = searchText.toLowerCase();
                    data = $scope.historialList.filter(function(item) {
                        return JSON.stringify(item).toLowerCase().indexOf(ft) != -1;
                    });
                    $scope.setPagingData(data,page,pageSize);
                } else {
                    $scope.setPagingData($scope.historialList,page,pageSize);
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
                        CajaSessionService.getHistorialTransaccion().then(function (data){
                            $scope.historialList = data;
                            $scope.setPagingData($scope.historialList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                    } else {
                        CajaSessionService.getHistorialTransaccion().then(function(data){
                            $scope.historialList = data;
                            $scope.setPagingData($scope.historialList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                    }
                },true);

            $scope.$watch('filterOptions', function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
                }
            }, true);

            var gridLayoutPlugin = new ngGridLayoutPlugin();
            $scope.gridOptions = {
                data: 'historialListFilter',
                multiSelect: false,
                enablePaging: true,
                showFooter: true,
                totalServerItems: 'totalServerItems',
                pagingOptions: $scope.pagingOptions,
                filterOptions: $scope.filterOptions,
                plugins: [gridLayoutPlugin],
                columnDefs: [
                    {field:"idTransaccion", displayName:'TRANS.', width:70},
                    {field:"tipoTransaccion", displayName:'TIPO TRANS.', width:90},
                    {field:"numeroOperacion", displayName:'Nº OP.', width:60},
                    {field:"moneda", displayName:'MONEDA'},
                    {field:"monto", displayName:'MONTO', width:120},
                    {field:"fecha | date : 'dd/MM/yyyy'", displayName:'FECHA', width:80},
                    {field:"hora | date : 'HH:mm:ss'", displayName:'HORA', width:70},
                    {displayName: 'ESTADO', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estado">ACTIVO</span><span ng-hide="row.entity.estado">EXTORNADO</span></div>', width:70},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button>&nbsp;<button type="button" class="btn btn-danger btn-xs" ng-click="extornar(row.entity)"><span class="glyphicon glyphicon-remove"></span>Extornar</button></div>', width:150}
                ]
            };
            $scope.updateGridLayout = function(){
                gridLayoutPlugin.updateGridLayout();
            }

            $scope.getVoucher = function(row){
                $state.transitionTo("app.socio.editarCuentaBancaria", { id: row.id });
            }
            $scope.extornar = function(row){
                $state.transitionTo("app.socio.editarCuentaBancaria", { id: row.id });
            }
        }]);
});