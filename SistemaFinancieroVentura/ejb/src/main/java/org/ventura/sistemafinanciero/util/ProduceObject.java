package org.ventura.sistemafinanciero.util;

import java.math.BigInteger;

import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.CuentaAporte;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.type.TipoCuentaBancaria;

public class ProduceObject {

	public final static String CODIGO_CUENTA_APORTE = "10";
	public final static String CODIGO_CUENTA_AHORRO = "11";
	public final static String CODIGO_CUENTA_CORRIENTE = "12";
	public final static String CODIGO_CUENTA_PLAZO_FIJO = "13";
	
	public static String completar(String cad, int caracteres){
		StringBuffer sb = new StringBuffer(cad);
		for (int i = cad.length(); i < caracteres; i++) {
			sb.append("0");
		}
		return sb.toString();
	}
	
	public static String getNumeroCuenta(CuentaAporte cuenta){
		String numeroCuenta = cuenta.getNumeroCuenta();
		
		BigInteger idCuenta = cuenta.getIdCuentaaporte();
		BigInteger idMoneda = cuenta.getMoneda().getIdMoneda();	
		
		numeroCuenta = numeroCuenta +  completar(idCuenta.toString(), 8);
		numeroCuenta = numeroCuenta + idMoneda;
		numeroCuenta = numeroCuenta + ProduceObject.CODIGO_CUENTA_APORTE;		
		
		return numeroCuenta;
	}
	
	public static String getNumeroCuenta(CuentaBancaria cuentaBancaria){
		String numeroCuenta = cuentaBancaria.getNumeroCuenta();
		
		BigInteger idCuenta = cuentaBancaria.getIdCuentaBancaria();
		BigInteger idMoneda = cuentaBancaria.getMoneda().getIdMoneda();
		TipoCuentaBancaria tipoCuenta = cuentaBancaria.getTipoCuentaBancaria();		
		
		numeroCuenta = numeroCuenta +  completar(idCuenta.toString(), 8);
		numeroCuenta = numeroCuenta + idMoneda;
		
		switch (tipoCuenta) {
		case AHORRO:
			numeroCuenta = numeroCuenta + ProduceObject.CODIGO_CUENTA_AHORRO;
			break;
		case CORRIENTE:
			numeroCuenta = numeroCuenta + ProduceObject.CODIGO_CUENTA_CORRIENTE;
			break;
		case PLAZO_FIJO:
			numeroCuenta = numeroCuenta + ProduceObject.CODIGO_CUENTA_PLAZO_FIJO;
			break;
		default:
			break;
		}
		
		return numeroCuenta;
	}
	
	public static Agencia getAgenciafromNumeroCuenta(String numeroCuenta){
		Agencia agencia = null;
		return agencia;
	}
	
	public static Moneda getMonedaPrincipal(){
		Moneda moneda = new Moneda();
		moneda.setIdMoneda(BigInteger.ONE);
		moneda.setDenominacion("NUEVO SOL");
		moneda.setSimbolo("S/.");
		return moneda;
	}
}
