define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("BuscarCuentaBancariaController", ["$scope", "$state", "ngProgress","focus", "CuentaBancariaService",
        function($scope, $state, ngProgress,focus, CuentaBancariaService) {

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
                var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
                $scope.cuentasList = pagedData;
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
                        data = $scope.cuentasList.filter(function(item) {
                            return JSON.stringify(item).toUpperCase().indexOf(ft) != -1;
                        });
                        $scope.setPagingData(data,page,pageSize);

                    } else {
                        if( angular.isUndefined($scope.filterOptions.filterText) || $scope.filterOptions.filterText === null) {
                            var result = $scope.cuentasList = SocioService.getSocios();
                            $scope.setPagingData(result,page,pageSize);
                        } else {
                            $scope.setPagingData($scope.cuentasList,page,pageSize);
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
                        CuentaBancariaService.findByFilterText(ft).then(function (socios){
                            $scope.cuentasList = socios;
                        });
                        ngProgress.complete();
                    } else {
                        ngProgress.start();
                        $scope.cuentasList = CuentaBancariaService.getCuentasBancarias();
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
                data: 'cuentasList',
                multiSelect: false,
                enablePaging: true,
                showFooter: true,
                totalServerItems: 'totalServerItems',
                pagingOptions: $scope.pagingOptions,
                filterOptions: $scope.filterOptions,
                columnDefs: [
                    {field:"tipoCuentaBancaria", displayName:'TIPO CTA.'},
                    {field:"numeroCuenta", displayName:'NUMERO CUENTA', width:150},
                    {field:"moneda.denominacion", displayName:'MONEDA'},
                    {field:"fechaApertura | date : 'dd/MM/yyyy'", displayName:'F.APERTURA'},
                    {field:"fechaFin | date : 'dd/MM/yyyy'", displayName:'F.CIERRE'},
                    {field:"cantidadRetirantes", displayName:'CANT.RETIRANT.'},
                    {field:"estado", displayName:'ESTADO'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="editCuenta(row.entity)"><span class="glyphicon glyphicon-share"></span>Editar</button></div>'}
                ]
            };

            $scope.editCuenta = function(row){
                $state.transitionTo("app.socio.editarCuentaBancaria", { id: row.id });
            }
        }]);
});