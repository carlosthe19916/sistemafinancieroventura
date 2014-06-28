define(['./module'], function (services) {
    'use strict';
    services.factory('RedirectService', function(){

        //STATE TO TRANSITION
        var nextState = [];
        var paramsState = [];
        var object = [];
        var focusElement = [];

        return {
            haveNext: function(){
                return nextState.length > 0;
            },
            addNext: function(state, params, obj, element){
                if(arguments.length == 1){
                    nextState.push(state);
                    paramsState.push({});
                    object.push({});
                    focusElement.push({});
                } else if(arguments.length == 2){
                    nextState.push(state);
                    paramsState.push(params);
                    object.push({});
                    focusElement.push({});
                } else if(arguments.length == 3){
                    nextState.push(state);
                    paramsState.push(params);
                    object.push(obj);
                    focusElement.push({});
                } else if(arguments.length == 4){
                    nextState.push(state);
                    paramsState.push(params);
                    object.push(obj);
                    focusElement.push(element);
                }
            },
            getNextState: function() {
                return nextState[nextState.length-1];
            },
            getNextObject: function(){
                return object[object.length-1];
            },
            getNextParamsState: function(){
                return paramsState[paramsState.length-1];
            },
            getNextFocusElement: function(){
                return focusElement[focusElement.length-1];
            },
            clearLast: function(){
                object.pop();
                nextState.pop();
                paramsState.pop();
                focusElement.pop();
            },
            limpiar: function(){
                object = [];
                nextState = [];
                paramsState = [];
                focusElement = [];
            }
        }
    })
});
