define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('PersonanaturalBuscarController', ['$scope','$state','$dialogs', 'ngProgress','Restangular', "PersonanaturalService",
        function($scope, $state, $dialogs, ngProgress, Restangular, PersonanaturalService) {

            $scope.personasList = [];

            $scope.create = function() {
                $state.go("app.administracion.personanaturalCreate");
            };

            $scope.edit = function(persona) {
                $state.transitionTo('app.administracion.personanaturalUpdate', { id: persona.id });
            };

            $scope.delete = function(persona) {
                var dlg = $dialogs.confirm('Confirmacion','¿Está seguro de eliminar la persona? "Si" or "No"');
                dlg.result.then(function(btn){
                    PersonanaturalService.remove(persona.id);
                    var index = $scope.personasList.indexOf(persona);
                    if (index > -1) {
                        $scope.personasList.splice(index, 1);
                    }
                },function(btn){});
            };

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
                $scope.personasList = pagedData;
                $scope.totalServerItems = data.length;
                if (!$scope.$$phase) {
                    $scope.$apply();
                }
            };

            $scope.getPagedDataAsync = function (pageSize, page, searchText) {
                setTimeout(function () {
                    var data;
                    if (searchText) {
                        var ft = searchText.toLowerCase();
                        data = $scope.personasList.filter(function(item) {
                            return JSON.stringify(item).toLowerCase().indexOf(ft) != -1;
                        });
                        $scope.setPagingData(data,page,pageSize);
                    } else {
                        if( angular.isUndefined($scope.filterOptions.filterText) || $scope.filterOptions.filterText === null) {
                            var result = $scope.personasList = PersonanaturalService.getPersonas();
                            $scope.setPagingData(result,page,pageSize);
                        } else {
                            $scope.setPagingData($scope.personasList,page,pageSize);
                        }
                    }
                }, 100);
            };

            $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);

            ngProgress.color("#2d6ca2");

            $scope.getPagedDataSearched = function () {
                setTimeout(function () {
                    if ($scope.filterOptions.filterText) {
                        ngProgress.start();
                        var ft = $scope.filterOptions.filterText.toLowerCase();
                        PersonanaturalService.findByFilterText(ft).then(function (personas){
                            $scope.personasList = personas;
                        });
                        ngProgress.complete();
                    } else {
                        ngProgress.start();
                        $scope.personasList = PersonanaturalService.getPersonas();
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
                data: 'personasList',
                multiSelect: false,
                enablePaging: true,
                showFooter: true,
                totalServerItems: 'totalServerItems',
                pagingOptions: $scope.pagingOptions,
                filterOptions: $scope.filterOptions,
                columnDefs: [
                    {field:'tipodocumento.abreviatura', displayName:'T.doc.'},
                    {field:'numerodocumento', displayName:'Num.doc.'},
                    {field:'apellidopaterno', displayName:'Ap.paterno'},
                    {field:'apellidomaterno', displayName:'Ap.materno'},
                    {field:'nombres', displayName:'Nombres'},
                    {field:'sexo', displayName:'Sexo'},
                    {field:"fechanacimiento | date:'dd-MM-yyyy'", displayName:'F.nacimiento'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="edit(row.entity)"><span class="glyphicon glyphicon-share"></span>Edit</button>&nbsp;<button type="button" class="btn btn-danger btn-xs" ng-click="delete(row.entity)"><span class="glyphicon glyphicon-remove"></span>Del</button></div>'}]
            };
        }]);
});