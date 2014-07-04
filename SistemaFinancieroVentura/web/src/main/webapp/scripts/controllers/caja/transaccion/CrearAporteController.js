define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearAporteController', ["$scope", "$state", "$window", "$filter", "$modal","focus", "CajaSessionService",
        function($scope, $state, $window, $filter, $modal,focus,CajaSessionService) {

            $scope.viewState = "app.transaccion.aporte";

            $scope.focusElements = {
                buscarSocio: 'focusBuscarSocio',
                monto: 'focusMonto',
                mes: 'focusMes',
                referencia: 'focusReferencia',
                guardar: 'focusGuardar'
            };

            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus($scope.focusElements.buscarSocio);
            };
            $scope.setInitialFocus();

            $scope.control = {
                success:false,
                inProcess: false,
                submitted : false
            };

            $scope.combo = {
                mes: [
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
                ]
            };

            $scope.view = {
                monto: parseFloat("0.00"),
                mes: undefined,
                anio: new Date().getFullYear()
            };

            $scope.objetosCargados = {
                cuentaAporte: undefined,
                socio: undefined
            };

            $scope.openBuscarSocio = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/buscarSocioPopUp.html',
                    controller: "BuscarSocioPopUpController",
                    size: 'lg'
                });

                modalInstance.result.then(function (socio) {
                    $scope.objetosCargados.socio = socio;
                    focus($scope.focusElements.monto);
                }, function () {
                });
            };

            $scope.openCalculadora = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/calculadora.html',
                    controller: "CalculadoraController"
                });

                modalInstance.result.then(function (total) {
                    $scope.view.monto = total;
                    focus($scope.focusElements.mes);
                }, function () {
                });
            };

            //transaccion
            $scope.crearTransaccion = function(){
                if($scope.formCrearTransaccion.$valid){
                    $scope.control.inProcess = true;

                    if(angular.isUndefined($scope.objetosCargados.socio)){
                        $scope.alerts = [{ type: "danger", msg: "Socio no cargado."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    }
                    if(parseFloat($scope.view.monto) <= 0){
                        $scope.alerts = [{ type: "danger", msg: "Monto de transaccion no valido."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    }

                    var transaccion = {
                        "idSocio" : $scope.objetosCargados.socio.id,
                        "monto": $scope.view.monto,
                        "mes": $scope.view.mes.value,
                        "anio": $scope.view.anio,
                        "referencia" : $scope.view.referencia
                    };
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
            };

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            };

            $scope.cancel = function(){
                $state.transitionTo('app.transaccion.aporte');
            };

        }]);
});
