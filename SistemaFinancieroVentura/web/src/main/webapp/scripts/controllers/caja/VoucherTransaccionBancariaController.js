define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherTransaccionBancariaController', ["$scope", "$state", "$filter", "CuentaBancariaService",
        function($scope, $state, $filter, CuentaBancariaService) {

    		CuentaBancariaService.getVoucherCuentaBancaria($scope.id).then(
                function(transaccionCuentaBancaria){
                    $scope.transaccionCuentaBancaria = transaccionCuentaBancaria;
                },
                function error(error){
                    alert("transaccion Cuenta Bancaria no encontrado");
                }
            );

            $scope.cancelar = function(){

            };

            $scope.imprimir = function(){

                qz.findPrinter("EPSON TM-U220");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                qz.append("______ C.A.C. CAJA VENTURA ______\r\n");
                qz.append("\x1B\x21\x01"); // 3

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                qz.append("PENDIENTE\r\n");
                qz.append("\x1B\x21\x01"); // 3
                qz.append("Fecha:"+($filter('date')($scope.pendiente.fecha, 'dd/MM/yyyy'))+ " ");
                qz.append("Hora:"+($filter('date')($scope.pendiente.hora, 'HH:mm:ss'))+"\r\n");
                qz.append("Tipo:"+($scope.pendiente.tipoPendiente)+ "\r\n");
                qz.append("Moneda:"+($scope.pendiente.moneda.denominacion)+ "\r\n");
                qz.append("Monto:"+($filter('currency')($scope.pendiente.monto, $scope.pendiente.moneda.simbolo))+ "\r\n");

                qz.append("\x1D\x56\x41"); // 4
                qz.append("\x1B\x40"); // 5
                qz.print();
            }


        }]);
});