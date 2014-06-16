define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("FirmasCuentaBancariaController", ["$scope", "$state","focus", "CuentaBancariaService",
        function($scope, $state, focus, CuentaBancariaService) {

            $scope.alerts = [];
            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};

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
            if(!angular.isUndefined($scope.id)){
                CuentaBancariaService.getSocio($scope.id).then(
                    function(data){
                        $scope.socio = data;
                    }, function error(error){
                        $scope.socio = undefined;
                        $scope.alerts.push({ type: "danger", msg: "Socio no encontrado."});
                    }
                );
            }
            if(!angular.isUndefined($scope.id)){
                CuentaBancariaService.getTitulares($scope.id).then(
                    function(data){
                        $scope.titulares = [];
                        var x = 0;
                        var y = 0;
                        for(var i = 0; i < data.length ; i++){
                            if(x == 0)
                                $scope.titulares[y] = [];
                            $scope.titulares[y][x] = data[i];
                            if(x != 2){
                                x++;
                            } else {
                                x = 0;
                                y++;
                            }
                        }
                    }, function error(error){
                        $scope.titulares = undefined;
                        $scope.alerts.push({ type: "danger", msg: "Titulares no encontrados."});
                    }
                );
            }

            $scope.goToPanelCuenta = function(){
                $state.transitionTo("app.socio.editarCuentaBancaria", { id: $scope.id });
            }

        }]);
});