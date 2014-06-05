define(['angular'], function (angular) {
  'use strict';

  angular.module('cajaApp.filters.Myfilter', [])
  	.filter('myFilter', function () {
      return function (input) {
      	return 'myFilter filter: ' + input;
      };
  	});
});
