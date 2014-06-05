define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CerrarCajaController', ["$scope", "$state","$window", "$filter", "CajaSessionService",
        function($scope, $state,$window, $filter, CajaSessionService) {

            $scope.control = {"success":false, "inProcess": false};

            //cargar los datos del web service
            CajaSessionService.getDetalle().then(function(detalleCaja){
                for(var i = 0; i<detalleCaja.length; i++){
                    angular.forEach(detalleCaja[i].detalle, function(row){
                        row.subtotal = function(){
                            return this.valor * this.cantidad;
                        }
                    });
                }
                $scope.detalleCajaInicial = angular.copy(detalleCaja);
                $scope.detalleCajaFinal = angular.copy(detalleCaja);
            });


            //update table grid
            $scope.updateLayout = function(index){
                $scope.updateLayoutInicial[index];
                $scope.updateLayoutFinal[index];
            };

            //configurar las tablas
            $scope.myDataInicial = [];
            $scope.gridOptionsInicial = [];
            $scope.totalInicial = [];
            var gridLayoutPluginInicial = [];
            $scope.updateLayoutInicial = [];
            $scope.getTemplateInicial = function(index, simbolo){
                gridLayoutPluginInicial[index] = new ngGridLayoutPlugin();
                $scope.updateLayoutInicial[index] = function(){
                    gridLayoutPluginInicial[index].updateGridLayout();
                };

                $scope.myDataInicial[index] = $scope.detalleCajaInicial[index].detalle;
                $scope.gridOptionsInicial[index] = {
                    data: 'myDataInicial['+index+']',
                    plugins: [gridLayoutPluginInicial[index]],
                    multiSelect: false,
                    columnDefs: [
                        { field: "valor | currency : '"+simbolo+" '", displayName: "Valor" },
                        { field: "cantidad", displayName: "Cantidad" },
                        { field: "subtotal() | currency : '' ", displayName: "Subtotal" }
                    ]
                };
                $scope.totalInicial[index] = function(){
                    var total = 0;
                    for(var i = 0; i < $scope.myDataInicial[index].length; i++){
                        total = total + ($scope.myDataInicial[index][i].valor * $scope.myDataInicial[index][i].cantidad);
                    }
                    return $filter('currency')(total," ")
                }
                return $scope.gridOptionsInicial[index];
            }

            $scope.myDataFinal = [];
            $scope.gridOptionsFinal = [];
            $scope.totalFinal = [];
            var gridLayoutPluginFinal = [];
            $scope.updateLayoutFinal = [];
            $scope.getTemplateFinal = function(index, simbolo){
                gridLayoutPluginFinal[index] = new ngGridLayoutPlugin();
                $scope.updateLayoutFinal[index] = function(){
                    gridLayoutPluginFinal[index].updateGridLayout();
                };
                $scope.myDataFinal[index] = $scope.detalleCajaFinal[index].detalle;
                $scope.gridOptionsFinal[index] = {
                    data: 'myDataFinal['+index+']',
                    plugins: [gridLayoutPluginFinal[index]],
                    multiSelect: false,
                    enableCellSelection: true,
                    enableRowSelection: false,
                    enableCellEditOnFocus: true,
                    columnDefs: [
                        { field: "valor | currency : '"+simbolo+" '", displayName: "Valor", enableCellEdit: false },
                        { field: "cantidad", displayName: "Cantidad", enableCellEdit: true },
                        { field: "subtotal() | currency : '' ", displayName: "Subtotal", enableCellEdit: false }
                    ]
                };
                $scope.totalFinal[index] = function(){
                    var total = 0;
                    for(var i = 0; i < $scope.myDataFinal[index].length; i++){
                        total = total + ($scope.myDataFinal[index][i].valor * $scope.myDataFinal[index][i].cantidad);
                    }
                    return $filter('currency')(total," ")
                }
                return $scope.gridOptionsFinal[index];
            }

            //cerrar caja
            $scope.cerrarCaja = function () {
                $scope.control.inProcess = true;

                CajaSessionService.cerrar($scope.detalleCajaFinal).then(
                    function(data){
                        $scope.control.inProcess = false;
                        $scope.control.success = true;

                        $scope.cajaSession.abierto = false;
                        $scope.cajaSession.estadoMovimiento = false;
                        //redireccion
                        $state.transitionTo('app.caja.voucherCerrarCaja', { id: data.id });
                    },
                    function error(error){
                        $scope.control.inProcess = false;
                        $scope.control.success = false;

                        if(error.status == 400){
                            $scope.pendiente = [];
                            var mensajes = [];
                            for(var i = 0; i<error.data.length; i++){
                                $scope.pendiente[i] = {
                                    "idboveda":error.data[i].idboveda,
                                    "boveda": error.data[i].boveda,
                                    "monto": error.data[i].monto
                                }
                                mensajes[i] = {
                                    type: "danger",
                                    msg: 'Monto de cierre invalido en '+error.data[i].boveda +' necesita:'
                                };
                            }
                            $scope.alerts = mensajes;

                            $scope.crearPendiente = function(index){
                                $scope.closeAlert(index);
                                $window.open("http://localhost:8080/SistemaFinancieroVentura-web/index.html#/app/caja/pendiente/crear/?idboveda="+$scope.pendiente[index].idboveda+"&monto="+$scope.pendiente[index].monto);
                            }

                            $window.scrollTo(0,0);

                        } else {
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                        }
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    }
                );
            };

            $scope.cancelar = function(){
                $state.go("app.caja", null, { reload: true });
            }

            $scope.alertMessageDisplay = function(){
                if($scope.cajaSession.denominacion == "undefined")
                    return true;
                if($scope.cajaSession.abierto == false)
                    return true;
                else
                    return false;
            }

            $scope.buttonDisableState = function(){
                return $scope.alertMessageDisplay() || $scope.control.inProcess;
            }

        }]);
});
