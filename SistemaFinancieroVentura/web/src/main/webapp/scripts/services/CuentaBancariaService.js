define(['./module'], function (services) {
    'use strict';
    services.factory("CuentaBancariaService",["Restangular",
        function(Restangular){
            var _historialCajaService = Restangular.all("cuentaBancaria");

            return {
                getCuentasBancarias: function(){
                    return _historialCajaService.getList();
                },
                getCuentasBancariasView: function(desde,hasta,tipoCuentaList,tipoPersonaList,estadoCuentaList){
                    if(arguments.length == 0){
                        return Restangular.all("cuentaBancaria/view").getList();
                    }else if (arguments.length == 1) {
                        return Restangular.all("cuentaBancaria/view").getList({"desde":desde},{});
                    } else if (arguments.length == 2) {
                        return Restangular.all("cuentaBancaria/view").getList({"desde":desde,"hasta":hasta},{});
                    } else if (arguments.length == 3) {
                        return Restangular.all("cuentaBancaria/view").getList({"desde":desde,"hasta":hasta,"tipoCuenta":tipoCuentaList},{});
                    } else if (arguments.length == 4) {
                        return Restangular.all("cuentaBancaria/view").getList({"desde":desde,"hasta":hasta,"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList},{});
                    } else if (arguments.length == 5) {
                        return Restangular.all("cuentaBancaria/view").getList({"desde":desde,"hasta":hasta,"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList,"tipoEstadoCuenta":estadoCuentaList},{});
                    }
                },
                findByFilterTextView: function(text,desde,hasta,tipoCuentaList,tipoPersonaList,estadoCuentaList){
                    if(arguments.length == 0){
                        return Restangular.all("cuentaBancaria/view").getList();
                    } else if (arguments.length == 1) {
                        return Restangular.all("cuentaBancaria/view/filtertext/"+text).getList({},{});
                    } else if (arguments.length == 2) {
                        return Restangular.all("cuentaBancaria/view/filtertext/"+text).getList({"desde":desde},{});
                    } else if (arguments.length == 3) {
                        return Restangular.all("cuentaBancaria/view/filtertext/"+text).getList({"desde":desde,"hasta":hasta},{});
                    } else if (arguments.length == 4) {
                        return Restangular.all("cuentaBancaria/view/filtertext/"+text).getList({"desde":desde,"hasta":hasta,"tipoCuenta":tipoCuentaList},{});
                    } else if (arguments.length == 5) {
                        return Restangular.all("cuentaBancaria/view/filtertext/"+text).getList({"desde":desde,"hasta":hasta,"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList},{});
                    } else if (arguments.length == 6) {
                        return Restangular.all("cuentaBancaria/view/filtertext/"+text).getList({"desde":desde,"hasta":hasta,"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList,"tipoEstadoCuenta":estadoCuentaList},{});
                    }
                },
                count: function(){
                    return Restangular.one("cuentaBancaria/view/count").get();
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
