define(['./module'], function (services) {
    'use strict';
    services.factory('RedirectService', function(){

        //STATE TO TRANSITION
        var nextState = '';
        var paramsState = undefined;
        var object = undefined;
        var focusElement = undefined;

        return {
            getNextState: function() {
                return nextState;
            },
            setNextState: function(state) {
                nextState = state;
            },
            haveNextState: function(){
              return nextState.length > 0;
            },
            clearNextState: function(){
                nextState = '';
            },
            getObject: function(){
                return object;
            },
            setObject: function(obj){
                object = obj;
            },
            clearObject: function(){
                object = undefined;
            },
            getParamsState: function(){
                return paramsState;
            },
            setParamsState: function(params){
                paramsState = params;
            },
            clearParamsState: function(){
                paramsState = undefined;
            },
            getFocusElement: function(){
                return focusElement;
            },
            setFocusElement: function(element){
                focusElement = element;
            },
            clearFocusElement: function(){
                focusElement = undefined;
            },
            clearAll:function(){
                object = undefined;
                nextState = '';
                paramsState = undefined;
                focusElement = undefined;
            }
        }
    })
});
