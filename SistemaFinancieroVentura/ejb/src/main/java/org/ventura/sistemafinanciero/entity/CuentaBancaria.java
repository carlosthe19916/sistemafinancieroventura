package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CuentaBancaria generated by hbm2java
 */
@Entity
@Table(name = "CUENTA_BANCARIA", schema = "BDSISTEMAFINANCIERO")
public class CuentaBancaria implements java.io.Serializable {

	private BigDecimal idCuentaBancaria;
	private Socio socio;
	private TipoCuentaBancaria tipoCuentaBancaria;
	private String numeroCuenta;
	private Date fechaApertura;
	private Date fechaCierre;
	private BigDecimal saldo;
	private BigDecimal cantidadRetirantes;
	private BigDecimal estado;
	private String estadoCuenta;
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

	public CuentaBancaria(BigDecimal idCuentaBancaria, Socio socio,
			TipoCuentaBancaria tipoCuentaBancaria, String numeroCuenta,
			Date fechaApertura, BigDecimal saldo,
			BigDecimal cantidadRetirantes, BigDecimal estado,
			String estadoCuenta) {
		this.idCuentaBancaria = idCuentaBancaria;
		this.socio = socio;
		this.tipoCuentaBancaria = tipoCuentaBancaria;
		this.numeroCuenta = numeroCuenta;
		this.fechaApertura = fechaApertura;
		this.saldo = saldo;
		this.cantidadRetirantes = cantidadRetirantes;
		this.estado = estado;
		this.estadoCuenta = estadoCuenta;
	}

	public CuentaBancaria(BigDecimal idCuentaBancaria, Socio socio,
			TipoCuentaBancaria tipoCuentaBancaria, String numeroCuenta,
			Date fechaApertura, Date fechaCierre, BigDecimal saldo,
			BigDecimal cantidadRetirantes, BigDecimal estado,
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
		this.estadoCuenta = estadoCuenta;
		this.cuentaBancariaTasas = cuentaBancariaTasas;
		this.titulars = titulars;
		this.transferenciaBancariasForIdCuentaBancariaOrigen = transferenciaBancariasForIdCuentaBancariaOrigen;
		this.transferenciaBancariasForIdCuentaBancariaDestino = transferenciaBancariasForIdCuentaBancariaDestino;
		this.cuentaBancariaInteresGeneras = cuentaBancariaInteresGeneras;
		this.beneficiarios = beneficiarios;
		this.transaccionBancarias = transaccionBancarias;
	}

	@Id
	@Column(name = "ID_CUENTA_BANCARIA", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getIdCuentaBancaria() {
		return this.idCuentaBancaria;
	}

	public void setIdCuentaBancaria(BigDecimal idCuentaBancaria) {
		this.idCuentaBancaria = idCuentaBancaria;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SOCIO", nullable = false)
	public Socio getSocio() {
		return this.socio;
	}

	public void setSocio(Socio socio) {
		this.socio = socio;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TIPO_CUENTA_BANCARIA", nullable = false)
	public TipoCuentaBancaria getTipoCuentaBancaria() {
		return this.tipoCuentaBancaria;
	}

	public void setTipoCuentaBancaria(TipoCuentaBancaria tipoCuentaBancaria) {
		this.tipoCuentaBancaria = tipoCuentaBancaria;
	}

	@Column(name = "NUMERO_CUENTA", nullable = false, length = 40,columnDefinition = "nvarchar2")
	public String getNumeroCuenta() {
		return this.numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_APERTURA", nullable = false, length = 7)
	public Date getFechaApertura() {
		return this.fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_CIERRE", length = 7)
	public Date getFechaCierre() {
		return this.fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	@Column(name = "SALDO", nullable = false, precision = 18)
	public BigDecimal getSaldo() {
		return this.saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	@Column(name = "CANTIDAD_RETIRANTES", nullable = false, precision = 22, scale = 0)
	public BigDecimal getCantidadRetirantes() {
		return this.cantidadRetirantes;
	}

	public void setCantidadRetirantes(BigDecimal cantidadRetirantes) {
		this.cantidadRetirantes = cantidadRetirantes;
	}

	@Column(name = "ESTADO", nullable = false, precision = 22, scale = 0)
	public BigDecimal getEstado() {
		return this.estado;
	}

	public void setEstado(BigDecimal estado) {
		this.estado = estado;
	}

	@Column(name = "ESTADO_CUENTA", nullable = false, length = 30,columnDefinition = "nvarchar2")
	public String getEstadoCuenta() {
		return this.estadoCuenta;
	}

	public void setEstadoCuenta(String estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cuentaBancaria")
	public Set<CuentaBancariaTasa> getCuentaBancariaTasas() {
		return this.cuentaBancariaTasas;
	}

	public void setCuentaBancariaTasas(Set cuentaBancariaTasas) {
		this.cuentaBancariaTasas = cuentaBancariaTasas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cuentaBancaria")
	public Set<Titular> getTitulars() {
		return this.titulars;
	}

	public void setTitulars(Set titulars) {
		this.titulars = titulars;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cuentaBancariaByIdCuentaBancariaOrigen")
	public Set<TransferenciaBancaria> getTransferenciaBancariasForIdCuentaBancariaOrigen() {
		return this.transferenciaBancariasForIdCuentaBancariaOrigen;
	}

	public void setTransferenciaBancariasForIdCuentaBancariaOrigen(
			Set transferenciaBancariasForIdCuentaBancariaOrigen) {
		this.transferenciaBancariasForIdCuentaBancariaOrigen = transferenciaBancariasForIdCuentaBancariaOrigen;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cuentaBancariaByIdCuentaBancariaDestino")
	public Set<TransferenciaBancaria> getTransferenciaBancariasForIdCuentaBancariaDestino() {
		return this.transferenciaBancariasForIdCuentaBancariaDestino;
	}

	public void setTransferenciaBancariasForIdCuentaBancariaDestino(
			Set transferenciaBancariasForIdCuentaBancariaDestino) {
		this.transferenciaBancariasForIdCuentaBancariaDestino = transferenciaBancariasForIdCuentaBancariaDestino;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cuentaBancaria")
	public Set<CuentaBancariaInteresGenera> getCuentaBancariaInteresGeneras() {
		return this.cuentaBancariaInteresGeneras;
	}

	public void setCuentaBancariaInteresGeneras(Set cuentaBancariaInteresGeneras) {
		this.cuentaBancariaInteresGeneras = cuentaBancariaInteresGeneras;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cuentaBancaria")
	public Set<Beneficiario> getBeneficiarios() {
		return this.beneficiarios;
	}

	public void setBeneficiarios(Set beneficiarios) {
		this.beneficiarios = beneficiarios;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cuentaBancaria")
	public Set<TransaccionBancaria> getTransaccionBancarias() {
		return this.transaccionBancarias;
	}

	public void setTransaccionBancarias(Set transaccionBancarias) {
		this.transaccionBancarias = transaccionBancarias;
	}

}
