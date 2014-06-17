define(['./module'], function (services) {
    'use strict';
    services.factory("CuentaBancariaService",["Restangular",
        function(Restangular){
            var _historialCajaService = Restangular.all("cuentaBancaria");
            return {
                getCuentasBancarias: function(){
                    return _historialCajaService.getList();
                },
                getCuentasBancariasView: function(tipoPersonaList, tipoCuentaList, estadoCuentaList, monedaList ){
                    if(arguments.length == 0){
                        return Restangular.all("cuentaBancaria/view").getList();
                    }else if (arguments.length == 1) {
                        var trans = {"tipoPersonaList":tipoPersonaList}
                        return Restangular.all("cuentaBancaria/view/tipoCuenta/estadoCuenta/").post(trans);
                    } else if (arguments.length == 2) {
                        var trans = {
                            "tipoPersonaList":tipoPersonaList,
                            "tipoCuentaList":tipoCuentaList
                        }
                        return Restangular.all("cuentaBancaria/view/tipoCuenta/estadoCuenta/").post(trans);
                    } else if (arguments.length == 3) {
                        var trans = {
                            "tipoPersonaList":tipoPersonaList,
                            "tipoCuentaList":tipoCuentaList,
                            "estadoCuentaList":estadoCuentaList
                        }
                        return Restangular.all("cuentaBancaria/view/tipoCuenta/estadoCuenta/").post(trans);
                    } else if (arguments.length == 4) {
                        var trans = {
                            "tipoPersonaList":tipoPersonaList,
                            "tipoCuentaList":tipoCuentaList,
                            "estadoCuentaList":estadoCuentaList,
                            "monedaList":monedaList
                        }
                        return Restangular.all("cuentaBancaria/view/tipoCuenta/estadoCuenta/").post(trans);
                    }
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
					return Restangular.all("cuentaBancaria/view/filtertext/"+text).getList();
                },
                getSocio: function(idCuenta){
                    return Restangular.one("cuentaBancaria/"+idCuenta+"/socio").get();
                },
                getTitulares: function(idCuenta){
                    return Restangular.all("cuentaBancaria/"+idCuenta+"/titulares").getList();
                },
                getBeneficiarios: function(idCuenta){
                    return Restangular.all("cuentaBancaria/"+idCuenta+"/beneficiarios").getList();
                },
                getVoucherCuentaBancaria: function(id) {
                    return Restangular.one("cuentaBancaria/"+id+"/voucherCuentaBancaria").get();
                },
                getEstadoCuenta: function(id, desde, hasta) {
                    return Restangular.all("cuentaBancaria/"+id+"/estadoCuenta").getList({"desde":desde,"hasta":hasta},{});
                }
            }
        }])
});
