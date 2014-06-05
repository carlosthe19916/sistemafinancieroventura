define(['./module'], function (services) {
    'use strict';
    services.factory("AgenciaSessionService",["Restangular",
        function(Restangular){

            var _agenciaService = Restangular.all("agencia");

            return {
                getCurrentAgencia: function(){
                    return Restangular.one("agencia/session").get();
                },
                getCajasOfAgencia: function(){
                    return Restangular.one("agencia/session/cajas").get();
                }
            }
        }])
});
