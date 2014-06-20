define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CompraVentaController', ["$scope", "$state", "$window", "$filter", "$modal", "CuentaBancariaService", "CajaSessionService","MonedaService",
        function($scope, $state, $window, $filter, $modal, CuentaBancariaService, CajaSessionService, MonedaService) {

            $scope.control = {"success":false, "inProcess": false, "submitted":false};

            $scope.view = {
                "numeroDocumento": undefined,
                "nombreCliente": undefined,
                "monedaRecibida": undefined,
                "monedaEntregada": undefined,
                "monedasRecibidas": undefined,
                "monedasEntregadas": undefined,
                "tasaCambio":0,
                "login":{"result":false, "tasaCambio": undefined}
            };
            $scope.transaccion = {

            };

            $scope.$watch("view.monedaRecibida",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $scope.view.monedasEntregadas = angular.copy($scope.view.monedasRecibidas);
                    for(var i=0; i < $scope.view.monedasEntregadas.length; i++){
                        if($scope.view.monedaRecibida.id == $scope.view.monedasEntregadas[i].id){
                            $scope.view.monedasEntregadas.splice(i,1);
                        }
                    }
                }
            },true);

            MonedaService.getMonedas().then(
                function(data){
                    $scope.view.monedasRecibidas = angular.copy(data);
                    $scope.view.monedasEntregadas = angular.copy(data);
                },
                function error(error){
                    $scope.alerts = [{ type: "danger", msg: "No se cargaron las monedas"}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                }
            );

            $scope.buscarCliente = function($event){

            };
            $scope.openLoginPopUp = function(){

            };
            $scope.setTasaInteres = function(){

            };


            $scope.$watch("view.monto",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                   $scope.view.monto = Math.abs($scope.view.monto);
                }
            },true);


            $scope.loadDenominacionesMoneda = function(){
                if(!angular.isUndefined($scope.view.cuentaBancariaOrigen)){
                    MonedaService.getDenominaciones($scope.view.cuentaBancariaOrigen.moneda.id).then(function(data){
                        $scope.view.denominacionesMoneda = data;
                    });
                }
            };

            $scope.openCalculadora = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/calculadora.html',
                    controller: "CalculadoraController",
                    resolve: {
                        denominaciones: function () {
                            return !angular.isUndefined($scope.view.denominacionesMoneda) ? $scope.view.denominacionesMoneda : [];
                        },
                        moneda: function () {
                            return !angular.isUndefined($scope.view.cuentaBancariaOrigen) ? $scope.view.cuentaBancariaOrigen.moneda.simbolo : '';
                        }
                    }
                });
                modalInstance.result.then(function (total) {
                    $scope.view.monto = total;
                }, function () {
                });
            };

            //transaccion
            $scope.crearTransaccion = function(){
                if($scope.formCrearTransferencia.$valid){
                    if($scope.view.cuentaBancariaOrigen.numeroCuenta == $scope.view.cuentaBancariaDestino.numeroCuenta){
                        $scope.alerts = [{ type: "danger", msg: "Error: el origen y el destino son los mismos."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        return;
                    }

                    $scope.transaccion = {
                        "numeroCuentaOrigen" : $scope.view.cuentaBancariaOrigen.numeroCuenta,
                        "numeroCuentaDestino" : $scope.view.cuentaBancariaDestino.numeroCuenta,
                        "monto" : $scope.view.monto,
                        "referencia" : $scope.view.referencia
                    };

                    $scope.control.inProcess = true;

                    CajaSessionService.crearTransferenciaBancaria($scope.transaccion).then(
                        function(data){
                            $scope.control.success = true;
                            $scope.control.inProcess = false;
                            $state.transitionTo('app.transaccion.transferenciaVoucher', { id: data.id });
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
