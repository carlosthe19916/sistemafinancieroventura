define(['./module'], function (services) {
    'use strict';
    services.factory("VariablesService",["Restangular",
        function(Restangular){

            var tipoPersonas = ["NATURAL", "JURIDICA"];
            var tipoCuentasBancarias = ["AHORRO", "PLAZO_FIJO", "CORRIENTE"];
            var tipoEstadoCuentasBancarias = ["ACTIVO", "CONGELADO", "INACTIVO"];

            return {
                getTipoPersonas: function(){
                    return tipoPersonas;
                },
                getPersonaNatural: function(){
                    return tipoPersonas[0];
                },
                getPersonaJuridica: function(){
                    return tipoPersonas[1];
                },

                getTipoCuentasBancarias: function(){
                    return tipoCuentasBancarias;
                },
                getAhorro: function(){
                    return tipoCuentasBancarias[0];
                },
                getPlazoFijo: function(){
                    return tipoCuentasBancarias[1];
                },
                getCorriente: function(){
                    return tipoCuentasBancarias[2];
                },

                getEstadosCuentaBancaria: function(){
                    return tipoEstadoCuentasBancarias;
                },
                getEstadoBancarioActivo: function(){
                    return tipoEstadoCuentasBancarias[0];
                },
                getEstadoBancarioCongelado: function(){
                    return tipoEstadoCuentasBancarias[1];
                },
                getEstadoBancarioInactivo: function(){
                    return tipoEstadoCuentasBancarias[2];
                }
            }
        }])
});
