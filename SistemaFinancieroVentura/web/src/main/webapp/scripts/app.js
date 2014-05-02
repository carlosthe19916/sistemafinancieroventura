

var sistCoopApp = angular.module('sistCoopApp',[
    "ui.router",
    "restangular",
    "ui.bootstrap"
]);

sistCoopApp.config(function(RestangularProvider) {
        RestangularProvider.setBaseUrl("/localhost:8080/SistCoop-web/services");
});


sistCoopApp.config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.when('', '/index');

    $urlRouterProvider.otherwise('/index');

    $stateProvider
        .state('index', {
            url: '/index',
            template: '<h3>Bienvenido al sistema</h3>'
        })
        .state('sucursal', {
            url: "/sucursal",
            views: {
                "viewMenu": {
                    templateUrl: "views/menu.html",
                    controller: function($scope){
                        $scope.menus = [{
                            'name':'Sucursal', submenus:[
                                { 'name':'Registrar' , 'state':'sucursal.create'},
                                { 'name':'Buscar' , 'state':'sucursal.search'}
                            ]},{
                            'name':'Menu 01', submenus:[
                                { 'name':'Submenu 01' , 'state':'submenu.create'},
                                { 'name':'Submenu 02' , 'state':'submenu.search'}
                                ]}
                        ];
                    }
                },
                "viewContent": { template: "<div ui-view ='prueba'>b</div>" }
            }
        })
        .state('sucursal.create', {
            url: "/create",
            views: {
                "prueba":{
                    templateUrl: "views/sucursal/create.html",
                    controller:"SucursalCreateController"
                }
            }
        })
        .state('sucursal.search', {
            url: "/buscar",
            views: {
                "viewMenu": { template: "<h2>menu buscar</h2>" },
                "viewContent": { template: "<h1>buscando sucursal</h1>" }
            }
        })

        .state('sucursal.update', {
            url: "/update/:id",
            views: {
                "viewMenu": { templateUrl: "views/boveda/menu.html" },
                "viewContent": { templateUrl: "views/boveda/boveda.list.html" }
            }
        })

        .state('sistema', {
            url: "/sistema",
            views: {
                "viewMenu": { templateUrl: "views/caja/menu.html" },
                "viewContent": { template: "CAJAAA" }
            }
        })
});




