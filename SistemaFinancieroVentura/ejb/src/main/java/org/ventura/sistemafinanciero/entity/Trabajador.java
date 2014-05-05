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
 * Trabajador generated by hbm2java
 */
@Entity
@Table(name = "TRABAJADOR", schema = "BDSISTEMAFINANCIERO")
public class Trabajador implements java.io.Serializable {

	private int idTrabajador;
	private PersonaNatural personaNatural;
	private Agencia agencia;
	private BigDecimal estado;
	private Set trabajadorUsuarios = new HashSet(0);
	private Set trabajadorCajas = new HashSet(0);

	public Trabajador() {
	}

	public Trabajador(int idTrabajador, PersonaNatural personaNatural,
			Agencia agencia, BigDecimal estado) {
		this.idTrabajador = idTrabajador;
		this.personaNatural = personaNatural;
		this.agencia = agencia;
		this.estado = estado;
	}

	public Trabajador(int idTrabajador, PersonaNatural personaNatural,
			Agencia agencia, BigDecimal estado, Set trabajadorUsuarios,
			Set trabajadorCajas) {
		this.idTrabajador = idTrabajador;
		this.personaNatural = personaNatural;
		this.agencia = agencia;
		this.estado = estado;
		this.trabajadorUsuarios = trabajadorUsuarios;
		this.trabajadorCajas = trabajadorCajas;
	}

	@Id
	@Column(name = "ID_TRABAJADOR", unique = true, nullable = false, precision = 22, scale = 0)
	public int getIdTrabajador() {
		return this.idTrabajador;
	}

	public void setIdTrabajador(int idTrabajador) {
		this.idTrabajador = idTrabajador;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PERSONA_NATURAL", nullable = false)
	public PersonaNatural getPersonaNatural() {
		return this.personaNatural;
	}

	public void setPersonaNatural(PersonaNatural personaNatural) {
		this.personaNatural = personaNatural;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_AGENCIA", nullable = false)
	public Agencia getAgencia() {
		return this.agencia;
	}

	public void setAgencia(Agencia agencia) {
		this.agencia = agencia;
	}

	@Column(name = "ESTADO", nullable = false, precision = 22, scale = 0)
	public BigDecimal getEstado() {
		return this.estado;
	}

	public void setEstado(BigDecimal estado) {
		this.estado = estado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trabajador")
	public Set<TrabajadorUsuario> getTrabajadorUsuarios() {
		return this.trabajadorUsuarios;
	}

	public void setTrabajadorUsuarios(Set trabajadorUsuarios) {
		this.trabajadorUsuarios = trabajadorUsuarios;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trabajador")
	public Set<TrabajadorCaja> getTrabajadorCajas() {
		return this.trabajadorCajas;
	}

	public void setTrabajadorCajas(Set trabajadorCajas) {
		this.trabajadorCajas = trabajadorCajas;
	}

}
