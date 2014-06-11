define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BeneficiarioController', [ "$scope",
        function($scope) {

            $scope.control = {"submitted" : false};

            $scope.beneficiario = {
                "numeroDocumento" : undefined,
                "apellidoPaterno" : undefined,
                "apellidoMaterno" : undefined,
                "nombres" : undefined,
                "porcentajeBeneficio" : undefined
            }

            //$scope.beneficiarios = [];

            $scope.addBeneficiario = function() {
                if($scope.formBeneficiario.$valid){
                    var totalActual = $scope.totalPorcentaje();
                    var totalFinal = totalActual + $scope.beneficiario.porcentajeBeneficio;
                    if(totalFinal != 100){
                        $scope.beneficiarios.push({
                            "numeroDocumento" : $scope.beneficiario.numeroDocumento,
                            "apellidoPaterno" : $scope.beneficiario.apellidoPaterno,
                            "apellidoMaterno" : $scope.beneficiario.apellidoMaterno,
                            "nombres" : $scope.beneficiario.nombres,
                            "porcentajeBeneficio" : $scope.beneficiario.porcentajeBeneficio
                        });
                        $scope.clear();
                        $scope.resetFocus();
                    } else {
                        $scope.alerts = [
                            { type: 'danger', msg: 'Error: Porcentaje debe ser el 100%' }
                        ];
                        $scope.closeAlert = function(index) {
                            $scope.alerts.splice(index, 1);
                        };
                    }
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.removeBeneficiario = function(index){
                $scope.beneficiarios.splice(index, 1);
                $scope.resetFocus();
            }

            $scope.clear = function(){
                $scope.beneficiario = {
                    "numeroDocumento" : "",
                    "apellidoPaterno" : "",
                    "apellidoMaterno" : "",
                    "nombres" : "",
                    "porcentajeBeneficio" : undefined
                }
                $scope.resetFocus();
            }

            $scope.resetFocus = function(){
                angular.element("#txtNumeroDocumentoBeneficiario").focus();
            }

            $scope.totalPorcentaje = function(){
                var total = 0;
                for(var i = 0; i < $scope.beneficiarios.length; i++)
                    total = total + $scope.beneficiarios[i].porcentajeBeneficio;
                return total;
            }
        }]);
});