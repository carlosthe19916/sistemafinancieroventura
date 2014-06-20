define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearTransaccionDepositoRetiroController', ["$scope", "$state", "$window", "$filter", "$modal", "CuentaBancariaService", "CajaSessionService","MonedaService","TransitionService",
        function($scope, $state, $window, $filter, $modal, CuentaBancariaService, CajaSessionService, MonedaService, TransitionService) {

            $scope.control = {"success":false, "inProcess": false, "submitted":false};

            $scope.tipotransacciones = [{"denominacion":"DEPOSITO","factor":1},{"denominacion":"RETIRO","factor":-1}];

            $scope.cuenta;

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

            $scope.loadDenominacionesMoneda = function(){
                if(!angular.isUndefined($scope.cuenta)){
                    MonedaService.getDenominaciones($scope.cuenta.moneda.id).then(function(data){
                        $scope.denominacionesMoneda = data;
                    });
                }
            }

            $scope.openCalculadora = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/calculadora.html',
                    controller: "CalculadoraController",
                    resolve: {
                        denominaciones: function () {
                            return !angular.isUndefined($scope.denominacionesMoneda) ? $scope.denominacionesMoneda : [];
                        },
                        moneda: function () {
                            return !angular.isUndefined($scope.cuenta) ? $scope.cuenta.moneda.simbolo : '';
                        }
                    }
                });

                modalInstance.result.then(function (total) {
                    $scope.transaccion.monto = total;
                }, function () {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.openBuscarCuentaBancaria = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/buscarCuentaBancariaPopUp.html',
                    controller: "BuscarCuentaBancariaPopUpController",
                    size: 'lg'
                });
                modalInstance.result.then(function (cuenta) {
                    setTimeout(function () {
                        angular.element(document.querySelector('#cmbTipoTransaccion')).focus();
                    }, 10);
                    $scope.cuenta = cuenta;
                    $scope.transaccion.numeroCuenta = $scope.cuenta.numeroCuenta;
                    $scope.loadTitulares();
                    $scope.loadDenominacionesMoneda();
                }, function () {
                });
            };

            $scope.loadTitulares = function(){
                CuentaBancariaService.getTitulares($scope.cuenta.id).then(
                    function(data){
                        $scope.titulares = data;
                    }
                );
            }
            $scope.showFirma = function(index){
                if(!angular.isUndefined($scope.titulares)){
                    if(!angular.isUndefined($scope.titulares[index])){
                        var modalInstance = $modal.open({
                            templateUrl: 'views/cajero/util/firmaPopUp.html',
                            controller: "FirmaPopUpController",
                            resolve: {
                                idPersona: function () {
                                    return $scope.titulares[index].personaNatural.id;
                                },
                                descripcion: function(){
                                    return ($scope.titulares[index].personaNatural.apellidoPaterno+" "+$scope.titulares[index].personaNatural.apellidoMaterno+","+$scope.titulares[index].personaNatural.nombres);
                                }
                            }
                        });
                        modalInstance.result.then(function (cuenta) {
                        }, function () {
                        });
                    }
                }
            }

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
