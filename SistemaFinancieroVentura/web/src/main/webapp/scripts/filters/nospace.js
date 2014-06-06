define(['./module'], function (filters) {
    'use strict';
    filters.filter('nospace', function () {
        return function (value) {
            return (!value) ? '' : value.replace(/ /g, '');
        };
    });
});

