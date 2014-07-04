define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherTransaccionAporteController', ["$scope", "$state", "$filter","focus", "SocioService", "RedirectService",
        function($scope, $state, $filter,focus, SocioService, RedirectService) {

            $scope.focusElements = {
                imprimir: 'focusImprimir',
                salir: 'focusSalir'
            };
            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus($scope.focusElements.imprimir);
            };
            $scope.setInitialFocus();

    		SocioService.getVoucherCuentaAporte($scope.id).then(
                function(data){
                    $scope.transaccionCuentaAporte = data;
                },
                function error(error){
                    alert("Transaccion Cuenta Aporte no Encontrado");
                }
            );

            $scope.salir = function(){
                $scope.redireccion();
            };

            $scope.redireccion = function(){
                if(RedirectService.haveNext()){
                    var nextState = RedirectService.getNextState();
                    var parametros = RedirectService.getNextParamsState();
                    $state.transitionTo(nextState,parametros);
                } else {
                    $state.transitionTo('app.transaccion.aporte');
                }
            };

            $scope.imprimir = function(){
                qz.findPrinter("EPSON TM-U220");												//Elegir impresora
                qz.append("\x1B\x40");															//reset printer
                
                qz.append("\x1B\x21\x08");														//texto en negrita
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
                qz.append("C.A.C. CAJA VENTURA \r\n");											// \r\n salto de linea
                qz.append(($scope.transaccionCuentaAporte.tipoTransaccion) + " CUENTA APORTE" + "\r\n");
                																				// \t tabulador
                qz.append("\x1B\x21\x01");														//texto normal (no negrita)
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda
                
                qz.append(($scope.transaccionCuentaAporte.agenciaAbreviatura) + "\t\t" + "TRANS:" + "\t" + ($scope.transaccionCuentaAporte.id) + "\r\n");
                qz.append("CAJA:" + "\t" + ($scope.transaccionCuentaAporte.cajaDenominacion) + "\t\t" + "Nro OP:" + "\t" + ($scope.transaccionCuentaAporte.numeroOperacion) + "\r\n");                
                qz.append("FECHA:" + "\t" + ($filter('date')($scope.transaccionCuentaAporte.fecha, 'dd/MM/yyyy')) + " " + ($filter('date')($scope.transaccionCuentaAporte.hora, 'HH:mm:ss')) + "\r\n");
                qz.append("CUENTA:" + "\t" + ($scope.transaccionCuentaAporte.numeroCuenta) + "\r\n");
                qz.append("SOCIO:" + "\t" + ($scope.transaccionCuentaAporte.socio) + "\r\n");
                //qz.append("MONEDA:" + "\t" + ($scope.transaccionCuentaAporte.moneda.denominacion) + "\r\n");
                
                if(!angular.isUndefined($scope.transaccionCuentaAporte.referencia))
                	qz.append("REF:" + "\t" + ($scope.transaccionCuentaAporte.referencia) + "\r\n");
                else{
                	qz.append(" ");
                }
                	
                if (($scope.transaccionCuentaAporte.tipoTransaccion)=="DEPOSITO") {
                	qz.append("\r\n");
                    qz.append("IMPORTE RECIBIDO:" + "\t" + ($filter('currency')($scope.transaccionCuentaAporte.monto, $scope.transaccionCuentaAporte.moneda.simbolo)) + "\r\n");
                    qz.append("SALDO DISPONIBLE:" + "\t" + ($filter('currency')($scope.transaccionCuentaAporte.saldoDisponible, $scope.transaccionCuentaAporte.moneda.simbolo)) + "\r\n");
                    qz.append("\r\n");
                    qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                	qz.append("Verifique su dinero antes  de retirarse de ventanilla" + "\r\n");
				} else {
					qz.append("\r\n");
					qz.append("IMPORTE PAGADO:" + "\t\t" + ($filter('currency')($scope.transaccionCuentaAporte.monto, $scope.transaccionCuentaAporte.moneda.simbolo)) + "\r\n");
	                qz.append("SALDO DISPONIBLE:" + "\t" + ($filter('currency')($scope.transaccionCuentaAporte.saldoDisponible, $scope.transaccionCuentaAporte.moneda.simbolo)) + "\r\n");
	                qz.append("\r\n");
	                qz.append("\r\n");
	                qz.append(String.fromCharCode(27) + "\x61" + "\x31");
					qz.append("_________________" + "\r\n");
					qz.append(String.fromCharCode(27) + "\x61" + "\x31");
	                qz.append("Firma Socio" + "\r\n");
	                qz.append("\r\n");
	                qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                	qz.append("Verifique su dinero antes de retirarse  de ventanilla" + "\r\n");
				}
                qz.append("\x1D\x56\x41");														//cortar papel
                qz.append("\x1B\x40");
                qz.print();

                focus($scope.focusElements.salir);
            };
        }]);
});