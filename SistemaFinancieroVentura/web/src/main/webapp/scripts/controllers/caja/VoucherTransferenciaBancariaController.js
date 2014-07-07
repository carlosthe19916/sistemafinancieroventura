define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherTransferenciaBancariaController', ["$scope", "$state", "$filter", "CuentaBancariaService", "RedirectService",
        function($scope, $state, $filter, CuentaBancariaService, RedirectService) {

            $scope.loadVoucher = function(){
                if(!angular.isUndefined($scope.id)){
                    CuentaBancariaService.getVoucherTransferenciaBancaria($scope.id).then(
                        function(data){
                            $scope.transferenciaCuentaBancaria = data;
                        },
                        function error(error){
                            alert("Transaccion Cuenta Bancaria no Encontrado");
                        }
                    );
                }
            };
            $scope.loadVoucher();

            $scope.salir = function(){
                $scope.redireccion();
            };

            $scope.redireccion = function(){
                if(RedirectService.haveNext()){
                    var nextState = RedirectService.getNextState();
                    var parametros = RedirectService.getNextParamsState();
                    $state.transitionTo(nextState,parametros);
                } else {
                    $state.transitionTo('app.transaccion.transferencia');
                }
            };

            $scope.imprimir = function(){
                qz.findPrinter("EPSON TM-U220");												//Elegir impresora
                qz.append("\x1B\x40");															//reset printer
                
                qz.append("\x1B\x21\x08");														//texto en negrita
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
                qz.append("C.A.C. CAJA VENTURA \r\n");											// \r\n salto de linea
                qz.append("TRANSFERENCIA BANCARIA " + "\r\n");
                																				// \t tabulador
                qz.append("\x1B\x21\x01");														//texto normal (no negrita)
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda
                
                qz.append(($scope.transferenciaCuentaBancaria.agenciaAbreviatura) + "\t\t" + "TRANS:" + "\t" + ($scope.transferenciaCuentaBancaria.id) + "\r\n");
                qz.append("CAJA:" + "\t" + ($scope.transferenciaCuentaBancaria.cajaDenominacion) + "\t\t" + "Nro OP:" + "\t" + ($scope.transferenciaCuentaBancaria.numeroOperacion) + "\r\n");
                qz.append("FECHA:" + "\t" + ($filter('date')($scope.transferenciaCuentaBancaria.fecha, 'dd/MM/yyyy')) + " " + ($filter('date')($scope.transferenciaCuentaBancaria.hora, 'HH:mm:ss')) + "\r\n");
                qz.append("CUENTA:" + "\t" + ($scope.transferenciaCuentaBancaria.numeroCuentaOrigen) + "\r\n");
                qz.append("CUENTA:" + "\t" + ($scope.transferenciaCuentaBancaria.numeroCuentaDestino) + "\r\n");
                qz.append("SOCIO:" + "\t" + ($scope.transferenciaCuentaBancaria.socio) + "\r\n");
                qz.append("MONEDA:" + "\t" + ($scope.transferenciaCuentaBancaria.moneda.denominacion) + "\r\n");

                if(!angular.isUndefined($scope.transferenciaCuentaBancaria.referencia))
                	qz.append("REF:" + "\t" + ($scope.transferenciaCuentaBancaria.referencia) + "\r\n");
                else{
                	qz.append(" ");
                }
                	
                if (($scope.transaccionCuentaBancaria.tipoTransaccion)=="DEPOSITO") {
                	qz.append("\r\n");
                    qz.append("IMPORTE RECIBIDO:" + "\t" + ($filter('currency')($scope.transferenciaCuentaBancaria.monto, $scope.transferenciaCuentaBancaria.moneda.simbolo)) + "\r\n");
                    qz.append("\r\n");
                    qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                	qz.append("Verifique su dinero antes  de retirarse de ventanilla" + "\r\n");
				} else {
					qz.append("\r\n");
					qz.append("IMPORTE PAGADO:" + "\t\t" + ($filter('currency')($scope.transferenciaCuentaBancaria.monto, $scope.transferenciaCuentaBancaria.moneda.simbolo)) + "\r\n");
	                qz.append("SALDO DISPONIBLE:" + "\t" + ($filter('currency')($scope.transferenciaCuentaBancaria.saldoDisponible, $scope.transferenciaCuentaBancaria.moneda.simbolo)) + "\r\n");
	                qz.append("\r\n");
	                qz.append("\r\n");
	                qz.append(String.fromCharCode(27) + "\x61" + "\x31");
					qz.append("_________________" + "\r\n");
					qz.append(String.fromCharCode(27) + "\x61" + "\x31");
	                qz.append("Firma Titular(es)" + "\r\n");
	                qz.append("\r\n");
	                qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                	qz.append("Verifique su dinero antes de retirarse  de ventanilla" + "\r\n");
				}
                qz.append("\x1D\x56\x41");														//cortar papel
                qz.append("\x1B\x40");
                qz.print();
            };
        }]);
});