define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('LoadImageController', function ($scope) {


        $scope.config = function(id){
            return {
                singleFile: true,
                target: '/SistemaFinancieroVentura-web/services/file/upload',
                query: function (flowFile, flowChunk) {
                    return {
                        id: id, source: 'flow_query'
                    };
                }
            }
        }

    });
});