define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarPersonaJuridicaController', ['$scope','$state','$location','$window','ngProgress','PersonaJuridicaService','RedirectService',
        function($scope, $state,$location,$window,ngProgress,PersonaJuridicaService,RedirectService){

            $scope.nuevo = function() {
                RedirectService.limpiar();
                $state.transitionTo('app.administracion.crearPersonaJuridica');
            };
            $scope.editar = function(persona) {
                RedirectService.limpiar();
                $state.transitionTo('app.administracion.editarPersonaJuridica', { id: persona.id });
            };

            $scope.personasList = [];

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
            $scope.getOffset = function(){
                return ($scope.pagingOptions.pageSize*$scope.pagingOptions.currentPage)-$scope.pagingOptions.pageSize;
            };
            $scope.getLimit = function(){
                return $scope.pagingOptions.pageSize;
            };

            $scope.getPagedDataInitial = function () {
                setTimeout(function () {
                    $scope.pagingOptions.currentPage = 1;
                    PersonaJuridicaService.getPersonas($scope.getOffset(), $scope.getLimit()).then(function(data){
                        $scope.personasList = data;
                        $scope.setPagingData($scope.personasList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                    });
                }, 100);
            };
            $scope.getPagedDataInitial();

            $scope.getPagedDataSearched = function () {
                setTimeout(function () {
                    if ($scope.filterOptions.filterText) {
                        var ft = $scope.filterOptions.filterText.toUpperCase();
                        PersonaJuridicaService.findByFilterText(ft, $scope.getOffset(), $scope.getLimit()).then(function (data){
                            $scope.personasList = data;
                            $scope.setPagingData($scope.personasList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });;
                    } else {
                        $scope.getPagedDataInitial();
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
                        PersonaJuridicaService.findByFilterText(ft, $scope.getOffset(), $scope.getLimit()).then(function (data){
                            $scope.personasList = data;
                            $scope.setPagingData($scope.personasList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                    } else {
                        PersonaJuridicaService.getPersonas($scope.getOffset(), $scope.getLimit()).then(function(data){
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
                    {field:'razonSocial', displayName:'RAZON SOCIAL'},
                    {field:'nombreComercial', displayName:'N.COMERCIAL'},
                    {field:'tipoEmpresa', displayName:'T.EMPRESA'},
                    {field:"fechaConstitucion | date:'dd/MM/yyyy'", displayName:'F.CONSTITUCION'},
                    {displayName: 'EDITAR', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="editar(row.entity)"><span class="glyphicon glyphicon-share"></span>Edit</button></div>'}]
            };
        }]);
});