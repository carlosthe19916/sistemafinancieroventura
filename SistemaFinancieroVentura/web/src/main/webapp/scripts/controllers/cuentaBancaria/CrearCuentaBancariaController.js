define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("CrearCuentaBancariaController", ["$scope", "$state", "RedirectService",
        function($scope, $state, RedirectService) {

            $scope.viewState = 'app.socio.crearCuentaBancaria';

            $scope.cuentaSelected;
            $scope.cuentaAhorro = "views/cajero/cuentaBancaria/crearCuentaAhorro.html";
            $scope.cuentaCorriente = "views/cajero/cuentaBancaria/crearCuentaCorriente.html";
            $scope.cuentaPlazoFijo = "views/cajero/cuentaBancaria/crearCuentaPlazoFijo.html";

            $scope.loadRedireccion = function(){
                if(RedirectService.haveNext()){
                    var state = RedirectService.getNextState();
                    if(state == $scope.viewState){
                        var obj = RedirectService.getNextParamsState();
                        if(obj == 'AHORRO'){
                            $scope.cuentaSelected = $scope.cuentaAhorro;
                        } else if(obj == 'CORRIENTE'){
                            $scope.cuentaSelected = $scope.cuentaCorriente;
                        } else if(obj == 'PLAZO_FIJO'){
                            $scope.cuentaSelected = $scope.cuentaPlazoFijo;
                        }
                    }
                }
            };
            $scope.loadRedireccion();
        }]);
});