define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("CrearCuentaBancariaController", ["$scope", "$state",
        function($scope, $state) {
            $scope.cuentaSelected;
            $scope.cuentaAhorro = "modules/caja/views/cuenta/crearCuentaAhorro.html";
            $scope.cuentaCorriente = "modules/caja/views/cuenta/crearCuentaCorriente.html";
            $scope.cuentaPlazoFijo = "modules/caja/views/cuenta/crearCuentaPlazoFijo.html";
        }]);
});