define(['./module'], function (services) {
    'use strict';
    services.factory("SocioService",["Restangular",
        function(Restangular){

            var _socioService = Restangular.all("socio");
            var baseUrl = "socio";

            return {
                findById: function(id){
                    return Restangular.one(baseUrl, id).get();
                },
                findByFilterText: function(filterText,estadoCuentaAporte, estadoSocio, offset, limit){
                    if(arguments.length == 0){
                        return Restangular.all("socio/filtertext/"+"").getList({},{});
                    } else if(arguments.length == 1){
                        return Restangular.all("socio/filtertext/"+filterText).getList({},{});
                    } else if(arguments.length == 2){
                        return Restangular.all("socio/filtertext/"+filterText).getList({"estadoCuentaAporte":estadoCuentaAporte},{});
                    } else if(arguments.length == 3){
                        return Restangular.all("socio/filtertext/"+filterText).getList({"estadoCuentaAporte":estadoCuentaAporte,"estadoSocio":estadoSocio},{});
                    } else if(arguments.length == 4){
                        return Restangular.all("socio/filtertext/"+filterText).getList({"estadoCuentaAporte":estadoCuentaAporte,"estadoSocio":estadoSocio,"offset":offset},{});
                    } else if(arguments.length == 5){
                        return Restangular.all("socio/filtertext/"+filterText).getList({"estadoCuentaAporte":estadoCuentaAporte,"estadoSocio":estadoSocio,"offset":offset,"limit":limit},{});
                    }
                },
                getSocios: function(estadoCuentaAporte, estadoSocio, offset, limit){
                    if(arguments.length == 0){
                        return _socioService.getList({},{});
                    } else if(arguments.length == 1){
                        return _socioService.getList({"estadoCuentaAporte":estadoCuentaAporte},{});
                    } else if(arguments.length == 2){
                        return _socioService.getList({"estadoCuentaAporte":estadoCuentaAporte,"estadoSocio":estadoSocio},{});
                    } else if(arguments.length == 3){
                        return _socioService.getList({"estadoCuentaAporte":estadoCuentaAporte,"estadoSocio":estadoSocio,"offset":offset},{});
                    } else if(arguments.length == 4){
                        return _socioService.getList({"estadoCuentaAporte":estadoCuentaAporte,"estadoSocio":estadoSocio,"offset":offset,"limit":limit},{});
                    }
                },
                count: function(filterText){
                    if(arguments.length == 0){
                        return Restangular.one(baseUrl + "/count").get();
                    } else if(arguments.length == 1){
                        return Restangular.one(baseUrl + "/count").get({"filterText":filterText},{});
                    }
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
                },
                congelarCuentaAporte: function(id){
                    return Restangular.one(baseUrl +"/"+id+"/cuentaAporte/congelar").customPUT({},'',{},{});
                },
                descongelarCuentaAporte: function(id){
                    return Restangular.one(baseUrl +"/"+id+"/cuentaAporte/descongelar").customPUT({},'',{},{});
                },
                inactivarSocio: function(id){
                    return Restangular.one(baseUrl +"/"+id).remove();
                },
                cambiarApoderado: function(idSocio, apoderado){
                    return Restangular.all(baseUrl+"/"+idSocio+"/apoderado").customPUT(apoderado,'',{},{});
                },
                eliminarApoderado: function(idSocio){
                    return Restangular.all(baseUrl+"/"+idSocio+"/apoderado").remove();
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