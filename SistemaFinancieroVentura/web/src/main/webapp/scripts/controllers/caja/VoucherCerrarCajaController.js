define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherCerrarCajaController', ['$scope', "$state", '$filter', "HistorialCajaService",
        function($scope, $state, $filter, HistorialCajaService) {

            HistorialCajaService.getVoucherCierreCaja($scope.id).then(
                function(voucher){
                    $scope.voucherByMoneda = voucher;
                }
            );
            HistorialCajaService.getResumenCierreCaja($scope.id).then(
                function(resumen){
                    $scope.resumenCaja = resumen;
                }
            );

            $scope.total = function(detalle){
                var totalVoucher = 0;
                for(var i = 0; i < detalle.length; i++){
                    totalVoucher = totalVoucher + (detalle[i].valor*detalle[i].cantidad);
                }
                return totalVoucher;
            }

            $scope.imprimirResumen = function(){

                qz.findPrinter("EPSON TM-U220");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                qz.append("______ C.A.C. CAJA VENTURA ______\r\n");
                qz.append("\x1B\x21\x01"); // 3

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                qz.append("RESUMEN DE OPERACIONES\r\n");
                qz.append("\x1B\x21\x01"); // 3
                qz.append("Agencia:"+ $scope.resumenCaja.agencia + " ");
                qz.append("Caja:"+ $scope.resumenCaja.caja + " " + "\r\n");
                qz.append("F.Apertu:"+($filter('date')($scope.resumenCaja.fechaApertura, 'dd/MM/yyyy'))+ " ");
                qz.append("H.Apertu:"+($filter('date')($scope.resumenCaja.horaApertura, 'HH:mm:ss'))+"\r\n");
                qz.append("F.Cierre:"+($filter('date')($scope.resumenCaja.fechaCierre, 'dd/MM/yyyy'))+ " ");
                qz.append("H.Cierre:"+($filter('date')($scope.resumenCaja.horaCierre, 'HH:mm:ss'))+"\r\n");
                qz.append("Trabajador:"+$scope.resumenCaja.trabajador+"\r\n");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append("Depositos(Total):"+($scope.resumenCaja.depositosAporte + $scope.resumenCaja.depositosAhorro + $scope.resumenCaja.depositosPlazoFijo + $scope.resumenCaja.depositosCorriente)+"\n");
                qz.append("\x1B\x21\x01"); // 3
                qz.append("C.Aporte:"+$scope.resumenCaja.depositosAporte+" ");
                qz.append("C.Ahorro:"+$scope.resumenCaja.depositosAhorro+"\r\n");
                qz.append("C.P.fijo:"+$scope.resumenCaja.depositosPlazoFijo+" ");
                qz.append("C.Corrie:"+$scope.resumenCaja.depositosCorriente+"\r\n");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append("Retiros(Total):"+($scope.resumenCaja.retirosAporte + $scope.resumenCaja.retirosAhorro + $scope.resumenCaja.retirosPlazoFijo + $scope.resumenCaja.retirosCorriente)+"\n");
                qz.append("\x1B\x21\x01"); // 3
                qz.append("C.Aporte:"+$scope.resumenCaja.retirosAporte+" ");
                qz.append("C.Ahorro:"+$scope.resumenCaja.retirosAhorro+"\r\n");
                qz.append("C.P.fijo:"+$scope.resumenCaja.retirosPlazoFijo+" ");
                qz.append("C.Corrie:"+$scope.resumenCaja.retirosCorriente+"\r\n");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append("Compra/Venta(Total):"+($scope.resumenCaja.compra + $scope.resumenCaja.venta)+"\n");
                qz.append("\x1B\x21\x01"); // 3
                qz.append("Compra:"+$scope.resumenCaja.compra+" ");
                qz.append("Venta:"+$scope.resumenCaja.venta+"\r\n");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append("Trans. mayor cuantia(Total):"+($scope.resumenCaja.depositoMayorCuantia+$scope.resumenCaja.retiroMayorCuantia+$scope.resumenCaja.compraVentaMayorCuantia)+"\n");
                qz.append("\x1B\x21\x01"); // 3
                qz.append("Depositos:"+$scope.resumenCaja.depositoMayorCuantia+" ");
                qz.append("Retiros  :"+$scope.resumenCaja.retiroMayorCuantia+"\r\n");
                qz.append("Compra/venta:"+$scope.resumenCaja.compraVentaMayorCuantia+"\r\n");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append("Trans. caja-caja(Total):"+($scope.resumenCaja.enviadoCajaCaja+$scope.resumenCaja.recibidoCajaCaja)+"\n");
                qz.append("\x1B\x21\x01"); // 3
                qz.append("Enviado :"+$scope.resumenCaja.enviadoCajaCaja+" ");
                qz.append("Recibido:"+$scope.resumenCaja.recibidoCajaCaja+"\r\n");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append("Trans. boveda-caja(Total):"+($scope.resumenCaja.enviadoBovedaCaja+$scope.resumenCaja.enviadoBovedaCaja)+"\n");
                qz.append("\x1B\x21\x01"); // 3
                qz.append("Enviado :"+$scope.resumenCaja.enviadoBovedaCaja+" ");
                qz.append("Recibido:"+$scope.resumenCaja.enviadoBovedaCaja+"\r\n");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append("Cierre caja(Pendiente):\n");
                qz.append("\x1B\x21\x01"); // 3
                qz.append("Sobrante :"+$scope.resumenCaja.pendienteSobrante+" ");
                qz.append("Faltante:"+$scope.resumenCaja.pendienteSobrante+"\r\n");

                qz.append("\x1D\x56\x41"); // 4
                qz.append("\x1B\x40"); // 5
                qz.print();
            }

            $scope.imprimirVoucherPorMoneda = function(index){

                qz.findPrinter("EPSON TM-U220");

                $scope.voucherPrint = $scope.voucherByMoneda[index];

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                qz.append("______ C.A.C. CAJA VENTURA ______\r\n");
                qz.append("\x1B\x21\x01"); // 3

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                qz.append("VOUCHER CIERRE CAJA\r\n");
                qz.append("\x1B\x21\x01"); // 3
                qz.append("Agencia:"+ $scope.voucherPrint.agencia + " ");
                qz.append("Caja:"+ $scope.voucherPrint.caja + " " + "\r\n");
                qz.append("F.Apertu:"+($filter('date')($scope.voucherPrint.fechaApertura, 'dd/MM/yyyy'))+ " ");
                qz.append("H.Apertu:"+($filter('date')($scope.voucherPrint.horaApertura, 'HH:mm:ss'))+"\r\n");
                qz.append("F.Cierre:"+($filter('date')($scope.voucherPrint.fechaCierre, 'dd/MM/yyyy'))+ " ");
                qz.append("H.Cierre:"+($filter('date')($scope.voucherPrint.horaCierre, 'HH:mm:ss'))+"\r\n");
                qz.append("Trabajador:"+$scope.voucherPrint.trabajador+"\r\n");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append("Denominacion   Cantidad   Subtotal"+"\n");
                qz.append("\x1B\x21\x01"); // 3
                for(var i = 0; i<$scope.voucherPrint.detalle.length;i++){
                    qz.append($scope.voucherPrint.detalle.valor + "   ");
                    qz.append($scope.voucherPrint.detalle.cantidad + "   ");
                    qz.append($filter('currency')(($scope.voucherPrint.detalle.valor*$scope.voucherPrint.detalle.cantidad),$scope.voucherPrint.moneda.simbolo)+ "\r\n");
                }

                qz.append("\n");
                qz.append("Saldo ayer:"+$scope.voucherPrint.saldoAyer+"\r\n");
                qz.append("Entradas:"+$scope.voucherPrint.entradas+"\r\n");
                qz.append("Salidas:"+$scope.voucherPrint.salidas+"\r\n");
                qz.append("Sobrantes:"+$scope.voucherPrint.entradas+"\r\n");
                qz.append("Faltantes:"+$scope.voucherPrint.salidas+"\r\n");
                qz.append("Faltantes:"+$scope.voucherPrint.porDevolver+"\r\n");

                qz.append("\x1D\x56\x41"); // 4
                qz.append("\x1B\x40"); // 5
                qz.print();
            }
        }]);
});