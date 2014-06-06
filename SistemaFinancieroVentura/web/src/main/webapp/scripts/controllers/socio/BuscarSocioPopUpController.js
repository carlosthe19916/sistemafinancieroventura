define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("BuscarSocioPopUpController", ["$scope","$modalInstance", "$state", "ngProgress", "SocioService",
        function($scope, $modalInstance, $state, ngProgress, SocioService) {

            ngProgress.color("#2d6ca2");

            //configurar tabla
            $scope.sociosList = [];

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
                $scope.sociosList = pagedData;
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
                        data = $scope.sociosList.filter(function(item) {
                            return JSON.stringify(item).toUpperCase().indexOf(ft) != -1;
                        });
                        $scope.setPagingData(data,page,pageSize);

                    } else {
                        if( angular.isUndefined($scope.filterOptions.filterText) || $scope.filterOptions.filterText === null) {
                            SocioService.getSocios("APORTE").then(function(data){
                                var result = $scope.sociosList = data;
                                $scope.setPagingData(result,page,pageSize);
                            });
                        } else {
                            $scope.setPagingData($scope.sociosList,page,pageSize);
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
                        SocioService.findByFilterText(ft).then(function (data){
                            $scope.sociosList = data;
                        });
                        ngProgress.complete();
                    } else {
                        ngProgress.start();
                        SocioService.getSocios("APORTE").then(function(data){
                            $scope.sociosList = data;
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
                data: 'sociosList',
                selectedItems: [],
                multiSelect: false,
                enablePaging: true,
                showFooter: true,
                totalServerItems: 'totalServerItems',
                pagingOptions: $scope.pagingOptions,
                filterOptions: $scope.filterOptions,
                columnDefs: [
                    {field:"tipoDocumento", displayName:'T.DOC.', width:60},
                    {field:"numeroDocumento", displayName:'NUMERO',width:100},
                    {field:"socio", displayName:'SOCIO',width:220},
                    {field:"fechaAsociado | date : 'dd/MM/yyyy'", displayName:'F.ASOCIADO'},
                    {field:"estado", displayName:'ESTADO',width:80},
                    {displayName: 'Select', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="selectSocio(row.entity)"><span class="glyphicon glyphicon-share"></span>Select</button></div>'}
                ]
            };

            $scope.selectSocio = function(row){
                $scope.socioSelected = row;
                $scope.ok();
            }

            $scope.ok = function () {
                var socio = $scope.gridOptions.selectedItems[0];
                if(socio !== undefined && socio !== null){
                    $scope.socioSelected = cta;
                }
                if ($scope.socioSelected !== undefined && $scope.socioSelected !== null) {
                    $modalInstance.close($scope.socioSelected);
                }
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        }]);
});