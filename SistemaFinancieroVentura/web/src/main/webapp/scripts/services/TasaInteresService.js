define(['./module'], function (services) {
    'use strict';
    services.factory("TasaInteresService",["Restangular",
        function(Restangular){
            return {
                getInteresGenerado : function(tasa, periodo, monto){
                    if(tasa !== undefined && tasa !== null && periodo !== undefined && periodo !== null && monto !== undefined && monto !== null){
                        var result = monto * Math.pow((tasa+1),(periodo/360));
                        return result;
                    }
                    else {
                        return 0;
                    }
                },
                getTasaCuentaAhorro: function(idMoneda) {
                    return Restangular.one("tasa/ahorro/"+idMoneda).get();
                },
                getTasaCuentaCorriente: function(idMoneda) {
                    return Restangular.one("tasa/corriente/"+idMoneda).get();
                },
                getTasaCuentaPlazoFijo: function(idMoneda, periodo, monto) {
                    if (arguments.length == 1) {
                        return Restangular.one("tasa/plazoFijo/"+idMoneda).get();
                    } else if (arguments.length == 2) {
                        return Restangular.one("tasa/plazoFijo/"+idMoneda+"/"+periodo+"/"+0).get();
                    } else if (arguments.length == 3) {
                        return Restangular.one("tasa/plazoFijo/"+idMoneda+"/"+periodo+"/"+monto).get();
                    }
                },
                getTasaCambio: function(idMonedaRecibida, idMonedaEntregada){
                    return Restangular.one("tasa/tasaCambio").get({idMonedaRecibida:idMonedaRecibida,idMonedaEntregada:idMonedaEntregada},{});
                }
            }
        }])
});
