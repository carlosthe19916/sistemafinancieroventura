package org.ventura.sistemafinanciero.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.ventura.sistemafinanciero.entity.type.EstadoCuentaAporte;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;

/**
 * The persistent class for the SOCIO_VIEW database table.
 * 
 */
@Entity
@Table(name = "SOCIO_VIEW", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "socioview")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({
		@NamedQuery(name = SocioView.findAll, query = "SELECT sv FROM SocioView sv ORDER BY sv.idsocio ASC"),
		@NamedQuery(name = SocioView.FindByFilterTextSocioView, query = "SELECT sv FROM SocioView sv WHERE sv.socio LIKE :filtertext or sv.numerodocumento like :filtertext"),
		@NamedQuery(name = SocioView.FindByFilterTextSocioViewAllHaveCuentaAporte, query = "SELECT sv FROM SocioView sv WHERE sv.cuentaAporte IS NOT NULL AND (sv.socio LIKE :filtertext or sv.numerodocumento like :filtertext) ORDER BY sv.idsocio ASC"),
		@NamedQuery(name = SocioView.FindAllHaveCuentaAporte, query = "SELECT sv FROM SocioView sv WHERE sv.cuentaAporte IS NOT NULL ORDER BY sv.idsocio ASC") })
public class SocioView implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static String findAll = "SocioView.findAll";
	public final static String FindByFilterTextSocioView = "SocioView.FindByFilterTextSocioView";
	public final static String FindByFilterTextSocioViewAllHaveCuentaAporte = "SocioView.FindByFilterTextSocioViewAllHaveCuentaAporte";
	public final static String FindAllHaveCuentaAporte = "SocioView.FindAllHaveCuentaAporte";

	@XmlElement(name = "id")
	@Id
	@Column(name = "ID_SOCIO", unique = true, nullable = false, precision = 22, scale = 0)
	private BigInteger idsocio;

	@XmlElement(name = "estado")
	@Column(name = "ESTADO_SOCIO", nullable = false)
	private int estado;

	@XmlElement(name = "cuentaAporte")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CUENTA_APORTE", nullable = true)
	private CuentaAporte cuentaAporte;

	@XmlElement(name = "numeroCuentaAporte")
	@Column(name = "NUMERO_CUENTA_APORTE", nullable = false, length = 14, columnDefinition = "nvarchar2")
	private String numeroCuentaAporte;

	@XmlElement(name = "estadoCuentaAporte")
	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO_CUENTA_APORTE", nullable = false, length = 20, columnDefinition = "nvarchar2")
	private EstadoCuentaAporte estadoCuentaAporte;

	@XmlElement(name = "tipoDocumento")
	@Column(name = "TIPO_DOCUMENTO", nullable = false, length = 20, columnDefinition = "nvarchar2")
	private String tipodocumento;

	@XmlElement(name = "tipoPersona")
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_PERSONA", nullable = false, length = 20, columnDefinition = "nvarchar2")
	private TipoPersona tipopersona;

	@XmlElement(name = "numeroDocumento")
	@Column(name = "NUMERO_DOCUMENTO", nullable = false, length = 20, columnDefinition = "nvarchar2")
	private String numerodocumento;

	@XmlElement
	@Column(name = "SOCIO", nullable = false, length = 192, columnDefinition = "nvarchar2")
	private String socio;

	@XmlElement
	@Temporal(TemporalType.DATE)
	@Column(name = "\"FEC. NAC. / FEC. CONST.\"")
	private Date fecNacfecConst;

	@XmlElement(name = "fechaAsociado")
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_ASOCIADO")
	private Date fechaasociado;

	@XmlElement(name = "direccion")
	@Column(name = "DIRECCION", nullable = false, length = 70, columnDefinition = "nvarchar2")
	private String direccion;

	@XmlElement(name = "email")
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

	public BigInteger getIdsocio() {
		return this.idsocio;
	}

	public void setIdsocio(BigInteger idsocio) {
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

	public TipoPersona getTipopersona() {
		return tipopersona;
	}

	public void setTipopersona(TipoPersona tipopersona) {
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

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public CuentaAporte getCuentaAporte() {
		return cuentaAporte;
	}

	public void setCuentaAporte(CuentaAporte cuentaAporte) {
		this.cuentaAporte = cuentaAporte;
	}

	public String getNumeroCuentaAporte() {
		return numeroCuentaAporte;
	}

	public void setNumeroCuentaAporte(String numeroCuentaAporte) {
		this.numeroCuentaAporte = numeroCuentaAporte;
	}

	public EstadoCuentaAporte getEstadoCuentaAporte() {
		return estadoCuentaAporte;
	}

	public void setEstadoCuentaAporte(EstadoCuentaAporte estadoCuentaAporte) {
		this.estadoCuentaAporte = estadoCuentaAporte;
	}

	public Date getFecNacfecConst() {
		return fecNacfecConst;
	}

	public void setFecNacfecConst(Date fecNacfecConst) {
		this.fecNacfecConst = fecNacfecConst;
	}

}