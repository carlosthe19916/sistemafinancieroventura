define(['./module'], function (controllers) {
    'use strict';

    controllers.controller('MainController', [ "$scope", "$window", "hotkeys", "CajaSessionService", "UsuarioSessionService", "AgenciaSessionService","HotKeysFunctionsService",
        function($scope, $window, hotkeys,  CajaSessionService, UsuarioSessionService, AgenciaSessionService, HotKeysFunctionsService) {

            $scope.cajaSession = {
                "denominacion":"undefined",
                "abreviatura":"undefined",
                "abierto": false,
                "estadoMovimiento":false,
                "estado": false
            };

            $scope.agenciaSession = {
                "denominacion":"undefined",
                "abreviatura":"undefined",
                "ubigeo": "undefined",
                "estado":false
            };

            $scope.usuarioSession = undefined;

            CajaSessionService.getCurrentCaja().then(
                function(caja){
                    $scope.cajaSession = caja;
                }
            );
            UsuarioSessionService.getCurrentUsuario().then(
                function(usuario){
                    $scope.usuarioSession = usuario;
                }
            );
            AgenciaSessionService.getCurrentAgencia().then(
                function(agencia){
                    $scope.agenciaSession = agencia;
                }
            );

            hotkeys.add({
                combo: 'ctrl+enter',
                description: 'Guardar',
                allowIn: ['INPUT', 'SELECT', 'TEXTAREA'],
                callback: function(event, hotkey) {
                    var crearTransaccion = HotKeysFunctionsService.getEnterFunction();
                    crearTransaccion();
                    event.preventDefault();
                }
            });

        }]);
});