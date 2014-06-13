define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BeneficiarioPopUpController', [ "$scope", "$modalInstance", "total", "obj",
        function($scope, $modalInstance, total, obj) {

            $scope.control = {"submitted" : false};

            $scope.beneficiario = {
                "id" : undefined,
                "numeroDocumento" : undefined,
                "apellidoPaterno" : undefined,
                "apellidoMaterno" : undefined,
                "nombres" : undefined,
                "porcentajeBeneficio" : undefined
            }

            //cargar beneficiario si es enviado
            $scope.loadParameters = function(){
                if(!angular.isUndefined(obj)){
                    $scope.beneficiario.id = obj.id;
                    $scope.beneficiario.numeroDocumento = obj.numeroDocumento;
                    $scope.beneficiario.apellidoPaterno = obj.apellidoPaterno;
                    $scope.beneficiario.apellidoMaterno = obj.apellidoMaterno;
                    $scope.beneficiario.nombres = obj.nombres;
                    $scope.beneficiario.porcentajeBeneficio = obj.porcentajeBeneficio;

                    total = total - $scope.beneficiario.porcentajeBeneficio;
                }
            }
            $scope.loadParameters();

            $scope.formBeneficiario = {};
            $scope.addBeneficiario = function() {
                if($scope.formBeneficiario.$valid){
                    var totalActual = total;
                    var totalFinal = totalActual + $scope.beneficiario.porcentajeBeneficio;
                    if(totalFinal <= 100){
                        $scope.ok();
                    } else {
                        $scope.alertsBeneficiario = [{ type: 'danger', msg: 'Error: Porcentaje no debe superar el 100%' } ];
                        $scope.closeAlert = function(index) {$scope.alertsBeneficiario.splice(index, 1);};
                    }
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.ok = function () {
                $modalInstance.close($scope.beneficiario);
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        }]);
});