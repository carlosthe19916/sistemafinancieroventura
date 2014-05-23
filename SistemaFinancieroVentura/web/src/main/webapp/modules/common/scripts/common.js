var cajaApp = angular.module('commonApp',[
    "restangular",
    "commonApp.controller",
    "commonApp.service",
    "ngProgress"
]);

cajaApp.config(function(RestangularProvider) {

    RestangularProvider.setBaseUrl("http://localhost:8080/SistemaFinancieroVentura-web/services");

});


cajaApp.config(function($stateProvider, $urlRouterProvider) {

    $stateProvider
        .state('app.common.crearPersonaNatural', {
            url: "/personaNatural",
            views: {
                "viewContent":{
                    templateUrl: "modules/common/views/personanatural/create.html"
                }
            }
        })
        .state('app.common.editarPersonaNatural', {
            url: "/personaNatural/:id",
            views: {
                "viewContent":{
                    templateUrl: "modules/common/views/personanatural/update.html"
                }
            }
        })
        .state('app.common.crearPersonaJuridica', {
            url: "/personaJuridica",
            views: {
                "viewContent":{
                    templateUrl: "modules/common/views/personanatural/create.html"
                }
            }
        })
        .state('app.common.editarPersonaJuridica', {
            url: "/Juridica/:id",
            views: {
                "viewContent":{
                    templateUrl: "modules/common/views/personanatural/update.html"
                }
            }
        });

});