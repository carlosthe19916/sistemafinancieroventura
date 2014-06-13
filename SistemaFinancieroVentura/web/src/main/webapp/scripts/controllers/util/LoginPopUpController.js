define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("LoginPopUpController", ["$scope", "$modalInstance", "UsuarioService",
        function($scope, $modalInstance, UsuarioService) {

            $scope.control = {"submited":false,"inProgress":false, "failure" : true};

            $scope.usuario = {"username":"", "password":""};
            $scope.login = {"result" : false};

            $scope.formLoginPopUp = {};
            $scope.ok = function () {
                if($scope.formLoginPopUp.$valid){
                    UsuarioService.authenticationAsAdministrator($scope.usuario.username, $scope.usuario.password).then(
                        function(data){
                            $scope.login.result = true;
                            $modalInstance.close($scope.login);
                        },
                        function error(error){
                            $scope.login.result = false;
                            $scope.control.failure = false;
                        }
                    );
                } else {
                    $scope.control = {"submited":true};
                }
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        }]);
});