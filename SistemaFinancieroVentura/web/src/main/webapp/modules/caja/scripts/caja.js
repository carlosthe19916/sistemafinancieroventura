var cajaApp = angular.module('cajaApp',[
    "ui.router",
    "restangular",
    "ngCookies",
    "ui.bootstrap",
    "dialogs",
    "ngGrid",
    "ui.keypress",
    "blockUI",
    "cajaApp.controller",
    "cajaApp.service",
    "commonApp"
]);

cajaApp.config(["RestangularProvider", "$cookiesProvider",
    function (RestangularProvider, $cookies) {

        RestangularProvider.setBaseUrl("http://localhost:8080/SistemaFinancieroVentura-web/services");

        /*
        RestangularProvider.addRequestInterceptor(
            function(element, operation, what, url) {
                var caja = $cookies.caja;
                if(caja != null && caja !== undefined){
                    return element;
                } else {
                    if (operation === "get" || operation === "getList") {
                        return element;
                    } else {
                        alert("No se encontró caja en sesion, no puede realizar ninguna transaccion");
                        return null;
                    }
                }
            });*/
    }] );

cajaApp.config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.when('', '/index')
        .when("/app/administracion", "/app/administracion/persona/buscar")
        .when("/errorCaja", "/modules/caja/views/error.html");

    $urlRouterProvider.otherwise('/index');

    $stateProvider
        .state('index', {
            url: '/index',
            template: "</br></br></br></br><div class='center-block'><h3 class='text-center'>Bienvenido al sistema</h3></div>"
        })
        .state('app', {
            abstract: true,
            url: "/app",
            template: "" +
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
                "</div>"
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
                                { 'name':'Historial' , 'state':'app.caja.historial'}
                            ]},
                            {
                                'name':'Transacciones internas', submenus:[
                                { 'name':'Transaccion con boveda' , 'state':'app.caja.buscarTransaccionBovedaCaja'},
                                { 'name':'Transaccion con caja' , 'state':'app.caja.tranaccionCajaCaja'}
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
                    templateUrl: "modules/caja/views/caja/abrir.html"
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
        .state('app.caja.historial', {
            url: "/historial",
            views: {
                "viewContent":{
                    templateUrl: "modules/caja/views/caja/historial.html"
                }
            }
        })
        .state('app.caja.voucherCerrarCaja', {
            url: "/voucherCerrarCaja?fechaApertura",
            views: {
                "viewContent":{
                    templateUrl: "modules/caja/views/voucher/cerrarCaja.html",
                    controller: function($scope, $stateParams) {
                        $scope.fechaApertura = $stateParams.fechaApertura;
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
        .state('app.caja.buscarTransaccionCajaCaja', {
            url: "/transCajaCaja",
            views: {
                "viewContent":{
                    templateUrl: "modules/caja/views/caja/transaccionCajaCaja.html"
                }
            }
        })

        .state('transacciones.transaccionbancaria', {
            url: "/transaccionbancaria",
            views: {

                "viewSubContent":{
                    templateUrl: "caja/views/transaccion/transaccionbancaria.html"
                }
            }
        })



        .state('app.administracion', {
            url: "/administracion",
            views: {
                "viewMenu":{
                    controller: function($scope){
                        $scope.menus = [{
                            'name':'Persona natural', submenus:[
                                { 'name':'Registrar' , 'state':'app.administracion.personanaturalCreate'},
                                { 'name':'Buscar' , 'state':'app.administracion.personanaturalBuscar'}
                            ]},{
                            'name':'Persona juridica', submenus:[
                                { 'name':'Registrar' , 'state':'app.administracion.personajuridicaCreate'},
                                { 'name':'Buscar' , 'state':'app.administracion.personajuridicaBuscar'}
                            ]}
                        ];
                    }
                },
                "viewContent":{
                    template: "<div ui-view='viewContent' style='min-height: 472px;'></div>"
                }
            }
        })
        .state('app.administracion.personanaturalBuscar', {
            url: "/persona/buscar",
            views: {
                "viewContent":{
                    templateUrl: "modules/common/views/personanatural/buscar.html"
                }
            }
        })
        .state('app.administracion.personanaturalCreate', {
            url: "/persona/crear",
            views: {
                "viewContent":{
                    templateUrl: "modules/common/views/personanatural/create.html"
                }
            }
        })
        .state('app.administracion.personanaturalUpdate', {
            url: "/persona/update/:id",
            views: {
                "viewContent":{
                    templateUrl: "modules/common/views/personanatural/update.html",
                    controller: function($scope, $stateParams) {
                        $scope.id = $stateParams.id;
                    }
                }
            }
        });
});

cajaApp.run(["$rootScope", "CajaService", "UsuarioService", "AgenciaService",
    function ($rootScope, CajaService, UsuarioService, AgenciaService) {
        CajaService.getCurrentCaja().then(
            function(caja){
                $rootScope.caja = caja;
            },
            function error(error){
                $rootScope.caja = undefined;
            }
        );
        UsuarioService.getCurrentUsuario().then(
            function(usuario){
                $rootScope.usuario = usuario;
            },
            function error(error){
                $rootScope.usuario = undefined;
            }
        );
        AgenciaService.getCurrentAgencia().then(
            function(agencia){
                $rootScope.agencia = agencia;
            },
            function error(error){
                $rootScope.agencia = undefined;
            }
        );
    }] );
