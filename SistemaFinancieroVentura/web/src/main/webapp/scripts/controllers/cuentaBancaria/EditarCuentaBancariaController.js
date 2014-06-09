define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('EditarCuentaBancariaController', [ "$scope", "$state", "$filter", "$window", "focus", "MaestroService", "MonedaService", "PersonaNaturalService", "PersonaJuridicaService", "SocioService", "TasaInteresService", "CuentaBancariaService",
        function($scope, $state, $filter, $window, focus, MaestroService, MonedaService, PersonaNaturalService, PersonaJuridicaService, SocioService, TasaInteresService, CuentaBancariaService) {


            CuentaBancariaService.getCuentasBancaria($scope.id).then(
                function(data){
                    $scope.cuentaBancaria = data;
                }, function error(error){
                    $scope.cuentaBancaria = undefined;
                    $scope.alerts = [{ type: "danger", msg: "Cuenta bancaria no encontrada."}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                }
            );

            CuentaBancariaService.getSocio($scope.id).then(
                function(data){
                    $scope.socio = data;
                }, function error(error){
                    $scope.socio = undefined;
                    $scope.alerts = [{ type: "danger", msg: "Socio no encontrado."}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                }
            );

        }]);
});