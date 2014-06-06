define(['./module'], function (services) {
    'use strict';
    services.factory("SocioService",["Restangular",
        function(Restangular){

            var _socioService = Restangular.all("socio");

            return {
                getSocios: function(mode){
                    if(mode === undefined || mode === null){
                        return _socioService.getList({"mode":"ALL"},{});
                    } else if(mode == 'ALL'){
                        return _socioService.getList({"mode":"ALL"},{});
                    } else if(mode == 'APORTE'){
                        return _socioService.getList({"mode":"APORTE"},{});
                    }
                },
                findByFilterText: function(text){
                    return Restangular.all("socio/filtertext/"+text).getList();
                },
                crear : function(tipoPersona, idTipoDocumentoSocio, numeroDocumentoSocio, idTipoDocumentoApoderado,numeroDocumentoApoderado){
                    var socio = {
                        "tipoPersona" : tipoPersona,
                        "idTipoDocumentoSocio" : idTipoDocumentoSocio,
                        "numeroDocumentoSocio" : numeroDocumentoSocio,
                        "idTipoDocumentoApoderado" : idTipoDocumentoApoderado,
                        "numeroDocumentoApoderado" : numeroDocumentoApoderado
                    }
                    return Restangular.all("socio").post(socio);
                },
                getSocio: function(id){
                    return Restangular.one("socio/"+id).get();
                },
                getCuentaAporte: function(id){
                    return Restangular.one("socio/"+id+"/cuentaAporte").get();
                },
                getPersonaNatural: function(id){
                    return Restangular.one("socio/"+id+"/personaNatural").get();
                },
                getPersonaJuridica: function(id){
                    return Restangular.one("socio/"+id+"/personaJuridica").get();
                },
                getApoderado: function(id){
                    return Restangular.one("socio/"+id+"/apoderado").get();
                },
                getCuentasBancarias: function(id){
                    return Restangular.all("socio/"+id+"/cuentasBancarias").getList();
                }
            }
        }])
});


/*crear: function(tipoPersona, idTipoDocumentoSocio, numeroDocumentoSocio, idTipoDocumentoApoderado,numeroDocumentoApoderado){
 var data = $.param({
 tipoPersona:tipoPersona,
 idTipoDocumentoSocio:idTipoDocumentoSocio,
 numeroDocumentoSocio:numeroDocumentoSocio,
 idTipoDocumentoApoderado:idTipoDocumentoApoderado,
 numeroDocumentoApoderado:numeroDocumentoApoderado}
 );
 return Restangular.one("socio").customPOST(
 data,
 '',{},{
 "Content-Type":"application/x-www-form-urlencoded"}
 );
 },*/