define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CancelarCuentaBancariaController', [ "$scope","$state","$location","$filter","$window","focus","$modal","CuentaBancariaService","RedirectService",
        function($scope,$state,$location,$filter,$window,focus,$modal,CuentaBancariaService,RedirectService) {

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

            $scope.crearTransaccion = function(){
                if(!angular.isUndefined($scope.cuentaBancaria)){
                    if($scope.cuentaBancaria.saldo > 0){
                        var savedParameters = {
                            id: $scope.id
                        };
                        var sendParameters = {
                            numeroCuenta: $scope.cuentaBancaria.numeroCuenta,
                            tipoTransaccion: 'RETIRO',
                            monto: $scope.cuentaBancaria.saldo,
                            referencia: 'RETIRO POR CANCELACION DE CUENTA'
                        };
                        var nextState = $scope.viewState;
                        RedirectService.addNext(nextState, savedParameters);
                        $state.transitionTo('app.transaccion.depositoRetiro', sendParameters);
                    }
                }
            };
            $scope.cancelarCuentaBancaria = function(){
                if($scope.view.condiciones == true){
                    if(!angular.isUndefined($scope.cuentaBancaria)){
                        if($scope.cuentaBancaria.saldo == 0){
                            CuentaBancariaService.cancelarCuenta($scope.cuentaBancaria.id).then(
                                function(data){
                                    $state.transitionTo("app.socio.editarCuentaBancaria", { id: $scope.cuentaBancaria.id, redirect: true});
                                }, function error(error){
                                    $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                                }
                            );
                        }
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