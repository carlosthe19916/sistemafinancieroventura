define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("BuscarCuentaBancariaPopUpController", ["$scope","$modalInstance", "$state", "ngProgress","focus", "CuentaBancariaService",
        function($scope, $modalInstance, $state, ngProgress,focus, CuentaBancariaService) {

            ngProgress.color("#2d6ca2");

            $scope.tipoPersonaList = [];
            $scope.tipoPersonaList.push("NATURAL");
            $scope.tipoPersonaList.push("JURIDICA");

            $scope.tipoCuentaList = [];
            $scope.tipoCuentaList.push("AHORRO");
            $scope.tipoCuentaList.push("CORRIENTE");

            $scope.estadoCuentaList = [];
            $scope.estadoCuentaList.push("ACTIVO");

            /*CuentaBancariaService.getCuentasBancariasView(
                $scope.tipoPersonaList,$scope.tipoCuentaList, $scope.estadoCuentaList
            ).then(function(data){
                $scope.cuentasList = data;
            });*/

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
                            CuentaBancariaService.getCuentasBancariasView(
                             $scope.tipoPersonaList,$scope.tipoCuentaList, $scope.estadoCuentaList
                             ).then(function(data){
                                    var result = $scope.cuentasList = data;
                                    $scope.setPagingData(result,page,pageSize);
                             });
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
                        CuentaBancariaService.findByFilterTextView(ft).then(function (data){
                            $scope.cuentasList = data;
                        });
                        ngProgress.complete();
                    } else {
                        ngProgress.start();
                        CuentaBancariaService.getCuentasBancariasView(
                            $scope.tipoPersonaList,$scope.tipoCuentaList, $scope.estadoCuentaList
                        ).then(function(data){
                                $scope.cuentasList = data;
                            });
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
                selectedItems: [],
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
                    {displayName: 'Select', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="selectCuenta(row.entity)"><span class="glyphicon glyphicon-share"></span>Select</button></div>'}
                ]
            };

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