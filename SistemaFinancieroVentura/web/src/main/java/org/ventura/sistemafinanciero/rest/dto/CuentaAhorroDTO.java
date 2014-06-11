package org.ventura.sistemafinanciero.rest.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.ventura.sistemafinanciero.entity.Beneficiario;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;

@XmlRootElement(name = "crearCuentaAhorro")
@XmlAccessorType(XmlAccessType.NONE)
public class CuentaAhorroDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger idMoneda;
	private TipoPersona tipoPersona;
	private BigInteger idPersona;
	private int cantRetirantes;
	private BigDecimal tasaInteres;
	private List<BigInteger> titulares;
	private List<Beneficiario> beneficiarios;

	@XmlElement
	public BigInteger getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(BigInteger idMoneda) {
		this.idMoneda = idMoneda;
	}

	@XmlElement
	public TipoPersona getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(TipoPersona tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	@XmlElement
	public BigInteger getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(BigInteger idPersona) {
		this.idPersona = idPersona;
	}

	@XmlElement
	public int getCantRetirantes() {
		return cantRetirantes;
	}

	public void setCantRetirantes(int cantRetirantes) {
		this.cantRetirantes = cantRetirantes;
	}

	@XmlElement
	public List<BigInteger> getTitulares() {
		return titulares;
	}

	public void setTitulares(List<BigInteger> titulares) {
		this.titulares = titulares;
	}

	@XmlElement
	public List<Beneficiario> getBeneficiarios() {
		return beneficiarios;
	}

	public void setBeneficiarios(List<Beneficiario> beneficiarios) {
		this.beneficiarios = beneficiarios;
	}

	@XmlElement
	public BigDecimal getTasaInteres() {
		return tasaInteres;
	}

	public void setTasaInteres(BigDecimal tasaInteres) {
		this.tasaInteres = tasaInteres;
	}

}
