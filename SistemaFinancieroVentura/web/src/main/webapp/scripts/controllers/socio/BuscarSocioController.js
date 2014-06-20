define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("BuscarSocioController", ["$scope", "$state", "ngProgress","focus", "SocioService",
        function($scope, $state, ngProgress,focus, SocioService) {

            focus("firstFocus");
            ngProgress.color("#2d6ca2");

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
                $scope.sociosList = data;
                if (!$scope.$$phase) {
                    $scope.$apply();
                }
            };

            $scope.getDesde = function(){
                return ($scope.pagingOptions.pageSize*$scope.pagingOptions.currentPage)-$scope.pagingOptions.pageSize;
            }
            $scope.getHasta = function(){
                return ($scope.pagingOptions.pageSize*$scope.pagingOptions.currentPage);
            }

            //eventos

            //cargar datos por primera vez
            $scope.getPagedDataInitial = function () {
                setTimeout(function () {
                    $scope.pagingOptions.currentPage = 1;
                    SocioService.getSocios($scope.getDesde(), $scope.getHasta(), true).then(function(data){
                        $scope.sociosList = data;
                        $scope.setPagingData($scope.sociosList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                    });
                    SocioService.count().then(function(data){
                        $scope.totalServerItems = data;
                    });
                }, 100);
            };
            $scope.getPagedDataInitial();

            //buscar con enter
            $scope.getPagedDataSearched = function () {
                setTimeout(function () {
                    if ($scope.filterOptions.filterText) {
                        var ft = $scope.filterOptions.filterText.toUpperCase();
                        SocioService.findByFilterText(ft, $scope.getDesde(), $scope.getHasta(), true).then(function (data){
                            $scope.sociosList = data;
                            $scope.setPagingData($scope.sociosList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                        SocioService.count().then(function(data){
                            $scope.totalServerItems = data;
                        });
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
                        SocioService.findByFilterText(ft, $scope.getDesde(), $scope.getHasta(), true).then(function(data){
                            $scope.sociosList = data;
                            $scope.setPagingData($scope.sociosList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                    } else {
                        SocioService.getSocios($scope.getDesde(), $scope.getHasta(), true).then(function(data){
                            $scope.sociosList = data;
                            $scope.setPagingData($scope.sociosList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                    }
                },true);

            $scope.gridOptions = {
                data: 'sociosList',
                multiSelect: false,
                enablePaging: true,
                showFooter: true,
                totalServerItems: 'totalServerItems',
                pagingOptions: $scope.pagingOptions,
                filterOptions: $scope.filterOptions,
                columnDefs: [
                    {field:'id', displayName:'ID SOCIO', width:"8%"},
                    {field:'tipoDocumento', displayName:'TIPO DOC.',width:"10%"},
                    {field:'numeroDocumento', displayName:'NUM. DOC.',width:"12%"},
                    {field:'tipoPersona', displayName:'T.PERSONA',width:"10%"},
                    {field:'socio', displayName:'SOCIO',width:"26%"},
                    {field:'numeroCuentaAporte', displayName:'CUENTA.',width:"14%"},
                    {field:"fechaAsociado | date:'dd-MM-yyyy'", displayName:'F. ASOCIADO',width:"10%"},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="editSocio(row.entity)"><span class="glyphicon glyphicon-share"></span>Editar</button></div>'}
                ]
            };

            $scope.editSocio = function(row){
                $state.transitionTo("app.socio.panelSocio", { id: row.id });
            }
        }]);
});