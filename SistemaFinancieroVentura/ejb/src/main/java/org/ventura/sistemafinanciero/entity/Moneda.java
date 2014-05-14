package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Moneda generated by hbm2java
 */
@Entity
@Table(name = "MONEDA", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "moneda")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({
		@NamedQuery(name = Moneda.allActive, query = "SELECT m FROM Moneda m WHERE m.estado = 1"),
		@NamedQuery(name = Moneda.findByDenominacion, query = "SELECT m FROM Moneda m WHERE LOWER(m.denominacion) = LOWER(:denominacion)"),
		@NamedQuery(name = Moneda.findBySimbolo, query = "SELECT m FROM Moneda m WHERE LOWER(m.simbolo) = LOWER(:simbolo)") })
public class Moneda implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String allActive = "Moneda.allActive";
	public final static String findByDenominacion = "Moneda.findByDenominacion";
	public final static String findBySimbolo = "Moneda.findBySimbolo";

	@XmlTransient
	@Id
	@Column(name = "ID_MONEDA", unique = true, nullable = false, precision = 22, scale = 0)
	private int idMoneda;

	@XmlElement
	@Column(name = "DENOMINACION", nullable = false, length = 40, columnDefinition = "nvarchar2")
	private String denominacion;

	@XmlElement
	@Column(name = "SIMBOLO", nullable = false, length = 10, columnDefinition = "nvarchar2")
	private String simbolo;

	@XmlTransient
	@Column(name = "ESTADO", nullable = false, precision = 22, scale = 0)
	private BigDecimal estado;

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "monedaByIdMonedaRecibido")
	private Set<TransaccionCompraVenta> transaccionCompraVentasForIdMonedaRecibido = new HashSet(
			0);

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "moneda")
	private Set<Boveda> bovedas = new HashSet(0);

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "monedaByIdMonedaEntregado")
	private Set<TransaccionCompraVenta> transaccionCompraVentasForIdMonedaEntregado = new HashSet(
			0);

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "moneda")
	private Set<MonedaDenominacion> monedaDenominacions = new HashSet(0);

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "moneda")
	private Set<PendienteCaja> pendienteCajas = new HashSet(0);

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "moneda")
	private Set<TransaccionCajaCaja> transaccionCajaCajas = new HashSet(0);

	public Moneda() {
	}

	public Moneda(int idMoneda, String denominacion, String simbolo,
			BigDecimal estado) {
		this.idMoneda = idMoneda;
		this.denominacion = denominacion;
		this.simbolo = simbolo;
		this.estado = estado;
	}

	public Moneda(int idMoneda, String denominacion, String simbolo,
			BigDecimal estado, Set transaccionCompraVentasForIdMonedaRecibido,
			Set bovedas, Set transaccionCompraVentasForIdMonedaEntregado,
			Set monedaDenominacions, Set pendienteCajas,
			Set transaccionCajaCajas) {
		this.idMoneda = idMoneda;
		this.denominacion = denominacion;
		this.simbolo = simbolo;
		this.estado = estado;
		this.transaccionCompraVentasForIdMonedaRecibido = transaccionCompraVentasForIdMonedaRecibido;
		this.bovedas = bovedas;
		this.transaccionCompraVentasForIdMonedaEntregado = transaccionCompraVentasForIdMonedaEntregado;
		this.monedaDenominacions = monedaDenominacions;
		this.pendienteCajas = pendienteCajas;
		this.transaccionCajaCajas = transaccionCajaCajas;
	}

	public int getIdMoneda() {
		return this.idMoneda;
	}

	public void setIdMoneda(int idMoneda) {
		this.idMoneda = idMoneda;
	}

	public String getDenominacion() {
		return this.denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public String getSimbolo() {
		return this.simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public BigDecimal getEstado() {
		return this.estado;
	}

	public void setEstado(BigDecimal estado) {
		this.estado = estado;
	}

	public Set<TransaccionCompraVenta> getTransaccionCompraVentasForIdMonedaRecibido() {
		return this.transaccionCompraVentasForIdMonedaRecibido;
	}

	public void setTransaccionCompraVentasForIdMonedaRecibido(
			Set transaccionCompraVentasForIdMonedaRecibido) {
		this.transaccionCompraVentasForIdMonedaRecibido = transaccionCompraVentasForIdMonedaRecibido;
	}

	public Set<Boveda> getBovedas() {
		return this.bovedas;
	}

	public void setBovedas(Set bovedas) {
		this.bovedas = bovedas;
	}

	public Set<TransaccionCompraVenta> getTransaccionCompraVentasForIdMonedaEntregado() {
		return this.transaccionCompraVentasForIdMonedaEntregado;
	}

	public void setTransaccionCompraVentasForIdMonedaEntregado(
			Set transaccionCompraVentasForIdMonedaEntregado) {
		this.transaccionCompraVentasForIdMonedaEntregado = transaccionCompraVentasForIdMonedaEntregado;
	}

	public Set<MonedaDenominacion> getMonedaDenominacions() {
		return this.monedaDenominacions;
	}

	public void setMonedaDenominacions(Set monedaDenominacions) {
		this.monedaDenominacions = monedaDenominacions;
	}

	public Set<PendienteCaja> getPendienteCajas() {
		return this.pendienteCajas;
	}

	public void setPendienteCajas(Set pendienteCajas) {
		this.pendienteCajas = pendienteCajas;
	}

	public Set<TransaccionCajaCaja> getTransaccionCajaCajas() {
		return this.transaccionCajaCajas;
	}

	public void setTransaccionCajaCajas(Set transaccionCajaCajas) {
		this.transaccionCajaCajas = transaccionCajaCajas;
	}

	@Override
	public String toString() {
		return this.denominacion;
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof Moneda)) {
			return false;
		}
		final Moneda other = (Moneda) obj;
		return other.getSimbolo().equalsIgnoreCase(this.simbolo);
	}

	@Override
	public int hashCode() {
		return this.denominacion.hashCode() * this.simbolo.hashCode();
	}
}
