define(['./module'], function (services) {
    'use strict';
    services.factory("TransaccionInternaService",["Restangular",
        function(Restangular){

            var _cajaService = Restangular.all("transaccionInterna");

            return {
                getVoucherTransaccionBovedaCaja: function(idTransaccionBovedaCaja){
                    return Restangular.one("transaccionInterna/transaccionBovedaCaja/"+idTransaccionBovedaCaja+"/voucher").get();
                }
            }
        }])
});
