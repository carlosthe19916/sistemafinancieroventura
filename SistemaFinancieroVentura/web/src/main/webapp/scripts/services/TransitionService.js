define(['./module'], function (services) {
    'use strict';
    services.factory("TransitionService", function(){

        //STATE TO TRANSITION
        var urlTransition = "app.transaccion.depositoRetiro";

        //PARAMETERS TO PASS
        var parameters = {};

        //REDIRECT OR CLOSE ON COMPLETE
        var mode = 'CLOSE';
        var modes = ['REDIRECT', 'CLOSE'];

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
            },

            isModeRedirect: function(){
              return mode == modes[0];
            },
            isModeClose: function(){
              return mode == modes[1];
            },
            getMode: function(){
                return mode;
            },
            setModeRedirect: function(){
                mode = modes[0];
            },
            setModeClose: function(){
                mode = modes[1];
            }
        }
    })
});
