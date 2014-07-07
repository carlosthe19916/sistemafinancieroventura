define(['./module'], function (services) {
    'use strict';
    services.factory("CajaService",["Restangular",
        function(Restangular){
            var _cajaService = Restangular.all("caja");
            return {
                getVoucherCompraVenta: function(idCompraVenta){
                    return Restangular.one("caja/"+idCompraVenta+"/voucherCompraVenta").get();
                }
            }
        }])
});
