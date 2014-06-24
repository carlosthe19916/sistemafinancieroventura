define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("PanelSocioController", ["$scope", "$state","$location", "$window", "SocioService", "MaestroService",
        function($scope, $state,$location, $window, SocioService, MaestroService) {

            SocioService.getSocio($scope.id).then(
                function(data){
                    $scope.socio = data;
                }, function error(error){
                    $scope.alerts = [{ type: "danger", msg: "Socio no encontrado."}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                }
            );
            SocioService.getCuentaAporte($scope.id).then(
                function(data){
                    $scope.cuentaAporte = data;
                }, function error(error){
                    $scope.alerts = [{ type: "warning", msg: "Cuenta de aporte no encontrada."}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                }
            );
            SocioService.getPersonaNatural($scope.id).then(function(data){
                $scope.personaNatural = data;
                var abreviaturaPais = $scope.personaNatural.codigoPais;
                MaestroService.getPaisByAbreviatura(abreviaturaPais).then(function(pais){
                    $scope.pais = pais;
                });
            });
            SocioService.getPersonaJuridica($scope.id).then(function(data){
                $scope.personaJuridica = data;
                var abreviaturaPais = $scope.personaJuridica.representanteLegal.codigoPais;
                MaestroService.getPaisByAbreviatura(abreviaturaPais).then(function(pais){
                    $scope.pais = pais;
                });
            });
            SocioService.getApoderado($scope.id).then(function(data){
                $scope.apoderado = data;
            });

            SocioService.getCuentasBancarias($scope.id).then(function(data){
                $scope.cuentasBancarias = data;
            });


            $scope.editarSocioPN = function(){
                var baseLen = $location.absUrl().length - $location.url().length;
                var url = $location.absUrl().substring(0, baseLen);
                $window.open(url + "/app/administracion/personaNatural/"+$scope.personaNatural.id);
            };
            $scope.editarSocioPJ = function(){
                var baseLen = $location.absUrl().length - $location.url().length;
                var url = $location.absUrl().substring(0, baseLen);
                $window.open(url + "/app/administracion/personaJuridica/"+$scope.personaJuridica.id);
            };
            $scope.editarRepresentantePJ = function(){
                var baseLen = $location.absUrl().length - $location.url().length;
                var url = $location.absUrl().substring(0, baseLen);
                $window.open(url + "/app/administracion/personaNatural/"+$scope.personaJuridica.representanteLegal.id);
            };
            $scope.editarApoderado = function(){
                if(!angular.isUndefined($scope.apoderado)){
                    var baseLen = $location.absUrl().length - $location.url().length;
                    var url = $location.absUrl().substring(0, baseLen);
                    $window.open(url + "/app/administracion/personaNatural/"+$scope.apoderado.id);
                }
            };
            $scope.editarCuentaBancaria = function(index){
                if(!angular.isUndefined($scope.cuentasBancarias)){
                    $state.transitionTo("app.socio.editarCuentaBancaria", { id: $scope.cuentasBancarias[index].id });
                }
            };

        }]);
});