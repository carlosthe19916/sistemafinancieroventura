define(['./app'], function(app) {
    'use strict';
    return app.config(function($stateProvider, $urlRouterProvider, RestangularProvider) {

        RestangularProvider.setBaseUrl("http://localhost:8080/SistemaFinancieroVentura-web/services");

        $urlRouterProvider.when('', '/app/home');

        $urlRouterProvider.otherwise('/app/home');

        $stateProvider
            .state('app', {
                abstract: true,
                url: "/app",
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
                    '<img alt="Google AdSense" src="images/logo.png">' +
                    '</a>' +
                    '</div>' +
                    '<div class="navbar-collapse collapse">' +
                    '<ul class="nav navbar-nav">'+
                    '<li ui-sref-active="active" class="active">'+
                    '   <a href="#" ui-sref="app.home">Página Principal</a>'+
                    '</li>'+
                    '<li ui-sref-active="active">'+
                    '   <a href="#contact" ui-sref="app.caja">Caja</a>'+
                    '</li>'+
                    '<li ui-sref-active="active">'+
                    '<a href="#about" ui-sref="app.transaccion">Transacciones</a>'+
                    '</li>'+
                    '<li ui-sref-active="active">'+
                    '<a href="#contact" ui-sref="app.socio">Cuentas Personales</a>'+
                    '</li>'+
                    '<li ui-sref-active="active">'+
                    '<a href="#contact" ui-sref="app.administracion">Administracion</a>'+
                    '</li>'+
                    '</ul>'+
                    '<ul class="nav navbar-nav navbar-right" ng-controller="CajaNavbarController">'+
                    '<li style="height: 50px;">'+
                    '<a style="color: #333;">'+
                    '<span ng-show="cajaSession.abierto && cajaSession.estadoMovimiento" ng-cloak class="label label-info">Abierto</span>'+
                    '<span ng-show="cajaSession.abierto && !cajaSession.estadoMovimiento" ng-cloak class="label label-warning">Congelado</span>'+
                    '<span ng-show="!cajaSession.abierto" ng-cloak class="label label-danger">Cerrado</span>'+
                    '<strong><span ng-bind="cajaSession.denominacion"></span></strong>'+
                    '</a>'+
                    '</li>'+
                    '<li>'+
                    '<a>||</a>'+
                    '</li>'+
                    '<li>'+
                    '<a><span ng-bind="usuarioSession"></span></a>'+
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

                    "<div class='col-sm-6 col-md-3'>" +
                    "<div class='thumbnail' style='min-height: 500px;'>" +
                    "<div class='caption sf-nav-bar-left-content ng-scope' ui-view='viewMenu' style='width: 88%;'>"+
                    "<ul class='ng-scope'>" +
                    "<li ng-repeat='menu in menus' class='sf-nav-bar-left-menuitem ng-scope sf-nav-bar-left-menuitem-selected'>"+
                    "<a class='gwt-Anchor sf-nav-bar-left-menuitem-header ng-binding' style='background-color: #F7F7F7;width: 100%;'>{{menu.name}}</a>"+
                    "<div class='sf-nav-bar-left-submenu'>" +
                    "<ul class='sf-ul-submenu-position sf-ul-submenu-theme'>" +
                    "<li ng-repeat='submenu in menu.submenus' ui-sref-active = 'GGLKX0UBOUD' class='sf-li-submenu ng-scope'>"+
                    "<a ui-sref='{{submenu.state}}' class='gwt-Anchor sf-link-submenu ng-binding'>{{submenu.name}}</a>"+
                    "</li>" +
                    "</ul>" +
                    "</div>"+
                    "</li>" +
                    "</ul>"+
                    "</div>"+
                    "</div>" +
                    "</div>"+

                    "<div class='col-sm-6 col-md-9'>"+
                    "<div class='thumbnail'>"+
                    "<div class='caption' ui-view='viewContent'>"+
                    "</div>"+
                    "</div>"+
                    "</div>"+

                    '</div>'+
                    '</div>'+
                    '</div>'
            })
            .state('app.home', {
                url: '/home',
                template: '</br></br></br></br><div class="center-block"><h3 class="text-center">Bienvenido al sistema</h3></div>'
            })
            .state('app.caja', {
                url: "/caja",
                views: {
                    "viewMenu":{
                        controller: function($scope){
                            $scope.menus = [
                                {'name':'Panel control', submenus:[
                                    { 'name':'Panel control' , 'state':'app.caja.panelControl'}
                                ]},
                                {'name':'Abrir / cerrar', submenus:[
                                    { 'name':'Abrir caja' , 'state':'app.caja.abrirCaja'},
                                    { 'name':'Cerrar caja' , 'state':'app.caja.cerrarCaja'},
                                    { 'name':'Pendientes' , 'state':'app.caja.pendiente'},
                                    { 'name':'Historial' , 'state':'app.caja.historial'}
                                ]},
                                {
                                    'name':'Transacciones internas', submenus:[
                                    { 'name':'Transaccion con boveda' , 'state':'app.caja.buscarTransaccionBovedaCaja'},
                                    { 'name':'Transaccion con caja' , 'state':'app.caja.buscarTransaccionCajaCaja'}
                                ]}
                            ];
                        }
                    },
                    "viewContent":{
                        template: "<div ui-view='viewContent' style='min-height: 472px;'></div>"
                    }
                }
            })
            .state('app.transaccion', {
                url: "/transaccion",
                views: {
                    "viewMenu":{
                        controller: function($scope){
                            $scope.menus = [
                                {'name':'Socio', submenus:[
                                    { 'name':'Aporte' , 'state':'app.transaccion.aporte'}
                                ]},
                                {'name':'Cuenta bancaria', submenus:[
                                    { 'name':'Deposito/retiro' , 'state':'app.transaccion.depositoRetiro'},
                                    { 'name':'Transferencia' , 'state':'app.transaccion.transferencia'},
                                    { 'name':'Compra/venta' , 'state':'app.transaccion.compraVenta'}
                                ]},
                                {'name':'Historial', submenus:[
                                    { 'name':'Buscar transaccion' , 'state':'app.transaccion.buscarTransaccion'}
                                ]}
                            ];
                        }
                    },
                    "viewContent":{
                        template: "<div ui-view='viewContent' style='min-height: 472px;'></div>"
                    }
                }
            })
            .state('app.socio', {
                url: "/socio",
                views: {
                    "viewMenu":{
                        controller: function($scope){
                            $scope.menus = [
                                {'name':'Cuenta Aporte', submenus:[
                                    { 'name':'Nuevo' , 'state':'app.socio.crearSocio'},
                                    { 'name':'Buscar' , 'state':'app.socio.buscarSocio'}
                                ]},
                                {'name':'Cuentas Bancarias', submenus:[
                                    { 'name':'Nuevo' , 'state':'app.socio.crearCuentaBancaria'},
                                    { 'name':'Buscar' , 'state':'app.socio.buscarCuentaBancaria'}
                                ]}
                            ];
                        }
                    },
                    "viewContent":{
                        template: "<div ui-view='viewContent' style='min-height: 472px;'></div>"
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
                url: "/pendiente/crear/?idboveda&monto",
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
                        templateUrl: "views/cajero/voucher/transaccionBovedaCaja.html",
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
                url: "/depositoRetiro",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/transaccion/depositoRetiro.html"
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
                url: "/depositoRetiro/:id/voucher",
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
            .state('app.socio.buscarCuentaBancaria', {
                url: "/buscarCuentaBancaria",
                views: {
                    "viewContent": {
                        templateUrl: "views/cajero/cuentaBancaria/buscarCuentaBancaria.html"
                    }
                }
            })

            .state('app.socio.crearPersonaNatural', {
                url: "/personaNatural?tipoDocumento&numeroDocumento",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/socio/personaNatural/crearPersonaNatural.html",
                        controller: function($scope, $stateParams) {
                            $scope.params = {};
                            $scope.params.idTipoDocumento = $stateParams.tipoDocumento;
                            $scope.params.numeroDocumento = $stateParams.numeroDocumento;
                        }
                    }
                }
            })
            .state('app.socio.editarPersonaNatural', {
                url: "/personaNatural/:id",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/socio/personaNatural/editarPersonaNatural.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.socio.crearPersonaJuridica', {
                url: "/personaJuridica?tipoDocumento&numeroDocumento",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/socio/personaJuridica/crearPersonaJuridica.html",
                        controller: function($scope, $stateParams) {
                            $scope.params = {};
                            $scope.params.idTipoDocumento = $stateParams.tipoDocumento;
                            $scope.params.numeroDocumento = $stateParams.numeroDocumento;
                        }
                    }
                }
            })
            .state('app.socio.editarPersonaJuridica', {
                url: "/personaJuridica/:id",
                views: {
                    "viewContent":{
                        templateUrl: "views/cajero/socio/personaJuridica/editarPersonaJuridica.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
    })
});
