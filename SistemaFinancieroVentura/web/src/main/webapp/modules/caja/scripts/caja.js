var cajaApp = angular.module('cajaApp',[
    "ui.router",
    "restangular",
    "ui.bootstrap",
    "dialogs",
    "ngGrid",
    "ui.keypress",
    "cajaApp.controller",
    "cajaApp.service"
]);

cajaApp.config(function(RestangularProvider) {
    RestangularProvider.setBaseUrl("http://localhost:8080/SistemaFinancieroVentura-web/services");
});

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
                        $scope.menus = [{
                            'name':'Abrir / cerrar', submenus:[
                                { 'name':'Abrir caja' , 'state':'app.caja.abrirCaja'},
                                { 'name':'Cerrar caja' , 'state':'app.administracion.personanaturalBuscar'}
                            ]},{
                            'name':'Transacciones con clientes', submenus:[
                                { 'name':'Buscar' , 'state':'app.administracion.personajuridicaCreate'}
                            ]},
                            {
                                'name':'Transacciones internas', submenus:[
                                { 'name':'Transaccion con boveda' , 'state':'app.administracion.personajuridicaCreate'},
                                { 'name':'Transaccion con caja' , 'state':'app.administracion.personajuridicaBuscar'}
                            ]}
                        ];
                    }
                },
                "viewContent":{
                    template: "<div ui-view='viewContent' style='min-height: 472px;'></div>"
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

        .state('app.transacciones', {
            url: "/transacciones",
            views: {
                "viewMenu": {
                    controller: function($scope){
                        $scope.menus = [{
                            'name':'Transaccion', submenus:[
                                { 'name':'Trans.Bancaria' , 'state':'transacciones.transaccionbancaria'},
                                { 'name':'Trans.Compra/Venta' , 'state':'transacciones.transaccioncompraventa'},
                                { 'name':'Trans.Aportes' , 'state':'transacciones.transaccionaportes'}
                            ]}
                        ];
                    }
                },
                "viewContent": { template: "<div ui-view ='viewSubContent'></div>" }
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

cajaApp.run(["$rootScope", "$state", "CajaService", function($rootScope, $location, CajaService){
    $rootScope.$on('$stateChangeStart', function(event, next) {
        CajaService.getCurrentCaja().then(
            function(caja){

            },
            function error(error){
                alert("No se pudo cargar la caja para el usuario ingresado");
            }
        );
    });
}]);