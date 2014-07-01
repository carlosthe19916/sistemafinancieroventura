define(['./module'], function (services) {
    'use strict';
    services.factory("PersonaJuridicaService",["Restangular",
        function(Restangular){
            var _personaJuridicaService = Restangular.all("personaJuridica");
            var baseUrl = "personaJuridica";
            return {
                getModel: function(){
                    return {
                        "id":undefined,
                        "tipoDocumento": undefined,
                        "numeroDocumento":undefined,
                        "razonSocial":undefined,
                        "nombreComercial":undefined,
                        "representanteLegal": undefined,
                        "fechaConstitucion":undefined,
                        "actividadPrincipal":undefined,
                        "tipoEmpresa":undefined,
                        "finLucro":undefined,
                        "direccion":undefined,
                        "referencia":undefined,
                        "telefono":undefined,
                        "celular":undefined,
                        "email":undefined,
                        "ubigeo":undefined,
                        "accionistas":undefined
                    };
                },
                findById: function(id){
                    return Restangular.one("personaJuridica", id).get();
                },
                findByTipoNumeroDocumento: function(idtipodocumento, numeroDocumento){
                    return Restangular.one(baseUrl + '/buscar').get({idTipoDocumento:idtipodocumento,numeroDocumento:numeroDocumento},{});
                },
                findByFilterText: function(filterText, offset, limit){
                    if(arguments.length == 0){
                        return Restangular.all(baseUrl).getList();
                    } else if(arguments.length == 1){
                        return Restangular.all(baseUrl).getList({filterText:filterText},{});
                    } else if(arguments.length == 2){
                        return Restangular.all(baseUrl).getList({filterText:filterText,offset:offset},{});
                    } else if(arguments.length == 3){
                        return Restangular.all(baseUrl).getList({filterText:filterText,offset:offset,limit:limit},{});
                    } else if(arguments.length > 2){
                        return Restangular.all(baseUrl).getList({filterText:filterText,offset:offset,limit:limit},{});
                    }
                },
                getPersonas: function(offset, limit){
                    if(arguments.length == 0){
                        return _personaJuridicaService.getList();
                    } else if(arguments.length == 1){
                        return _personaJuridicaService.getList({"offset":offset},{});
                    } else if(arguments.length == 2){
                        return _personaJuridicaService.getList({"offset":offset,"limit":limit},{});
                    } else if(arguments.length > 2){
                        return _personaJuridicaService.getList({"offset":offset,"limit":limit},{});
                    }
                },
                count: function(filterText){
                    if(arguments.length == 0){
                        return Restangular.one(baseUrl + "/count").get();
                    } else if(arguments.length == 1){
                        return Restangular.one(baseUrl + "/count").get({"filterText":filterText},{});
                    }
                },
                update: function(persona){
                    return Restangular.one(baseUrl + "/" + persona.id).customPUT(persona,'',{},{});
                },
                crear: function(personaJuridica){
                    return _personaJuridicaService.post(personaJuridica);
                },
                remove: function(id){
                    return Restangular.all(baseUrl + "/" + id).remove();
                }
            }
        }])
});
