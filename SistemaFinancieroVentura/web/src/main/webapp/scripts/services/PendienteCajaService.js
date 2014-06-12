define(['./module'], function (services) {
    'use strict';
    services.factory("PendienteCajaService",["Restangular",
        function(Restangular){
            return {
                getVoucherPendienteCaja: function(id) {
                    return Restangular.one("pendiente/"+id+"/voucherPendienteCaja").get();
                }
            }
        }])
});
