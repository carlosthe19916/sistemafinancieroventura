define(['./module'], function (services) {
    'use strict';
    services.factory("HistorialCajaService",["Restangular",
        function(Restangular){
            var _historialCajaService = Restangular.all("historialcaja");
            return {
                buscar: function(desde, hasta){
                    return Restangular.all("historialcaja/currentSession").getList({"desde":desde,"hasta":hasta},{});
                },
                getVoucherCierreCaja: function(idhistorial){
                    return Restangular.all("historialcaja/"+idhistorial+"/voucherCierreCaja").getList();
                },
                getResumenCierreCaja: function(idhistorial){
                    return Restangular.one("historialcaja/"+idhistorial+"/resumenCierreCaja").get();
                }
            }
        }])
});
