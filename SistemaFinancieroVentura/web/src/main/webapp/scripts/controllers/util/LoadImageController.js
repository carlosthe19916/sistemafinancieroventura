define(['../module'], function (controllers) {
    'use strict';

    controllers.controller("LoadImageController", ["$scope",
        function($scope) {

            $scope.uploader = {};
            $scope.upload = function () {
                //$scope.uploader.flow.upload();
               /* console.log($scope.uploader.flow);
                var blob = new Blob(['a'], {type: "image/png"});
                blob.name = 'file.png';
                $scope.uploader.flow.addFile(blob);*/
            }

            $scope.getFoto = function(id) {
                return "http://localhost:8080/SistemaFinancieroVentura-web/services/personanatural/image/"+id+"/firma";
            }

            $scope.config = function(id){
                return {
                    singleFile: true,
                    target: '/SistemaFinancieroVentura-web/services/personanatural/image/firma/upload',
                    query: function (flowFile, flowChunk) {
                        return {
                            id: id, source: 'flow_query'
                        };
                    }
                }
            }

        }]);
});