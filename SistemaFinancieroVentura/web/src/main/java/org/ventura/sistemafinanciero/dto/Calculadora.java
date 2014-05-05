package org.ventura.sistemafinanciero.dto;

import java.math.BigDecimal;

public class Calculadora {

	private int iddenominacion;
	private String denominacion;
	private BigDecimal valor;
	private int cantidad;
	
	public Calculadora() {
		// TODO Auto-generated constructor stub
	}

	public int getIddenominacion() {
		return iddenominacion;
	}

	public void setIddenominacion(int iddenominacion) {
		this.iddenominacion = iddenominacion;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

}
