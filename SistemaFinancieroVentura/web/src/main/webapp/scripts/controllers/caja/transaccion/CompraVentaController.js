define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CompraVentaController', ["$scope","$state","$window","$timeout","$filter","$modal","focus","CuentaBancariaService","CajaSessionService","MonedaService","PersonaNaturalService","TasaInteresService",
        function($scope,$state,$window,$timeout,$filter,$modal,focus,CuentaBancariaService,CajaSessionService,MonedaService,PersonaNaturalService,TasaInteresService) {

            $scope.viewState = 'app.transaccion.compraVenta';

            $scope.focusElements = {
                numeroDocumento: 'focusNumeroDocumento',
                tipoOperacion: 'focusTipoOperacion',
                montoRecibido: 'focusMontoRecibido',
                montoEntregado: 'focusMontoEntregado',

                linkEditar:'focusLinkEditar',
                tasaInteres: 'focusTasaInteresEdited'
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
                "idMonedaRecibida": undefined,
                "idMonedaEntregada": undefined,
                "montoRecibido": 0,
                "montoEntregado": 0,
                "tasaCambio":0
            };

            $scope.combo = {
                tipoOperacion:[
                    {denominacion: "COMPRA"},
                    {denominacion:"VENTA"}
                ],
                moneda: undefined
            };

            $scope.login = {
                "result":false,
                "tasaCambio": undefined
            };

            $scope.loadMonedas = function(){
                MonedaService.getMonedas().then(
                    function(data){
                        $scope.combo.moneda = data;
                    },
                    function error(error){
                        $scope.alerts = [{ type: "danger", msg: "No se cargaron las monedas"}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    }
                );
            };
            $scope.loadMonedas();

            $scope.getTipoMonedaRecibida = function(){
              if(!angular.isUndefined($scope.view.idMonedaRecibida) && !angular.isUndefined($scope.combo.moneda)){
                  for(var i=0;i<$scope.combo.moneda.length;i++)
                      if($scope.view.idMonedaRecibida == $scope.combo.moneda[i].id)
                        return $scope.combo.moneda[i];
              } else{
                  return undefined;
              }
            };
            $scope.getTipoMonedaEntregada = function(){
                if(!angular.isUndefined($scope.view.idMonedaEntregada) && !angular.isUndefined($scope.combo.moneda)){
                    for(var i=0;i<$scope.combo.moneda.length;i++)
                        if($scope.view.idMonedaEntregada == $scope.combo.moneda[i].id)
                            return $scope.combo.moneda[i];
                } else{
                    return undefined;
                }
            };

            $scope.buscarCliente = function($event){
                PersonaNaturalService.findByTipoNumeroDocumento(1, $scope.view.numeroDocumento).then(
                    function(data){
                        $scope.view.nombreCliente = data.apellidoPaterno +" "
                            + data.apellidoMaterno + " " + data.nombres;
                        focus($scope.focusElements.tipoOperacion);
                    }
                );
                if(!angular.isUndefined($event))
                    $event.preventDefault();
            };

            $scope.openLoginPopUp = function(){
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/loginPopUp.html',
                    controller: "LoginPopUpController"
                });
                modalInstance.result.then(function (result) {
                    $scope.login.result = result;
                    $timeout(function() {
                        focus($scope.focusElements.tasaInteres);
                    }, 100);
                }, function () {
                    $timeout(function() {
                        focus($scope.focusElements.linkEditar);
                    }, 100);
                });
            };
            $scope.setTasaInteres = function($event){
                if(!angular.isUndefined($scope.login.tasaCambio)){
                    var final = parseFloat($scope.login.tasaCambio.replace(',','.').replace(' ',''));
                    if(final >= 0 && final <= 100) {
                        $scope.view.tasaCambio = final;
                        $scope.login.result = false;
                        $timeout(function() {
                            if(!angular.isUndefined($scope.view.tipoOperacion)){
                                if($scope.view.tipoOperacion.denominacion == 'COMPRA'){
                                    focus($scope.focusElements.montoRecibido);
                                }
                                if($scope.view.tipoOperacion.denominacion == 'VENTA'){
                                    focus($scope.focusElements.montoEntregado);
                                }
                            }
                        }, 100);
                    }
                }
                if(!angular.isUndefined($event))
                    $event.preventDefault();
            };

            //transaccion
            $scope.crearTransaccion = function(){
                if($scope.formCrearCompraVenta.$valid){

                    var transaccion = {};
                    transaccion.tipoOperacion = $scope.view.tipoOperacion.denominacion;
                    transaccion.idMonedaRecibida = $scope.view.idMonedaRecibida;
                    transaccion.idMonedaEntregada = $scope.view.idMonedaEntregada;
                    transaccion.montoRecibido = Math.abs($scope.view.montoRecibido);
                    transaccion.montoEntregado = $scope.view.montoEntregado;
                    transaccion.tasaCambio = $scope.view.tasaCambio;
                    transaccion.referencia = $scope.view.numeroDocumento+"/"+$scope.view.nombreCliente;

                    $scope.control.inProcess = true;

                    CajaSessionService.crearTransaccionCompraVenta(transaccion).then(
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

            $scope.loadTasaCambio = function(){
                if($scope.formCrearCompraVenta.monedaRecibida.$valid && $scope.formCrearCompraVenta.monedaEntregada.$valid){
                    TasaInteresService.getTasaCambio($scope.view.idMonedaRecibida, $scope.view.idMonedaEntregada).then(function(data){
                        $scope.view.tasaCambio = data;
                    });
                } else {
                    $scope.view.tasaCambio = 0;
                }
            };

            $scope.$watch("view.idMonedaRecibida",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    if(!angular.isUndefined($scope.view.idMonedaRecibida) &&
                        !angular.isUndefined($scope.view.idMonedaEntregada)){
                        if($scope.view.idMonedaRecibida == $scope.view.idMonedaEntregada){
                            $scope.formCrearCompraVenta.monedaRecibida.$setValidity("sgmaxlength",false);
                        } else {
                            $scope.formCrearCompraVenta.monedaRecibida.$setValidity("sgmaxlength",true);
                            $scope.formCrearCompraVenta.monedaEntregada.$setValidity("sgmaxlength",true);
                        }
                        $scope.loadTasaCambio();
                    }
                }
            },true);
            $scope.$watch("view.idMonedaEntregada",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    if(!angular.isUndefined($scope.view.idMonedaRecibida) &&
                        !angular.isUndefined($scope.view.idMonedaEntregada)){
                        if($scope.view.idMonedaRecibida == $scope.view.idMonedaEntregada){
                            $scope.formCrearCompraVenta.monedaEntregada.$setValidity("sgmaxlength",false);
                        } else{
                            $scope.formCrearCompraVenta.monedaRecibida.$setValidity("sgmaxlength",true);
                            $scope.formCrearCompraVenta.monedaEntregada.$setValidity("sgmaxlength",true);
                            $scope.loadTasaCambio();
                        }
                    }
                }
            },true);

            $scope.$watch("view.montoRecibido",function (newVal, oldVal) {
                if(!angular.isUndefined($scope.view.tipoOperacion)){
                    if($scope.view.tipoOperacion.denominacion == 'COMPRA'){
                        if (newVal !== oldVal) {
                            if(!angular.isUndefined($scope.view.tasaCambio)){
                                $scope.view.montoEntregado = parseFloat($scope.view.montoRecibido)*$scope.view.tasaCambio;
                            } else {
                                $scope.view.montoEntregado = 0;
                            }
                        }
                    }
                }
            },true);
            $scope.$watch("view.montoEntregado",function (newVal, oldVal) {
                if(!angular.isUndefined($scope.view.tipoOperacion)){
                    if($scope.view.tipoOperacion.denominacion == 'VENTA'){
                        if (newVal !== oldVal) {
                            if(!angular.isUndefined($scope.view.tasaCambio)){
                                $scope.view.montoRecibido = parseFloat($scope.view.montoEntregado)*$scope.view.tasaCambio;
                            } else {
                                $scope.view.montoEntregado = 0;
                            }
                        }
                    }
                }
            },true);

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            };

            $scope.cancel = function(){

            };

        }]);
});
