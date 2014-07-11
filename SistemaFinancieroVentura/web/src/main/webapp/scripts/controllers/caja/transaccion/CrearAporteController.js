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
                    {"denominacion":"ENERO","value":1},
                    {"denominacion":"FEBRERO","value":2},
                    {"denominacion":"MARZO","value":3},
                    {"denominacion":"ABRIL","value":4},
                    {"denominacion":"MAYO","value":5},
                    {"denominacion":"JUNIO","value":6},
                    {"denominacion":"JULIO","value":7},
                    {"denominacion":"AGOSTO","value":8},
                    {"denominacion":"SEPTIEMBRE","value":9},
                    {"denominacion":"OCTUBRE","value":10},
                    {"denominacion":"NOVIEMBRE","value":11},
                    {"denominacion":"DICIEMBRE","value":12}
                ]
            };

            $scope.view = {
                monto: "10.00",
                mes: undefined,
                anio: new Date().getFullYear()
            };

            $scope.objetosCargados = {
                cuentaAporte: undefined,
                socio: undefined
            };

            $scope.setCurrentMonth = function(){
                var currentDate = new Date();
                $scope.view.mes = $scope.combo.mes[currentDate.getMonth()];
            };
            $scope.setCurrentMonth();

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
                    $scope.setInitialFocus();
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

            $scope.buscarHistorialAportes = function(){
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/socio/historialAportesPopUp.html',
                    controller: "HistorialAportesPopUpController",
                    resolve: {
                        idSocio: function () {
                            return $scope.objetosCargados.socio.id
                        }
                    }
                });
                modalInstance.result.then(function (mes, anio) {
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
