define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('ContratoInactivadoSocioController', [ '$scope','$state','CajaSessionService','SocioService',
        function($scope,$state,CajaSessionService,SocioService) {

            $scope.view = {
                condiciones: false
            };

            $scope.siguiente = function(){
                if(!angular.isUndefined($scope.id)){
                    if($scope.view.condiciones == true)
                        $scope.retirarSaldoCuentaAporte();
                }
            };

            $scope.retirarSaldoCuentaAporte = function(){
                if(!angular.isUndefined($scope.id)){
                    CajaSessionService.crearRetiroCuentaAporte($scope.id).then(
                        function(data){
                            var idTransaccion = data.id;
                            $state.transitionTo("app.socio.voucherCancelacionCuenta", { id:$scope.id, idTransaccion:idTransaccion });
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };
            $scope.inactivarSocio = function(){
                if(!angular.isUndefined($scope.id)){
                    SocioService.inactivarSocio($scope.id).then(
                        function(data){
                            $state.transitionTo("app.socio.panelSocio", { id:$scope.id});
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };

            $scope.loadVoucherRetiroCuentaAporte = function(){
                if(!angular.isUndefined($scope.idTransaccion)){
                    CajaSessionService.getVoucherCancelacionCuentaAporte($scope.idTransaccion).then(
                        function(data){
                            $scope.transaccion = data;
                        }
                    );
                }
            };
            $scope.loadVoucherRetiroCuentaAporte();

            $scope.imprimir = function(){
                if(angular.isUndefined($scope.transaccion))
                    return;

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
                qz.append("PENDIENTE:" + "\t" +($scope.pendiente.idPendienteCaja) + "\r\n");
                qz.append("T. PENDIENTE:" + "\t" +($scope.pendiente.tipoPendiente) + "\r\n");
                qz.append("FECHA:" + "\t\t" +($filter('date')($scope.pendiente.fecha, 'dd/MM/yyyy'))+ " " + ($filter('date')($scope.pendiente.hora, 'HH:mm:ss')) + "\r\n");
                qz.append("MONEDA:" + "\t\t" +($scope.pendiente.moneda.denominacion) + "\r\n");
                qz.append("MONTO:" + "\t\t" +($filter('currency')($scope.pendiente.monto, $scope.pendiente.moneda.simbolo)) + "\r\n");
                qz.append("CAJA:" + "\t\t" +($scope.pendiente.cajaDenominacion) + "(" +($scope.pendiente.cajaAbreviatura) + ")" + "\r\n");
                qz.append("CAJERO:" + "\t\t" +($scope.pendiente.trabajador) + "\r\n");
                qz.append("OBSERVACION:" + "\t" +($scope.pendiente.observacion) + "\r\n");
                qz.append("\r\n");
                qz.append("\r\n");
                qz.append("     " + "______________" + "\t" + "__________" + "\r\n");
                qz.append("     " + " JEFE DE CAJA " + "\t" + "  CAJERO  " + "\r\n");

                qz.append("\x1D\x56\x41");														//cortar papel
                qz.append("\x1B\x40");
                qz.print();
            };

            $scope.cancelar = function(){
                $state.transitionTo("app.socio.panelSocio", { id: $scope.id });
            };
        }]);
});