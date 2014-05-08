var cajaApp = angular.module('commonApp',[
    "restangular",
    "commonApp.controller",
    "commonApp.service"
]);

cajaApp.config(function(RestangularProvider) {

    RestangularProvider.setBaseUrl("http://localhost:8080/SistemaFinancieroVentura-web/services");

});
