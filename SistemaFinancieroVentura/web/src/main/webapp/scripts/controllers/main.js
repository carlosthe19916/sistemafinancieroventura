define([
    'angular',
    './MainController',
    './NavBarController',

    './caja/AbrirCajaController',
    './caja/CerrarCajaController',
    './caja/VoucherCerrarCajaController',
    './caja/BuscarPendienteController',
    './caja/CrearPendienteController',
    './caja/VoucherPendienteController',
    './caja/BuscarTransaccionBovedaCajaController',
    './caja/CrearTransaccionBovedaCajaController',
    './caja/VoucherTransaccionBovedaCajaController',
    './caja/BuscarTransaccionCajaCajaController',
    './caja/CrearTransaccionCajaCajaController',
    './caja/VoucherTransaccionCajaCajaController',
    './caja/HistorialCajaController',

    './caja/transaccion/CrearTransaccionDepositoRetiroController',
    './caja/transaccion/CrearAporteController',

    './cuentaBancaria/BeneficiarioController',
    './cuentaBancaria/TitularController',
    './cuentaBancaria/BuscarCuentaBancariaController',
    './cuentaBancaria/CrearCuentaBancariaController',
    './cuentaBancaria/EditarCuentaBancariaController',
    './cuentaBancaria/CrearCuentaAhorroController',
    './cuentaBancaria/CrearCuentaCorrienteController',
    './cuentaBancaria/CrearCuentaPlazoFijoController',
    './cuentaBancaria/BuscarCuentaBancariaPopUpController',
    './cuentaBancaria/BeneficiarioPopUpController',
    './cuentaBancaria/TitularPopUpController',
    './cuentaBancaria/FirmasCuentaBancariaController',

    './persona/natural/BuscarPersonaNaturalController',
    './persona/natural/CrearPersonaNaturalController',
    './persona/juridica/AccionistaController',
    './persona/juridica/CrearPersonaJuridicaController',

    './socio/CrearSocioController',
    './socio/BuscarSocioController',
    './socio/PanelSocioController',
    './socio/BuscarSocioPopUpController',

    './util/CalculadoraController',
    './util/LoginPopUpController',
    './util/ConfirmPopUpController',
    './util/LoadImageController'
], function () {

});


/*
define([
    'angular'
], function (angular) {
  'use strict';

  angular.module('cajaApp.controllers.MainCtrl', [])
    .controller('MainCtrl', function ($scope) {
      $scope.awesomeThings = [
        'HTML5 Boilerplate',
        'AngularJS',
        'Karma'
      ];
    });
});*/