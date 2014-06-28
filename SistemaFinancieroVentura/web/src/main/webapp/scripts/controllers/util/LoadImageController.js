define(['../module'], function (controllers) {
    'use strict';

    controllers.controller("LoadImageController", ["$scope",
        function($scope) {

            $scope.urlBase = "http://localhost:8080/SistemaFinancieroVentura-web/services/personaNatural/";
            $scope.baseTarget = "/SistemaFinancieroVentura-web/services/personaNatural/";
            $scope.idPersona = undefined;
            $scope.tipoImagen = undefined;

            //tipofoto ppuede ser FOTO O FIRMA
            $scope.configImagen = function(id, tipoFoto){
                $scope.tipoImagen = tipoFoto;
                if(!angular.isUndefined(id)){
                    return {
                        singleFile: true,
                        target: $scope.baseTarget + id + '/' + tipoFoto
                    }
                } else {
                    return {
                        singleFile: true,
                        target: '/upload'
                    }
                }
            };

            $scope.getImagen = function(id,tipoFoto) {
                if(!angular.isUndefined(id))
                    return $scope.urlBase + id + "/" + tipoFoto;
            };


            $scope.$watch("view.id", function(){
                if(!angular.isUndefined($scope.view.id)){
                    $scope.idPersona = $scope.view.id;
                    $scope.$flow.opts.target = $scope.baseTarget + $scope.idPersona + '/' + $scope.tipoImagen;
                }
            });

        }]);
});