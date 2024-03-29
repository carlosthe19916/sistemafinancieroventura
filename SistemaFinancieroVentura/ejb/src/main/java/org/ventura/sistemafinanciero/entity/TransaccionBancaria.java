package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.NamedNativeQueries;
import org.omg.CosNaming.NameComponent;
import org.ventura.sistemafinanciero.entity.type.Tipotransaccionbancaria;

/**
 * TransaccionBancaria generated by hbm2java
 */
@Entity
@Table(name = "TRANSACCION_BANCARIA", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "transaccionBancaria")
@XmlAccessorType(XmlAccessType.NONE)
public class TransaccionBancaria implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger idTransaccionBancaria;
	private Moneda moneda;
	private HistorialCaja historialCaja;
	private CuentaBancaria cuentaBancaria;
	private Date fecha;
	private Date hora;
	private BigInteger numeroOperacion;
	private BigDecimal monto;
	private String referencia;
	private int estado;
	private BigDecimal saldoDisponible;
	private Tipotransaccionbancaria tipoTransaccion;
	private String observacion;

	public TransaccionBancaria() {
	}

	public TransaccionBancaria(BigInteger idTransaccionBancaria,
			HistorialCaja historialCaja, CuentaBancaria cuentaBancaria,
			Date fecha, Date hora, BigInteger numeroOperacion,
			BigDecimal monto, boolean estado, BigDecimal saldoDisponible,
			Tipotransaccionbancaria tipoTransaccion) {
		this.idTransaccionBancaria = idTransaccionBancaria;
		this.historialCaja = historialCaja;
		this.cuentaBancaria = cuentaBancaria;
		this.fecha = fecha;
		this.hora = hora;
		this.numeroOperacion = numeroOperacion;
		this.monto = monto;
		this.estado = (estado ? 1 : 0);
		this.saldoDisponible = saldoDisponible;
		this.tipoTransaccion = tipoTransaccion;
	}

	public TransaccionBancaria(BigInteger idTransaccionBancaria,
			HistorialCaja historialCaja, CuentaBancaria cuentaBancaria,
			Date fecha, Date hora, BigInteger numeroOperacion,
			BigDecimal monto, String referencia, boolean estado,
			BigDecimal saldoDisponible,
			Tipotransaccionbancaria tipoTransaccion, String observacion) {
		this.idTransaccionBancaria = idTransaccionBancaria;
		this.historialCaja = historialCaja;
		this.cuentaBancaria = cuentaBancaria;
		this.fecha = fecha;
		this.hora = hora;
		this.numeroOperacion = numeroOperacion;
		this.monto = monto;
		this.referencia = referencia;
		this.estado = (estado ? 1 : 0);
		this.saldoDisponible = saldoDisponible;
		this.tipoTransaccion = tipoTransaccion;
		this.observacion = observacion;
	}

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="secuencia_transaccion_bancaria")
	@SequenceGenerator(name="secuencia_transaccion_bancaria", initialValue=1, allocationSize=1, sequenceName="TRANSACCION_SEQUENCE")
	@XmlElement(name = "id")
	@Id
	@Column(name = "ID_TRANSACCION_BANCARIA", unique = true, nullable = false, precision = 22, scale = 0)
	public BigInteger getIdTransaccionBancaria() {
		return this.idTransaccionBancaria;
	}

	public void setIdTransaccionBancaria(BigInteger idTransaccionBancaria) {
		this.idTransaccionBancaria = idTransaccionBancaria;
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

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_HISTORIAL_CAJA", nullable = false)
	public HistorialCaja getHistorialCaja() {
		return this.historialCaja;
	}

	public void setHistorialCaja(HistorialCaja historialCaja) {
		this.historialCaja = historialCaja;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CUENTA_BANCARIA", nullable = false)
	public CuentaBancaria getCuentaBancaria() {
		return this.cuentaBancaria;
	}

	public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}

	@XmlElement
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA", nullable = false, length = 7)
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@XmlElement
	@Column(name = "HORA", nullable = false)
	public Date getHora() {
		return this.hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	@XmlElement
	@Column(name = "NUMERO_OPERACION", nullable = false, precision = 22, scale = 0)
	public BigInteger getNumeroOperacion() {
		return this.numeroOperacion;
	}

	public void setNumeroOperacion(BigInteger numeroOperacion) {
		this.numeroOperacion = numeroOperacion;
	}

	@XmlElement
	@Column(name = "MONTO", nullable = false, precision = 18)
	public BigDecimal getMonto() {
		return this.monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	@XmlElement
	@Column(name = "REFERENCIA", length = 140, columnDefinition = "nvarchar2")
	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	@XmlElement
	@Column(name = "ESTADO", nullable = false, precision = 22, scale = 0)
	public boolean getEstado() {
		return (this.estado == 1 ? true : false);
	}

	public void setEstado(boolean estado) {
		this.estado = (estado ? 1 : 0);
	}

	@XmlElement
	@Column(name = "SALDO_DISPONIBLE", nullable = false, precision = 18)
	public BigDecimal getSaldoDisponible() {
		return this.saldoDisponible;
	}

	public void setSaldoDisponible(BigDecimal saldoDisponible) {
		this.saldoDisponible = saldoDisponible;
	}

	@XmlElement
	@Enumerated(value = EnumType.STRING)
	@Column(name = "TIPO_TRANSACCION", nullable = false, length = 40, columnDefinition = "nvarchar2")
	public Tipotransaccionbancaria getTipoTransaccion() {
		return this.tipoTransaccion;
	}

	public void setTipoTransaccion(Tipotransaccionbancaria tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

	@XmlElement
	@Column(name = "OBSERVACION", length = 100, columnDefinition = "nvarchar2")
	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

}
