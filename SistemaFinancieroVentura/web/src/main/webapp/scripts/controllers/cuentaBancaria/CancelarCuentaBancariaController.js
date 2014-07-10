define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CancelarCuentaBancariaController', [ "$scope","$state","$location","$filter","$window","focus","$modal","CuentaBancariaService","CajaSessionService","RedirectService",
        function($scope,$state,$location,$filter,$window,focus,$modal,CuentaBancariaService,CajaSessionService,RedirectService) {

            $scope.viewState = "app.socio.cancelarCuentaBancaria";

            $scope.view = {
                condiciones: undefined
            };

            $scope.alerts = [];
            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};

            $scope.loadRedireccion = function(){
                if(RedirectService.haveNext()){
                    var state = RedirectService.getNextState();
                    if(state == $scope.viewState){
                        RedirectService.clearLast();
                    }
                }
            };

            //cargar datos
            $scope.loadCuentaBancaria = function(){
                if(!angular.isUndefined($scope.id)){
                    CuentaBancariaService.getCuentasBancaria($scope.id).then(
                        function(data){
                            $scope.cuentaBancaria = data;
                        }, function error(error){
                            $scope.cuentaBancaria = undefined;
                            $scope.alerts.push({ type: "danger", msg: "Cuenta bancaria no encontrada."});
                        }
                    );
                }
            };
            $scope.loadCuentaBancaria();

            $scope.cancelarCuentaBancaria = function(){
                if($scope.view.condiciones == true){
                    if(!angular.isUndefined($scope.cuentaBancaria)){
                        CajaSessionService.cancelarCuentaBancaria($scope.cuentaBancaria.id).then(
                            function(data){
                                var savedParameters = {
                                    id: $scope.cuentaBancaria.id
                                };
                                var sendParameters = { id: data.id };
                                var nextState = 'app.socio.editarCuentaBancaria';
                                RedirectService.addNext(nextState, savedParameters);
                                $state.transitionTo('app.transaccion.depositoRetiroVoucher', sendParameters);
                            }, function error(error){
                                $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            }
                        );
                    }
                } else {
                    alert("acepte los terminos y condiciones");
                }
            };

            $scope.cancelar = function(){
                if(!angular.isUndefined($scope.id)){
                    $state.transitionTo("app.socio.editarCuentaBancaria", { id: $scope.id, redirect: true });
                }
            };

        }]);
});