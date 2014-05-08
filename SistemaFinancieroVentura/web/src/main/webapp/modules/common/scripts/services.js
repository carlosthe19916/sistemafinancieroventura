
angular.module("commonApp.service", ["restangular"])
    .factory("UsuarioService",["Restangular",
        function(Restangular){

            var _usuarioService = Restangular.all("usuario");

            return {
                getCurrentUsuario: function(){
                    return Restangular.one("usuario/currentSession").get();
                }
            }
        }])
    .factory("AgenciaService",["Restangular",
        function(Restangular){

            var _agenciaService = Restangular.all("agencia");

            return {
                getCurrentAgencia: function(){
                    return Restangular.one("agencia/currentSession").get();
                }
            }
        }])
    .factory("PersonanaturalService",["Restangular",
        function(Restangular){

            var _personanaturalService = Restangular.all("personanatural");

            return {
                create: function(persona){
                    return _personanaturalService.post(persona);
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
        }]);