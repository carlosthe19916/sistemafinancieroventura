define(['./module'], function (services) {
    'use strict';
    services.factory("PersonaNaturalService",["Restangular",
        function(Restangular){

            var personaResponse = undefined;
            var _personanaturalService = Restangular.all("personanatural");

            return {
                guardarPersonaResponse:function (data) {
                    personaResponse = data;
                    console.log(data);
                },
                getPersonaResponse:function () {
                    return personaResponse;
                },
                getModel: function(){
                    return {
                        "id":undefined,
                        "tipoDocumento":{"id":undefined},
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
                    return _personanaturalService.post(persona);
                },
                crearWithImages: function(persona,foto){
                    var data = $.param({persona:persona,foto:foto});
                    return Restangular.one("personanatural").customPOST(
                        data,
                        '',{},{
                            "Content-Type":"multipart/form-data"}
                    );
                },
                update: function(persona){
                    var copy = Restangular.copy(persona);
                    return copy.put();
                },
                remove: function(id){
                    return Restangular.all("personanatural/"+id).remove();
                },
                getPersonas: function(){
                    return _personanaturalService.getList().$object;
                },
                findById: function(id){
                    return Restangular.one("personanatural", id).get().$object;
                },
                findByTipoNumeroDocumento: function(idtipodocumento, numerodocumento){
                    return Restangular.one('personanatural'+'/'+idtipodocumento+'/'+numerodocumento).get();
                },
                findByFilterText: function(text){
                    return Restangular.all("personanatural/filtertext/"+text).getList();
                },
                currentSession: function(){
                    return Restangular.one("personanatural/currentSession").get().$object;
                }
            }
        }])
});
