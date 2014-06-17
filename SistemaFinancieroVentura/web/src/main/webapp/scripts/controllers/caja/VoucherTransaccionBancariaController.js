define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherTransaccionBancariaController', ["$scope", "$state", "$filter", "CuentaBancariaService",
        function($scope, $state, $filter, CuentaBancariaService) {

    		CuentaBancariaService.getVoucherCuentaBancaria($scope.id).then(
                function(transaccionCuentaBancaria){
                    $scope.transaccionCuentaBancaria = transaccionCuentaBancaria;
                },
                function error(error){
                    alert("Transaccion Cuenta Bancaria no Encontrado");
                }
            );

            $scope.cancelar = function(){

            };

            $scope.imprimir = function(){
                qz.findPrinter("EPSON TM-U220");												//Elegir impresora
                qz.append("\x1B\x40");															//reset printer
                
                //qz.appendImage("images/logo.png", "ESCP");
               // while (!qz.isDoneAppending()) {} // wait
               // qz.append("\r\n");
                
                qz.append("\x1B\x21\x08");														//texto en negrita
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
                qz.append("C.A.C. CAJA VENTURA \r\n");											// \r\n salto de linea
                qz.append(($scope.transaccionCuentaBancaria.tipoTransaccion) + " CUENTA " + ($scope.transaccionCuentaBancaria.tipoCuentaBancaria) + "\r\n");
                																				// \t tabulador
                qz.append("\x1B\x21\x01");														//texto normal (no negrita)
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda
                
                qz.append(($scope.transaccionCuentaBancaria.agenciaAbreviatura) + "\t\t" + "TRANS:" + "\t" + ($scope.transaccionCuentaBancaria.idTransaccionBancaria) + "\r\n");
                qz.append("CAJA:" + "\t" + ($scope.transaccionCuentaBancaria.cajaDenominacion) + "\t\t" + "Nro OP:" + "\t" + ($scope.transaccionCuentaBancaria.numeroOperacion) + "\r\n");                
                qz.append("FECHA:" + "\t" + ($filter('date')($scope.transaccionCuentaBancaria.fecha, 'dd/MM/yyyy')) + " " + ($filter('date')($scope.transaccionCuentaBancaria.hora, 'HH:mm:ss')) + "\r\n");
                qz.append("CUENTA:" + "\t" + ($scope.transaccionCuentaBancaria.numeroCuenta) + "\r\n");
                qz.append("SOCIO:" + "\t" + ($scope.transaccionCuentaBancaria.socio) + "\r\n");
                qz.append("MONEDA:" + "\t" + ($scope.transaccionCuentaBancaria.moneda.denominacion) + "\r\n");
                
                if(!angular.isUndefined($scope.transaccionCuentaBancaria.referencia))
                	qz.append("REF:" + "\t" + ($scope.transaccionCuentaBancaria.referencia) + "\r\n");
                else{
                	qz.append(" ");
                }
                	
                if (($scope.transaccionCuentaBancaria.tipoTransaccion)=="DEPOSITO") {
                	qz.append("\r\n");
                    qz.append("IMPORTE RECIBIDO:" + "\t" + $filter('currency')($scope.transaccionCuentaBancaria.monto, $scope.transaccionCuentaBancaria.moneda.simbolo) + "\r\n");
                    qz.append("\r\n");
                    qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                	qz.append("Verifique su dinero antes  de retirarse de ventanilla" + "\r\n");
				} else {
					qz.append("\r\n");
					qz.append("IMPORTE RECIBIDO:" + "\t" + $filter('currency')($scope.transaccionCuentaBancaria.monto, $scope.transaccionCuentaBancaria.moneda.simbolo) + "\r\n");
	                qz.append("SALDO DISPONIBLE:" + "\t" + $filter('currency')($scope.transaccionCuentaBancaria.saldoDisponible, $scope.transaccionCuentaBancaria.moneda.simbolo) + "\r\n");
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