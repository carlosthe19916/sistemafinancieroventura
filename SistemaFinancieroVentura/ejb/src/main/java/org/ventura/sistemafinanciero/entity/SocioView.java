package org.ventura.sistemafinanciero.entity;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the SOCIO_VIEW database table.
 * 
 */
@Entity
@Table(name = "SOCIO_VIEW", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "socioview")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({
		@NamedQuery(name = SocioView.FindByFilterTextSocioView, query = "SELECT sv FROM SocioView sv WHERE sv.socio LIKE :filtertext or sv.numerodocumento like :filtertext"),
		@NamedQuery(name = SocioView.findAll, query = "SELECT sv FROM SocioView sv ORDER BY sv.idsocio ASC") })
public class SocioView implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static String FindByFilterTextSocioView = "SocioView.FindByFilterTextSocioView";
	public final static String findAll = "SocioView.findAll";

	@XmlElement(name = "id")
	@Id
	@Column(name = "IDSOCIO", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal idsocio;

	@XmlElement
	@Column(name = "SOCIO", nullable = false, length = 192, columnDefinition = "nvarchar2")
	private String socio;

	@XmlElement
	@Temporal(TemporalType.DATE)
	@Column(name = "\"FECHA ASOCIADO\"")
	private Date fechaasociado;

	@XmlElement
	@Column(name = "\"TIPO DOCUMENTO\"", nullable = false, length = 20, columnDefinition = "nvarchar2")
	private String tipodocumento;

	@XmlElement
	@Column(name = "\"NUMERO DOCUMENTO\"", nullable = false, length = 20, columnDefinition = "nvarchar2")
	private String numerodocumento;

	@XmlElement
	@Temporal(TemporalType.DATE)
	@Column(name = "\"FEC. NAC. / FEC. CONST.\"")
	private Date fecNacfecConst;

	@XmlElement
	@Column(name = "\"TIPO PERSONA\"", nullable = false, length = 20, columnDefinition = "nvarchar2")
	private String tipopersona;

	@XmlElement
	@Column(name = "DIRECCION", nullable = false, length = 70, columnDefinition = "nvarchar2")
	private String direccion;

	@XmlElement
	@Column(name = "EMAIL", nullable = false, length = 70, columnDefinition = "nvarchar2")
	private String email;

	public SocioView() {
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getIdsocio() {
		return this.idsocio;
	}

	public void setIdsocio(BigDecimal idsocio) {
		this.idsocio = idsocio;
	}

	public String getSocio() {
		return this.socio;
	}

	public void setSocio(String socio) {
		this.socio = socio;
	}

	public String getTipodocumento() {
		return tipodocumento;
	}

	public void setTipodocumento(String tipodocumento) {
		this.tipodocumento = tipodocumento;
	}

	public String getNumerodocumento() {
		return numerodocumento;
	}

	public void setNumerodocumento(String numerodocumento) {
		this.numerodocumento = numerodocumento;
	}

	public String getTipopersona() {
		return tipopersona;
	}

	public void setTipopersona(String tipopersona) {
		this.tipopersona = tipopersona;
	}

	public Date getFecNacFecConst() {
		return fecNacfecConst;
	}

	public void setFecNacFecConst(Date fecNacFecConst) {
		fecNacfecConst = fecNacFecConst;
	}

	public Date getFechaasociado() {
		return fechaasociado;
	}

	public void setFechaasociado(Date fechaasociado) {
		this.fechaasociado = fechaasociado;
	}

}