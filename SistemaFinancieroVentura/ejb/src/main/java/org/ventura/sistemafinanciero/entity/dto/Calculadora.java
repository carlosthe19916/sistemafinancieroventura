package org.ventura.sistemafinanciero.entity.dto;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "calculadora")
@XmlAccessorType(XmlAccessType.NONE)
public class Calculadora implements java.io.Serializable {

	@XmlElement
	private int iddenominacion;

	@XmlElement
	private String denominacion;

	@XmlElement
	private BigDecimal valor;

	@XmlElement
	private BigDecimal cantidad;

	public Calculadora() {
		// TODO Auto-generated constructor stub
	}

	public Calculadora(int iddenominacion, String denominacion,
			BigDecimal valor, BigDecimal cantidad) {
		this.iddenominacion = iddenominacion;
		this.denominacion = denominacion;
		this.valor = valor;
		this.cantidad = cantidad;
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

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

}
