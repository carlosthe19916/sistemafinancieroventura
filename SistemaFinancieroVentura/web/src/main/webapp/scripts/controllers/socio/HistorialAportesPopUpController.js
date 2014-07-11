define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("HistorialAportesPopUpController", ["$scope","$timeout","$modalInstance","idSocio","focus",
        function($scope,$timeout,$modalInstance,idSocio,focus) {

            $scope.focusElements = {
                mesDesde: 'focusMesDesde'
            };

            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                $timeout(function() {
                    focus($scope.focusElements.mesDesde);
                }, 100);
            };
            $scope.setInitialFocus();

            $scope.view = {
                mesDesde: undefined,
                anioDesde: undefined,
                mesHasta: undefined,
                anioHasta: undefined
            };

            $scope.combo = {
                mes: [
                    {"denominacion":"ENERO","value":0},
                    {"denominacion":"FEBRERO","value":1},
                    {"denominacion":"MARZO","value":2},
                    {"denominacion":"ABRIL","value":3},
                    {"denominacion":"MAYO","value":4},
                    {"denominacion":"JUNIO","value":5},
                    {"denominacion":"JULIO","value":6},
                    {"denominacion":"AGOSTO","value":7},
                    {"denominacion":"SEPTIEMBRE","value":8},
                    {"denominacion":"OCTUBRE","value":9},
                    {"denominacion":"NOVIEMBRE","value":10},
                    {"denominacion":"DICIEMBRE","value":11}
                ]
            };

            $scope.setDefaultDates = function(){
                var pastDate = new Date();
                pastDate.setMonth(pastDate.getMonth()-12);
                $scope.view.anioDesde = pastDate.getFullYear();
                $scope.view.mesDesde = $scope.combo.mes[pastDate.getMonth()];

                var currentDate = new Date();
                $scope.view.anioHasta = currentDate.getFullYear();
                $scope.view.mesHasta = $scope.combo.mes[currentDate.getMonth()];
            };
            $scope.setDefaultDates();

            //definicion de la tabla
            $scope.aportesList = [];

            $scope.totalServerItems = 0;
            $scope.pagingOptions = {
                pageSizes: [10, 20, 40],
                pageSize: 10,
                currentPage: 1
            };
            $scope.setPagingData = function(data, page, pageSize){
                $scope.aportesList = data;
                if (!$scope.$$phase) {
                    $scope.$apply();
                }
            };
            $scope.getDesde = function(){
                return ($scope.pagingOptions.pageSize*$scope.pagingOptions.currentPage)-$scope.pagingOptions.pageSize;
            };
            $scope.getHasta = function(){
                return $scope.pagingOptions.pageSize;
            };
/*
            $scope.getPagedDataInitial = function () {
                $scope.pagingOptions.currentPage = 1;
                CuentaBancariaService.getCuentasBancariasView($scope.tipoCuentasBancarias, $scope.tipoPersonas, $scope.tipoEstadoCuenta,$scope.getDesde(), $scope.getHasta()).then(function(data){
                    $scope.aportesList = data;
                    $scope.setPagingData($scope.aportesList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                });
                CuentaBancariaService.count().then(function(data){
                    $scope.totalServerItems = data;
                });
            };
            $scope.getPagedDataInitial();*/
/*
            $scope.getPagedDataSearched = function () {
                if ($scope.filterOptions.filterText) {
                    var ft = $scope.filterOptions.filterText.toUpperCase();
                    CuentaBancariaService.findByFilterTextView(ft, $scope.tipoCuentasBancarias, $scope.tipoPersonas, $scope.tipoEstadoCuenta,$scope.getDesde(), $scope.getHasta()).then(function (data){
                        $scope.aportesList = data;
                        $scope.setPagingData($scope.aportesList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                    });
                    CuentaBancariaService.count().then(function(data){
                        $scope.totalServerItems = data;
                    });
                } else {
                    $scope.getPagedDataInitial();
                }
                $scope.setInitialFocus();
            };*/
/*
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
                        CuentaBancariaService.findByFilterTextView(ft, $scope.tipoCuentasBancarias, $scope.tipoPersonas, $scope.tipoEstadoCuenta, $scope.getDesde(), $scope.getHasta()).then(function (data){
                            $scope.aportesList = data;
                            $scope.setPagingData($scope.aportesList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                    } else {
                        CuentaBancariaService.getCuentasBancariasView($scope.tipoCuentasBancarias, $scope.tipoPersonas, $scope.tipoEstadoCuenta, $scope.getDesde(), $scope.getHasta()).then(function(data){
                            $scope.aportesList = data;
                            $scope.setPagingData($scope.aportesList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                    }
                },true);*/

            var gridLayoutPlugin = new ngGridLayoutPlugin();
            $scope.gridOptions = {
                data: 'aportesList',
                selectedItems: [],
                multiSelect: false,
                enablePaging: true,
                showFooter: true,
                totalServerItems: 'totalServerItems',
                pagingOptions: $scope.pagingOptions,
                plugins: [gridLayoutPlugin],
                columnDefs: [
                    {field:"anio", displayName:'AÑO'},
                    {field:"mes", displayName:'MES'},
                    {field:"monto", displayName:'MONTO APORTE'},
                    {field:"estado", displayName:'ESTADO'},
                    {displayName: 'Select', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="selectCuenta(row.entity)"><span class="glyphicon glyphicon-share"></span>Select</button></div>'}
                ]
            };

            $scope.updateGridLayout = function(){
                gridLayoutPlugin.updateGridLayout();
            }
            setTimeout(function () {
                $scope.updateGridLayout();
            }, 100);


            $scope.ok = function () {

            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        }]);
});