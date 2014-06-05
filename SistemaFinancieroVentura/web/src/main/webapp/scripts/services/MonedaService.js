define(['./module'], function (services) {
    'use strict';
    services.factory("MonedaService",["Restangular",
        function(Restangular){
            var _monedaService = Restangular.all('moneda');
            return {
                getMonedas: function() {
                    return Restangular.all("moneda").getList();
                },
                getDenominaciones: function(moneda) {
                    return Restangular.all("moneda/"+moneda+"/denominaciones").getList();
                }
            }
        }])
});
