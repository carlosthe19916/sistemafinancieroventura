define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearTransferenciaController', ["$scope", "$state", "$window", "$filter", "$modal", "CuentaBancariaService", "CajaSessionService","MonedaService","TransitionService",
        function($scope, $state, $window, $filter, $modal, CuentaBancariaService, CajaSessionService, MonedaService, TransitionService) {

            $scope.control = {"success":false, "inProcess": false, "submitted":false};

            $scope.view = {
                "cuentaBancariaOrigen" : undefined,
                "cuentaBancariaDestino": undefined,
                "monto": undefined,
                "referencia": undefined,
                "titularesOrigen": undefined,
                "denominacionesMoneda": undefined
            };
            $scope.transaccion = {
                "numeroCuentaOrigen" : undefined,
                "numeroCuentaDestino" : undefined,
                "monto" : undefined,
                "referencia" : undefined
            };

            $scope.$watch("view.monto",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                   $scope.view.monto = Math.abs($scope.view.monto);
                }
            },true);
            $scope.$watch("view.cuentaBancariaOrigen",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $scope.loadTitulares();
                    $scope.loadDenominacionesMoneda();
                }
            },true);
            $scope.$watch("view.cuentaBancariaDestino",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    if(!angular.isUndefined($scope.view.cuentaBancariaOrigen)){
                        if(!angular.isUndefined($scope.view.cuentaBancariaDestino)){
                            if($scope.view.cuentaBancariaOrigen.numeroCuenta == $scope.view.cuentaBancariaDestino.numeroCuenta){
                                $scope.alerts = [{ type: "warning", msg: "Warning: origen y detino iguales."}];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            }
                        }
                    }
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

            $scope.openBuscarCuentaBancariaOrigen = function(){
                $scope.openBuscarCuentaBancaria("origen");
            };
            $scope.openBuscarCuentaBancariaDestino = function(){
                $scope.openBuscarCuentaBancaria("destino");
            };

            $scope.openBuscarCuentaBancaria = function (destino) {
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/buscarCuentaBancariaPopUp.html',
                    controller: "BuscarCuentaBancariaPopUpController",
                    size: 'lg'
                });
                modalInstance.result.then(function (cuenta) {
                    if(destino == "origen"){
                        $scope.view.cuentaBancariaOrigen = cuenta;
                    }
                    if(destino == "destino"){
                        $scope.view.cuentaBancariaDestino = cuenta;
                    }
                }, function () {
                });
            };

            $scope.loadTitulares = function(){
                if(!angular.isUndefined($scope.view.cuentaBancariaOrigen)){
                    CuentaBancariaService.getTitulares($scope.view.cuentaBancariaOrigen.id).then(
                        function(data){
                            $scope.view.titularesOrigen = data;
                        }
                    );
                }
            };
            $scope.showFirma = function(index){
                if(!angular.isUndefined($scope.view.titularesOrigen)){
                    if(!angular.isUndefined($scope.view.titularesOrigen[index])){
                        var modalInstance = $modal.open({
                            templateUrl: 'views/cajero/util/firmaPopUp.html',
                            controller: "FirmaPopUpController",
                            resolve: {
                                idPersonas: function () {
                                    var idPersonas = [];
                                    idPersonas.push($scope.view.titularesOrigen[index].personaNatural.id);
                                    return idPersonas;
                                },
                                nombres: function(){
                                    var nombres = [];
                                    nombres.push($scope.view.titularesOrigen[index].personaNatural.apellidoPaterno+" "+
                                        $scope.view.titularesOrigen[index].personaNatural.apellidoMaterno+","+
                                        $scope.view.titularesOrigen[index].personaNatural.nombres);
                                    return nombres;
                                }
                            }
                        });
                        modalInstance.result.then(function (cuenta) {
                        }, function () {
                        });
                    }
                }
            };
            $scope.showFirmaTodos = function(){
                if(!angular.isUndefined($scope.view.titularesOrigen)){
                    var modalInstance = $modal.open({
                        templateUrl: 'views/cajero/util/firmaPopUp.html',
                        controller: "FirmaPopUpController",
                        resolve: {
                            idPersonas: function () {
                                var idPersonas = [];
                                for(var i = 0; i < $scope.view.titularesOrigen.length; i++)
                                    idPersonas.push($scope.view.titularesOrigen[i].personaNatural.id);
                                return idPersonas;
                            },
                            nombres: function(){
                                var nombres = [];
                                for(var i = 0; i < $scope.view.titularesOrigen.length; i++)
                                    nombres.push($scope.view.titularesOrigen[i].personaNatural.apellidoPaterno+
                                        " "+$scope.view.titularesOrigen[i].personaNatural.apellidoMaterno+
                                        ","+$scope.view.titularesOrigen[i].personaNatural.nombres);
                                return nombres;
                            }
                        }
                    });
                    modalInstance.result.then(function (cuenta) {
                    }, function () {
                    });
                }
            }

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
