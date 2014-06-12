define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherPendienteController', ["$scope", "$state", "$filter", "PendienteCajaService",
        function($scope, $state, $filter, PendienteCajaService) {

    	PendienteCajaService.getVoucherPendienteCaja($scope.id).then(
                 function(pendiente){
                     $scope.pendiente = pendiente;
                 }
         );    
    		
    		
            $scope.cancelar = function(){

            };

            $scope.imprimir = function(){
                qz.findPrinter("EPSON TM-U220");												//Elegir impresora
                qz.append("\x1B\x40");															//reset printer
                
                qz.append("\x1B\x21\x08");														//texto en negrita
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
                qz.append("C.A.C. CAJA VENTURA \r\n");											// \r\n salto de linea
                qz.append("PENDIENTE \r\n");
                																				// \t tabulador
                qz.append("\x1B\x21\x01");														//texto normal (no negrita)
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda
                
                qz.append("AGENCIA:" + "\t" +($scope.pendiente.agenciaAbreviatura) + "\r\n");
                qz.append("FECHA:" + "\t" +($filter('date')($scope.pendiente.fecha, 'dd/MM/yyyy'))+ " " + ($filter('date')($scope.pendiente.hora, 'HH:mm:ss')) + "\r\n");
                qz.append("TIPO PENDIENTE:" + "\t" +($scope.pendiente.tipoPendiente) + "\r\n");
                qz.append("MONEDA:" + "\t" +($scope.pendiente.moneda.denominacion) + "\r\n");
                qz.append("MONTO:" + "\t" +($filter('currency')($scope.pendiente.monto, $scope.pendiente.moneda.simbolo)) + "\r\n");
                
                qz.append("\x1D\x56\x41");														//cortar papel
                qz.append("\x1B\x40");
                qz.print();
            };
        }]);
});