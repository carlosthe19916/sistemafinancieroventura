define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearTransaccionDepositoRetiroController', ["$scope", "$state", "$window", "$filter", "$modal","focus","CuentaBancariaService", "CajaSessionService","MonedaService",
        function($scope, $state, $window, $filter, $modal,focus,CuentaBancariaService, CajaSessionService, MonedaService) {

            $scope.viewState = 'app.socio.crearCuentaBancaria';

            $scope.focusElements = {
                buscarCuenta: 'focusBuscarCuenta',
                tipoTransaccion: 'focusTipoTransaccion',
                monto: 'focusMonto'
            };
            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus($scope.focusElements.buscarCuenta);
            };
            $scope.setInitialFocus();

            $scope.control = {
                success: false,
                inProcess: false,
                submitted : false
            };

            $scope.combo = {
                tipoTransacciones: [
                    {denominacion: "DEPOSITO", factor: 1},
                    {denominacion: "RETIRO", factor: -1}
                ]
            };

            $scope.view = {
                numeroCuenta: undefined,
                tipoTransaccion: undefined,
                monto: parseFloat('0.00'),
                referencia: undefined
            };

            $scope.objetosCargados = {
                cuentaBancaria: undefined,
                denominacionesMoneda: [],
                titulares: []
            };

            $scope.loadParametros = function(){
                $scope.view.numeroCuenta = $scope.params.numeroCuenta;
                $scope.view.tipoTransaccion = $scope.params.tipoTransaccion;
                $scope.view.monto = parseFloat($scope.params.monto);
                $scope.view.referencia = $scope.params.referencia;
                $scope.loadCuentaBancaria();
            };

            $scope.loadCuentaBancaria = function(){
                if(!angular.isUndefined($scope.view.numeroCuenta) && $scope.view.numeroCuenta != null && $scope.numeroCuenta != ''){
                    CuentaBancariaService.findCuentaByNumeroCuenta($scope.view.numeroCuenta).then(
                        function(data){
                            $scope.objetosCargados.cuentaBancaria = data;
                        },
                        function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error: no se pudo cargar la cuenta bancaria."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };

            $scope.getTipoTransaccion = function(){
                for(var i=0; i<$scope.combo.tipoTransacciones.length; i++){
                    if($scope.view.tipoTransaccion == $scope.combo.tipoTransacciones[i].denominacion)
                        return $scope.combo.tipoTransacciones[i];
                }
            };

            $scope.openBuscarCuentaBancaria = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/buscarCuentaBancariaPopUp.html',
                    controller: "BuscarCuentaBancariaPopUpController",
                    size: 'lg'
                });
                modalInstance.result.then(function (cuenta) {
                    $scope.objetosCargados.cuentaBancaria = cuenta;
                    $scope.view.numeroCuenta = $scope.objetosCargados.cuentaBancaria.numeroCuenta;
                    focus($scope.focusElements.tipoTransaccion);
                }, function () {
                });
            };

            $scope.openCalculadora = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/calculadora.html',
                    controller: "CalculadoraController",
                    resolve: {
                        denominaciones: function () {
                            return !angular.isUndefined($scope.objetosCargados.denominacionesMoneda) ? $scope.objetosCargados.denominacionesMoneda : [];
                        },
                        moneda: function () {
                            return !angular.isUndefined($scope.objetosCargados.cuentaBancaria) ? $scope.objetosCargados.cuentaBancaria.moneda.simbolo : '';
                        }
                    }
                });

                modalInstance.result.then(function (total) {
                    $scope.view.monto = total;
                    focus($scope.focusElements.monto);
                }, function () {
                });
            };

            $scope.loadTitulares = function(){
                CuentaBancariaService.getTitulares($scope.objetosCargados.cuentaBancaria.id).then(
                    function(data){
                        $scope.objetosCargados.titulares = data;
                    }
                );
            };

            $scope.loadDenominacionesMoneda = function(){
                if(!angular.isUndefined($scope.objetosCargados.cuentaBancaria)){
                    MonedaService.getDenominaciones($scope.objetosCargados.cuentaBancaria.moneda.id).then(function(data){
                        $scope.objetosCargados.denominacionesMoneda = data;
                    });
                }
            };

            $scope.$watch('objetosCargados.cuentaBancaria', function(){
                if(!angular.isUndefined($scope.objetosCargados.cuentaBancaria)){
                    $scope.loadTitulares();
                    $scope.loadDenominacionesMoneda();
                }
            });

            //firmas
            $scope.showFirma = function(index){
                if(!angular.isUndefined($scope.objetosCargados.titulares)){
                    if(!angular.isUndefined($scope.objetosCargados.titulares[index])){
                        var modalInstance = $modal.open({
                            templateUrl: 'views/cajero/util/firmaPopUp.html',
                            controller: "FirmaPopUpController",
                            resolve: {
                                idPersonas: function () {
                                    var idPersonas = [];
                                    idPersonas.push($scope.objetosCargados.titulares[index].personaNatural.id);
                                    return idPersonas;
                                },
                                nombres: function(){
                                    var nombres = [];
                                    nombres.push($scope.objetosCargados.titulares[index].personaNatural.apellidoPaterno+" "+$scope.objetosCargados.titulares[index].personaNatural.apellidoMaterno+","+$scope.objetosCargados.titulares[index].personaNatural.nombres);
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
                if(!angular.isUndefined($scope.objetosCargados.titulares)){
                    var modalInstance = $modal.open({
                        templateUrl: 'views/cajero/util/firmaPopUp.html',
                        controller: "FirmaPopUpController",
                        resolve: {
                            idPersonas: function () {
                                var idPersonas = [];
                                for(var i = 0; i < $scope.objetosCargados.titulares.length; i++)
                                    idPersonas.push($scope.objetosCargados.titulares[i].personaNatural.id);
                                return idPersonas;
                            },
                            nombres: function(){
                                var nombres = [];
                                for(var i = 0; i < $scope.objetosCargados.titulares.length; i++)
                                    nombres.push($scope.objetosCargados.titulares[i].personaNatural.apellidoPaterno+" "+$scope.objetosCargados.titulares[i].personaNatural.apellidoMaterno+","+$scope.objetosCargados.titulares[i].personaNatural.nombres);
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
                if($scope.formCrearTransaccion.$valid){
                    $scope.control.inProcess = true;
                    var transaccion = {
                        "numeroCuenta" : $scope.view.numeroCuenta,
                        "monto": (Math.abs(parseFloat($scope.view.monto.toString())) * $scope.getTipoTransaccion().factor),
                        "referencia" : $scope.view.referencia
                    };
                    CajaSessionService.crearTransaccionBancaria(transaccion).then(
                        function(data){
                            $scope.control.success = true;
                            $scope.control.inProcess = false;
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
            };
            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            };

            $scope.cancel = function(){

            };

            $scope.loadParametros();

        }]);
});
