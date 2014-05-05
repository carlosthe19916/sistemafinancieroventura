package org.ventura.sistemafinanciero.dto;

import java.util.List;

import org.ventura.sistemafinanciero.entity.Moneda;

public class MonedaCalculadora {

	private Moneda moneda;
	private List<Calculadora> calculadora;
	
	public Moneda getMoneda() {
		return moneda;
	}
	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}
	public List<Calculadora> getCalculadora() {
		return calculadora;
	}
	public void setCalculadora(List<Calculadora> calculadora) {
		this.calculadora = calculadora;
	}

	

}
