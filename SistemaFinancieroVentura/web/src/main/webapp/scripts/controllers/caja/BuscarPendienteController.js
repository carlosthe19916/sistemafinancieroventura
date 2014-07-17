define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarPendienteController', ["$scope", "$state", "$filter", "CajaSessionService",
        function($scope, $state, $filter, CajaSessionService) {

            $scope.crear = function(){
                $state.transitionTo('app.caja.pendienteCrear');
            };

            CajaSessionService.getPendientes().then(
                function(pendientes){
                    $scope.pendientes = pendientes;
                }
            );

            $scope.gridOptions = {
                data: 'pendientes',
                multiSelect: false,
                columnDefs: [
                    { field: "moneda.denominacion", displayName: "MONEDA"},
                    { displayName: 'MONTO', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span>{{row.entity.monto|number:2}}</span></div>'},
                    { field: "tipoPendiente", displayName: "TIPO"},
                    { field: "fecha | date : 'dd/MM/yyyy'", displayName: "FECHA"},
                    { field: "hora | date : 'HH:mm:ss' ", displayName: "HORA"},
                    {displayName: 'VOUCHER', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="voucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button></div>'}
                ]
            };

            $scope.voucher = function(pendiente) {
                $state.transitionTo('app.caja.pendienteVoucher', { id: pendiente.id });
            };

        }]);
});