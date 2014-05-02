angular.module('sistCoopApp', ['restangular']).factory('SucursalService', [
        'Restangular', function(Restangular) {
            var restAngular = Restangular.withConfig(function(Configurer) {
                    Configurer.setBaseUrl('/sucursal');
            });

            var _messageService = restAngular.all('sucursal');

            return {
                getMessages: function() {
                    return _messageService.getList();
                }
            }
        }
]);


