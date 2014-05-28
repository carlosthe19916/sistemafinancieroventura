package org.ventura.sistemafinanciero.rest.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.ventura.sistemafinanciero.entity.Beneficiario;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;

@XmlRootElement(name = "crearCuentaAhorro")
@XmlAccessorType(XmlAccessType.NONE)
public class CrearCuentaAhorro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigInteger idMoneda;
	private TipoPersona tipoPersona;
	private BigInteger idPersona;
	private int cantRetirantes;
	private List<BigInteger> titulares;
	private List<Beneficiario> beneficiarios;

	public BigInteger getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(BigInteger idMoneda) {
		this.idMoneda = idMoneda;
	}

	public TipoPersona getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(TipoPersona tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public BigInteger getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(BigInteger idPersona) {
		this.idPersona = idPersona;
	}

	public int getCantRetirantes() {
		return cantRetirantes;
	}

	public void setCantRetirantes(int cantRetirantes) {
		this.cantRetirantes = cantRetirantes;
	}

	public List<BigInteger> getTitulares() {
		return titulares;
	}

	public void setTitulares(List<BigInteger> titulares) {
		this.titulares = titulares;
	}

	public List<Beneficiario> getBeneficiarios() {
		return beneficiarios;
	}

	public void setBeneficiarios(List<Beneficiario> beneficiarios) {
		this.beneficiarios = beneficiarios;
	}

}
