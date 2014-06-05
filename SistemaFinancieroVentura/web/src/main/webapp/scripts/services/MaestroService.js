define(['./module'], function (services) {
    'use strict';
    services.factory("MaestroService",["Restangular",
        function(Restangular){
            return {
                getTipoDocumentoPN: function() {
                    return Restangular.all("tipodocumento/personanatural").getList();
                },
                getTipoDocumentoPJ: function() {
                    return Restangular.all("tipodocumento/personajuridica").getList();
                },
                getSexos: function() {
                    return Restangular.all("sexo").getList();
                },
                getEstadosciviles: function() {
                    return Restangular.all("estadocivil").getList();
                },
                getTiposEmpresa: function() {
                    return Restangular.all("tipoEmpresa").getList();
                },
                getPaises: function() {
                    return Restangular.all("pais").getList();
                },
                getPaisByAbreviatura: function(abreviatura) {
                    return Restangular.one("pais/abreviatura/"+abreviatura).get();
                },
                getPaisByCodigo: function(codigo) {
                    return Restangular.one("pais/codigo/"+codigo).get();
                },
                getDepartamentos: function() {
                    return Restangular.all("departamento").getList();
                },
                getProvincias: function(departamento) {
                    return Restangular.all("departamento/"+departamento+"/provincias").getList();
                },
                getDistritos: function(provincia) {
                    return Restangular.all("provincia/"+provincia+"/distritos").getList();
                }
            }
        }])
});
