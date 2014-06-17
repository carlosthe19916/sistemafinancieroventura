define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearTransaccionDepositoRetiroController', ["$scope", "$state", "$window", "$filter", "$modal", "CuentaBancariaService", "CajaSessionService", "ngProgress","TransitionService",
        function($scope, $state, $window, $filter, $modal, CuentaBancariaService, CajaSessionService, ngProgress, TransitionService) {

            $scope.control = {"success":false, "inProcess": false, "submitted":false};

            $scope.tipotransacciones = [{"denominacion":"DEPOSITO","factor":1},{"denominacion":"RETIRO","factor":-1}];

            $scope.cuenta;
            $scope.monto;

            $scope.transaccion = {
                "numeroCuenta" : undefined,
                "monto" : undefined,
                "montoReal" : undefined,
                "tipoTransaccion": undefined,
                "referencia" : undefined
            }

            $scope.$watch('transaccion.monto', function() {
                if($scope.transaccion.monto !== undefined && $scope.transaccion.monto !== null){
                    if($scope.transaccion.tipoTransaccion !== undefined && $scope.transaccion.tipoTransaccion !== null){
                        $scope.transaccion.montoReal = ( Math.abs($scope.transaccion.monto) * $scope.transaccion.tipoTransaccion.factor );
                    } else {
                        $scope.transaccion.montoReal = $scope.transaccion.monto;
                    }
                } else {
                    $scope.transaccion.montoReal = 0;
                }
            });

            $scope.openCalculadora = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'modules/common/views/util/calculadora.html',
                    controller: "CalculadoraController"
                });

                modalInstance.result.then(function (total) {
                    $scope.monto = total;
                }, function () {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.openBuscarCuentaBancaria = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/buscarCuentaBancaria.html',
                    controller: "BuscarCuentaBancariaPopUpController",
                    size: 'lg'
                });

                modalInstance.result.then(function (cuenta) {
                    $scope.cuenta = cuenta;
                    $scope.transaccion.numeroCuenta = $scope.cuenta.numeroCuenta;
                }, function () {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };

            //transaccion
            $scope.crearTransaccion = function(){
                if($scope.formCrearTransaccion.$valid){
                    $scope.control.inProcess = true;
                    var transaccion = {
                        "numeroCuenta" : $scope.transaccion.numeroCuenta,
                        "monto": $scope.transaccion.montoReal,
                        "referencia" : $scope.transaccion.referencia
                    }
                    CajaSessionService.crearTransaccionBancaria(transaccion).then(
                        function(data){
                            $scope.control.success = true;
                            $scope.control.inProcess = false;
                            TransitionService.setUrl("app.transaccion.depositoRetiro");
                            TransitionService.setParameters({});
                            $state.transitionTo('app.transaccion.depositoRetiroVoucher', { id: data.id });
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $window.scrollTo(0,0);
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            }
            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            }

            $scope.cancel = function(){

            }

        }]);
});
