define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BeneficiarioController', [ "$scope","$filter","focus",
        function($scope,$filter, focus) {

            $scope.control = {"submitted" : false};

            $scope.beneficiario = {
                "numeroDocumento" : undefined,
                "apellidoPaterno" : undefined,
                "apellidoMaterno" : undefined,
                "nombres" : undefined,
                "porcentajeBeneficio" : undefined
            };

            //$scope.beneficiarios = [];

            $scope.addBeneficiario = function() {
                if($scope.formBeneficiario.$valid){
                    var totalActual = $scope.totalPorcentaje();
                    var totalFinal = totalActual + $scope.beneficiario.porcentajeBeneficio;
                    if(totalFinal <= 100){
                        $scope.$parent.view.beneficiarios.push({
                            "numeroDocumento" : $scope.beneficiario.numeroDocumento,
                            "apellidoPaterno" : $scope.beneficiario.apellidoPaterno,
                            "apellidoMaterno" : $scope.beneficiario.apellidoMaterno,
                            "nombres" : $scope.beneficiario.nombres,
                            "porcentajeBeneficio" : $scope.beneficiario.porcentajeBeneficio
                        });
                        $scope.$parent.view.beneficiarios = $filter('unique')($scope.$parent.view.beneficiarios);
                        $scope.clear();
                        $scope.resetFocus();
                    } else {
                        $scope.alertsBeneficiario = [{ type: 'danger', msg: 'Error: Porcentaje no debe superar el 100%' } ];
                        $scope.closeAlert = function(index) {$scope.alertsBeneficiario.splice(index, 1);};
                    }
                }
            };

            $scope.removeBeneficiario = function(index){
                $scope.view.beneficiarios.splice(index, 1);
                $scope.resetFocus();
            };

            $scope.clear = function(){
                $scope.beneficiario = {
                    "numeroDocumento" : "",
                    "apellidoPaterno" : "",
                    "apellidoMaterno" : "",
                    "nombres" : "",
                    "porcentajeBeneficio" : undefined
                };
                $scope.resetFocus();
            };

            $scope.resetFocus = function(){
                focus($scope.focusElements.numeroDocumentoBeneficiario);
                $scope.formBeneficiario.$setPristine();
            };

            $scope.totalPorcentaje = function(){
                var total = 0;
                for(var i = 0; i < $scope.$parent.view.beneficiarios.length; i++)
                    total = total + $scope.$parent.view.beneficiarios[i].porcentajeBeneficio;
                return total;
            }
        }]);
});