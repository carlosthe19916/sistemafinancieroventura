
angular.module('cajaApp.controller')
    .controller('CrearTransaccionDepositoRetiroController', ["$scope", "$state", "$filter", "$modal", "CuentaBancariaService", "CajaSessionService", "ngProgress",
        function($scope, $state, $filter, $modal, CuentaBancariaService, CajaSessionService, ngProgress) {

            $scope.control = {"success":false, "inProcess": false, "submitted":false};

            $scope.cuentabancaria;
            $scope.monto;
            $scope.boveda;
            $scope.search;

            CajaSessionService.getBovedasOfCurrentCaja().then(function(bovedas){
                $scope.bovedas = bovedas;
            });

            $scope.openCaulculadora = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'modules/common/views/util/calculadora.html',
                    controller: "CalculadoraController",
                    
                });

                modalInstance.result.then(function (total) {
                    $scope.monto = total;
                }, function () {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };
            
            
            
            
            
            
            $scope.cuentabancariaList = [];

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
                $scope.cuentabancariaList = pagedData;
                $scope.totalServerItems = data.length;
                if (!$scope.$$phase) {
                    $scope.$apply();
                }
            };

            $scope.getPagedDataAsync = function (pageSize, page, searchText) {
                setTimeout(function () {
                    var data;
                    if (searchText) {
                        var ft = searchText.toUpperCase();
                        data = $scope.cuentabancariaList.filter(function(item) {
                            return JSON.stringify(item).toUpperCase().indexOf(ft) != -1;
                        });
                        $scope.setPagingData(data,page,pageSize);

                    } else {
                        if( angular.isUndefined($scope.filterOptions.filterText) || $scope.filterOptions.filterText === null) {
                            var result = $scope.cuentabancariaList = CuentaBancariaService.getCuentasBancariasView();
                            $scope.setPagingData(result,page,pageSize);
                        } else {
                            $scope.setPagingData($scope.cuentabancariaList,page,pageSize);
                        }
                    }
                }, 100);
            };

            $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);

            $scope.getPagedDataSearched = function () {
                setTimeout(function () {
                    if ($scope.filterOptions.filterText) {
                        ngProgress.start();
                        var ft = $scope.filterOptions.filterText.toUpperCase();
                        CuentaBancariaService.findByFilterTextView(ft).then(function (cuentasbancarias){
                            $scope.cuentabancariaList = cuentasbancarias;
                        });
                        ngProgress.complete();
                    } else {
                        ngProgress.start();
                        $scope.cuentabancariaList = CuentaBancariaService.getCuentasBancariasView();
                        ngProgress.complete();
                    }
                }, 100);
            };

            $scope.getPagedDataSearched();

            $scope.$watch('pagingOptions', function (newVal, oldVal) {
                if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
                    $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
                }
            }, true);

            $scope.$watch('filterOptions', function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
                }
            }, true);

            $scope.gridOptions = {
                data: 'cuentabancariaList'
                
            };
            
            $scope.openBuscarCuentaBancaria = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'modules/common/views/util/buscarCuentaBancaria.html',
                    controller: "BuscarCuentaBancariaController"
                });

                modalInstance.result.then(function (cuenta) {
                    $scope.cuentabancaria = cuenta;
                }, function () {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            }

        }]);