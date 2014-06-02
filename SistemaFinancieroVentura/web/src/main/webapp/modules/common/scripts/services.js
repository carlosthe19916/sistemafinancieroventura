
angular.module("commonApp.service", ["restangular"])
    .factory("UsuarioSessionService",["Restangular",
        function(Restangular){

            var _usuarioService = Restangular.all("usuario/session");

            return {
                getCurrentUsuario: function(){
                    return Restangular.one("usuario/session").get();
                }
            }
        }])
    .factory("AgenciaSessionService",["Restangular",
        function(Restangular){

            var _agenciaService = Restangular.all("agencia");

            return {
                getCurrentAgencia: function(){
                    return Restangular.one("agencia/session").get();
                },
                getCajasOfAgencia: function(){
                    return Restangular.one("agencia/session/cajas").get();
                }
            }
        }])
    .factory("PersonaNaturalService",["Restangular",
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
    .factory("PersonaJuridicaService",["Restangular",
        function(Restangular){
            var _personaJuridicaService = Restangular.all("personaJuridica");

            return {
                getModel: function(){
                    return {
                        "id":undefined,
                        "tipoDocumento":{"id":undefined},
                        "numeroDocumento":undefined,
                        "razonSocial":undefined,
                        "nombreComercial":undefined,
                        "representanteLegal": {},
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
                crear: function(personaJuridica, representanteLegal, accionistas){
                    personaJuridica.representanteLegal = {"id":representanteLegal.id};
                    var accionistasFinal = [];
                    for(var i = 0; i < accionistas.length; i++){
                        var porcentaje = accionistas[i].porcentajeParticipacion;
                        var personaNatural = {"id":accionistas[i].personaNatural.id};
                        accionistasFinal[i] = {
                            "porcentajeParticipacion" : porcentaje,
                            "personaNatural" : personaNatural
                        }
                    }
                    personaJuridica.accionistas = accionistasFinal;
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
    .factory("TipodocumentoService",["Restangular",
        function(Restangular){

            var _personanaturalService = Restangular.all('tipodocumento/personanatural');
            var _personajuridicaService = Restangular.all('tipodocumento/personajuridica');

            return {
                getTipodocumentosPersonanatural: function() {
                    return _personanaturalService.getList();
                },
                getTipodocumentosPersonajuridica: function(user) {
                    return [
                        {'idTipoDocumento':'1',"denominacion":"prueba11"},{'idTipoDocumento':'2',"denominacion":"prudfdeba11"}
                    ];
                }
            }
        }])
    .factory("SexoService",["Restangular",
        function(Restangular){
            var _sexoService = Restangular.all('sexo');
            return {
                getSexos: function() {
                    return _sexoService.getList();
                }
            }
        }])
    .factory("EstadocivilService",["Restangular",
        function(Restangular){
            var _estadocivilService = Restangular.all('estadocivil');
            return {
                getEstadosciviles: function() {
                    return _estadocivilService.getList();
                }
            }
        }])
    .factory("MonedaService",["Restangular",
        function(Restangular){
            var _monedaService = Restangular.all('moneda');
            return {
                getMonedas: function() {
                    return Restangular.all("moneda").getList();
                },
                getDenominaciones: function(moneda) {
                    return Restangular.all("moneda/"+moneda+"/denominaciones").getList();
                }
            }
        }])
    .factory("MaestroService",["Restangular",
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
    .factory("TasaInteresService",["Restangular",
        function(Restangular){
            return {
                getTasaCuentaAhorro: function(idMoneda) {
                    return Restangular.one("tasa/ahorro/"+idMoneda).get();
                }
            }
        }]);