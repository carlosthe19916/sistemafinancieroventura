package org.ventura.sistemafinanciero.entity.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.ventura.sistemafinanciero.entity.Moneda;

@XmlRootElement(name = "monedacalculadora")
@XmlAccessorType(XmlAccessType.NONE)
public class MonedaCalculadora implements java.io.Serializable{

	@XmlElement
	private Moneda moneda;

	@XmlElement
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
