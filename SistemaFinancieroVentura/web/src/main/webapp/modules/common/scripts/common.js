var cajaApp = angular.module('commonApp',[
    "restangular",
    "commonApp.controller",
    "commonApp.service",
    "ngProgress"
]);

cajaApp.config(function(RestangularProvider) {

    RestangularProvider.setBaseUrl("http://localhost:8080/SistemaFinancieroVentura-web/services");

    RestangularProvider.addResponseInterceptor(
        function(element, operation, what, url) {

            return element;
        }
    );

    RestangularProvider.addResponseInterceptor (
        function(data, operation, what,url, reponse, deferred) {

            return data;
        }
    );


    /*
    RestangularProvider.setErrorInterceptor(
        function(resp) {
            //mostrar error
            return false; // stop the promise chain
        });*/
});
