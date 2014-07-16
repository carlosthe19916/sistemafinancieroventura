define(['./module'], function (controllers) {
    'use strict';

    controllers.controller('MainController', ["$scope","$state", "$window", "hotkeys", "CajaSessionService", "UsuarioSessionService", "AgenciaSessionService","HotKeysFunctionsService", "RedirectService",
        function($scope,$state, $window, hotkeys,  CajaSessionService, UsuarioSessionService, AgenciaSessionService, HotKeysFunctionsService, RedirectService) {

            $scope.$watch('redirect', function(newValue, oldvalue){
                if(newValue != oldvalue)
                    if($scope.redirect == true)
                        RedirectService.limpiar();
            }, true);

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

        }]);
});