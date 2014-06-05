define(['./module'], function (services) {
    'use strict';
    services.factory("UsuarioSessionService",["Restangular",
        function(Restangular){

            var _usuarioService = Restangular.all("usuario/session");

            return {
                getCurrentUsuario: function(){
                    return Restangular.one("usuario/session").get();
                }
            }
        }])
});
