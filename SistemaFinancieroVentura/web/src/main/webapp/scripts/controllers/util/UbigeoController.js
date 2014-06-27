define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('UbigeoController', ['$scope','MaestroService',
        function($scope,MaestroService) {

            $scope.ubigeo = {
              departamento: undefined,
              provincia: undefined,
              distrito: undefined,
              codigo:''
            };

            $scope.combos = {
              departamentos: undefined,
              provincias: undefined,
              distritos: undefined
            };

            //metodos para cargar los combos
            $scope.loadDepartamentos = function(){
                MaestroService.getDepartamentos().then(function(data){
                    $scope.combos.departamentos = data;
                });
            };
            $scope.loadDepartamentos();
            $scope.loadProvincias = function(){
                if(!angular.isUndefined($scope.ubigeo.departamento)){
                    MaestroService.getProvincias($scope.ubigeo.departamento.id).then(function(data){
                        $scope.combos.provincias = data;
                    });
                } else {
                    $scope.combos.provincias = [];
                }
            };
            $scope.loadDistritos = function(){
                if(!angular.isUndefined($scope.ubigeo.provincia)){
                    MaestroService.getDistritos($scope.ubigeo.provincia.id).then(function(data){
                        $scope.combos.distritos = data;
                    });
                } else {
                    $scope.combos.distritos = [];
                }
            };

            //metodos de cambio
            $scope.changeDepartamento = function(){
                $scope.loadProvincias();
            };
            $scope.changeProvincia = function(){
                $scope.loadDistritos();
            };
            $scope.changeDistrito = function(){
                $scope.initializeUbigeo();
            };

            $scope.initializeUbigeo = function(){
                var ubigeo = '';
                if(!angular.isUndefined($scope.ubigeo.departamento)){
                    ubigeo = $scope.ubigeo.departamento.codigo;
                    if(!angular.isUndefined($scope.ubigeo.provincia)){
                        ubigeo = ubigeo + $scope.ubigeo.provincia.codigo;
                        if(!angular.isUndefined($scope.ubigeo.distrito)){
                            ubigeo = ubigeo + $scope.ubigeo.distrito.codigo;

                            //poner ubigeo al objeto padre
                            $scope.view.persona.ubigeo = ubigeo;
                        }
                    }
                }
                $scope.ubigeo.codigo = ubigeo;
            };

        }]);
});