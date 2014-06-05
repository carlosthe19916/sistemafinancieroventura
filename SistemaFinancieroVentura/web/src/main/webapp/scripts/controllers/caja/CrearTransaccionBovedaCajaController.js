define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearTransaccionBovedaCajaController', ['$scope', "$state", '$filter', "MonedaService", "CajaSessionService",
        function($scope, $state, $filter, MonedaService,CajaSessionService) {

            $scope.control = {"success":false, "inProcess": false, "submitted" : false};

            //objetos de transaccion
            $scope.boveda;
            $scope.detalles = [];
            //bovedas de caja
            $scope.bovedas = [];


            CajaSessionService.getBovedasOfCurrentCaja().then(
                function(bovedas){
                    $scope.bovedas = bovedas;
                }
            );

            $scope.cargarDetalle = function(){
                MonedaService.getDenominaciones($scope.boveda.moneda.id).then(
                    function(detalle){
                        $scope.detalles = detalle;
                    },
                    function error(error){
                        //mostrar error al usuario
                        $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    }
                );
            }

            $scope.total = function(){
                var total = 0;
                for(var i = 0; i<$scope.detalles.length; i++){
                    total = total + ($scope.detalles[i].valor * $scope.detalles[i].cantidad);
                }
                return total;
            }

            $scope.crearTransaccion = function(){
                if ($scope.formCrearTransaccionBovedaCaja.$valid && ($scope.total() != 0 || $scope.total() !== undefined)) {
                    $scope.control.inProcess = true;
                    CajaSessionService.crearTransaccionBovedaCaja($scope.boveda.id,$scope.detalles).then(
                        function(data){
                            $scope.control.inProcess = false;
                            $scope.control.success = true;
                            //redireccion al voucher
                            $state.transitionTo('app.caja.voucherTransaccionBovedaCaja', { id: data.id});
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;

                            //mostrar error al usuario
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            }
        }]);
});