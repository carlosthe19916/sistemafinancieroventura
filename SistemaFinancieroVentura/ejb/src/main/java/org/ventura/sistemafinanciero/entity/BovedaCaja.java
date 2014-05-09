package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * BovedaCaja generated by hbm2java
 */
@Entity
@Table(name = "BOVEDA_CAJA", schema = "BDSISTEMAFINANCIERO")
public class BovedaCaja implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BovedaCajaId id;
	private Caja caja;
	private Boveda boveda;
	private BigDecimal saldo;

	public BovedaCaja() {
	}

	public BovedaCaja(BovedaCajaId id, Caja caja, Boveda boveda) {
		this.id = id;
		this.caja = caja;
		this.boveda = boveda;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "idBoveda", column = @Column(name = "ID_BOVEDA", nullable = false, precision = 22, scale = 0)),
			@AttributeOverride(name = "idCaja", column = @Column(name = "ID_CAJA", nullable = false, precision = 22, scale = 0)) })
	public BovedaCajaId getId() {
		return this.id;
	}

	public void setId(BovedaCajaId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CAJA", nullable = false, insertable = false, updatable = false)
	public Caja getCaja() {
		return this.caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_BOVEDA", nullable = false, insertable = false, updatable = false)
	public Boveda getBoveda() {
		return this.boveda;
	}

	public void setBoveda(Boveda boveda) {
		this.boveda = boveda;
	}

	@Column(name = "SALDO", nullable = false, precision = 18)
	public BigDecimal getSaldo() {
		return this.saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

}
