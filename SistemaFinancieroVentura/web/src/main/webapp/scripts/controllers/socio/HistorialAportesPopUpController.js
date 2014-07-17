define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("HistorialAportesPopUpController", ["$scope","$timeout","$modalInstance","idSocio","focus","SocioService",
        function($scope,$timeout,$modalInstance,idSocio,focus,SocioService) {

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
                id: idSocio,
                mesDesde: undefined,
                anioDesde: undefined,
                mesHasta: undefined,
                anioHasta: undefined
            };

            $scope.combo = {
                mes: [
                    {"denominacion":"ENERO","value":1},
                    {"denominacion":"FEBRERO","value":2},
                    {"denominacion":"MARZO","value":3},
                    {"denominacion":"ABRIL","value":4},
                    {"denominacion":"MAYO","value":5},
                    {"denominacion":"JUNIO","value":6},
                    {"denominacion":"JULIO","value":7},
                    {"denominacion":"AGOSTO","value":8},
                    {"denominacion":"SEPTIEMBRE","value":9},
                    {"denominacion":"OCTUBRE","value":10},
                    {"denominacion":"NOVIEMBRE","value":11},
                    {"denominacion":"DICIEMBRE","value":12}
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

            $scope.getDesdeFecha = function(){
                var mesdesde = $scope.view.mesDesde.value;
                var aniodesde = $scope.view.anioDesde;
                var stringdesde = '01-'+mesdesde+'-'+aniodesde;
                var fechaInicio = new Date(stringdesde);
                return fechaInicio.getTime();
            };
            $scope.getHastaFecha = function(){
                var meshasta = $scope.view.mesHasta.value;
                var aniohasta = $scope.view.anioHasta;
                var stringhasta = '01-'+meshasta+'-'+aniohasta;
                var fechaFin = new Date(stringhasta);
                return fechaFin.getTime();
            };

            $scope.getPagedDataInitial = function () {
                $scope.pagingOptions.currentPage = 1;
                SocioService.getHistorialAportes($scope.view.id, $scope.getDesdeFecha(), $scope.getHastaFecha(),$scope.getDesde(), $scope.getHasta()).then(function(data){
                    $scope.aportesList = data;
                    $scope.setPagingData($scope.aportesList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                });
                $scope.totalServerItems = $scope.aportesList.length;
            };
            $scope.getPagedDataInitial();
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