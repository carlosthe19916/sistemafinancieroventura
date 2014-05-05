package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Caja generated by hbm2java
 */
@Entity
@Table(name = "CAJA", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "caja")
@XmlAccessorType(XmlAccessType.NONE)
public class Caja implements java.io.Serializable {

	private int idCaja;
	private String denominacion;
	private String abreviatura;
	private int estado;
	private int abierto;
	private int estadoMovimiento;
	private Set trabajadorCajas = new HashSet(0);
	private Set bovedaCajas = new HashSet(0);
	private Set historialCajas = new HashSet(0);

	public Caja() {
	}

	public Caja(int idCaja, String denominacion, String abreviatura,
			boolean estado, boolean abierto, boolean estadoMovimiento) {
		this.idCaja = idCaja;
		this.denominacion = denominacion;
		this.abreviatura = abreviatura;
		this.estado = (estado ? 1 : 0 );
		this.abierto = (abierto ? 1 : 0);
		this.estadoMovimiento = (estadoMovimiento ? 1 : 0);
	}

	public Caja(int idCaja, String denominacion, String abreviatura,
			boolean estado, boolean abierto, boolean estadoMovimiento,
			Set trabajadorCajas, Set bovedaCajas, Set historialCajas) {
		this.idCaja = idCaja;
		this.denominacion = denominacion;
		this.abreviatura = abreviatura;
		this.estado = (estado ? 1 : 0 );
		this.abierto = (abierto ? 1 : 0);
		this.estadoMovimiento = (estadoMovimiento ? 1 : 0);
		this.trabajadorCajas = trabajadorCajas;
		this.bovedaCajas = bovedaCajas;
		this.historialCajas = historialCajas;
	}

	@XmlElement(name = "id")
	@Id
	@Column(name = "ID_CAJA", unique = true, nullable = false, precision = 22, scale = 0)
	public int getIdCaja() {
		return this.idCaja;
	}

	public void setIdCaja(int idCaja) {
		this.idCaja = idCaja;
	}

	@XmlElement
	@Column(name = "DENOMINACION", nullable = false, length = 40, columnDefinition = "nvarchar2")
	public String getDenominacion() {
		return this.denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	@XmlElement
	@Column(name = "ABREVIATURA", nullable = false, length = 20, columnDefinition = "nvarchar2")
	public String getAbreviatura() {
		return this.abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	@XmlElement
	@Column(name = "ESTADO", nullable = false, precision = 22, scale = 0)
	public boolean getEstado() {
		return (this.estado == 1 ? true : false);
	}

	public void setEstado(boolean estado) {
		this.estado = (estado ? 1 : 0 );
	}

	@XmlElement
	@Column(name = "ABIERTO", nullable = false, precision = 22, scale = 0)
	public boolean getAbierto() {
		return (this.abierto == 1 ? true : false);
	}

	public void setAbierto(boolean abierto) {
		this.abierto = (abierto ? 1 : 0 );
	}

	@XmlElement
	@Column(name = "ESTADO_MOVIMIENTO", nullable = false, precision = 22, scale = 0)
	public boolean getEstadoMovimiento() {
		return (this.estadoMovimiento == 1 ? true : false);
	}

	public void setEstadoMovimiento(boolean estadoMovimiento) {
		this.estadoMovimiento = (estadoMovimiento ? 1 : 0 );
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "caja")
	public Set<TrabajadorCaja> getTrabajadorCajas() {
		return this.trabajadorCajas;
	}

	public void setTrabajadorCajas(Set trabajadorCajas) {
		this.trabajadorCajas = trabajadorCajas;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "caja")
	public Set<BovedaCaja> getBovedaCajas() {
		return this.bovedaCajas;
	}

	public void setBovedaCajas(Set bovedaCajas) {
		this.bovedaCajas = bovedaCajas;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "caja")
	public Set<HistorialCaja> getHistorialCajas() {
		return this.historialCajas;
	}

	public void setHistorialCajas(Set historialCajas) {
		this.historialCajas = historialCajas;
	}

}
