define(['./module'], function (controllers) {
    'use strict';

    controllers.controller('MainController', [ "$scope", "hotkeys", "CajaSessionService", "UsuarioSessionService", "AgenciaSessionService",
        function($scope, hotkeys,  CajaSessionService, UsuarioSessionService, AgenciaSessionService) {

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
                    $scope.crearTransaccion();
                    event.preventDefault();
                }
            });

            hotkeys.add({
                combo: 'ctrl+esc',
                description: 'Cancelar',
                allowIn: ['INPUT', 'SELECT', 'TEXTAREA'],
                callback: function(event, hotkey) {
                    $scope.cancelar();
                    event.preventDefault();
                }
            });

            $scope.crearTransaccion = function(){
                console.log("creando transaccion");
            }

            $scope.cancelar = function(){
                console.log("cancelar");
            }

        }]);
});