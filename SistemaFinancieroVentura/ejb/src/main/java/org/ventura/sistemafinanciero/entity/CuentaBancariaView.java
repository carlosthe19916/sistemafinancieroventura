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
 * The persistent class for the CUENTA_BANCARIA_VIEW database table.
 * 
 */
@Entity
@Table(name="CUENTA_BANCARIA_VIEW", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "cuentabancariaview")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({ @NamedQuery(name = CuentaBancariaView.FindByFilterTextCuentaBancariaView, query = "SELECT cbv FROM CuentaBancariaView cbv WHERE cbv.numerocuenta LIKE :filtertext OR cbv.numerodocumento LIKE :filtertext OR cbv.socio LIKE :filtertext") })
public class CuentaBancariaView implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static String FindByFilterTextCuentaBancariaView = "FindByFilterTextCuentaBancariaView";
	
	private String numerocuenta;
	private BigDecimal idcuentabancaria;
	private String estadocuenta;
	private Date fecnac_fecconst;
	private Moneda moneda;
	private BigDecimal idsocio;
	private String numerodocumento;
	private String socio;
	private String tipocuenta;
	private String tipodocumento;
	private String tipopersona;

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
	
	@XmlElement
	@Column(name = "NUMEROCUENTA", nullable = false, length = 40, columnDefinition = "nvarchar2")
	public String getNumerocuenta() {
		return this.numerocuenta;
	}

	public void setNumerocuenta(String numerocuenta) {
		this.numerocuenta = numerocuenta;
	}
	
	@XmlElement
	@Column(name = "ESTADOCUENTA", nullable = false, columnDefinition = "nvarchar2")
	public String getEstadocuenta() {
		return this.estadocuenta;
	}

	public void setEstadocuenta(String estadocuenta) {
		this.estadocuenta = estadocuenta;
	}

	@XmlElement
	@Temporal(TemporalType.DATE)
	@Column(name = "FECNAC_FECCONST", nullable = false, length = 7)
	public Date getFecnac_fecconst() {
		return this.fecnac_fecconst;
	}

	public void setFecnac_fecconst(Date fecnac_fecconst) {
		this.fecnac_fecconst = fecnac_fecconst;
	}
	
	@XmlElement
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDMONEDA", nullable = false)
	public Moneda getMoneda() {
		return this.moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	@Column(name = "IDSOCIO", precision = 22, scale = 0)
	public BigDecimal getIdsocio() {
		return this.idsocio;
	}

	public void setIdsocio(BigDecimal idsocio) {
		this.idsocio = idsocio;
	}

	@XmlElement
	@Column(name = "NUMERODOCUMENTO", nullable = false, length = 40, columnDefinition = "nvarchar2")
	public String getNumerodocumento() {
		return this.numerodocumento;
	}

	public void setNumerodocumento(String numerodocumento) {
		this.numerodocumento = numerodocumento;
	}

	@XmlElement
	@Column(name = "SOCIO", nullable = false, columnDefinition = "nvarchar2")
	public String getSocio() {
		return this.socio;
	}

	public void setSocio(String socio) {
		this.socio = socio;
	}

	@XmlElement
	@Column(name = "TIPOCUENTA", nullable = false, columnDefinition = "nvarchar2")
	public String getTipocuenta() {
		return this.tipocuenta;
	}

	public void setTipocuenta(String tipocuenta) {
		this.tipocuenta = tipocuenta;
	}

	@XmlElement
	@Column(name = "TIPODOCUMENTO", nullable = false, length = 20, columnDefinition = "nvarchar2")
	public String getTipoDocumento() {
		return this.tipodocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipodocumento = tipoDocumento;
	}

	@XmlElement
	@Column(name = "TIPOPERSONA", columnDefinition = "nvarchar2")
	public String getTipopersona() {
		return this.tipopersona;
	}

	public void setTipopersona(String tipopersona) {
		this.tipopersona = tipopersona;
	}

}