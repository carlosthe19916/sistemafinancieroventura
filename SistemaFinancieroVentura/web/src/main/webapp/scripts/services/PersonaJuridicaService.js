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
                    var copy = Restangular.copy(persona);
                    return copy.put();
                },
                remove: function(id){
                    return Restangular.all("personaJuridica/"+id).remove();
                },
                getPersonas: function(){
                    return _personaJuridicaService.getList().$object;
                },
                findById: function(id){
                    return Restangular.one("personaJuridica", id).get().$object;
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
