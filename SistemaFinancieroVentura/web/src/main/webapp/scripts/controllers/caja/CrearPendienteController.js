define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearPendienteController', ["$scope", "$state", "$filter", "$modal", "CajaSessionService","MonedaService",
        function($scope, $state, $filter,$modal, CajaSessionService, MonedaService) {

            $scope.Math = window.Math;

            $scope.control = {"success":false, "inProcess": false, "submitted":false};

            $scope.tipopendientes = [{"denominacion":"FALTANTE", "factor":1},{"denominacion":"SOBRANTE", "factor":-1}];
            $scope.monto;
            $scope.boveda;

            CajaSessionService.getBovedasOfCurrentCaja().then(function(bovedas){
                $scope.bovedas = bovedas;
                //cargar datos precargados
                if($scope.idboveda !== undefined && $scope.idboveda !== null){
                    for(var i = 0 ; i < $scope.bovedas.length ; i++){
                        if($scope.idboveda == $scope.bovedas[i].id){
                            $scope.boveda = $scope.bovedas[i];
                            $scope.bovedaChange($scope.boveda);
                        }
                    }
                }
            });

            $scope.cargarMonto = function(){
                if($scope.monto !== undefined && $scope.monto !== null){
                    if($scope.monto >= 0){
                        $scope.tipopendiente = $scope.tipopendientes[0];
                    } else {
                        $scope.tipopendiente = $scope.tipopendientes[1];
                    }
                    $scope.monto = Math.abs($scope.monto);
                }
            }
            $scope.cargarMonto();

            $scope.$watch('monto', function() {
                if($scope.tipopendiente !== undefined && $scope.tipopendiente !== null){
                    $scope.montoReal = Math.abs($scope.monto) * $scope.tipopendiente.factor;
                } else {
                    $scope.montoReal = Math.abs($scope.monto);
                }
            });

            $scope.bovedaChange = function(boveda){
                MonedaService.getDenominaciones($scope.boveda.moneda.id).then(
                    function(denominaciones){
                        $scope.denominacionesMoneda = denominaciones;
                    }
                );
            }


            $scope.open = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'modules/common/views/util/calculadora.html',
                    controller: "CalculadoraController",
                    resolve: {
                        denominaciones: function () {
                            return $scope.denominacionesMoneda;
                        },
                        moneda: function () {
                            return $scope.boveda.moneda.simbolo;
                        }
                    }
                });

                modalInstance.result.then(function (total) {
                    $scope.monto = total;
                }, function () {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.crearPendiente = function(){
                if($scope.formCrearPendiente.$valid && $scope.monto != 0){
                    $scope.control.inProcess = true;
                    CajaSessionService.crearPendiente($scope.boveda.id, (Math.abs($scope.monto) * $scope.tipopendiente.factor), $scope.observacion).then(
                        function(data){
                            $scope.control.inProcess = false;
                            $state.transitionTo('app.caja.pendienteVoucher', { id: data.id });
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            }

        }]);
});