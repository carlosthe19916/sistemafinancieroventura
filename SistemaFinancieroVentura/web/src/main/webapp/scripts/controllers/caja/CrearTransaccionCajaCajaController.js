define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearTransaccionCajaCajaController', ['$scope', "$state", '$filter', "CajaSessionService","AgenciaSessionService",
        function($scope, $state, $filter,CajaSessionService,AgenciaSessionService) {

            $scope.control = {"success":false, "inProcess": false, "submitted" : false};

            //objetos de transaccion
            $scope.moneda;
            $scope.caja;

            $scope.cajas = [];
            $scope.monedas = [];

            CajaSessionService.getMonedasOfCurrentCaja().then(
                function(monedas){
                    $scope.monedas = monedas;
                }
            );

            AgenciaSessionService.getCajasOfAgencia().then(
                function(cajas){
                    $scope.cajas = cajas;
                }
            );

            $scope.crearTransaccion = function(){
                if ($scope.formCrearTransaccionCajaCaja.$valid) {
                    $scope.control.inProcess = true;

                } else {
                    $scope.control.submitted = true;
                }
            }
        }]);
});