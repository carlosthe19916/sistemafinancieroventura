define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherCompraVentaController', ["$scope", "$state", "$filter","CajaService", "RedirectService",
        function($scope, $state, $filter, CajaService, RedirectService) {

            $scope.loadTransaccionCompraVenta = function(){
                if(!angular.isUndefined($scope.id)){
                    CajaService.getVoucherCompraVenta($scope.id).then(
                        function(data){
                            $scope.compraVentaMoneda = data;
                        },
                        function error(error){
                            alert("Transaccion no encontrada");
                        }
                    );
                }
            };
            $scope.loadTransaccionCompraVenta();


            $scope.salir = function(){
                $scope.redireccion();
            };

            $scope.redireccion = function(){
                if(RedirectService.haveNext()){
                    var nextState = RedirectService.getNextState();
                    var parametros = RedirectService.getNextParamsState();
                    $state.transitionTo(nextState,parametros);
                } else {
                    $state.transitionTo('app.transaccion.compraVenta');
                }
            };

            $scope.imprimir = function(){
                qz.findPrinter("EPSON TM-U220");												//Elegir impresora
                qz.append("\x1B\x40");															//reset printer
                
                qz.append("\x1B\x21\x08");														//texto en negrita
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
                qz.append("C.A.C. CAJA VENTURA \r\n");											// \r\n salto de linea
                qz.append("COMPRA/VENTA" + "\r\n");
                																				// \t tabulador
                qz.append("\x1B\x21\x01");														//texto normal (no negrita)
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda
                
                qz.append(($scope.compraVentaMoneda.agenciaAbreviatura) + "\t\t" + "TRANS:" + "\t" + ($scope.compraVentaMoneda.id) + "\r\n");
                qz.append("CAJA:" + "\t\t" + ($scope.compraVentaMoneda.cajaDenominacion) + "\t\t" + "Nro OP:" + "\t" + ($scope.compraVentaMoneda.numeroOperacion) + "\r\n");
                qz.append("FECHA:" + "\t\t" + ($filter('date')($scope.compraVentaMoneda.fecha, 'dd/MM/yyyy')) + " " + ($filter('date')($scope.compraVentaMoneda.hora, 'HH:mm:ss')) + "\r\n");
                qz.append("RECIBIDO:" + "\t" + ($filter('currency')($scope.compraVentaMoneda.montoRecibido, $scope.compraVentaMoneda.monedaRecibida.simbolo)) + "\r\n");
                qz.append("ENTREGADO:" + "\t" + ($filter('currency')($scope.compraVentaMoneda.montoEntregado, $scope.compraVentaMoneda.monedaEntregada.simbolo)) + "\r\n");
                qz.append("TIP.CAMBIO:" + "\t" + ($scope.compraVentaMoneda.tipoCambio) + "\r\n");
                qz.append("CLIENTE:" + "\t" + ($scope.compraVentaMoneda.referencia) + "\r\n");

                qz.append("\r\n");
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                qz.append("Verifique su dinero antes  de retirarse de ventanilla" + "\r\n");

                qz.append("\x1D\x56\x41");														//cortar papel
                qz.append("\x1B\x40");
                qz.print();
            };
        }]);
});