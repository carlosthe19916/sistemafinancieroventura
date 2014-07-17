define(['./app'], function(app) {
    'use strict';
    return app.config(function($stateProvider, $urlRouterProvider, RestangularProvider) {

        RestangularProvider.setBaseUrl('http://localhost:8080/SistemaFinancieroVentura-web/services');

        $urlRouterProvider.when('', '/app/home');

        $urlRouterProvider.otherwise('/app/home');

        $stateProvider
            .state('app', {
                abstract: true,
                url: '/app?redirect',
                template: '' +
                    '<div class="container" ng-controller="MainController" style="padding-top: 70px;">' +
                    '<div class="navbar navbar-default navbar-fixed-top" role="navigation">' +
                    '<div class="container">' +
                    '<div class="navbar-header">' +
                    '<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">' +
                    '               <span class="sr-only">Toggle navigation</span>' +
                    '               <span class="icon-bar"></span>' +
                    '               <span class="icon-bar"></span>' +
                    '               <span class="icon-bar"></span>' +
                    '</button>' +
                    '<a class="navbar-brand" href="#">' +
                    '<img alt="Logo Caja Ventura" src="images/logos_coop/logo_coop_header.png">' +
                    '</a>' +
                    '</div>' +
                    '<div class="navbar-collapse collapse">' +
                    '<ul class="nav navbar-nav">'+
                    '<li ng-class="{active: $state.includes(&#39;app.home&#39;)}">'+
                    '   <a href="#" ui-sref="app.home({redirect:true})" ng-class="{&#39;sf-nav-bar-menu sf-nav-bar-menu-selected&#39;: $state.includes(&#39;app.home&#39;)}">Página Principal</a>'+
                    '</li>'+
                    '<li ng-class="{active: $state.includes(&#39;app.caja&#39;)}">'+
                    '   <a href="#contact" ui-sref="app.caja({redirect:true})" ng-class="{&#39;sf-nav-bar-menu sf-nav-bar-menu-selected&#39;: $state.includes(&#39;app.caja&#39;)}">Caja</a>'+
                    '</li>'+
                    '<li ng-class="{active: $state.includes(&#39;app.transaccion&#39;)}">'+
                    '<a href="#about" ui-sref="app.transaccion({redirect:true})" ng-class="{&#39;sf-nav-bar-menu sf-nav-bar-menu-selected&#39;: $state.includes(&#39;app.transaccion&#39;)}">Transacciones</a>'+
                    '</li>'+
                    '<li ng-class="{active: $state.includes(&#39;app.socio&#39;)}">'+
                    '<a href="#contact" ui-sref="app.socio({redirect:true})" ng-class="{&#39;sf-nav-bar-menu sf-nav-bar-menu-selected&#39;: $state.includes(&#39;app.socio&#39;)}">Cuentas Personales</a>'+
                    '</li>'+
                    '<li ng-class="{active: $state.includes(&#39;app.administracion&#39;)}">'+
                    '<a href="#contact" ui-sref="app.administracion({redirect:true})" ng-class="{&#39;sf-nav-bar-menu sf-nav-bar-menu-selected&#39;: $state.includes(&#39;app.administracion&#39;)}">Administración</a>'+
                    '</li>'+
                    '</ul>'+
                    '<ul class="nav navbar-nav navbar-right" ng-controller="CajaNavbarController">'+
                    '<li style="height: 50px;">'+
                    '<a style="color: #333;">'+
                    '<span ng-show="cajaSession.abierto && cajaSession.estadoMovimiento" ng-cloak class="label label-info">Abierto</span>'+
                    '<span ng-show="cajaSession.abierto && !cajaSession.estadoMovimiento" ng-cloak class="label label-warning">Congelado</span>'+
                    '<span ng-show="!cajaSession.abierto" ng-cloak class="label label-danger">Cerrado</span>'+
                    '&nbsp;<strong><span ng-bind="cajaSession.denominacion"></span></strong>'+
                    '</a>'+
                    '</li>'+
                    '<li style="cursor: auto;">'+
                    '<a>||</a>'+
                    '</li>'+
                    '<li>'+

                    '<div class="btn-group" dropdown style="padding-top: 10px;">'+
                        '<button type="button" class="btn btn-link dropdown-toggle" style="color:#29465F">'+
                            '<span class="glyphicon glyphicon-user">&nbsp;</span>'+
                            '{{usuarioSession}}&nbsp;&nbsp;&nbsp;' +
                            '<span class="caret"></span>'+
                        '</button>'+
                        '<ul class="dropdown-menu" role="menu">'+
                            '<li><a href="#">Ver perfil</a></li>'+
                            '<li><a href="#">Configuracion</a></li>'+
                            '<li class="divider"></li>'+
                            '<li><a href="/SistemaFinancieroVentura-web/logout">Salir</a></li>'+
                        '</ul>'+
                    '</div>'+

                    '</li>'+
                    '<li>'+
                    '<a class="gwt-Anchor  sf-notification-image" style="padding-right: 30px"></a>'+
                    '</li>'+
                    '<li>'+
                    '<a class="gwt-Anchor  sf-config-image" style="padding-right: 30px"></a>'+
                    '</li>'+
                    '<li>'+
                    '<a class="gwt-Anchor  sf-alert-image" style="padding-right: 30px"></a>'+
                    '</li>'+
                    '</ul>'+
                    '</div>'+
                    '</div>'+
                    '</div>'+
                    '<div class="alert alert-danger" ng-show="cajaSession.denominacion === undefined" ng-cloak style="border-radius: 0px; margin-top: -19px;">'+
                    '<p><strong>Warning:</strong>No se pudo cargar la <strong>CAJA</strong> para el usuario ingresado, no podrá realizar transacciones</p>'+
                    '</div>'+
                    '<div class="container">'+
                    '<div class="row" ui-view>'+

                    '<div class="col-sm-6 col-md-3">' +
                    '<div class="thumbnail" style="min-height: 500px;">' +
                    '<div class="caption sf-nav-bar-left-content ng-scope" ui-view="viewMenu" style="width: 88%;">'+
                    '<ul class="ng-scope">' +
                    '<li ng-repeat="menu in menus" class="sf-nav-bar-left-menuitem ng-scope sf-nav-bar-left-menuitem-selected">'+
                    '<a class="gwt-Anchor sf-nav-bar-left-menuitem-header ng-binding" style="background-color: #F7F7F7;width: 100%;">{{menu.name}}</a>'+
                    '<div class="sf-nav-bar-left-submenu">' +
                    '<ul class="sf-ul-submenu-position sf-ul-submenu-theme">' +
                    '<li ng-repeat="submenu in menu.submenus" ng-class="{GGLKX0UBOUD: $state.includes(&#39;{{submenu.state}}&#39;)}" class="sf-li-submenu ng-scope">'+
                    '<a ui-sref="{{submenu.state}}({redirect:true})" class="gwt-Anchor sf-link-submenu ng-binding">{{submenu.name}}</a>'+
                    '</li>' +
                    '</ul>' +
                    '</div>'+
                    '</li>' +
                    '</ul>'+
                    '</div>'+
                    '</div>' +
                    '</div>'+

                    '<div class="col-sm-6 col-md-9">'+
                    '<div class="thumbnail">'+

                    '<div ng-show="loadingStateProgress" class="row">' +
                        '<div class="col-md-1 col-md-offset-5">' +
                            '<br/>'+'<br/>'+'<br/>'+'<br/>'+'<br/>'+
                            '<span class="navbar-spinner">'+
                                '<img src="images/loader.gif">'+
                            '</span>'+
                            '<br/>'+'<br/>'+'<br/>'+'<br/>'+'<br/>'+
                        '</div>'+
                    '</div>'+

                    '<div ui-view="viewContent" ng-hide="loadingStateProgress" class="caption">'+
                    '</div>'+

                    '</div>'+
                    '</div>'+

                    '</div>'+
                    '</div>'+

                    '<div class="row">'+
                    '<div class="col-sm-12">'+
                    '<div class="center-block text-center" style="font-size: 11px; padding-bottom: 10px;">'+
                    '<span class="l3">'+
                    '<span dir="ltr">©2014&nbsp;Cooperativa de Ahorro y Crédito Caja Ventura</span>'+
                    '</span>&nbsp;-&nbsp;'+
                    '<a href="#" target="_blank" class="l9">Términos y privacidad</a>'+
                    '</div>'+
                    '</div>'+
                    '</div>'+

                    '</div>'
                ,
                controller: function($scope, $stateParams) {
                    $scope.redirect = $stateParams.redirect;
                }
            })
            .state('app.home', {
                url: '/home',
                template: '</br><div class="center-block"><h2 class="text-center" style="font-weight: bold; color: seagreen;">Bienvenido al Sistema Financiero</h2></div></br></br>' + 
                		  '<h3 class="text-center"><img alt="Caja Ventura" src="images/logos_coop/logo_coop.png"></h3></br></br></br>'
            })
            .state('app.caja', {
                url: "/caja",
                views: {
                    "viewMenu":{
                        controller: function($scope){
                            $scope.menus = [
                                {'name':'Panel Control', submenus:[
                                    { 'name':'Panel Control' , 'state':'app.caja.panelControl'}
                                ]},
                                {'name':'Abrir/Cerrar', submenus:[
                                    { 'name':'Abrir Caja' , 'state':'app.caja.abrirCaja'},
                                    { 'name':'Cerrar Caja' , 'state':'app.caja.cerrarCaja'},
                                    { 'name':'Pendientes' , 'state':'app.caja.pendiente'},
                                    { 'name':'Historial' , 'state':'app.caja.historial'}
                                ]},
                                {
                                    'name':'Transacciones Internas', submenus:[
                                    { 'name':'Transaccion con Boveda' , 'state':'app.caja.buscarTransaccionBovedaCaja'},
                                    { 'name':'Transaccion con Caja' , 'state':'app.caja.buscarTransaccionCajaCaja'}
                                ]}
                            ];
                        }
                    },
                    "viewContent":{
                        template: "<div ui-view='viewContent' style='min-height: 472px;'><h4 class='text-center' style='font-weight: bold; color: blue;'>CAJA</h4>" +
                        "Permite registrar todas las transacciones y movimientos realizados con las bovedas y cajas que tiene la institución y proporciona información general y detallada de las mismas." +
                        '</br></br><h3 class="text-center"><img alt="Caja Ventura" src="images/modules/caja.gif"></h3></br></br>' +
                        "</div>"
                    }
                }
            })
            .state('app.transaccion', {
                url: "/transaccion",
                views: {
                    "viewMenu":{
                        controller: function($scope){
                            $scope.menus = [
                                {'name':'Cuenta Aporte', submenus:[
                                    { 'name':'Aporte' , 'state':'app.transaccion.aporte'}
                                ]},
                                {'name':'Cuenta Bancaria', submenus:[
                                    { 'name':'Deposito/Retiro' , 'state':'app.transaccion.depositoRetiro'},
                                    { 'name':'Transferencia' , 'state':'app.transaccion.transferencia'},
                                    { 'name':'Compra/Venta' , 'state':'app.transaccion.compraVenta'}
                                ]},
                                {'name':'Historial', submenus:[
                                    { 'name':'Buscar Transacción' , 'state':'app.transaccion.buscarTransaccion'}
                                ]}
                            ];
                        }
                    },
                    "viewContent":{
                        template: "<div ui-view='viewContent' style='min-height: 472px;'><h4 class='text-center' style='font-weight: bold; color: blue;'>TRANSACCIONES</h4>" +
                        "Permite registrar todas las transacciones y movimientos realizados en las diferentes cuentas bancarias que tiene la institución y proporciona información general y detallada de las mismas."+
                        '</br></br><h3 class="text-center"><img alt="Caja Ventura" src="images/modules/transacciones.png"></h3></br></br>' +
                        "</div>"
                    }
                }
            })
            .state('app.socio', {
                url: "/socio",
                views: {
                    "viewMenu":{
                        controller: function($scope){
                            $scope.menus = [
                                {'name':'Socio', submenus:[
                                    { 'name':'Buscar' , 'state':'app.socio.buscarSocio'}
                                ]},
                                {'name':'Cuenta Aporte', submenus:[
                                    { 'name':'Nuevo' , 'state':'app.socio.crearSocio'}
                                ]},
                                {'name':'Cuentas Bancarias', submenus:[
                                    { 'name':'Nuevo' , 'state':'app.socio.crearCuentaBancaria'},
                                    { 'name':'Buscar' , 'state':'app.socio.buscarCuentaBancaria'}
                                ]}
                            ];
                        }
                    },
                    "viewContent":{
                        template: "<div ui-view='viewContent' style='min-height: 472px;'><h4 class='text-center' style='font-weight: bold; color: blue;'>CUENTAS PERSONALES</h4>" +
                        		  '</br></br><h3 class="text-center"><img alt="Caja Ventura" src="images/modules/plazoFijo.jpg"></h3></br></br>' +
                        		"</div>"
                    }
                }
            })
            .state('app.administracion', {
                url: "/administracion",
                views: {
                    "viewMenu":{
                        controller: function($scope){
                            $scope.menus = [
                                {'name':'Persona Natural', submenus:[
                                    { 'name':'Nuevo' , 'state':'app.administracion.crearPersonaNatural'},
                                    { 'name':'Buscar' , 'state':'app.administracion.buscarPersonaNatural'}
                                ]},
                                {'name':'Persona Jurídica', submenus:[
                                    { 'name':'Nuevo' , 'state':'app.administracion.crearPersonaJuridica'},
                                    { 'name':'Buscar' , 'state':'app.administracion.buscarPersonaJuridica'}
                                ]}
                            ];
                        }
                    },
                    "viewContent":{
                        template: "<div ui-view='viewContent' style='min-height: 472px;'> <h4 class='text-center' style='font-weight: bold; color: blue;'>ADMINISTRAR PERSONA</h4>" +
                        		  '</br></br><h3 class="text-center"><img alt="Caja Ventura" src="images/modules/registrarPersona.png"></h3></br></br>' +
                        		"</div>"
                    }
                }
            })
            .state('app.caja.panelControl', {
                url: "/panelControl",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/caja/panelCaja.html"
                    }
                }
            })
            .state('app.caja.abrirCaja', {
                url: "/abrir",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/caja/abrirCaja.html"
                    }
                }
            })
            .state('app.caja.cerrarCaja', {
                url: "/cerrar",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/caja/cerrarCaja.html"
                    }
                }
            })
            .state('app.caja.pendiente', {
                url: "/pendiente/buscar",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/caja/buscarPendiente.html"
                    }
                }
            })
            .state('app.caja.pendienteCrear', {
                url: "/pendiente/crear?idboveda&monto",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/caja/crearPendiente.html",
                        controller: function($scope, $stateParams) {
                            $scope.idboveda = $stateParams.idboveda;
                            $scope.monto = $stateParams.monto;
                        }
                    }
                }
            })
            .state('app.caja.pendienteVoucher', {
                url: "/pendiente/:id/voucher",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/voucher/pendienteVoucher.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.caja.historial', {
                url: "/historial",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/caja/historialCaja.html"
                    }
                }
            })
            .state('app.caja.voucherCerrarCaja', {
                url: "/historial/:id/voucherCerrarCaja",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/voucher/cerrarCajaVoucher.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.caja.buscarTransaccionBovedaCaja', {
                url: "/buscarBovedaCaja",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/caja/buscarTransaccionBovedaCaja.html"
                    }
                }
            })
            .state('app.caja.createTransaccionBovedaCaja', {
                url: "/crearBovedaCaja",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/caja/crearTransaccionBovedaCaja.html"
                    }
                }
            })
            .state('app.caja.voucherTransaccionBovedaCaja', {
                url: "/voucherTransaccionBovedaCaja/:id",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/voucher/transaccionBovedaCajaVoucher.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.caja.buscarTransaccionCajaCaja', {
                url: "/transCajaCaja",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/caja/buscarTransaccionCajaCaja.html"
                    }
                }
            })
            .state('app.caja.createTransaccionCajaCaja', {
                url: "/crearCajaCaja",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/caja/crearTransaccionCajaCaja.html"
                    }
                }
            })
            .state('app.caja.voucherTransaccionCajaCaja', {
                url: "/voucherTransaccionCajaCaja/:id",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/voucher/transaccionCajaCajaVoucher.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })



            .state('app.transaccion.aporte', {
                url: "/aporte",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/transaccion/aporte.html"
                    }
                }
            })
            .state('app.transaccion.aporteVoucher', {
                url: "/aporte/:id/voucher",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/voucher/aporteVoucher.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.transaccion.depositoRetiro', {
                url: "/depositoRetiro?numeroCuenta&tipoTransaccion&monto&referencia",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/transaccion/depositoRetiro.html",
                        controller: function($scope, $stateParams) {
                            $scope.params = {};
                            $scope.params.numeroCuenta = $stateParams.numeroCuenta;
                            $scope.params.tipoTransaccion = $stateParams.tipoTransaccion;
                            $scope.params.monto = $stateParams.monto;
                            $scope.params.referencia = $stateParams.referencia;
                        }
                    }
                }
            })
            .state('app.transaccion.depositoRetiroVoucher', {
                url: "/depositoRetiro/:id/voucher",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/voucher/transaccionBancariaVoucher.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.transaccion.transferencia', {
                url: "/transferencia",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/transaccion/transferencia.html"
                    }
                }
            })
            .state('app.transaccion.transferenciaVoucher', {
                url: "/transferencia/:id/voucher",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/voucher/transferenciaVoucher.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.transaccion.compraVenta', {
                url: "/compraVenta",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/transaccion/compraVenta.html"
                    }
                }
            })
            .state('app.transaccion.compraVentaVoucher', {
                url: "/compraVenta/:id/voucher",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/voucher/compraVentaVoucher.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.transaccion.buscarTransaccion', {
                url: "/buscarTransaccion",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/transaccion/buscarTransaccion.html"
                    }
                }
            })



            .state('app.socio.crearSocio', {
                url: "/crearSocio",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/socio/crearSocio.html",
                        controller:"CrearSocioController"
                    }
                }
            })
            .state('app.socio.buscarSocio', {
                url: "/buscarSocio",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/socio/buscarSocio.html"
                    }
                }
            })
            .state('app.socio.panelSocio', {
                url: "/:id/panelSocio",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/socio/panelSocio.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.socio.contratoInactivadoSocio', {
                url: "/:id/contradoInactivadoSocio",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/socio/contratoInactivadoSocio.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.socio.voucherCancelacionCuenta', {
                url: "/:id/contradoInactivadoSocio/:idTransaccion",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/voucher/cancelacionCuentaAporteVoucher.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                            $scope.idTransaccion = $stateParams.idTransaccion;
                        }
                    }
                }
            })

            .state('app.socio.crearCuentaBancaria', {
                url: "/cuentaBancaria",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/cuentaBancaria/crearCuentaBancaria.html"
                    }
                }
            })
            .state('app.socio.editarCuentaBancaria', {
                url: "/cuentaBancaria/:id",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/cuentaBancaria/editarCuentaBancaria.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.socio.firmasCuentaBancaria', {
                url: "/cuentaBancaria/:id/firma",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/cuentaBancaria/firmasCuentaBancaria.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.socio.cancelarCuentaBancaria', {
                url: "/cuentaBancaria/cancelarCuenta/:id",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/cuentaBancaria/cancelarCuentaBancaria.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.socio.recalcularPlazoFijo', {
                url: "/cuentaBancaria/recalcularPlazoFijo/:id",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/cuentaBancaria/recalcularPlazoFijo.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.socio.renovarPlazoFijo', {
                url: "/cuentaBancaria/renovarPlazoFijo/:id",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/cuentaBancaria/renovarPlazoFijo.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.socio.buscarCuentaBancaria', {
                url: "/buscarCuentaBancaria",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/cuentaBancaria/buscarCuentaBancaria.html"
                    }
                }
            })

            .state('app.administracion.buscarPersonaNatural', {
                url: '/personaNatural/buscar',
                views: {
                    "viewContent":{
                        templateUrl: 'views/cajero/persona/natural/buscarPersonaNatural.html',
                        controller: 'BuscarPersonaNaturalController'
                    }
                }
            })
            .state('app.administracion.crearPersonaNatural', {
                url: "/personaNatural?tipoDocumento&numeroDocumento",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/persona/natural/crearPersonaNatural.html",
                        controller: function($scope, $stateParams) {
                            $scope.params = {};
                            $scope.params.idTipoDocumento = $stateParams.tipoDocumento;
                            $scope.params.numeroDocumento = $stateParams.numeroDocumento;
                        }
                    }
                }
            })
            .state('app.administracion.editarPersonaNatural', {
                url: "/personaNatural/:id",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/persona/natural/editarPersonaNatural.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.administracion.buscarPersonaJuridica', {
                url: "/personaJuridica/buscar",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/persona/juridica/buscarPersonaJuridica.html",
                        controller: 'BuscarPersonaJuridicaController'
                    }
                }
            })
            .state('app.administracion.crearPersonaJuridica', {
                url: "/personaJuridica?tipoDocumento&numeroDocumento",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/persona/juridica/crearPersonaJuridica.html",
                        controller: function($scope, $stateParams) {
                            $scope.params = {};
                            $scope.params.idTipoDocumento = $stateParams.tipoDocumento;
                            $scope.params.numeroDocumento = $stateParams.numeroDocumento;
                        }
                    }
                }
            })
            .state('app.administracion.editarPersonaJuridica', {
                url: "/personaJuridica/:id",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/persona/juridica/editarPersonaJuridica.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
    })
        /*.run(function($rootScope) {


        $rootScope.$on("$stateChangeStart", function (event, toState, toStateParams, fromState, fromStateParams) {
            var isLoading = toState;
            if (isLoading) {
                $rootScope.loadingStateProgress = true;
            }
        });
        $rootScope.$on("$stateChangeSuccess", function (event, toState, toParams, fromState, fromParams) {
            //hide loading
            $rootScope.loadingStateProgress = false;
        });
        $rootScope.$on("$stateChangeError", function (event, toState, toParams, fromState, fromParams, error) {
            //hide loading
            alert("error al cargar los datos");
        });

    });*/

    .run(['$rootScope', '$state', '$stateParams',
            function ($rootScope,   $state,   $stateParams) {

                $rootScope.$state = $state;
                $rootScope.$stateParams = $stateParams;

                $rootScope.$on("$stateChangeStart", function (event, toState, toStateParams, fromState, fromStateParams) {
                    var isLoading = toState;
                    if (isLoading) {
                        $rootScope.loadingStateProgress = true;
                    }
                });
                $rootScope.$on("$stateChangeSuccess", function (event, toState, toParams, fromState, fromParams) {
                    //hide loading
                    $rootScope.loadingStateProgress = false;
                });
                $rootScope.$on("$stateChangeError", function (event, toState, toParams, fromState, fromParams, error) {
                    //hide loading
                    alert("error al cargar los datos");
                });
            }
        ]
    );

});
