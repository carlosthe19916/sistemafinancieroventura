define(['../module'], function (controllers) {
    'use strict';

    controllers.controller("LoadImageController", ["$scope",
        function($scope) {

            $scope.getFirma = function(id) {
                return "http://localhost:8080/SistemaFinancieroVentura-web/services/personanatural/image/"+id+"/firma";
            }
            $scope.getFoto = function(id) {
                return "http://localhost:8080/SistemaFinancieroVentura-web/services/personanatural/image/"+id+"/foto";
            }

            $scope.configFirma = function(id){
                return {
                    singleFile: true,
                    target: '/SistemaFinancieroVentura-web/services/personanatural/image/firma/upload',
                    query: function (flowFile, flowChunk) {
                        return {
                            id: angular.isUndefined(id) ? $scope.idPersona : id
                        };
                    }
                }
            }
            $scope.configFoto = function(id){
                return {
                    singleFile: true,
                    target: '/SistemaFinancieroVentura-web/services/personanatural/image/foto/upload',
                    query: function (flowFile, flowChunk) {
                        return {
                            id: $scope.idPersona
                        };
                    }
                }
            }

            $scope.idPersona = undefined;
            $scope.$watch("persona", function(){
                if(!angular.isUndefined($scope.persona)){
                    $scope.idPersona = $scope.persona.id;
                }
            });

        }]);
});