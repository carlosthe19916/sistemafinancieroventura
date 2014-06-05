define(['./app'], function(app) {
    'use strict';
    return app.config(function($stateProvider, $urlRouterProvider, RestangularProvider) {

        RestangularProvider.setBaseUrl("http://localhost:8080/SistemaFinancieroVentura-web/services");

        $urlRouterProvider.when('', '/index')
            .when("/app/administracion", "/app/administracion/persona/buscar")
            .when("/errorCaja", "/modules/caja/views/error.html");

        $urlRouterProvider.otherwise('/index');

        $stateProvider
            .state('index', {
                url: '/index',
                template:
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
                    '   <a href="#" ui-sref="index">Página principal</a>'+
                    '</li>'+
                    '<li ui-sref-active="active">'+
                    '   <a href="#contact" ui-sref="app.caja">Caja</a>'+
                    '</li>'+
                    '<li ui-sref-active="active">'+
                    '<a href="#about" ui-sref="app.transaccion">Transacciones</a>'+
                    '</li>'+
                    '<li ui-sref-active="active">'+
                    '<a href="#contact" ui-sref="app.socio">Socios</a>'+
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
                        '</br></br></br></br><div class="center-block"><h3 class="text-center">Bienvenido al sistema</h3></div>'+
                    '</div>'+
                    '</div>'+
                    '</div>'
            })
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
                    '   <a href="#" ui-sref="index">Página principal</a>'+
                    '</li>'+
                    '<li ui-sref-active="active">'+
                    '   <a href="#contact" ui-sref="app.caja">Caja</a>'+
                    '</li>'+
                    '<li ui-sref-active="active">'+
                    '<a href="#about" ui-sref="app.transaccion">Transacciones</a>'+
                    '</li>'+
                    '<li ui-sref-active="active">'+
                    '<a href="#contact" ui-sref="app.socio">Socios</a>'+
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
                                {'name':'Socio', submenus:[
                                    { 'name':'Nuevo' , 'state':'app.socio.crearSocio'},
                                    { 'name':'Buscar' , 'state':'app.socio.buscarSocio'}
                                ]},
                                {'name':'Cuentas bancarias', submenus:[
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
                        templateUrl: "modules/caja/views/caja/panel.html"
                    }
                }
            })
            .state('app.caja.abrirCaja', {
                url: "/abrir",
                views: {
                    "viewContent":{
                        templateUrl: "modules/caja/views/caja/abrir.html"
                    }
                }
            })
            .state('app.caja.cerrarCaja', {
                url: "/cerrar",
                views: {
                    "viewContent":{
                        templateUrl: "modules/caja/views/caja/cerrar.html"
                    }
                }
            })
            .state('app.caja.pendiente', {
                url: "/pendiente/buscar",
                views: {
                    "viewContent":{
                        templateUrl: "modules/caja/views/caja/buscarPendiente.html"
                    }
                }
            })
            .state('app.caja.pendienteCrear', {
                url: "/pendiente/crear/?idboveda&monto",
                views: {
                    "viewContent":{
                        templateUrl: "modules/caja/views/caja/crearPendiente.html",
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
                        templateUrl: "modules/caja/views/voucher/pendiente.html",
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
                        templateUrl: "modules/caja/views/caja/historial.html"
                    }
                }
            })
            .state('app.caja.voucherCerrarCaja', {
                url: "/historial/:id/voucherCerrarCaja",
                views: {
                    "viewContent":{
                        templateUrl: "modules/caja/views/voucher/cerrarCaja.html",
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
                        templateUrl: "modules/caja/views/caja/buscarTransaccionBovedaCaja.html"
                    }
                }
            })
            .state('app.caja.createTransaccionBovedaCaja', {
                url: "/crearBovedaCaja",
                views: {
                    "viewContent":{
                        templateUrl: "modules/caja/views/caja/crearTransaccionBovedaCaja.html"
                    }
                }
            })
            .state('app.caja.voucherTransaccionBovedaCaja', {
                url: "/voucherTransaccionBovedaCaja/:id",
                views: {
                    "viewContent":{
                        templateUrl: "modules/caja/views/voucher/transaccionBovedaCaja.html",
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
                        templateUrl: "modules/caja/views/caja/buscarTransaccionCajaCaja.html"
                    }
                }
            })
            .state('app.caja.createTransaccionCajaCaja', {
                url: "/crearCajaCaja",
                views: {
                    "viewContent":{
                        templateUrl: "modules/caja/views/caja/crearTransaccionCajaCaja.html"
                    }
                }
            })
            .state('app.caja.voucherTransaccionCajaCaja', {
                url: "/voucherTransaccionCajaCaja/:id",
                views: {
                    "viewContent":{
                        templateUrl: "modules/caja/views/voucher/transaccionCajaCaja.html",
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
                        templateUrl: "modules/caja/views/transaccion/aporte.html"
                    }
                }
            })
            .state('app.transaccion.aporteVoucher', {
                url: "/aporte/:id/voucher",
                views: {
                    "viewContent": {
                        templateUrl: "modules/caja/views/voucher/aporte.html",
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
                        templateUrl: "modules/caja/views/transaccion/depositoRetiro.html"
                    }
                }
            })
            .state('app.transaccion.depositoRetiroVoucher', {
                url: "/depositoRetiro/:id/voucher",
                views: {
                    "viewContent": {
                        templateUrl: "modules/caja/views/voucher/depositoRetiro.html",
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
                        templateUrl: "modules/caja/views/transaccion/transferencia.html"
                    }
                }
            })
            .state('app.transaccion.transferenciaVoucher', {
                url: "/depositoRetiro/:id/voucher",
                views: {
                    "viewContent": {
                        templateUrl: "modules/caja/views/voucher/transferencia.html",
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
                        templateUrl: "modules/caja/views/transaccion/compraVenta.html"
                    }
                }
            })
            .state('app.transaccion.compraVentaVoucher', {
                url: "/compraVenta/:id/voucher",
                views: {
                    "viewContent": {
                        templateUrl: "modules/caja/views/voucher/compraVenta.html",
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
                        templateUrl: "modules/caja/views/transaccion/buscarTransaccion.html"
                    }
                }
            })



            .state('app.socio.crearSocio', {
                url: "/crearSocio",
                views: {
                    "viewContent": {
                        templateUrl: "modules/caja/views/socio/crearSocio.html"
                    }
                }
            })
            .state('app.socio.buscarSocio', {
                url: "/buscarSocio",
                views: {
                    "viewContent": {
                        templateUrl: "modules/caja/views/socio/buscarSocio.html"
                    }
                }
            })
            .state('app.socio.panelSocio', {
                url: "/:id/panelSocio",
                views: {
                    "viewContent": {
                        templateUrl: "modules/caja/views/socio/panelSocio.html",
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
                        templateUrl: "modules/caja/views/cuenta/crear.html"
                    }
                }
            })
            .state('app.socio.editarCuentaBancaria', {
                url: "/cuentaBancaria/:id",
                views: {
                    "viewContent": {
                        templateUrl: "modules/caja/views/cuenta/editar.html",
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
                        templateUrl: "modules/caja/views/cuenta/buscar.html"
                    }
                }
            })

            .state('app.socio.crearPersonaNatural', {
                url: "/personaNatural",
                views: {
                    "viewContent":{
                        templateUrl: "modules/common/views/personaNatural/crear.html"
                    }
                }
            })
            .state('app.socio.editarPersonaNatural', {
                url: "/personaNatural/:id",
                views: {
                    "viewContent":{
                        templateUrl: "modules/common/views/personaNatural/editar.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
            .state('app.socio.crearPersonaJuridica', {
                url: "/personaJuridica",
                views: {
                    "viewContent":{
                        templateUrl: "modules/common/views/personaJuridica/crear.html"
                    }
                }
            })
            .state('app.socio.editarPersonaJuridica', {
                url: "/personaJuridica/:id",
                views: {
                    "viewContent":{
                        templateUrl: "modules/common/views/personaJuridica/editar.html",
                        controller: function($scope, $stateParams) {
                            $scope.id = $stateParams.id;
                        }
                    }
                }
            })
    })
});