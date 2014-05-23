package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.ventura.sistemafinanciero.entity.type.EstadoCuentaAporte;

/**
 * CuentaAporte generated by hbm2java
 */
@Entity
@Table(name = "CUENTA_APORTE", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "cuentaaporte")
@XmlAccessorType(XmlAccessType.NONE)
public class CuentaAporte implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger idCuentaaporte;
	private BigDecimal saldo;
	private Moneda moneda;
	private EstadoCuentaAporte estadoCuenta;
	private Set socios = new HashSet(0);

	public CuentaAporte() {
	}

	public CuentaAporte(BigInteger idCuentaaporte, BigDecimal saldo,
			EstadoCuentaAporte estadoCuenta) {
		this.idCuentaaporte = idCuentaaporte;
		this.saldo = saldo;
		this.estadoCuenta = estadoCuenta;
	}

	public CuentaAporte(BigInteger idCuentaaporte, BigDecimal saldo,
			EstadoCuentaAporte estadoCuenta, Set socios) {
		this.idCuentaaporte = idCuentaaporte;
		this.saldo = saldo;
		this.estadoCuenta = estadoCuenta;
		this.socios = socios;
	}

	@XmlElement(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "ID_CUENTAAPORTE", unique = true, nullable = false, precision = 22, scale = 0)
	public BigInteger getIdCuentaaporte() {
		return this.idCuentaaporte;
	}

	public void setIdCuentaaporte(BigInteger idCuentaaporte) {
		this.idCuentaaporte = idCuentaaporte;
	}

	@XmlElement
	@Column(name = "SALDO", nullable = false, precision = 18)
	public BigDecimal getSaldo() {
		return this.saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MONEDA", nullable = false)
	public Moneda getMoneda() {
		return this.moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO_CUENTA", nullable = false, length = 24, columnDefinition = "nvarchar2")
	public EstadoCuentaAporte getEstadoCuenta() {
		return this.estadoCuenta;
	}

	public void setEstadoCuenta(EstadoCuentaAporte estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cuentaAporte")
	public Set<Socio> getSocios() {
		return this.socios;
	}

	public void setSocios(Set socios) {
		this.socios = socios;
	}

}
