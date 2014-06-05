/*jshint unused: vars */
define([
        'angular',
        'uiRouter',
        './controllers/main',
       // './directives/index',
        //'./filters/index',
        './services/main'
    ]/*deps*/,
    function (angular, MainCtrl)/*invoke*/ {
        'use strict';

        return angular.module('cajaApp', [
            'cajaApp.services',
            'cajaApp.controllers',
           // 'cajaApp.filters',
           // 'cajaApp.directives'
            /*angJSDeps*/
            'ui.router',
            'restangular',
            'ngProgress',
            'ui.bootstrap',
            'ngGrid',
            'ngSanitize',
            'ui.utils',
            'blockUI',
            'flow',
            'focusOn'
        ]);
    }
);
