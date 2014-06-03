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
                getMonedasOfCurrentCaja: function(){
                    return Restangular.all("caja/session/monedas").getList();
                },
                abrir: function(){
                    return Restangular.one("caja/session/abrir").post();
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
                crearTransaccionCajaCaja: function(boveda, detalle){
                    var copy = Restangular.copy(detalle);
                    return Restangular.all("caja/session/transaccioncajacaja").post(copy ,{"boveda":boveda});
                },
                getTransaccionBovedaCajaEnviadas: function(){
                    return Restangular.all("caja/session/transaccionbovedacaja/enviados").getList();
                },
                getTransaccionBovedaCajaRecibidas: function(){
                    return Restangular.all("caja/session/transaccionbovedacaja/recibidos").getList();
                },
                getTransaccionCajaCajaEnviadas: function(){
                    return Restangular.all("caja/session/transaccioncajacaja/enviados").getList();
                },
                getTransaccionCajaCajaRecibidas: function(){
                    return Restangular.all("caja/session/transaccioncajacaja/recibidos").getList();
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
                },
                getHistoriales: function(desde, hasta){
                    return Restangular.all("caja/session/historial").getList({"desde":desde,"hasta":hasta},{});
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
                getVoucherCierreCaja: function(idhistorial){
                    return Restangular.all("historialcaja/"+idhistorial+"/voucherCierreCaja").getList();
                },
                getResumenCierreCaja: function(idhistorial){
                    return Restangular.one("historialcaja/"+idhistorial+"/resumenCierreCaja").get();
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
                },
                /*crear: function(tipoPersona, idTipoDocumentoSocio, numeroDocumentoSocio, idTipoDocumentoApoderado,numeroDocumentoApoderado){
                    var data = $.param({
                            tipoPersona:tipoPersona,
                            idTipoDocumentoSocio:idTipoDocumentoSocio,
                            numeroDocumentoSocio:numeroDocumentoSocio,
                            idTipoDocumentoApoderado:idTipoDocumentoApoderado,
                            numeroDocumentoApoderado:numeroDocumentoApoderado}
                    );
                    return Restangular.one("socio").customPOST(
                        data,
                        '',{},{
                            "Content-Type":"application/x-www-form-urlencoded"}
                    );
                },*/
                crear : function(tipoPersona, idTipoDocumentoSocio, numeroDocumentoSocio, idTipoDocumentoApoderado,numeroDocumentoApoderado){
                    var socio = {
                        "tipoPersona" : tipoPersona,
                        "idTipoDocumentoSocio" : idTipoDocumentoSocio,
                        "numeroDocumentoSocio" : numeroDocumentoSocio,
                        "idTipoDocumentoApoderado" : idTipoDocumentoApoderado,
                        "numeroDocumentoApoderado" : numeroDocumentoApoderado
                    }
                    return Restangular.all("socio").post(socio);
                },
                getSocio: function(id){
                    return Restangular.one("socio/"+id).get();
                },
                getCuentaAporte: function(id){
                    return Restangular.one("socio/"+id+"/cuentaAporte").get();
                },
                getPersonaNatural: function(id){
                    return Restangular.one("socio/"+id+"/personaNatural").get();
                },
                getPersonaJuridica: function(id){
                    return Restangular.one("socio/"+id+"/personaJuridica").get();
                },
                getApoderado: function(id){
                    return Restangular.one("socio/"+id+"/apoderado").get();
                },
                getCuentasBancarias: function(id){
                    return Restangular.all("socio/"+id+"/cuentasBancarias").getList();
                }
            }
        }])
    .factory("CuentaBancariaService",["Restangular",
        function(Restangular){
            var _historialCajaService = Restangular.all("cuentaBancaria");
            return {
                getCuentasBancarias: function(){
                    return _historialCajaService.getList().$object;
                },
                getCuentasBancariasView: function(){
                    return Restangular.all("cuentaBancaria/a").getList().$object;
                },
                getCuentasBancaria: function(id){
                    return Restangular.one("cuentaBancaria/"+id).get();
                },
                crearCuentaAhorro: function(transaccion){
                    return Restangular.all("cuentaBancaria/ahorro").post(transaccion);
                },
                crearCuentaCorriente: function(transaccion){
                    return Restangular.all("cuentaBancaria/corriente").post(transaccion);
                },
                crearCuentaPlazoFijo: function(transaccion){
                    return Restangular.all("cuentaBancaria/plazoFijo").post(transaccion);
                },
                findByFilterText: function(text){
                    return Restangular.all("cuentaBancaria/filtertext/"+text).getList();
                },
                findByFilterTextView: function(text){
                    return Restangular.all("cuentaBancaria/filtertext/"+text).getList();
                }
            }
        }]);