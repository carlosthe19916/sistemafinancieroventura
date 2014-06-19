define(['./module'], function (services) {
    'use strict';
    services.factory("SocioService",["Restangular",
        function(Restangular){

            var _socioService = Restangular.all("socio");

            return {
                getSocios: function(desde, hasta, modeSocio, modeEstado){
                    if(arguments.length == 0){
                        return _socioService.getList({},{});
                    } else if(arguments.length == 1){
                        return _socioService.getList({"desde":desde},{});
                    } else if(arguments.length == 2){
                        return _socioService.getList({"desde":desde,"hasta":hasta},{});
                    } else if(arguments.length == 3){
                        return _socioService.getList({"desde":desde,"hasta":hasta,"modeSocio":modeSocio},{});
                    } else if(arguments.length == 4){
                        return _socioService.getList({"desde":desde,"hasta":hasta,"modeSocio":modeSocio,"modeEstado":modeEstado},{});
                    }
                },
                findByFilterText: function(text, desde, hasta, modeSocio, modeEstado){
                    if(arguments.length == 0){
                        return Restangular.all("socio/filtertext/"+"").getList({},{});
                    } else if(arguments.length == 1){
                        return Restangular.all("socio/filtertext/"+text).getList({},{});
                    } else if(arguments.length == 2){
                        return Restangular.all("socio/filtertext/"+text).getList({"desde":desde},{});
                    } else if(arguments.length == 3){
                        return Restangular.all("socio/filtertext/"+text).getList({"desde":desde,"hasta":hasta},{});
                    } else if(arguments.length == 4){
                        return Restangular.all("socio/filtertext/"+text).getList({"desde":desde,"hasta":hasta,"modeSocio":modeSocio},{});
                    } else if(arguments.length == 5){
                        return Restangular.all("socio/filtertext/"+text).getList({"desde":desde,"hasta":hasta,"modeSocio":modeSocio,"modeEstado":modeEstado},{});
                    }
                },
                count: function(){
                    return Restangular.one("socio/count").get();
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