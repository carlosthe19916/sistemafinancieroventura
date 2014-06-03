/*jshint unused: vars */
require.config({
  paths: {
    jquery: '../bower_components/jquery/dist/jquery',
    bootstrap: '../bower_components/bootstrap/dist/js/bootstrap',
    'angular-scenario': '../bower_components/angular-scenario/angular-scenario',
    'angular-mocks': '../bower_components/angular-mocks/angular-mocks',
    angular: '../bower_components/angular/angular'
  },
  shim: {
    angular: {
      exports: 'angular'
    },
    'angular-mocks': {
      deps: [
        'angular'
      ],
      exports: 'angular.mock'
    }
  },
  priority: [
    'angular'
  ]
});

//http://code.angularjs.org/1.2.1/docs/guide/bootstrap#overview_deferred-bootstrap
window.name = 'NG_DEFER_BOOTSTRAP!';

require([
  'angular',
  'app'
], function(angular, app) {
  'use strict';
  /* jshint ignore:start */
  var $html = angular.element(document.getElementsByTagName('html')[0]);
  /* jshint ignore:end */
  angular.element().ready(function() {
    angular.resumeBootstrap([app.name]);
  });
});