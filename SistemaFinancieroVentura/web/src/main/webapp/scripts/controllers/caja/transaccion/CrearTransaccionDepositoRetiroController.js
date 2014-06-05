define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearTransaccionDepositoRetiroController', ["$scope", "$state", "$filter", "$modal", "CuentaBancariaService", "CajaSessionService", "ngProgress",
        function($scope, $state, $filter, $modal, CuentaBancariaService, CajaSessionService, ngProgress) {

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

            $scope.getMontoReal = function(){
                if($scope.transaccion.monto !== undefined && $scope.transaccion.monto !== null){
                    if($scope.transaccion.tipoTransaccion !== undefined && $scope.transaccion.tipoTransaccion !== null){
                        return ( Math.abs($scope.transaccion.monto) * $scope.transaccion.tipoTransaccion.factor );
                    } else {
                        return $scope.transaccion.monto;
                    }
                } else {
                    return 0;
                }
            }

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
                    templateUrl: 'modules/common/views/util/buscarCuentaBancaria.html',
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
                    alert("creando transaccion");
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
