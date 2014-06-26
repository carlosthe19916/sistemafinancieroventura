define(['./module'], function (services) {
    'use strict';
    services.factory("PersonaNaturalService",["Restangular",
        function(Restangular){

            var _personaNaturalService = Restangular.all("personaNatural");
            var baseUrl = "personaNatural";

            return {
                getModel: function(){
                    return {
                        "id":undefined,
                        "tipoDocumento":undefined,
                        "numeroDocumento":undefined,
                        "apellidoPaterno":undefined,
                        "apellidoMaterno":undefined,
                        "nombres":undefined,
                        "fechaNacimiento":undefined,
                        "sexo":undefined,
                        "estadoCivil":undefined,
                        "ocupacion":undefined,
                        "direccion":undefined,
                        "referencia":undefined,
                        "telefono":undefined,
                        "celular":undefined,
                        "email":undefined,
                        "ubigeo":undefined,
                        "codigoPais":undefined
                    };
                },
                findById: function(id){
                    return Restangular.one(baseUrl, id).get();
                },
                findByTipoNumeroDocumento: function(idTipoDocumento, numeroDocumento){
                    return Restangular.one(baseUrl + '/buscar').get({idTipoDocumento:idTipoDocumento,numeroDocumento:numeroDocumento},{});
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
                        return _personaNaturalService.getList();
                    } else if(arguments.length == 1){
                        return _personaNaturalService.getList({"offset":offset},{});
                    } else if(arguments.length == 2){
                        return _personaNaturalService.getList({"offset":offset,"limit":limit},{});
                    } else if(arguments.length > 2){
                        return _personaNaturalService.getList({"offset":offset,"limit":limit},{});
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
                    var copy = Restangular.copy(persona);
                    return Restangular.one(baseUrl + "/"+persona.id).customPUT(copy,'',{},{});
                },
                crear: function(persona){
                    return _personaNaturalService.post(persona);
                },
                remove: function(id){
                    return Restangular.all(baseUrl + "/" + id).remove();
                },
                currentSession: function(){
                    return Restangular.one("personanatural/currentSession").get();
                },
                getFirma: function(id){
                    return Restangular.one("personanatural/"+id+"/firma").get();
                }
            }
        }])
});
