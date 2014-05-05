package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
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

/**
 * Boveda generated by hbm2java
 */
@Entity
@Table(name = "BOVEDA", schema = "BDSISTEMAFINANCIERO")
public class Boveda implements java.io.Serializable {

	private BigDecimal idBoveda;
	private Moneda moneda;
	private Agencia agencia;
	private String denominacion;
	private BigDecimal estado;
	private int abierto;
	private BigDecimal congelado;
	private Set bovedaCajas = new HashSet(0);
	private Set historialBovedas = new HashSet(0);

	public Boveda() {
	}

	public Boveda(BigDecimal idBoveda, Moneda moneda, Agencia agencia,
			String denominacion, BigDecimal estado, boolean abierto,
			BigDecimal congelado) {
		this.idBoveda = idBoveda;
		this.moneda = moneda;
		this.agencia = agencia;
		this.denominacion = denominacion;
		this.estado = estado;
		this.abierto = (abierto ? 1 : 0);
		this.congelado = congelado;
	}

	public Boveda(BigDecimal idBoveda, Moneda moneda, Agencia agencia,
			String denominacion, BigDecimal estado, boolean abierto,
			BigDecimal congelado, Set bovedaCajas, Set historialBovedas) {
		this.idBoveda = idBoveda;
		this.moneda = moneda;
		this.agencia = agencia;
		this.denominacion = denominacion;
		this.estado = estado;
		this.abierto = (abierto ? 1 : 0);
		this.congelado = congelado;
		this.bovedaCajas = bovedaCajas;
		this.historialBovedas = historialBovedas;
	}

	@Id
	@Column(name = "ID_BOVEDA", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getIdBoveda() {
		return this.idBoveda;
	}

	public void setIdBoveda(BigDecimal idBoveda) {
		this.idBoveda = idBoveda;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MONEDA", nullable = false)
	public Moneda getMoneda() {
		return this.moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_AGENCIA", nullable = false)
	public Agencia getAgencia() {
		return this.agencia;
	}

	public void setAgencia(Agencia agencia) {
		this.agencia = agencia;
	}

	@Column(name = "DENOMINACION", nullable = false, length = 100, columnDefinition = "nvarchar2")
	public String getDenominacion() {
		return this.denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	@Column(name = "ESTADO", nullable = false, precision = 22, scale = 0)
	public BigDecimal getEstado() {
		return this.estado;
	}

	public void setEstado(BigDecimal estado) {
		this.estado = estado;
	}

	@Column(name = "ABIERTO", nullable = false, precision = 22, scale = 0)
	public boolean getAbierto() {
		return (this.abierto == 1 ? true : false);
	}

	public void setAbierto(boolean abierto) {
		this.abierto = (abierto ? 1 : 0);;
	}

	@Column(name = "CONGELADO", nullable = false, precision = 22, scale = 0)
	public BigDecimal getCongelado() {
		return this.congelado;
	}

	public void setCongelado(BigDecimal congelado) {
		this.congelado = congelado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "boveda")
	public Set<BovedaCaja> getBovedaCajas() {
		return this.bovedaCajas;
	}

	public void setBovedaCajas(Set bovedaCajas) {
		this.bovedaCajas = bovedaCajas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "boveda")
	public Set<HistorialBoveda> getHistorialBovedas() {
		return this.historialBovedas;
	}

	public void setHistorialBovedas(Set historialBovedas) {
		this.historialBovedas = historialBovedas;
	}

}
