angular.module('sistCoopApp', ['restangular']).factory('SucursalService', [
        'Restangular', function(Restangular) {
            var restAngular = Restangular.withConfig(function(Configurer) {
                    Configurer.setBaseUrl('/api/v2/messages');
            });

            var _messageService = restAngular.all('messages');

            return {
                getMessages: function() {
                    return _messageService.getList();
                }
            }
        }
]);
