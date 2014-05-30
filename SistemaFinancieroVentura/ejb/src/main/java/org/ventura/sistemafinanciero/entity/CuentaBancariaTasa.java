package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * CuentaBancariaTasa generated by hbm2java
 */
@Entity
@Table(name = "CUENTA_BANCARIA_TASA", schema = "BDSISTEMAFINANCIERO")
public class CuentaBancariaTasa implements java.io.Serializable {

	private BigDecimal idCuentaBancariaTasa;
	private CuentaBancaria cuentaBancaria;
	private BigDecimal valor;

	public CuentaBancariaTasa() {
	}

	public CuentaBancariaTasa(BigDecimal idCuentaBancariaTasa) {
		this.idCuentaBancariaTasa = idCuentaBancariaTasa;
	}

	public CuentaBancariaTasa(BigDecimal idCuentaBancariaTasa,
			CuentaBancaria cuentaBancaria, BigDecimal valor) {
		this.idCuentaBancariaTasa = idCuentaBancariaTasa;
		this.cuentaBancaria = cuentaBancaria;
		this.valor = valor;
	}

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "ID_CUENTA_BANCARIA_TASA", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getIdCuentaBancariaTasa() {
		return this.idCuentaBancariaTasa;
	}

	public void setIdCuentaBancariaTasa(BigDecimal idCuentaBancariaTasa) {
		this.idCuentaBancariaTasa = idCuentaBancariaTasa;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CUENTA_BANCARIA")
	public CuentaBancaria getCuentaBancaria() {
		return this.cuentaBancaria;
	}

	public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}

	@Column(name = "VALOR", precision = 5, scale = 4)
	public BigDecimal getValor() {
		return this.valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
