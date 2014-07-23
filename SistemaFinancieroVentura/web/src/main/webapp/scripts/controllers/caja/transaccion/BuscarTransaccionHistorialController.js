define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller("BuscarTransaccionHistorialController", ["$scope", "$state", "$modal","ngProgress","focus", "CajaSessionService",
        function($scope, $state,$modal, ngProgress,focus, CajaSessionService) {

            $scope.focusElements = {
                filterText: 'focusFilterText'
            };
            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus($scope.focusElements.filterText);
            };
            $scope.setInitialFocus();

            $scope.nuevo = function(){
                $state.transitionTo("app.socio.crearCuentaBancaria");
            };

            $scope.historialList = [];
            $scope.historialListFilter = [];

            $scope.filterOptions = {
                filterText: "",
                useExternalFilter: true
            };
            $scope.totalServerItems = 0;
            $scope.pagingOptions = {
                pageSizes: [20, 50, 100],
                pageSize: 20,
                currentPage: 1
            };
            $scope.setPagingData = function(data, page, pageSize){
                var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
                $scope.historialListFilter = pagedData;
                $scope.totalServerItems = data.length;
                if (!$scope.$$phase) {
                    $scope.$apply();
                }
            };
            $scope.getPagedDataInitial = function () {
                $scope.pagingOptions.currentPage = 1;
                CajaSessionService.getHistorialTransaccion().then(function(data){
                    $scope.historialList = data;
                    $scope.setPagingData($scope.historialList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                });
            };
            $scope.getPagedDataInitial();
            $scope.getPagedDataSearched = function () {
                if ($scope.filterOptions.filterText) {
                    var ft = $scope.filterOptions.filterText.toUpperCase();
                    CajaSessionService.findByFilterTextView(ft).then(function (data){
                        $scope.transaccionesList = data;
                        $scope.setPagingData($scope.transaccionesList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                    });
                    CajaSessionService.count().then(function(data){
                        $scope.totalServerItems = data;
                    });
                } else {
                    $scope.getPagedDataInitial();
                }
                $scope.setInitialFocus();
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
                        CajaSessionService.getHistorialTransaccion().then(function (data){
                            $scope.historialList = data;
                            $scope.setPagingData($scope.historialList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                    } else {
                        CajaSessionService.getHistorialTransaccion().then(function(data){
                            $scope.historialList = data;
                            $scope.setPagingData($scope.historialList, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
                        });
                    }
                },true);


            var gridLayoutPlugin = new ngGridLayoutPlugin();
            $scope.gridOptions = {
                data: 'historialListFilter',
                multiSelect: false,
                enablePaging: true,
                showFooter: true,
                totalServerItems: 'totalServerItems',
                pagingOptions: $scope.pagingOptions,
                filterOptions: $scope.filterOptions,
                plugins: [gridLayoutPlugin],
                columnDefs: [
                    {field:"idTransaccion", displayName:'TRANS.', width:70},
                    {field:"tipoTransaccion", displayName:'TIPO TRANS.', width:90},
                    {field:"numeroOperacion", displayName:'Nº OP.', width:60},
                    {field:"moneda", displayName:'MONEDA'},
                    {field:"monto", displayName:'MONTO', width:100},
                    {field:"fecha | date : 'dd/MM/yyyy'", displayName:'FECHA', width:80},
                    {field:"hora | date : 'HH:mm:ss'", displayName:'HORA', width:70},
                    {displayName: 'ESTADO', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estado">ACTIVO</span><span ng-hide="row.entity.estado">EXTORNADO</span></div>', width:90},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="voucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button>&nbsp;<button type="button" class="btn btn-danger btn-xs" ng-click="extornar(row.entity)" ng-disabled="getDisabledStateExtornar(row.entity)"><span class="glyphicon glyphicon-remove"></span>Extornar</button></div>', width:150}
                ]
            };
            
            $scope.getDisabledStateExtornar = function(transaccion){
                if(transaccion.tipoCuenta == "PLAZO_FIJO" || transaccion.estado == !true)
                    return true;
                return false;
            };
            
            $scope.updateGridLayout = function(){
                gridLayoutPlugin.updateGridLayout();
            };

            $scope.voucher = function(transaccion){
            	if(transaccion.tipoTransaccion == "APORTE")
            		$state.transitionTo("app.transaccion.aporteVoucher", { id: transaccion.idTransaccion});
            	else if(transaccion.tipoTransaccion == "DEPOSITO" || transaccion.tipoTransaccion == "RETIRO")
            		$state.transitionTo("app.transaccion.depositoRetiroVoucher", { id: transaccion.idTransaccion});
            	else if(transaccion.tipoTransaccion == "COMPRA" || transaccion.tipoTransaccion == "VENTA")
            		$state.transitionTo("app.transaccion.compraVentaVoucher", { id: transaccion.idTransaccion});
            	else if(transaccion.tipoTransaccion == "TRANSFERENCIA")
            		$state.transitionTo("app.transaccion.transferenciaVoucher", { id: transaccion.idTransaccion});
            	else
                	alert("Tipo de transacción no encontrado");
            };
            
            $scope.extornar = function(transaccion){
            	var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/confirmPopUp.html',
                    controller: "ConfirmPopUpController"
                });
                modalInstance.result.then(function (result) {
                	CajaSessionService.extornarTransaccion(transaccion.idTransaccion).then(
                			function(data){
                				$scope.getPagedDataInitial();
                				$scope.alerts = [{ type: "success", msg: "Extornación Éxitosa..."}];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);}
                            }
                            ,function error(error){
                                $scope.alerts = [{ type: "danger", msg: error.data.message + "."}];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);}
                            }
                    );
                }, function () {
                	//no
                });
            };
        }]);
});