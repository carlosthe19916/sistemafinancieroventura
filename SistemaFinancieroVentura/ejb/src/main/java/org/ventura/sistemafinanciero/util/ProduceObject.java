package org.ventura.sistemafinanciero.util;

import java.math.BigInteger;

import org.ventura.sistemafinanciero.entity.Moneda;

public class ProduceObject {

	public static Moneda getMonedaPrincipal(){
		Moneda moneda = new Moneda();
		moneda.setIdMoneda(BigInteger.ONE);
		moneda.setDenominacion("NUEVO SOL");
		moneda.setSimbolo("S/.");
		return moneda;
	}
}
