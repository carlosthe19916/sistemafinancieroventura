define(['./module'], function (services) {
    'use strict';
    services.factory("PersonaJuridicaService",["Restangular",
        function(Restangular){
            var _personaJuridicaService = Restangular.all("personaJuridica");

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
                crear: function(personaJuridica){
                    return _personaJuridicaService.post(personaJuridica);
                },
                update: function(persona){
                    return Restangular.one("personaJuridica/"+persona.id).customPUT(persona,'',{},{});
                },
                remove: function(id){
                    return Restangular.all("personaJuridica/"+id).remove();
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
                findById: function(id){
                    return Restangular.one("personaJuridica", id).get();
                },
                findByTipoNumeroDocumento: function(idtipodocumento, numerodocumento){
                    return Restangular.one('personaJuridica'+'/'+idtipodocumento+'/'+numerodocumento).get();
                },
                findByFilterText: function(text){
                    return Restangular.all("personaJuridica/filtertext/"+text).getList();
                }
            }
        }])
});
