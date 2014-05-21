angular.module('cajaApp.service', []);

angular.module("cajaApp.service", ["restangular"])
    .factory("CajaSessionService",["Restangular",
        function(Restangular){
            var _cajaService = Restangular.all("caja/session");
            return {
                getCurrentCaja: function(){
                    return Restangular.one("caja/session").get();
                },
                getDetalle: function(){
                    return Restangular.all("caja/session/detalle").getList();
                },
                abrir: function(){
                    return Restangular.one("caja/session/abrir").put();
                },
                cerrar: function(detalle){
                    var copy = Restangular.copy(detalle);
                    return Restangular.all("caja/session/cerrar").post(copy);
                },
                getBovedasOfCurrentCaja: function(){
                    return Restangular.all("caja/session/bovedas").getList();
                },
                crearTransaccionBovedaCaja: function(boveda, detalle){
                    var copy = Restangular.copy(detalle);
                    return Restangular.all("caja/session/transaccionbovedacaja").post(copy ,{"boveda":boveda});
                },
                getTransaccionBovedaCajaEnviadas: function(){
                    return Restangular.all("caja/session/transaccionbovedacaja/enviados").getList();
                },
                getTransaccionBovedaCajaRecibidas: function(){
                    return Restangular.all("caja/session/transaccionbovedacaja/recibidos").getList();
                },
                getPendientes: function(){
                    return Restangular.all("caja/session/pendiente").getList();
                },
                crearPendiente: function(boveda,monto,observacion){
                    var data = $.param({idboveda:boveda,monto:monto,observacion:observacion});
                    return Restangular.one("caja/session/pendiente").customPOST(
                        data,
                        '',{},{
                            "Content-Type":"application/x-www-form-urlencoded"}
                    );
                },
                getPendiente: function(idPendiente){
                    return Restangular.one("pendiente/"+idPendiente).get();
                }
            }
        }])
        .factory("SocioService",["Restangular",
        function(Restangular){

            var _socioService = Restangular.all("socio");

            return {
                getSocios: function(){
                    return _socioService.getList().$object;
                },
                findByFilterText: function(text){
                    return Restangular.all("socio/filtertext/"+text).getList();
                }
            }
        }])

    .factory("HistorialCajaService",["Restangular",
        function(Restangular){
            var _historialCajaService = Restangular.all("historialcaja");
            return {
                buscar: function(desde, hasta){
                    return Restangular.all("historialcaja/currentSession").getList({"desde":desde,"hasta":hasta},{});
                },
                getVoucherCierreCaja: function(fechaAperturaCaja){
                    return Restangular.all("historialcaja/voucherCierreCaja").getList({"fechaApertura":fechaAperturaCaja},{});
                },
                getResumenCierreCaja: function(fechaAperturaCaja){
                    return Restangular.one("historialcaja/resumenCierreCaja").get({"fechaApertura":fechaAperturaCaja},{});
                }
            }
        }]);