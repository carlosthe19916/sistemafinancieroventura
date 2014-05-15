package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.math.BigInteger;

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
 * TransaccionBovedaCajaDetall generated by hbm2java
 */
@Entity
@Table(name = "TRANSACCION_BOVEDA_CAJA_DETALL", schema = "BDSISTEMAFINANCIERO")
public class TransaccionBovedaCajaDetall implements java.io.Serializable {

	private BigDecimal idTransaccionBovedaCajaDet;
	private MonedaDenominacion monedaDenominacion;
	private TransaccionBovedaCaja transaccionBovedaCaja;
	private BigInteger cantidad;

	public TransaccionBovedaCajaDetall() {
	}

	public TransaccionBovedaCajaDetall(BigDecimal idTransaccionBovedaCajaDet,
			MonedaDenominacion monedaDenominacion,
			TransaccionBovedaCaja transaccionBovedaCaja, BigInteger cantidad) {
		this.idTransaccionBovedaCajaDet = idTransaccionBovedaCajaDet;
		this.monedaDenominacion = monedaDenominacion;
		this.transaccionBovedaCaja = transaccionBovedaCaja;
		this.cantidad = cantidad;
	}

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "ID_TRANSACCION_BOVEDA_CAJA_DET", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getIdTransaccionBovedaCajaDet() {
		return this.idTransaccionBovedaCajaDet;
	}

	public void setIdTransaccionBovedaCajaDet(
			BigDecimal idTransaccionBovedaCajaDet) {
		this.idTransaccionBovedaCajaDet = idTransaccionBovedaCajaDet;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MONEDA_DENOMINACION", nullable = false)
	public MonedaDenominacion getMonedaDenominacion() {
		return this.monedaDenominacion;
	}

	public void setMonedaDenominacion(MonedaDenominacion monedaDenominacion) {
		this.monedaDenominacion = monedaDenominacion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TRANSACCION_BOVEDA_CAJA", nullable = false)
	public TransaccionBovedaCaja getTransaccionBovedaCaja() {
		return this.transaccionBovedaCaja;
	}

	public void setTransaccionBovedaCaja(
			TransaccionBovedaCaja transaccionBovedaCaja) {
		this.transaccionBovedaCaja = transaccionBovedaCaja;
	}

	@Column(name = "CANTIDAD", nullable = false, precision = 22, scale = 0)
	public BigInteger getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(BigInteger cantidad) {
		this.cantidad = cantidad;
	}

}
