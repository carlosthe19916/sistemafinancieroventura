define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearTransferenciaController', ["$scope", "$state", "$window", "$filter", "$modal","focus","CuentaBancariaService","CajaSessionService","MonedaService","TransitionService",
        function($scope, $state, $window, $filter, $modal,focus, CuentaBancariaService, CajaSessionService, MonedaService, TransitionService) {

            $scope.viewState = 'app.transaccion.transferencia';
            $scope.focusElements = {
                buscarCuentaOrigen: 'focusBuscarCuentaOrigen',
                buscarCuentaDestino: 'focusBuscarCuentaDestino',
                monto: 'focusMonto',
                referencia: 'focusReferencia'
            };
            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus($scope.focusElements.buscarCuentaOrigen);
            };
            $scope.setInitialFocus();

            $scope.control = {
                success: false,
                inProcess: false,
                submitted : false
            };

            $scope.view = {
                numeroCuentaOrigen: undefined,
                numeroCuentaDestino: undefined,
                monto: parseFloat('0.00'),
                referencia: undefined
            };

            $scope.objetosCargados = {
                cuentaBancariaOrigen: undefined,
                cuentaBancariaDestino: undefined,
                denominacionesMonedaOrigen: [],
                titularesOrigen: []
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
                        $scope.objetosCargados.cuentaBancariaOrigen = cuenta;
                        $scope.view.numeroCuentaOrigen = cuenta.numeroCuenta;
                        focus($scope.focusElements.buscarCuentaDestino);
                    }
                    if(destino == "destino"){
                        $scope.objetosCargados.cuentaBancariaDestino = cuenta;
                        $scope.view.numeroCuentaDestino = cuenta.numeroCuenta;
                        focus($scope.focusElements.monto);
                    }
                }, function () {
                });
            };

            $scope.openCalculadora = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/calculadora.html',
                    controller: "CalculadoraController",
                    resolve: {
                        denominaciones: function () {
                            return !angular.isUndefined($scope.objetosCargados.denominacionesMonedaOrigen) ? $scope.objetosCargados.denominacionesMonedaOrigen : [];
                        },
                        moneda: function () {
                            return !angular.isUndefined($scope.objetosCargados.cuentaBancariaOrigen) ? $scope.objetosCargados.cuentaBancariaOrigen.moneda.simbolo : '';
                        }
                    }
                });
                modalInstance.result.then(function (total) {
                    $scope.view.monto = total;
                }, function () {
                });
            };

            $scope.loadTitulares = function(){
                if(!angular.isUndefined($scope.objetosCargados.cuentaBancariaOrigen)){
                    CuentaBancariaService.getTitulares($scope.objetosCargados.cuentaBancariaOrigen.id).then(
                        function(data){
                            $scope.objetosCargados.titularesOrigen = data;
                        }
                    );
                }
            };

            $scope.loadDenominacionesMoneda = function(){
                if(!angular.isUndefined($scope.objetosCargados.cuentaBancariaOrigen)){
                    MonedaService.getDenominaciones($scope.objetosCargados.cuentaBancariaOrigen.moneda.id).then(function(data){
                        $scope.objetosCargados.denominacionesMonedaOrigen = data;
                    });
                }
            };

            $scope.$watch('objetosCargados.cuentaBancariaOrigen', function(){
                if(!angular.isUndefined($scope.objetosCargados.cuentaBancariaOrigen)){
                    $scope.loadTitulares();
                    $scope.loadDenominacionesMoneda();
                }
            });
            $scope.$watch("objetosCargados.cuentaBancariaDestino",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    if(!angular.isUndefined($scope.objetosCargados.cuentaBancariaOrigen)){
                        if(!angular.isUndefined($scope.objetosCargados.cuentaBancariaDestino)){
                            if($scope.objetosCargados.cuentaBancariaOrigen.numeroCuenta == $scope.objetosCargados.cuentaBancariaDestino.numeroCuenta){
                                $scope.alerts = [{ type: "warning", msg: "Warning: origen y detino iguales."}];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            }
                        }
                    }
                }
            },true);

            $scope.showFirma = function(index){
                if(!angular.isUndefined($scope.objetosCargados.titularesOrigen)){
                    if(!angular.isUndefined($scope.objetosCargados.titularesOrigen[index])){
                        var modalInstance = $modal.open({
                            templateUrl: 'views/cajero/util/firmaPopUp.html',
                            controller: "FirmaPopUpController",
                            resolve: {
                                idPersonas: function () {
                                    var idPersonas = [];
                                    idPersonas.push($scope.objetosCargados.titularesOrigen[index].personaNatural.id);
                                    return idPersonas;
                                },
                                nombres: function(){
                                    var nombres = [];
                                    nombres.push($scope.objetosCargados.titularesOrigen[index].personaNatural.apellidoPaterno+" "+
                                        $scope.objetosCargados.titularesOrigen[index].personaNatural.apellidoMaterno+","+
                                        $scope.objetosCargados.titularesOrigen[index].personaNatural.nombres);
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
                if(!angular.isUndefined($scope.objetosCargados.titularesOrigen)){
                    var modalInstance = $modal.open({
                        templateUrl: 'views/cajero/util/firmaPopUp.html',
                        controller: "FirmaPopUpController",
                        resolve: {
                            idPersonas: function () {
                                var idPersonas = [];
                                for(var i = 0; i < $scope.objetosCargados.titularesOrigen.length; i++)
                                    idPersonas.push($scope.objetosCargados.titularesOrigen[i].personaNatural.id);
                                return idPersonas;
                            },
                            nombres: function(){
                                var nombres = [];
                                for(var i = 0; i < $scope.objetosCargados.titularesOrigen.length; i++)
                                    nombres.push($scope.objetosCargados.titularesOrigen[i].personaNatural.apellidoPaterno+
                                        " "+$scope.objetosCargados.titularesOrigen[i].personaNatural.apellidoMaterno+
                                        ","+$scope.objetosCargados.titularesOrigen[i].personaNatural.nombres);
                                return nombres;
                            }
                        }
                    });
                    modalInstance.result.then(function (cuenta) {
                    }, function () {
                    });
                }
            };

            //transaccion
            $scope.crearTransaccion = function(){
                if($scope.formCrearTransferencia.$valid){
                    if($scope.view.numeroCuentaOrigen == $scope.view.numeroCuentaDestino){
                        $scope.alerts = [{ type: "danger", msg: "Error: el origen y el destino son los mismos."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        return;
                    }

                    $scope.transaccion = {
                        "numeroCuentaOrigen" : $scope.view.numeroCuentaOrigen,
                        "numeroCuentaDestino" : $scope.view.numeroCuentaDestino,
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
            };

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            };

            $scope.cancel = function(){

            };

        }]);
});
