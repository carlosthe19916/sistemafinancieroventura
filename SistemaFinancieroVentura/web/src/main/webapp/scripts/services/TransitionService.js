define(['./module'], function (services) {
    'use strict';
    services.factory("TransitionService", function(){
        var urlTransition = "app.transaccion.depositoRetiro";
        var parameters = {};
        return {
            getUrl: function() {
                return urlTransition;
            },
            setUrl: function(url) {
                urlTransition = url;
            },
            getParameters: function(){
                return parameters;
            },
            setParameters: function(params){
                parameters = params;
            }
        }
    })
});
