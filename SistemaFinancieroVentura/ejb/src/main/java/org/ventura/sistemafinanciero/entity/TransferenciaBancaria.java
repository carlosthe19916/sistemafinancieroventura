package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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

/**
 * TransferenciaBancaria generated by hbm2java
 */
@Entity
@Table(name = "TRANSFERENCIA_BANCARIA", schema = "BDSISTEMAFINANCIERO")
public class TransferenciaBancaria implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger idTransferenciaBancaria;
	private HistorialCaja historialCaja;
	private CuentaBancaria cuentaBancariaOrigen;
	private CuentaBancaria cuentaBancariaDestino;
	private Date fecha;
	private Date hora;
	private BigInteger numeroOperacion;
	private BigDecimal monto;
	private String referencia;
	private BigDecimal saldoDisponibleOrigen;
	private BigDecimal saldoDisponibleDestino;
	private int estado;
	private String observacion;

	public TransferenciaBancaria() {
	}

	public TransferenciaBancaria(BigInteger idTransferenciaBancaria,
			HistorialCaja historialCaja, CuentaBancaria cuentaBancariaOrigen,
			CuentaBancaria cuentaBancariaDestino, Date fecha, Date hora,
			BigDecimal BigInteger, BigDecimal monto,
			BigDecimal saldoDisponibleOrigen,
			BigDecimal saldoDisponibleDestino, boolean estado) {
		this.idTransferenciaBancaria = idTransferenciaBancaria;
		this.historialCaja = historialCaja;
		this.cuentaBancariaOrigen = cuentaBancariaOrigen;
		this.cuentaBancariaDestino = cuentaBancariaDestino;
		this.fecha = fecha;
		this.hora = hora;
		this.numeroOperacion = numeroOperacion;
		this.monto = monto;
		this.saldoDisponibleOrigen = saldoDisponibleOrigen;
		this.saldoDisponibleDestino = saldoDisponibleDestino;
		this.estado = (estado ? 1 : 0);
	}

	public TransferenciaBancaria(BigInteger idTransferenciaBancaria,
			HistorialCaja historialCaja, CuentaBancaria cuentaBancariaOrigen,
			CuentaBancaria cuentaBancariaDestino, Date fecha, Date hora,
			BigInteger numeroOperacion, BigDecimal monto, String referencia,
			BigDecimal saldoDisponibleOrigen,
			BigDecimal saldoDisponibleDestino, boolean estado,
			String observacion) {
		this.idTransferenciaBancaria = idTransferenciaBancaria;
		this.historialCaja = historialCaja;
		this.cuentaBancariaOrigen = cuentaBancariaOrigen;
		this.cuentaBancariaDestino = cuentaBancariaDestino;
		this.fecha = fecha;
		this.hora = hora;
		this.numeroOperacion = numeroOperacion;
		this.monto = monto;
		this.referencia = referencia;
		this.saldoDisponibleOrigen = saldoDisponibleOrigen;
		this.saldoDisponibleDestino = saldoDisponibleDestino;
		this.estado = (estado ? 1 : 0);;
		this.observacion = observacion;
	}

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="secuencia_transaccion_transferencia")
	@SequenceGenerator(name="secuencia_transaccion_transferencia", initialValue=1, allocationSize=1, sequenceName="TRANSACCION_SEQUENCE")
	@Id
	@Column(name = "ID_TRANSFERENCIA_BANCARIA", unique = true, nullable = false, precision = 22, scale = 0)
	public BigInteger getIdTransferenciaBancaria() {
		return this.idTransferenciaBancaria;
	}

	public void setIdTransferenciaBancaria(BigInteger idTransferenciaBancaria) {
		this.idTransferenciaBancaria = idTransferenciaBancaria;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_HISTORIAL_CAJA", nullable = false)
	public HistorialCaja getHistorialCaja() {
		return this.historialCaja;
	}

	public void setHistorialCaja(HistorialCaja historialCaja) {
		this.historialCaja = historialCaja;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CUENTA_BANCARIA_ORIGEN", nullable = false)
	public CuentaBancaria getCuentaBancariaOrigen() {
		return this.cuentaBancariaOrigen;
	}

	public void setCuentaBancariaOrigen(CuentaBancaria cuentaBancariaOrigen) {
		this.cuentaBancariaOrigen = cuentaBancariaOrigen;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CUENTA_BANCARIA_DESTINO", nullable = false)
	public CuentaBancaria getCuentaBancariaDestino() {
		return this.cuentaBancariaDestino;
	}

	public void setCuentaBancariaDestino(CuentaBancaria cuentaBancariaDestino) {
		this.cuentaBancariaDestino = cuentaBancariaDestino;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA", nullable = false, length = 7)
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name = "HORA", nullable = false)
	public Date getHora() {
		return this.hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	@Column(name = "NUMERO_OPERACION", nullable = false, precision = 22, scale = 0)
	public BigInteger getNumeroOperacion() {
		return this.numeroOperacion;
	}

	public void setNumeroOperacion(BigInteger numeroOperacion) {
		this.numeroOperacion = numeroOperacion;
	}

	@Column(name = "MONTO", nullable = false, precision = 18)
	public BigDecimal getMonto() {
		return this.monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	@Column(name = "REFERENCIA", length = 100, columnDefinition = "nvarchar2")
	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	@Column(name = "SALDO_DISPONIBLE_ORIGEN", nullable = false, precision = 18)
	public BigDecimal getSaldoDisponibleOrigen() {
		return this.saldoDisponibleOrigen;
	}

	public void setSaldoDisponibleOrigen(BigDecimal saldoDisponibleOrigen) {
		this.saldoDisponibleOrigen = saldoDisponibleOrigen;
	}

	@Column(name = "SALDO_DISPONIBLE_DESTINO", nullable = false, precision = 18)
	public BigDecimal getSaldoDisponibleDestino() {
		return this.saldoDisponibleDestino;
	}

	public void setSaldoDisponibleDestino(BigDecimal saldoDisponibleDestino) {
		this.saldoDisponibleDestino = saldoDisponibleDestino;
	}

	@Column(name = "ESTADO", nullable = false, precision = 22, scale = 0)
	public boolean getEstado() {
		return (this.estado == 1 ? true : false);
	}

	public void setEstado(boolean estado) {
		this.estado = (estado ? 1 : 0);
	}

	@Column(name = "OBSERVACION", length = 100, columnDefinition = "nvarchar2")
	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

}
