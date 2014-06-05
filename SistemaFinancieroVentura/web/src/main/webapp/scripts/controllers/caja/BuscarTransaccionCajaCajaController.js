define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarTransaccionCajaCajaController', ['$scope', "$state", '$filter', "CajaSessionService",
        function($scope, $state, $filter, CajaSessionService) {

            $scope.nuevo = function(){
                $state.transitionTo('app.caja.createTransaccionCajaCaja');
            }

            CajaSessionService.getTransaccionCajaCajaEnviadas().then(
                function(enviados){
                    $scope.transaccionesEnviadas = enviados;
                }
            );
            CajaSessionService.getTransaccionCajaCajaRecibidas().then(
                function(recibidos){
                    $scope.transaccionesRecibidas = recibidos;
                }
            );

            $scope.gridOptionsEnviados = {
                data: 'transaccionesEnviadas',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha | date : 'dd/MM/yyyy'", displayName:'Fecha'},
                    {field:"hora | date : 'HH:mm:ss'", displayName:'Hora'},
                    {field:"estadoSolicitud | date : 'dd/MM/yyyy'", displayName:'Estado solicitud'},
                    {field:"estadoConfirmacion | date : 'hh:mm:ss'", displayName:'Estado cierre'},
                    {field:"origen", displayName:'Origen'},
                    {field:"monto | currency :''", displayName:'Monto'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button></div>'}]
            };

            $scope.gridOptionsRecibidos = {
                data: 'transaccionesRecibidas',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha | date : 'dd/MM/yyyy'", displayName:'Fecha'},
                    {field:"hora | date : 'HH:mm:ss'", displayName:'Hora'},
                    {field:"estadoSolicitud | date : 'dd/MM/yyyy'", displayName:'Estado solicitud'},
                    {field:"estadoConfirmacion | date : 'hh:mm:ss'", displayName:'Estado cierre'},
                    {field:"origen", displayName:'Origen'},
                    {field:"monto | currency :''", displayName:'Monto'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button></div>'}]
            };

            $scope.getVoucher = function(row){
                $state.transitionTo('app.caja.voucherTransaccionCajaCaja', { id: row.id });
            }

        }]);
});