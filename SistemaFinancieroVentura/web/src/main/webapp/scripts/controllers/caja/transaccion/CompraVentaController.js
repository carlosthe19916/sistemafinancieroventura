define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CompraVentaController', ["$scope","$state","$window","$timeout","$filter","$modal","focus","CuentaBancariaService","CajaSessionService","MonedaService",
        function($scope,$state,$window,$timeout,$filter,$modal,focus,CuentaBancariaService,CajaSessionService,MonedaService) {

            $scope.viewState = 'app.transaccion.compraVenta';

            $scope.focusElements = {
                numeroDocumento: 'focusNumeroDocumento'
            };
            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus($scope.focusElements.numeroDocumento);
            };
            $scope.setInitialFocus();

            $scope.control = {
                success: false,
                inProcess: false,
                submitted : false
            };

            $scope.view = {
                "numeroDocumento": undefined,
                "nombreCliente": undefined,
                "tipoOperacion": undefined,
                "monedaRecibida": undefined,
                "monedaEntregada": undefined,
                "montoRecibido": 0,
                "montoEntregado": 0,
                "tipoOperaciones":[{"denominacion": "COMPRA"},{"denominacion":"VENTA"}],
                "monedasRecibidas": undefined,
                "monedasEntregadas": undefined,
                "tasaCambio":0,
                "login":{"result":false, "tasaCambio": undefined}
            };
            $scope.transaccion = {
                "tipoOperacion":undefined,
                "idMonedaRecibida":undefined,
                "idMonedaEntregada":undefined,
                "montoRecibido":undefined,
                "montoEntregado":undefined,
                "tasaCambio":undefined,
                "referencia":undefined
            };

            $scope.$watch("view.monedaRecibida",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $scope.view.monedasEntregadas = angular.copy($scope.view.monedasRecibidas);
                    for(var i=0; i < $scope.view.monedasEntregadas.length; i++){
                        if($scope.view.monedaRecibida.id == $scope.view.monedasEntregadas[i].id){
                            $scope.view.monedasEntregadas.splice(i,1);
                        }
                    }
                }
            },true);
            $scope.$watch("view.monedaEntregada",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    if(!angular.isUndefined($scope.view.monedaRecibida)){

                    }
                }
            },true);
            $scope.$watch("view.montoRecibido",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $scope.view.montoRecibido = Math.abs($scope.view.montoRecibido);
                    if(!angular.isUndefined($scope.view.tasaCambio)){
                        $scope.view.montoEntregado = $scope.view.montoRecibido*$scope.view.tasaCambio;
                    } else {
                        $scope.view.montoEntregado = 0;
                    }
                }
            },true);

            MonedaService.getMonedas().then(
                function(data){
                    $scope.view.monedasRecibidas = angular.copy(data);
                    $scope.view.monedasEntregadas = angular.copy(data);
                },
                function error(error){
                    $scope.alerts = [{ type: "danger", msg: "No se cargaron las monedas"}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                }
            );

            $scope.buscarCliente = function($event){

            };
            $scope.openLoginPopUp = function(){
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/loginPopUp.html',
                    controller: "LoginPopUpController"
                });
                modalInstance.result.then(function (result) {
                    $scope.view.login.result = result;
                    $timeout(function() {
                        angular.element(document.querySelector('#txtTasaInteresEdited')).focus();
                    }, 100);
                }, function () {

                });
            };
            $scope.setTasaInteres = function($event){
                if(!angular.isUndefined($scope.view.login.tasaCambio)){
                    var final = parseFloat($scope.view.login.tasaCambio.replace(',','.').replace(' ',''));
                    if(final >= 0 && final <= 100) {
                        $scope.view.tasaCambio = final / 100;
                        $scope.view.login.result = false;
                        $timeout(function() {
                            angular.element(document.querySelector('#txtMontoRecibido')).focus();
                        }, 100);
                    }
                }
                if(!angular.isUndefined($event))
                    $event.preventDefault();
            };

            //transaccion
            $scope.crearTransaccion = function(){
                if($scope.formCrearCompraVenta.$valid){

                    $scope.transaccion.tipoOperacion = $scope.view.tipoOperacion.denominacion;
                    $scope.transaccion.idMonedaRecibida = $scope.view.monedaRecibida.id;
                    $scope.transaccion.idMonedaEntregada = $scope.view.monedaEntregada.id;
                    $scope.transaccion.montoRecibido = $scope.view.montoRecibido;
                    $scope.transaccion.montoEntregado = $scope.view.montoEntregado;
                    $scope.transaccion.tasaCambio = $scope.view.tasaCambio;
                    $scope.transaccion.referencia = $scope.view.numeroDocumento+"/"+$scope.view.nombreCliente;

                    $scope.control.inProcess = true;

                    CajaSessionService.crearTransaccionCompraVenta($scope.transaccion).then(
                        function(data){
                            $scope.control.success = true;
                            $scope.control.inProcess = false;
                            $state.transitionTo('app.transaccion.compraVentaVoucher', { id: data.id });
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

            };

        }]);
});
