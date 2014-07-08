define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarTransaccionBovedaCajaController', ['$scope', "$state", '$filter', "CajaSessionService",
        function($scope, $state, $filter, CajaSessionService) {

            $scope.nuevo = function(){
                $state.transitionTo('app.caja.createTransaccionBovedaCaja');
            }

            CajaSessionService.getTransaccionBovedaCajaEnviadas().then(
                function(enviados){
                    $scope.transaccionesEnviadas = enviados;
                }
            );
            CajaSessionService.getTransaccionBovedaCajaRecibidas().then(
                function(recibidos){
                    $scope.transaccionesRecibidas = recibidos;
                }
            );

            $scope.gridOptionsRecibidos = {
                data: 'transaccionesRecibidas',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha | date : 'dd/MM/yyyy'", displayName:'Fecha'},
                    {field:"hora | date : 'HH:mm:ss'", displayName:'Hora'},
                    {displayName: 'Estado solicitud', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estadoSolicitud">SOLICITADO</span><span ng-hide="row.entity.estadoSolicitud">NO SOLICITADO</span></div>'},
                    {displayName: 'Estado confirmacion', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estadoConfirmacion">CONFIRMADO</span><span ng-hide="row.entity.estadoConfirmacion">NO CONFIRMADO</span></div>'},
                    {field:"origen", displayName:'Origen'},
                    {field:"monto | currency :''", displayName:'Monto'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button>&nbsp;<button type="button" class="btn btn-danger btn-xs" ng-click="confirmarTransaccion(row.entity)"><span class="glyphicon glyphicon-remove"></span>Confirmar</button></div>'}]
            };

            $scope.gridOptionsEnviados = {
                data: 'transaccionesEnviadas',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha | date : 'dd/MM/yyyy'", displayName:'Fecha', width: "10%"},
                    {field:"hora | date : 'HH:mm:ss'", displayName:'Hora', width: "10%"},
                    {displayName: 'Estado solicitud', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estadoSolicitud">SOLICITADO</span><span ng-hide="row.entity.estadoSolicitud">NO SOLICITADO</span></div>'},
                    {displayName: 'Estado confirmacion', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estadoConfirmacion">CONFIRMADO</span><span ng-hide="row.entity.estadoConfirmacion">NO CONFIRMADO</span></div>'},
                    {field:"origen", displayName:'Origen', width: "12%"},
                    {field:"monto | currency :''", displayName:'Monto'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button>&nbsp;<button type="button" class="btn btn-danger btn-xs" ng-click="cancelarTransaccion(row.entity)"><span class="glyphicon glyphicon-remove"></span>Cancelar</button></div>'}]
            };

            $scope.getVoucher = function(row){
                $state.transitionTo('app.caja.voucherTransaccionBovedaCaja', { id: row.id });
            };

            $scope.cancelarTransaccion = function(row){

            };

        }]);
});