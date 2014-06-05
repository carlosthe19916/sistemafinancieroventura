define(['angular'], function (angular) {
  'use strict';

  angular.module('cajaApp.directives.Mydirective', [])
  	.directive('myDirective', function () {
      return {
      	template: '<div></div>',
      	restrict: 'E',
      	link: function postLink(scope, element, attrs) {
          element.text('this is the myDirective directive');
      	}
      };
  	});
});
