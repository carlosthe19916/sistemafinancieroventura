angular.module('cajaApp.controller', []);

angular.module('cajaApp.controller')
    .controller('cajaNavbarController', ["$rootScope", '$scope',
        function($rootScope, $scope, $cookieStore, $dialogs) {
            //$scope.caja = $rootScope.caja;
            //$scope.usuario = $rootScope.usuario;
            //$scope.agencia = $rootScope.agencia;
        }])
    .controller('AbrirCajaController', ["$rootScope", "$scope", "$state", '$filter', "CajaSessionService",
        function($rootScope, $scope, $state, $filter, CajaSessionService) {

            $scope.control = {"success":false, "inProcess": false};

            CajaSessionService.getDetalle().then(function(detalleCaja){
                for(var i = 0; i<detalleCaja.length; i++){
                    angular.forEach(detalleCaja[i].detalle, function(row){
                        row.subtotal = function(){
                            return this.valor * this.cantidad;
                        }
                    });
                }
                $scope.detalleCaja = angular.copy(detalleCaja);
            });

            $scope.myData = [];
            $scope.gridOptions = [];
            $scope.total = [];
            var gridLayoutPlugin = [];
            $scope.updateLayout = [];

            $scope.getTemplate = function(index, simbolo){
                gridLayoutPlugin[index] = new ngGridLayoutPlugin();
                $scope.updateLayout[index] = function(){
                    gridLayoutPlugin[index].updateGridLayout();
                };
                $scope.myData[index] = $scope.detalleCaja[index].detalle;
                $scope.gridOptions[index] = {
                    data: 'myData['+index+']',
                    plugins: [gridLayoutPlugin[index]],
                    multiSelect: false,
                    columnDefs: [
                        { field: "valor | currency : '"+simbolo+" '", displayName: "Valor" },
                        { field: "cantidad | number ", displayName: "Cantidad" },
                        { field: "subtotal() | currency : '' ", displayName: "Subtotal" }
                    ]
                };
                $scope.total[index] = function(){
                    var total = 0;
                    for(var i = 0; i < $scope.myData[index].length; i++){
                        total = total + ($scope.myData[index][i].valor * $scope.myData[index][i].cantidad);
                    }
                    return $filter('currency')(total," ")
                }
                return $scope.gridOptions[index];
            }

            $scope.abrirCaja = function () {
                $scope.control.inProcess = true;

                CajaSessionService.abrir().then(
                    function(data){
                        $scope.control.inProcess = false;
                        $scope.control.success = true;

                        $rootScope.cajaSession.abierto = true;
                        $rootScope.cajaSession.estadoMovimiento = true;

                        $state.go("app.caja", null, { reload: true })
                    },
                    function error(error){
                        $scope.control.inProcess = false;
                        $scope.control.success = false;
                        $scope.alerts = [
                            { type: 'danger', msg: 'Error:' + error.data + "." }
                        ];
                        $scope.closeAlert = function(index) {
                            $scope.alerts.splice(index, 1);
                        };
                    }
                );
            };

            $scope.cancelar = function(){
                $state.go("app.caja", null, { reload: true });
            }

            $scope.alertMessageDisplay = function(){
                if($rootScope.cajaSession.denominacion == "undefined")
                    return true;
                if($rootScope.cajaSession.abierto == true)
                    return true;
                else
                    return false;
            }

            $scope.buttonDisableState = function(){
                return $scope.alertMessageDisplay() || $scope.control.inProcess;
            }
        }])
    .controller('CerrarCajaController', ["$rootScope", "$scope", "$state", "$filter", "CajaSessionService",
        function($rootScope, $scope, $state, $filter, CajaSessionService) {

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

                        $rootScope.cajaSession.abierto = false;
                        $rootScope.cajaSession.estadoMovimiento = false;
                        //redireccion
                        $state.transitionTo('app.caja.voucherCerrarCaja', { id: data.id });
                    },
                    function error(error){
                        $scope.control.inProcess = false;
                        $scope.control.success = false;

                        if(error.status == 400){
                            var mensajes = [];
                            for(var i = 0; i<error.data.length; i++){
                                var link = "<a ng-click='crearPendiente(error.data[i])'>Crear Pendiente</a>";
                                mensajes[i] = {
                                    type: "danger",
                                    msg: 'Monto de cierre invalido en ' + error.data[i].boveda + ' necesita ' +
                                        '<a href="www.google.com">Crear pendiente</a>',
                                    prueba: error.data[i]
                                };
                            }
                            $scope.alerts = mensajes;

                            $scope.crearPendiente = function(){
                               alert("jaja");
                            }
                        } else {
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                        }
                        $scope.closeAlert = function(index) {
                            $scope.alerts.splice(index, 1);
                        };
                    }
                );
            };

            $scope.cancelar = function(){
                $state.go("app.caja", null, { reload: true });
            }

            $scope.alertMessageDisplay = function(){
                if($rootScope.cajaSession.denominacion == "undefined")
                    return true;
                if($rootScope.cajaSession.abierto == false)
                    return true;
                else
                    return false;
            }

            $scope.buttonDisableState = function(){
                return $scope.alertMessageDisplay() || $scope.control.inProcess;
            }

        }])
    .controller('BuscarPendienteController', ["$scope", "$state", "$filter", "CajaSessionService",
        function($scope, $state, $filter, CajaSessionService) {

            $scope.crear = function(){
                $state.transitionTo('app.caja.pendienteCrear');
            }

            CajaSessionService.getPendientes().then(
                function(pendientes){
                    $scope.pendientes = pendientes;
                }
            );

            $scope.gridOptions = {
                data: 'pendientes',
                multiSelect: false,
                columnDefs: [
                    { field: "moneda.denominacion", displayName: "Moneda"},
                    { field: "monto | currency : ''", displayName: "Monto"},
                    { field: "tipoPendiente", displayName: "Tipo"},
                    { field: "fecha | date : 'dd/MM/yyyy'", displayName: "Fecha"},
                    { field: "hora | date : 'HH:mm:ss' ", displayName: "Hora"},
                    {displayName: 'Voucher', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="voucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button></div>'}
                ]
            };

            $scope.voucher = function(pendiente) {
                $state.transitionTo('app.caja.pendienteVoucher', { id: pendiente.id });
            };

        }])
    .controller('CrearPendienteController', ["$scope", "$state", "$filter", "$modal", "CajaSessionService","MonedaService",
        function($scope, $state, $filter,$modal, CajaSessionService, MonedaService) {

            $scope.control = {"success":false, "inProcess": false, "submitted":false};

            $scope.tipopendientes = [{"denominacion":"FALTANTE", "factor":1},{"denominacion":"SOBRANTE", "factor":-1}];
            $scope.monto;
            $scope.boveda;

            CajaSessionService.getBovedasOfCurrentCaja().then(function(bovedas){
                $scope.bovedas = bovedas;
            });

            $scope.bovedaChange = function(boveda){
                MonedaService.getDenominaciones($scope.boveda.moneda.id).then(
                    function(denominaciones){
                        $scope.denominacionesMoneda = denominaciones;
                    }
                );
            }

            $scope.open = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'modules/common/views/util/calculadora.html',
                    controller: "CalculadoraController",
                    resolve: {
                        denominaciones: function () {
                            return $scope.denominacionesMoneda;
                        },
                        moneda: function () {
                            return $scope.boveda.moneda.simbolo;
                        }
                    }
                });

                modalInstance.result.then(function (total) {
                    $scope.monto = total;
                }, function () {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.crearPendiente = function(){
                if($scope.formCrearPendiente.$valid && $scope.monto != 0){
                    $scope.control.inProcess = true;
                    CajaSessionService.crearPendiente($scope.boveda.id, ($scope.monto * $scope.tipopendiente.factor), $scope.observacion).then(
                        function(data){
                            $scope.control.inProcess = false;
                            $state.transitionTo('app.caja.pendienteVoucher', { id: data.id });
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            }

        }])
    .controller('VoucherPendienteController', ["$scope", "$state", "$filter", "CajaSessionService",
        function($scope, $state, $filter, CajaSessionService) {

            CajaSessionService.getPendiente($scope.id).then(
                function(pendiente){
                    $scope.pendiente = pendiente;
                },
                function error(error){
                    alert("pendiente no encontrado");
                }
            );

            $scope.cancelar = function(){

            }

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


        }])
    .controller('HistorialCajaController', ['$scope', "$state", '$filter', "CajaSessionService",
        function($scope, $state, $filter, CajaSessionService) {

            $scope.today = function() {
                $scope.desde = new Date();
                $scope.hasta = new Date();
            };

            $scope.today();

            $scope.clear = function () {
                $scope.desde = null;
                $scope.hasta = null;
            };

            // Disable weekend selection
            $scope.disabled = function(date, mode) {
                return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
            };

            $scope.toggleMin = function() {
                $scope.minDate = $scope.minDate ? null : new Date();
            };
            $scope.toggleMin();

            $scope.openDesde = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openedDesde = true;
            };
            $scope.openHasta = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openedHasta = true;
            };

            $scope.dateOptions = {
                formatYear: 'yy',
                startingDay: 1
            };

            $scope.initDate = new Date('2016-15-20');
            $scope.formats = ['dd/MM/yyyy','dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[0];

            $scope.buscar = function(){
                CajaSessionService.getHistoriales($scope.desde.getTime(),$scope.hasta.getTime()).then(
                    function(historiales){
                        $scope.listHistoriales = historiales;
                    }
                );
            }

            $scope.getVoucher = function(cajaHistorial){
                $state.transitionTo('app.caja.voucherCerrarCaja', { id: cajaHistorial.id});
            }

            $scope.gridOptions = {
                data: 'listHistoriales',
                multiSelect: false,
                columnDefs: [
                    {field:"fechaApertura | date : 'dd/MM/yyyy'", displayName:'Fecha apertura'},
                    {field:"horaApertura | date : 'hh:mm:ss a'", displayName:'Hora apertura'},
                    {field:"fechaCierre | date : 'dd/MM/yyyy'", displayName:'Fecha cierre'},
                    {field:"horaCierre | date : 'HH:mm:ss'", displayName:'Hora cierre'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button></div>'}]
            };

        }])
    .controller('VoucherCerrarCajaController', ['$scope', "$state", '$filter', "HistorialCajaService",
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
        }])
    .controller('BuscarTransaccionBovedaCajaController', ['$scope', "$state", '$filter', "CajaSessionService",
        function($scope, $state, $filter, CajaSessionService) {

            $scope.nuevo = function(){
                $state.transitionTo('app.caja.createTransaccionBovedaCaja');
            }

            CajaSessionService.getTransaccionBovedaCajaEnviadas().then(
                function(enviados){
                    $scope.transaccionesEnviadas = enviados;
                }
            );
            CajaSessionService.getTransaccionBovedaCajaRecibidas().then(
                function(recibidos){
                    $scope.transaccionesRecibidas = recibidos;
                }
            );

            $scope.gridOptionsEnviados = {
                data: 'transaccionesEnviadas',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha | date : 'dd/MM/yyyy'", displayName:'Fecha'},
                    {field:"hora | date : 'HH:mm:ss'", displayName:'Hora'},
                    {field:"estadoSolicitud | date : 'dd/MM/yyyy'", displayName:'Estado solicitud'},
                    {field:"estadoConfirmacion | date : 'hh:mm:ss'", displayName:'Estado cierre'},
                    {field:"origen", displayName:'Origen'},
                    {field:"monto | currency :''", displayName:'Monto'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button></div>'}]
            };

            $scope.gridOptionsRecibidos = {
                data: 'transaccionesRecibidas',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha | date : 'dd/MM/yyyy'", displayName:'Fecha'},
                    {field:"hora | date : 'HH:mm:ss'", displayName:'Hora'},
                    {field:"estadoSolicitud | date : 'dd/MM/yyyy'", displayName:'Estado solicitud'},
                    {field:"estadoConfirmacion | date : 'hh:mm:ss'", displayName:'Estado cierre'},
                    {field:"origen", displayName:'Origen'},
                    {field:"monto | currency :''", displayName:'Monto'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button></div>'}]
            };

            $scope.getVoucher = function(row){
                $state.transitionTo('app.caja.voucherPendiente', { id: row.id });
            }

        }])
    .controller('CrearTransaccionBovedaCajaController', ['$scope', "$state", '$filter', "MonedaService", "CajaSessionService",
        function($scope, $state, $filter, MonedaService,CajaSessionService) {

            $scope.control = {"success":false, "inProcess": false, "submitted" : false};

            //objetos de transaccion
            $scope.boveda;
            $scope.detalles = [];
            //bovedas de caja
            $scope.bovedas = [];


            CajaSessionService.getBovedasOfCurrentCaja().then(
                function(bovedas){
                    $scope.bovedas = bovedas;
                }
            );

            $scope.cargarDetalle = function(){
                MonedaService.getDenominaciones($scope.boveda.moneda.id).then(
                    function(detalle){
                        $scope.detalles = detalle;
                    },
                    function error(error){
                        //mostrar error al usuario
                        $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    }
                );
            }

            $scope.total = function(){
                var total = 0;
                for(var i = 0; i<$scope.detalles.length; i++){
                    total = total + ($scope.detalles[i].valor * $scope.detalles[i].cantidad);
                }
                return total;
            }

            $scope.crearTransaccion = function(){
                if ($scope.formCrearTransaccionBovedaCaja.$valid && ($scope.total() != 0 || $scope.total() !== undefined)) {
                    $scope.control.inProcess = true;
                    CajaSessionService.crearTransaccionBovedaCaja($scope.boveda.id,$scope.detalles).then(
                        function(data){
                            $scope.control.inProcess = false;
                            $scope.control.success = true;
                            //redireccion al voucher
                            $state.transitionTo('app.caja.voucherTransaccionBovedaCaja', { id: data.id});
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;

                            //mostrar error al usuario
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            }
        }])
    .controller('VoucherTransaccionBovedaCajaController', ['$scope', "$state", '$filter', "MonedaService", "CajaSessionService",
        function($scope, $state, $filter, MonedaService,CajaSessionService) {


        }])
    .controller('BuscarTransaccionCajaCajaController', ['$scope', "$state", '$filter', "CajaSessionService",
        function($scope, $state, $filter, CajaSessionService) {

            $scope.nuevo = function(){
                $state.transitionTo('app.caja.createTransaccionCajaCaja');
            }

            CajaSessionService.getTransaccionCajaCajaEnviadas().then(
                function(enviados){
                    $scope.transaccionesEnviadas = enviados;
                }
            );
            CajaSessionService.getTransaccionCajaCajaRecibidas().then(
                function(recibidos){
                    $scope.transaccionesRecibidas = recibidos;
                }
            );

            $scope.gridOptionsEnviados = {
                data: 'transaccionesEnviadas',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha | date : 'dd/MM/yyyy'", displayName:'Fecha'},
                    {field:"hora | date : 'HH:mm:ss'", displayName:'Hora'},
                    {field:"estadoSolicitud | date : 'dd/MM/yyyy'", displayName:'Estado solicitud'},
                    {field:"estadoConfirmacion | date : 'hh:mm:ss'", displayName:'Estado cierre'},
                    {field:"origen", displayName:'Origen'},
                    {field:"monto | currency :''", displayName:'Monto'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button></div>'}]
            };

            $scope.gridOptionsRecibidos = {
                data: 'transaccionesRecibidas',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha | date : 'dd/MM/yyyy'", displayName:'Fecha'},
                    {field:"hora | date : 'HH:mm:ss'", displayName:'Hora'},
                    {field:"estadoSolicitud | date : 'dd/MM/yyyy'", displayName:'Estado solicitud'},
                    {field:"estadoConfirmacion | date : 'hh:mm:ss'", displayName:'Estado cierre'},
                    {field:"origen", displayName:'Origen'},
                    {field:"monto | currency :''", displayName:'Monto'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button></div>'}]
            };

            $scope.getVoucher = function(row){
                $state.transitionTo('app.caja.voucherTransaccionCajaCaja', { id: row.id });
            }

        }])
    .controller('CrearTransaccionCajaCajaController', ['$scope', "$state", '$filter', "CajaSessionService","AgenciaSessionService",
        function($scope, $state, $filter,CajaSessionService,AgenciaSessionService) {

            $scope.control = {"success":false, "inProcess": false, "submitted" : false};

            //objetos de transaccion
            $scope.moneda;
            $scope.caja;

            $scope.cajas = [];
            $scope.monedas = [];

            CajaSessionService.getMonedasOfCurrentCaja().then(
                function(monedas){
                    $scope.monedas = monedas;
                }
            );

            AgenciaSessionService.getCajasOfAgencia().then(
                function(cajas){
                    $scope.cajas = cajas;
                }
            );

            $scope.crearTransaccion = function(){
                if ($scope.formCrearTransaccionCajaCaja.$valid) {
                    $scope.control.inProcess = true;

                } else {
                    $scope.control.submitted = true;
                }
            }
        }])
    .controller('VoucherTransaccionCajaCajaController', ['$scope', "$state", '$filter', "MonedaService", "CajaSessionService",
        function($scope, $state, $filter, MonedaService,CajaSessionService) {


        }]);