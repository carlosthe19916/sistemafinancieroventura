define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller("BuscarTransaccionHistorialController", ["$scope", "$state", "ngProgress","focus", "CajaSessionService",
        function($scope, $state, ngProgress,focus, CajaSessionService) {

            ngProgress.color("#2d6ca2");
            focus("firstFocus");

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
                setTimeout(function () {
                    $scope.pagingOptions.currentPage = 1;
                    CajaSessionService.getHistorialTransaccion().then(function(data){
                        $scope.historialList = data;
                        $scope.setPagingData($scope.historialList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                    });
                }, 100);
            };
            $scope.getPagedDataInitial();
            $scope.getPagedDataAsync = function (pageSize, page, searchText) {
                setTimeout(function () {
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
                    {field:"tipoTransaccion", displayName:'TIPO TRANS.', width:120},
                    {field:"id.numeroOperacion", displayName:'NUM.OPERACION', width:125},
                    {field:"moneda", displayName:'MONEDA', width:100},
                    {field:"monto", displayName:'MONTO', width:130},
                    {field:"fecha | date : 'dd/MM/yyyy'", displayName:'FECHA',width:100},
                    {field:"hora | date : 'HH:mm:ss'", displayName:'HORA',width:100},
                    {displayName: 'ESTADO', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estado">ACTIVO</span><span ng-hide="row.entity.estado">EXTORNADO</span></div>',width:90},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button>&nbsp;<button type="button" class="btn btn-danger btn-xs" ng-click="extornar(row.entity)"><span class="glyphicon glyphicon-remove"></span>Extornar</button></div>',width:160}
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