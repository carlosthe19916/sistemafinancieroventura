define(['./module'], function (services) {
    'use strict';
    services.factory("TitularService",["Restangular",
        function(Restangular){
            var _titularService = Restangular.all("titular");
            return {
                getTitular : function(id){
                    return Restangular.one("titular/"+id).get();
                },
                crearTitular : function(idCuenta, titular){
                    return Restangular.all("cuentaBancaria/"+idCuenta+"/titular").post(titular);
                },
                actualizarTitular : function(titular){
                    return Restangular.one("titular").customPUT(titular,'',{},{});
                },
                eliminarTitular : function(id){
                    return Restangular.one("titular/"+id).remove();
                }
            }
        }])
});
