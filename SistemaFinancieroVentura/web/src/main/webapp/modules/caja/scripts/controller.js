angular.module('cajaApp.controller', []);

angular.module('cajaApp.controller')
    .controller('cajaNavbarController', ['$scope', "$cookieStore", "$dialogs", "CajaService","UsuarioService","AgenciaService",
        function($scope, $cookieStore, $dialogs, CajaService, UsuarioService, AgenciaService) {

            CajaService.getCurrentCaja().then(
                function(caja){
                    $cookieStore.put("caja", caja);
                    $scope.caja = caja;
                },
                function error(error){
                    $dialogs.error("Error no podrá realizar transacciones de ningun tipo","Error al cargar caja:\n"+JSON.stringify(error.data));;
                }
            );
            UsuarioService.getCurrentUsuario().then(
                function(usuario){
                    $cookieStore.put("usuario", usuario);
                    $scope.usuario = $cookieStore.get("usuario");
                }
            );
            AgenciaService.getCurrentAgencia().then(
                function(agencia){
                    $cookieStore.put("agencia", agencia);
                    $scope.agencia = agencia;
                }
            );
        }])

    .controller('AbrirCajaController', ['$scope', "$state", "$cookieStore", '$filter', "$dialogs", "CajaService",
        function($scope, $state, $cookieStore, $filter, $dialogs, CajaService) {

            $scope.currentAgencia = $cookieStore.get("agencia");
            $scope.currentCaja = $cookieStore.get("caja");

            $scope.detalleCaja = [];
            $scope.alertMonedasDisableState = function(){
                return $scope.detalleCaja.length == 0;
            }

            if($scope.currentCaja.abierto == false){
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
                    $scope.progressTransaction = true;
                    CajaService.abrir().then(
                        function(data){
                            $scope.progressTransaction = false;

                            //cookie
                            $scope.currentCaja = $cookieStore.get("caja");
                            $scope.currentCaja.abierto = true;
                            $scope.currentCaja.estadoMovimiento = true;
                            $cookieStore.put("caja", $scope.currentCaja);

                            //redireccion
                            $state.go("app.caja", null, { reload: true })
                        },
                        function error(error){
                            $dialogs.error("Error al abrir caja",JSON.stringify(error.message));
                            $scope.progressTransaction = false;
                        }
                    );
                };
            }

            $scope.buttonDisableState = function(){
                if($scope.progressTransaction == true)
                    return true;
                if($scope.currentCaja === undefined) {
                    return true;
                }
                else {
                    if(!$scope.alertMonedasDisableState())
                        return false;
                    else
                        return true;
                }
            }
        }])
    .controller('CerrarCajaController', ['$scope', "$state", "$cookieStore", '$filter', "$dialogs", "CajaService",
        function($scope, $state, $cookieStore, $filter, $dialogs, CajaService) {

            $scope.currentAgencia = $cookieStore.get("agencia");
            $scope.currentCaja = $cookieStore.get("caja");

            $scope.detalleCajaInicial = [];

            if($scope.currentCaja.abierto == true){
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
                    $scope.progressTransaction = true;
                    CajaService.cerrar($scope.detalleCajaFinal).then(
                        function(data){
                            $scope.progressTransaction = false;

                            //cookie
                            $scope.currentCaja = $cookieStore.get("caja");
                            $scope.currentCaja.abierto = false;
                            $scope.currentCaja.estadoMovimiento = false;
                            $cookieStore.put("caja", $scope.currentCaja);

                            //redireccion
                            $state.go("app.caja.voucherCerrarCaja")
                        },
                        function (error){
                            $dialogs.error("Error al cerrar caja",JSON.stringify(error).message);
                            $scope.progressTransaction = false;
                        }
                    );
                };

                //ocultar ceros
                $scope.hideZeroRows = function(){
                    for(var i = 0; i < $scope.gridOptionsFinal.length; i++){
                        $scope.gridOptionsFinal[i].$gridScope.filterText = Math.ceil(24 * Math.random());
                    }

                }
            }

            $scope.buttonDisableState = function(){
                if($scope.progressTransaction == true)
                    return true;
                if($scope.currentCaja === undefined) {
                    return true;
                }
                else {
                    return false;
                }
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


          /* $("#content").html2canvas({
                canvas: hidden_screenshot,
                onrendered: function() {
                    if (notReady()) { return; }
                    // Optional, set up custom page size.  These only work for PostScript printing.
                    // setPaperSize() must be called before setAutoSize(), setOrientation(), etc.
                    qz.setPaperSize("8.5in", "11.0in");  // US Letter
                    qz.setAutoSize(true);
                    qz.appendImage($("canvas")[0].toDataURL('image/png'));
                    // Automatically gets called when "qz.appendFile()" is finished.
                    window['qzDoneAppending'] = function() {
                        // Tell the applet to print.
                        qz.printPS();

                        // Remove reference to this function
                        window['qzDoneAppending'] = null;
                    };
                }
            });*/
        }]);