package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.ventura.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoCuentaBancaria;

/**
 * CuentaBancaria generated by hbm2java
 */
@Entity
@Table(name = "CUENTA_BANCARIA", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "cuentaBancaria")
@XmlAccessorType(XmlAccessType.NONE)
public class CuentaBancaria implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger idCuentaBancaria;
	private Socio socio;
	private TipoCuentaBancaria tipoCuentaBancaria;
	private String numeroCuenta;
	private Moneda moneda;
	private Date fechaApertura;
	private Date fechaCierre;
	private BigDecimal saldo;
	private int cantidadRetirantes;
	private EstadoCuentaBancaria estado;
	private Set cuentaBancariaTasas = new HashSet(0);
	private Set titulars = new HashSet(0);
	private Set transferenciaBancariasForIdCuentaBancariaOrigen = new HashSet(0);
	private Set transferenciaBancariasForIdCuentaBancariaDestino = new HashSet(
			0);
	private Set cuentaBancariaInteresGeneras = new HashSet(0);
	private Set beneficiarios = new HashSet(0);
	private Set transaccionBancarias = new HashSet(0);

	public CuentaBancaria() {
	}

	public CuentaBancaria(BigInteger idCuentaBancaria, Socio socio,
			TipoCuentaBancaria tipoCuentaBancaria, String numeroCuenta,
			Date fechaApertura, BigDecimal saldo, int cantidadRetirantes,
			EstadoCuentaBancaria estado, String estadoCuenta) {
		this.idCuentaBancaria = idCuentaBancaria;
		this.socio = socio;
		this.tipoCuentaBancaria = tipoCuentaBancaria;
		this.numeroCuenta = numeroCuenta;
		this.fechaApertura = fechaApertura;
		this.saldo = saldo;
		this.cantidadRetirantes = cantidadRetirantes;
		this.estado = estado;
	}

	public CuentaBancaria(BigInteger idCuentaBancaria, Socio socio,
			TipoCuentaBancaria tipoCuentaBancaria, String numeroCuenta,
			Date fechaApertura, Date fechaCierre, BigDecimal saldo,
			int cantidadRetirantes, EstadoCuentaBancaria estado,
			String estadoCuenta, Set cuentaBancariaTasas, Set titulars,
			Set transferenciaBancariasForIdCuentaBancariaOrigen,
			Set transferenciaBancariasForIdCuentaBancariaDestino,
			Set cuentaBancariaInteresGeneras, Set beneficiarios,
			Set transaccionBancarias) {
		this.idCuentaBancaria = idCuentaBancaria;
		this.socio = socio;
		this.tipoCuentaBancaria = tipoCuentaBancaria;
		this.numeroCuenta = numeroCuenta;
		this.fechaApertura = fechaApertura;
		this.fechaCierre = fechaCierre;
		this.saldo = saldo;
		this.cantidadRetirantes = cantidadRetirantes;
		this.estado = estado;
		this.cuentaBancariaTasas = cuentaBancariaTasas;
		this.titulars = titulars;
		this.transferenciaBancariasForIdCuentaBancariaOrigen = transferenciaBancariasForIdCuentaBancariaOrigen;
		this.transferenciaBancariasForIdCuentaBancariaDestino = transferenciaBancariasForIdCuentaBancariaDestino;
		this.cuentaBancariaInteresGeneras = cuentaBancariaInteresGeneras;
		this.beneficiarios = beneficiarios;
		this.transaccionBancarias = transaccionBancarias;
	}

	@XmlElement(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "ID_CUENTA_BANCARIA", unique = true, nullable = false, precision = 22, scale = 0)
	public BigInteger getIdCuentaBancaria() {
		return this.idCuentaBancaria;
	}

	public void setIdCuentaBancaria(BigInteger idCuentaBancaria) {
		this.idCuentaBancaria = idCuentaBancaria;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SOCIO", nullable = false)
	public Socio getSocio() {
		return this.socio;
	}

	public void setSocio(Socio socio) {
		this.socio = socio;
	}

	@XmlElement
	@Enumerated(value = EnumType.STRING)
	@Column(name = "TIPO_CUENTA_BANCARIA", nullable = false, length = 10, columnDefinition = "nvarchar2")
	public TipoCuentaBancaria getTipoCuentaBancaria() {
		return this.tipoCuentaBancaria;
	}

	public void setTipoCuentaBancaria(TipoCuentaBancaria tipoCuentaBancaria) {
		this.tipoCuentaBancaria = tipoCuentaBancaria;
	}

	@XmlElement
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MONEDA", nullable = false)
	public Moneda getMoneda() {
		return this.moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	@XmlElement
	@Column(name = "NUMERO_CUENTA", nullable = false, length = 40, columnDefinition = "nvarchar2")
	public String getNumeroCuenta() {
		return this.numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	@XmlElement
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_APERTURA", nullable = false, length = 7)
	public Date getFechaApertura() {
		return this.fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	@XmlElement
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_CIERRE", length = 7)
	public Date getFechaCierre() {
		return this.fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	@XmlElement
	@Column(name = "SALDO", nullable = false, precision = 18)
	public BigDecimal getSaldo() {
		return this.saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	@XmlElement
	@Column(name = "CANTIDAD_RETIRANTES", nullable = false, precision = 22, scale = 0)
	public int getCantidadRetirantes() {
		return this.cantidadRetirantes;
	}

	public void setCantidadRetirantes(int cantidadRetirantes) {
		this.cantidadRetirantes = cantidadRetirantes;
	}

	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO", nullable = false, length = 12, columnDefinition = "nvarchar2")
	public EstadoCuentaBancaria getEstado() {
		return this.estado;
	}

	public void setEstado(EstadoCuentaBancaria estado) {
		this.estado = estado;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cuentaBancaria")
	public Set<CuentaBancariaTasa> getCuentaBancariaTasas() {
		return this.cuentaBancariaTasas;
	}

	public void setCuentaBancariaTasas(Set cuentaBancariaTasas) {
		this.cuentaBancariaTasas = cuentaBancariaTasas;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cuentaBancaria")
	public Set<Titular> getTitulars() {
		return this.titulars;
	}

	public void setTitulars(Set titulars) {
		this.titulars = titulars;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cuentaBancariaByIdCuentaBancariaOrigen")
	public Set<TransferenciaBancaria> getTransferenciaBancariasForIdCuentaBancariaOrigen() {
		return this.transferenciaBancariasForIdCuentaBancariaOrigen;
	}

	public void setTransferenciaBancariasForIdCuentaBancariaOrigen(
			Set transferenciaBancariasForIdCuentaBancariaOrigen) {
		this.transferenciaBancariasForIdCuentaBancariaOrigen = transferenciaBancariasForIdCuentaBancariaOrigen;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cuentaBancariaByIdCuentaBancariaDestino")
	public Set<TransferenciaBancaria> getTransferenciaBancariasForIdCuentaBancariaDestino() {
		return this.transferenciaBancariasForIdCuentaBancariaDestino;
	}

	public void setTransferenciaBancariasForIdCuentaBancariaDestino(
			Set transferenciaBancariasForIdCuentaBancariaDestino) {
		this.transferenciaBancariasForIdCuentaBancariaDestino = transferenciaBancariasForIdCuentaBancariaDestino;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cuentaBancaria")
	public Set<CuentaBancariaInteresGenera> getCuentaBancariaInteresGeneras() {
		return this.cuentaBancariaInteresGeneras;
	}

	public void setCuentaBancariaInteresGeneras(Set cuentaBancariaInteresGeneras) {
		this.cuentaBancariaInteresGeneras = cuentaBancariaInteresGeneras;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cuentaBancaria")
	public Set<Beneficiario> getBeneficiarios() {
		return this.beneficiarios;
	}

	public void setBeneficiarios(Set beneficiarios) {
		this.beneficiarios = beneficiarios;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cuentaBancaria")
	public Set<TransaccionBancaria> getTransaccionBancarias() {
		return this.transaccionBancarias;
	}

	public void setTransaccionBancarias(Set transaccionBancarias) {
		this.transaccionBancarias = transaccionBancarias;
	}

}
