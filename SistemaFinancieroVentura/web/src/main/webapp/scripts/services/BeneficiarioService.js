define(['./module'], function (services) {
    'use strict';
    services.factory("BeneficiarioService",["Restangular",
        function(Restangular){
            var _historialCajaService = Restangular.all("beneficiario");
            return {
                getBeneficiario : function(idBeneficiario){
                    return Restangular.one("beneficiario/"+idBeneficiario).get();
                },
                crearBeneficiario : function(idCuenta, beneficiario){
                    return Restangular.all("cuentaBancaria/"+idCuenta+"/beneficiario").post(beneficiario);
                },
                actualizarBeneficiario : function(beneficiario){
                    return Restangular.one("beneficiario").customPUT(beneficiario,'',{},{});
                },
                eliminarBeneficiario : function(idBeneficiario){
                    return Restangular.one("beneficiario/"+idBeneficiario).remove();
                }
            }
        }])
});
