package org.ventura.sistemafinanciero.entity;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.ventura.sistemafinanciero.dao.QueryParameter;
import org.ventura.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;

import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the CUENTA_BANCARIA_VIEW database table.
 * 
 */
@Entity
@Table(name = "CUENTA_BANCARIA_VIEW", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "cuentabancariaview")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({
		//@NamedQuery(name = CuentaBancariaView.FindByLists, query = "SELECT cbv FROM CuentaBancariaView cbv WHERE cbv.tipocuenta IN :tipoCuenta AND cbv.tipopersona IN :tipoPersona AND cbv.estadocuenta IN :tipoEstadoCuenta ORDER BY cbv.socio, cbv.idcuentabancaria"),	
		@NamedQuery(name = CuentaBancariaView.FindByFilterTextCuentaBancariaView, query = "SELECT cbv FROM CuentaBancariaView cbv WHERE cbv.tipocuenta IN :tipoCuenta AND cbv.tipopersona IN :tipoPersona AND cbv.estadocuenta IN :tipoEstadoCuenta AND (cbv.numerocuenta LIKE :filtertext OR cbv.numerodocumento LIKE :filtertext OR UPPER(cbv.socio) LIKE :filtertext)") })
public class CuentaBancariaView implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static String FindByFilterTextCuentaBancariaView = "CuentaBancariaView.FindByFilterTextCuentaBancariaView";
	//public final static String FindByLists = "CuentaBancariaView.FindByLists";

	private String numerocuenta;
	private BigDecimal idcuentabancaria;
	private EstadoCuentaBancaria estadocuenta;
	private Date fecnac_fecconst;
	private Moneda moneda;
	private BigDecimal idsocio;
	private String numerodocumento;
	private String socio;
	private TipoCuentaBancaria tipocuenta;
	private String tipodocumento;
	private TipoPersona tipopersona;

	public CuentaBancariaView() {
	}

	@XmlElement(name = "id")
	@Id
	@Column(name = "IDCUENTABANCARIA", unique = true, nullable = false)
	public BigDecimal getIdcuentabancaria() {
		return this.idcuentabancaria;
	}

	public void setIdcuentabancaria(BigDecimal idcuentabancaria) {
		this.idcuentabancaria = idcuentabancaria;
	}

	@XmlElement(name = "numeroCuenta")
	@Column(name = "NUMEROCUENTA", nullable = false, length = 40, columnDefinition = "nvarchar2")
	public String getNumerocuenta() {
		return this.numerocuenta;
	}

	public void setNumerocuenta(String numerocuenta) {
		this.numerocuenta = numerocuenta;
	}

	@XmlElement(name = "estadoCuenta")
	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADOCUENTA", nullable = false, columnDefinition = "nvarchar2")
	public EstadoCuentaBancaria getEstadocuenta() {
		return this.estadocuenta;
	}

	public void setEstadocuenta(EstadoCuentaBancaria estadocuenta) {
		this.estadocuenta = estadocuenta;
	}

	@XmlElement(name = "fecha")
	@Temporal(TemporalType.DATE)
	@Column(name = "FECNAC_FECCONST", nullable = false, length = 7)
	public Date getFecnac_fecconst() {
		return this.fecnac_fecconst;
	}

	public void setFecnac_fecconst(Date fecnac_fecconst) {
		this.fecnac_fecconst = fecnac_fecconst;
	}

	@XmlElement(name = "moneda")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDMONEDA", nullable = false)
	public Moneda getMoneda() {
		return this.moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	@XmlTransient
	@Column(name = "IDSOCIO", precision = 22, scale = 0)
	public BigDecimal getIdsocio() {
		return this.idsocio;
	}

	public void setIdsocio(BigDecimal idsocio) {
		this.idsocio = idsocio;
	}

	@XmlElement(name = "numeroDocumento")
	@Column(name = "NUMERODOCUMENTO", nullable = false, length = 40, columnDefinition = "nvarchar2")
	public String getNumerodocumento() {
		return this.numerodocumento;
	}

	public void setNumerodocumento(String numerodocumento) {
		this.numerodocumento = numerodocumento;
	}

	@XmlElement(name = "socio")
	@Column(name = "SOCIO", nullable = false, columnDefinition = "nvarchar2")
	public String getSocio() {
		return this.socio;
	}

	public void setSocio(String socio) {
		this.socio = socio;
	}

	@XmlElement(name = "tipoCuenta")
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPOCUENTA", nullable = false, columnDefinition = "nvarchar2")
	public TipoCuentaBancaria getTipocuenta() {
		return this.tipocuenta;
	}

	public void setTipocuenta(TipoCuentaBancaria tipocuenta) {
		this.tipocuenta = tipocuenta;
	}

	@XmlElement(name = "tipoDocumento")
	@Column(name = "TIPODOCUMENTO", nullable = false, length = 20, columnDefinition = "nvarchar2")
	public String getTipoDocumento() {
		return this.tipodocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipodocumento = tipoDocumento;
	}

	@XmlElement(name = "tipoPersona")
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPOPERSONA", columnDefinition = "nvarchar2")
	public TipoPersona getTipopersona() {
		return this.tipopersona;
	}

	public void setTipopersona(TipoPersona tipopersona) {
		this.tipopersona = tipopersona;
	}

}