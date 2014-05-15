angular.module('cajaApp.controller', []);

angular.module('cajaApp.controller')
    .controller('cajaNavbarController', ["$rootScope", '$scope',
        function($rootScope, $scope, $cookieStore, $dialogs) {
            //$scope.caja = $rootScope.caja;
            //$scope.usuario = $rootScope.usuario;
            //$scope.agencia = $rootScope.agencia;
        }])

    .controller('AbrirCajaController', ["$rootScope", "$scope", "$state", '$filter', "CajaService",
        function($rootScope, $scope, $state, $filter, CajaService) {

            $scope.control = {"success":false, "inProcess": false};

            CajaService.getDetalle().then(function(detalleCaja){
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
                        //{ field: "valor", displayName: "Denominacion", cellTemplate: "<div><div class='ngCellText'>simbolo {{row.getProperty(col.field)}}</div></div>" },
                        { field: "valor | currency : '"+simbolo+" '", displayName: "Valor" },
                        { field: "cantidad", displayName: "Cantidad" },
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

                CajaService.abrir().then(
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
    .controller('CerrarCajaController', ["$rootScope", "$scope", "$state", "$filter", "CajaService",
        function($rootScope, $scope, $state, $filter, CajaService) {

            $scope.control = {"success":false, "inProcess": false};

            //cargar los datos del web service
            CajaService.getDetalle().then(function(detalleCaja){
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
                        //{ field: "denominacion", displayName: "Denominacion", cellTemplate: "<div><div class='ngCellText'>{{simbolo}} {{row.getProperty(col.field)}}</div></div>" },
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
                        //{ field: "denominacion", displayName: "Denominacion", cellTemplate: "<div><div class='ngCellText'>{{simbolo}} {{row.getProperty(col.field)}}</div></div>", enableCellEdit: false },
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

                CajaService.cerrar($scope.detalleCajaFinal).then(
                    function(data){
                        $scope.control.inProcess = false;
                        $scope.control.success = true;

                        $rootScope.cajaSession.abierto = false;
                        $rootScope.cajaSession.estadoMovimiento = false;
                        //redireccion
                        $state.go("app.caja.voucherCerrarCaja");
                    },
                    function error(error){
                        $scope.control.inProcess = false;
                        $scope.control.success = false;

                        $scope.alerts = [
                            { type: "danger", msg: "Error: " + error.data + "."}
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
                if($rootScope.cajaSession.abierto == false)
                    return true;
                else
                    return false;
            }

            $scope.buttonDisableState = function(){
                return $scope.alertMessageDisplay() || $scope.control.inProcess;
            }

        }])
    .controller('HistorialCajaController', ['$scope', "$state", '$filter', "HistorialCajaService",
        function($scope, $state, $filter, HistorialCajaService) {

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
                HistorialCajaService.buscar($filter('date')($scope.desde, "dd/MM/yyyy"), $filter('date')( $scope.hasta, "dd/MM/yyyy")).then(
                    function(historiales){
                        $scope.listHistoriales = historiales;
                    }
                );
            }

            $scope.getVoucher = function(cajaHistorial){
                $state.transitionTo('app.caja.voucherCerrarCaja', { fechaApertura: cajaHistorial.horaApertura});
            }

            $scope.gridOptions = {
                data: 'listHistoriales',
                multiSelect: false,
                columnDefs: [
                    {field:"fechaApertura | date : 'dd/MM/yyyy'", displayName:'Fecha apertura'},
                    {field:"horaApertura | date : 'hh:mm:ss'", displayName:'Hora apertura'},
                    {field:"fechaCierre | date : 'dd/MM/yyyy'", displayName:'Fecha cierre'},
                    {field:"horaCierre | date : 'hh:mm:ss'", displayName:'Hora cierre'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button></div>'}]
            };

        }])
    .controller('VoucherCerrarCajaController', ['$scope', "$state", '$filter', "HistorialCajaService",
        function($scope, $state, $filter, HistorialCajaService) {

            HistorialCajaService.getVoucherCierreCaja($scope.fechaApertura).then(
                function(voucher){
                    $scope.voucherByMoneda = voucher;
                }
            );
            HistorialCajaService.getResumenCierreCaja($scope.fechaApertura).then(
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
                qz.append("H.Apertu:"+($filter('date')($scope.resumenCaja.horaApertura, 'hh:mm:ss'))+"\r\n");
                qz.append("F.Cierre:"+($filter('date')($scope.resumenCaja.fechaCierre, 'dd/MM/yyyy'))+ " ");
                qz.append("H.Cierre:"+($filter('date')($scope.resumenCaja.horaCierre, 'hh:mm:ss'))+"\r\n");
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
        }])
    .controller('BuscarTransaccionBovedaCajaController', ['$scope', "$state", '$filter', "CajaService",
        function($scope, $state, $filter, CajaService) {

            $scope.nuevo = function(){
                $state.transitionTo('app.caja.createTransaccionBovedaCaja');
            }

            $scope.myData = [{name: "Moroni", age: 50},
                {name: "Tiancum", age: 43},
                {name: "Jacob", age: 27},
                {name: "Nephi", age: 29},
                {name: "Enos", age: 34}];

            $scope.gridOptionsRecibidos = { data: 'myData' };
            $scope.gridOptionsEnviados = { data: 'myData' };


            $scope.transaccionesEnviadas = [];
            $scope.gridOptionsRecibidos = {
                data: 'transaccionesEnviadas',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha | date : 'dd/MM/yyyy'", displayName:'Fecha apertura'},
                    {field:"hora | date : 'hh:mm:ss'", displayName:'Hora apertura'},
                    {field:"estadoSolicitud | date : 'dd/MM/yyyy'", displayName:'Fecha cierre'},
                    {field:"estadoConfirmacion | date : 'hh:mm:ss'", displayName:'Hora cierre'},
                    {field:"origen", displayName:'Hora cierre'},
                    {field:"monto", displayName:'Hora cierre'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button></div>'}]
            };


            CajaService.getTransaccionBovedaCajaEnviadas().then(
                function(enviados){
                    $scope.transaccionesEnviadas = enviados;
                }
            );
            CajaService.getTransaccionBovedaCajaRecibidas().then(
                function(recibidos){
                    $scope.transaccionesRecibidas = recibidos;
                }
            );

        }])
    .controller('CrearTransaccionBovedaCajaController', ['$scope', "$state", '$filter', "CajaService", "MonedaService", "CajaService",
        function($scope, $state, $filter, CajaService, MonedaService,CajaService) {

            $scope.control = {"success":false, "inProcess": false, "submitted" : false};

            //objetos de transaccion
            $scope.boveda;
            $scope.detalles = [];
            //bovedas de caja
            $scope.bovedas = [];


            CajaService.getBovedasOfCurrentCaja().then(
                function(bovedas){
                    $scope.bovedas = bovedas;
                }
            );

            $scope.cargarDetalle = function(){
                MonedaService.getDenominaciones($scope.boveda.moneda.denominacion).then(
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
                    CajaService.crearTransaccionBovedaCaja($scope.boveda.denominacion,$scope.detalles).then(
                        function(data){
                            $scope.control.inProcess = false;
                            $scope.control.success = true;
                            //redireccion al voucher
                            //$state.transitionTo('app.caja.voucherCerrarCaja', { fechaApertura: cajaHistorial.horaApertura});
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







        }]);