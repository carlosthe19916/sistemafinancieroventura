define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarPersonaNaturalController', ['$scope','$state','$location','$window','ngProgress','PersonaNaturalService','TransitionService',
        function($scope, $state,$location,$window,ngProgress,PersonaNaturalService,TransitionService){

            $scope.nuevo = function() {
                TransitionService.setUrl('app.administracion.buscarPersonaNatural');
                TransitionService.setParameters({});
                TransitionService.setModeRedirect();
                $state.transitionTo('app.administracion.crearPersonaNatural');
            };
            $scope.editar = function(persona) {
                TransitionService.setUrl('app.administracion.buscarPersonaNatural');
                TransitionService.setParameters({});
                TransitionService.setModeRedirect(),
                $state.transitionTo('app.administracion.editarPersonaNatural', { id: persona.id });
            };

            $scope.personasList = [];

            $scope.view = {
                filterText: ""
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
                $scope.personasList = data;
                if (!$scope.$$phase) {
                    $scope.$apply();
                }
            };
            $scope.getDesde = function(){
                return ($scope.pagingOptions.pageSize*$scope.pagingOptions.currentPage)-$scope.pagingOptions.pageSize;
            }
            $scope.getHasta = function(){
                return ($scope.pagingOptions.pageSize);
            }

            //carga inicial de datos
            $scope.getPagedDataInitial = function () {
                $scope.pagingOptions.currentPage = 1;
                PersonaNaturalService.getPersonas($scope.getDesde(), $scope.getHasta()).then(function(data){
                    $scope.personasList = data;
                    $scope.setPagingData($scope.personasList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                });
                PersonaNaturalService.count().then(function(data){
                    $scope.totalServerItems = data;
                });
            };
            $scope.getPagedDataInitial();

            $scope.getPagedDataSearched = function () {
                if ($scope.filterOptions.filterText) {
                    var ft = $scope.filterOptions.filterText.toUpperCase();
                    PersonaNaturalService.findByFilterText(ft, $scope.getDesde(), $scope.getHasta()).then(function (data){
                        $scope.view.filterText = ft;
                        $scope.pagingOptions.currentPage = 1;
                        $scope.setPagingData(data, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                    });
                    PersonaNaturalService.count(ft).then(function(data){
                        $scope.totalServerItems = data;
                    });
                } else {
                    $scope.getPagedDataInitial();
                }
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
                        PersonaNaturalService.findByFilterText(ft, $scope.getDesde(), $scope.getHasta()).then(function (data){
                            $scope.personasList = data;
                            $scope.setPagingData($scope.personasList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                    } else {
                        PersonaNaturalService.getPersonas($scope.getDesde(), $scope.getHasta()).then(function(data){
                            $scope.personasList = data;
                            $scope.setPagingData($scope.personasList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                    }
                },true);

            $scope.gridOptions = {
                data: 'personasList',
                multiSelect: false,
                enablePaging: true,
                showFooter: true,
                totalServerItems: 'totalServerItems',
                pagingOptions: $scope.pagingOptions,
                filterOptions: $scope.filterOptions,
                columnDefs: [
                    {field:'tipoDocumento.abreviatura', displayName:'DOCUMENTO'},
                    {field:'numeroDocumento', displayName:'NUM.DOCUMENTO'},
                    {field:'apellidoPaterno', displayName:'AP.PATERNO'},
                    {field:'apellidoMaterno', displayName:'AP.MATERNO'},
                    {field:'nombres', displayName:'NOMBRES'},
                    {field:'sexo', displayName:'SEXO'},
                    {field:"fechaNacimiento | date:'dd/MM/yyyy'", displayName:'F.NACIMIENTO'},
                    {displayName: 'EDITAR', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="editar(row.entity)"><span class="glyphicon glyphicon-share"></span>Edit</button></div>'}]
            };
        }]);
});