var cajaApp = angular.module('sistCoopApp.cajaApp',[
    "ui.router",
    "restangular",
    "ui.bootstrap"
]);

cajaApp.config(function(RestangularProvider) {
    RestangularProvider.setBaseUrl("/localhost:8080/SistCoop-web/services");
});

cajaApp.config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.when('', '/index');

    $urlRouterProvider.otherwise('/index');

    $stateProvider
        .state('index', {
            url: '/index',
            template: '<h3>Bienvenido al sistema</h3>'
        })
        .state('transacciones', {
            url: "/transacciones",
            views: {
                "viewMenu": {
                    templateUrl: "caja/menu.html",
                    controller: function($scope){
                        $scope.menus = [{
                            'name':'Transaccion', submenus:[
                                { 'name':'Trans.Bancaria' , 'state':'transacciones.transaccionbancaria'},
                                { 'name':'Trans.Compra/Venta' , 'state':'transacciones.transaccioncompraventa'},
                                { 'name':'Trans.Aportes' , 'state':'transacciones.transaccionaportes'}
                            ]},{
                            'name':'Menu 01', submenus:[
                                { 'name':'Submenu 01' , 'state':'submenu.create'},
                                { 'name':'Submenu 02' , 'state':'submenu.search'}
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
        });
});
