define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("BuscarSocioController", ["$scope", "$state", "ngProgress","focus", "SocioService",
        function($scope, $state, ngProgress,focus, SocioService) {

            ngProgress.color("#2d6ca2");
            focus("firstFocus");

            $scope.nuevo = function(){
                $state.transitionTo("app.socio.crearSocio");
            }

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
                            var result = $scope.sociosList = SocioService.getSocios();
                            $scope.setPagingData(result,page,pageSize);
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
                        SocioService.findByFilterText(ft).then(function (socios){
                            $scope.sociosList = socios;
                        });
                        ngProgress.complete();
                    } else {
                        ngProgress.start();
                        $scope.sociosList = SocioService.getSocios();
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
                multiSelect: false,
                enablePaging: true,
                showFooter: true,
                totalServerItems: 'totalServerItems',
                pagingOptions: $scope.pagingOptions,
                filterOptions: $scope.filterOptions,
                columnDefs: [
                    {field:'id', displayName:'ID SOCIO', width:"10%"},
                    {field:'tipodocumento', displayName:'TIPO DOC.',width:"15%"},
                    {field:'numerodocumento', displayName:'NUM. DOC.',width:"12%"},
                    {field:'tipopersona', displayName:'TIPO PERSONA',width:"12%"},
                    {field:'socio', displayName:'SOCIO',width:"28%"},
                    {field:"fechaasociado | date:'dd-MM-yyyy'", displayName:'F. ASOCIADO',width:"14%"},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="editSocio(row.entity)"><span class="glyphicon glyphicon-share"></span>Editar</button></div>'}
                ]
            };

            $scope.editSocio = function(row){
                $state.transitionTo("app.socio.panelSocio", { id: row.id });
            }
        }]);
});