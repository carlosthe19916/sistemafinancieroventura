define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("CrearCuentaBancariaController", ["$scope", "$state",
        function($scope, $state) {
            $scope.cuentaSelected;
            $scope.cuentaAhorro = "views/cajero/cuentaBancaria/crearCuentaAhorro.html";
            $scope.cuentaCorriente = "views/cajero/cuentaBancaria/crearCuentaCorriente.html";
            $scope.cuentaPlazoFijo = "views/cajero/cuentaBancaria/crearCuentaPlazoFijo.html";
        }]);
});