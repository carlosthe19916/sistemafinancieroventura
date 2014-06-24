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
                crear: function(persona){
                    persona.tipoDocumento = {"id": persona.tipoDocumento.id};
                    return _personaNaturalService.post(persona);
                },
                update: function(persona){
                    var copy = Restangular.copy(persona);
                    copy.tipoDocumento = {"id":copy.tipoDocumento.id}
                    return copy.put();
                },
                remove: function(id){
                    return Restangular.all(baseUrl + "/" + id).remove();
                },

                findById: function(id){
                    return Restangular.one(baseUrl, id).get();
                },
                findByTipoNumeroDocumento: function(idtipodocumento, numerodocumento){
                    return Restangular.one(baseUrl+'/'+idtipodocumento+'/'+numerodocumento).get();
                },
                getPersonas: function(desde, hasta){
                    if(arguments.length == 0){
                        return _personaNaturalService.getList();
                    } else if(arguments.length == 1){
                        return _personaNaturalService.getList({"desde":desde},{});
                    } else if(arguments.length == 2){
                        return _personaNaturalService.getList({"desde":desde,"hasta":hasta},{});
                    } else if(arguments.length > 2){
                        return _personaNaturalService.getList({"desde":desde,"hasta":hasta},{});
                    }
                },
                findByFilterText: function(filterText, desde, hasta){
                    if(arguments.length == 0){
                        return Restangular.all(baseUrl + "/filtertext/" + filterText).getList();
                    } else if(arguments.length == 1){
                        return Restangular.all(baseUrl + "/filtertext/" + filterText).getList({"desde":desde},{});
                    } else if(arguments.length == 2){
                        return Restangular.all(baseUrl + "/filtertext/" + filterText).getList({"desde":desde,"hasta":hasta},{});
                    } else if(arguments.length > 2){
                        return Restangular.all(baseUrl + "/filtertext/" + filterText).getList({"desde":desde,"hasta":hasta},{});
                    }
                },
                count: function(filterText){
                    if(arguments.length == 0){
                        return Restangular.one(baseUrl + "/count").get();
                    } else if(arguments.length == 1){
                        return Restangular.one(baseUrl + "/count").get({"filterText":filterText},{});
                    }
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
