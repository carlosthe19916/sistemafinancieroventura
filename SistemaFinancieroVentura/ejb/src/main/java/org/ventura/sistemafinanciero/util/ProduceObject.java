package org.ventura.sistemafinanciero.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.ventura.sistemafinanciero.entity.CuentaAporte;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.type.TipoCuentaBancaria;

public class ProduceObject {

	public final static String CODIGO_CUENTA_APORTE = "10";
	public final static String CODIGO_CUENTA_AHORRO = "11";
	public final static String CODIGO_CUENTA_CORRIENTE = "12";
	public final static String CODIGO_CUENTA_PLAZO_FIJO = "13";
	
	final private  static String[] units = {"CERO","UNO","DOS","TRES","CUATRO",
		"CINCO","SEIS","SIETE","OCHO","NUEVE","DIEZ",
		"ONCE","DOCE","TRECE","CATORCE","QUINCE",
		"DIECISEIS","DIECISIETE","DIECIOCHO","DIECINUEVE"};
	final private static String[] tens = {"","","VEINTE","TREINTA","CUARENTA","CINCUENTA",
		"SESENTA","SETENTA","OCHENTA","NOVENTA"};
	
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
	
	public static String getCodigoAgenciaFromNumeroCuenta(String numeroCuenta){
		if(numeroCuenta == null)
			return null;
		return numeroCuenta.substring(0, 3);
	}
	
	public static Moneda getMonedaPrincipal(){
		Moneda moneda = new Moneda();
		moneda.setIdMoneda(BigInteger.ONE);
		moneda.setDenominacion("NUEVO SOL");
		moneda.setSimbolo("S/.");
		return moneda;
	}

	public static BigDecimal getInteresPlazoFijo(BigDecimal capital, BigDecimal tasaInteres, int periodo) {
		BigDecimal result = null;
		
		Double base = tasaInteres.add(BigDecimal.ONE).doubleValue();
		Double potencia = (new Double(periodo)/360);
		Double a = Math.pow(base, potencia) - 1;
		result = capital.multiply(new BigDecimal(a));
		result = result.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		return result;
	}
	
	public static String getTextOfNumber(int i){		
		if( i < 20)  return units[i];
		if( i < 100) return tens[i/10] + ((i % 10 > 0)? " " + getTextOfNumber(i % 10):"");
		if( i < 1000) return units[i/100] + " CIEN" + ((i % 100 > 0)?" Y " + getTextOfNumber(i % 100):"");
		if( i < 1000000) return getTextOfNumber(i / 1000) + " MIL " + ((i % 1000 > 0)? " " + getTextOfNumber(i % 1000):"") ;
		return getTextOfNumber(i / 1000000) + " MILLON " + ((i % 1000000 > 0)? " " + getTextOfNumber(i % 1000000):"") ;
	}
}
