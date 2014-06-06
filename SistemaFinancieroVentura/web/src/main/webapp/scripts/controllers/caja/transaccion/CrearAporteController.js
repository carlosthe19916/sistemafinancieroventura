define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearAporteController', ["$scope", "$state", "$window", "$filter", "$modal", "CajaSessionService", "ngProgress",
        function($scope, $state, $window, $filter, $modal, CajaSessionService, ngProgress) {

            $scope.control = {"success":false, "inProcess": false, "submitted":false};

            $scope.meses = [
                {"denominacion":"ENERO","value":0},
                {"denominacion":"FEBRERO","value":1},
                {"denominacion":"MARZO","value":2},
                {"denominacion":"ABRIL","value":3},
                {"denominacion":"MAYO","value":4},
                {"denominacion":"JUNIO","value":5},
                {"denominacion":"JULIO","value":6},
                {"denominacion":"AGOSTO","value":7},
                {"denominacion":"SEPTIEMBRE","value":8},
                {"denominacion":"OCTUBRE","value":9},
                {"denominacion":"NOVIEMBRE","value":10},
                {"denominacion":"DICIEMBRE","value":11}
            ];

            $scope.cuenta;
            $scope.monto;

            $scope.transaccion = {
                "socio" : undefined,
                "idSocio":undefined,
                "monto" : 0,
                "montoReal" : 0,
                "mes" : undefined,
                "anio": new Date().getFullYear(),
                "referencia" : undefined
            }

            $scope.$watch('transaccion.monto', function() {
                $scope.transaccion.montoReal = Math.abs($scope.transaccion.monto);
            });

            $scope.openCalculadora = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'modules/common/views/util/calculadora.html',
                    controller: "CalculadoraController"
                });

                modalInstance.result.then(function (total) {
                    $scope.monto = total;
                }, function () {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.openBuscarSocio = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/buscarSocioPopUp.html',
                    controller: "BuscarSocioPopUpController",
                    size: 'lg'
                });

                modalInstance.result.then(function (socio) {
                    $scope.transaccion.socio = socio;
                }, function () {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };

            //transaccion
            $scope.crearTransaccion = function(){
                if($scope.formCrearTransaccion.$valid){
                    $scope.control.inProcess = true;
                    var transaccion = {
                        "idSocio" : $scope.transaccion.socio.id,
                        "monto": $scope.transaccion.montoReal,
                        "mes": $scope.transaccion.mes.value,
                        "anio": $scope.transaccion.anio,
                        "referencia" : $scope.transaccion.referencia
                    }
                    CajaSessionService.crearAporte(transaccion).then(
                        function(data){
                            $scope.control.success = true;
                            $scope.control.inProcess = false;
                            $state.transitionTo('app.transaccion.aporteVoucher', { id: data.id });
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $window.scrollTo(0,0);
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            }
            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            }

            $scope.cancel = function(){

            }

        }]);
});
