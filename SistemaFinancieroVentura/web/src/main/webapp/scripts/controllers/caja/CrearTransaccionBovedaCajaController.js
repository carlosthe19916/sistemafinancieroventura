define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearTransaccionBovedaCajaController', ['$scope','$state', '$filter','focus', "MonedaService", "CajaSessionService",
        function($scope,$state,$filter,focus,MonedaService,CajaSessionService) {

            $scope.focusElements = {
                boveda: 'focusBoveda'
            };
            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus($scope.focusElements.boveda);
            };
            $scope.setInitialFocus();

            $scope.control = {
                success:false,
                inProcess: false,
                submitted : false
            };

            $scope.view = {
              idBoveda: undefined
            };

            $scope.combo = {
                boveda: undefined
            };

            $scope.objetosCargados = {
                detalles: []
            };

            $scope.loadBovedas = function(){
                CajaSessionService.getBovedasOfCurrentCaja().then(
                    function(data){
                        $scope.combo.boveda = data;
                    }
                );
            };
            $scope.loadBovedas();

            $scope.loadDetalleBoveda = function(){
                if(!angular.isUndefined($scope.view.idBoveda)){
                    MonedaService.getDenominaciones($scope.view.idBoveda).then(
                        function(data){
                            $scope.objetosCargados.detalles = data;
                        },
                        function error(error){
                            $scope.objetosCargados.detalles = [];
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };
            $scope.$watch('view.idBoveda', function(newVal, oldVal){
                if(newVal != oldVal){
                    if(!angular.isUndefined($scope.view.idBoveda)){
                        $scope.loadDetalleBoveda();
                    }
                }
            });

            $scope.total = function(){
                var total = 0;
                for(var i = 0; i<$scope.objetosCargados.detalles.length; i++){
                    total = total + ($scope.objetosCargados.detalles[i].valor * $scope.objetosCargados.detalles[i].cantidad);
                }
                return total;
            };

            $scope.crearTransaccion = function(){
                if ($scope.formCrearTransaccionBovedaCaja.$valid && ($scope.total() != 0 || $scope.total() !== undefined)) {
                    $scope.control.inProcess = true;
                    CajaSessionService.crearTransaccionBovedaCaja($scope.view.idBoveda,$scope.objetosCargados.detalles).then(
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
            };

            $scope.cancelar = function(){
                $state.transitionTo('app.caja.buscarTransaccionBovedaCaja');
            };

        }]);
});