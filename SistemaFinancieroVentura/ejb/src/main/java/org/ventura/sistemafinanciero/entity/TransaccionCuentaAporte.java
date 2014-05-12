package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.ventura.sistemafinanciero.entity.type.Tipotransaccionbancaria;

/**
 * TransaccionCuentaAporte generated by hbm2java
 */
@Entity
@Table(name = "TRANSACCION_CUENTA_APORTE", schema = "BDSISTEMAFINANCIERO")
public class TransaccionCuentaAporte implements java.io.Serializable {

	private BigDecimal idTransaccionCuentaAporte;
	private HistorialCaja historialCaja;
	private CuentaAporte cuentaAporte;
	private Date fecha;
	private Date hora;
	private BigDecimal numeroOperacion;
	private BigDecimal monto;
	private BigDecimal mesAfecta;
	private BigDecimal anioAfecta;
	private BigDecimal estado;
	private BigDecimal saldoDisponible;
	private String referencia;
	private String observacion;
	private Tipotransaccionbancaria tipoTransaccion;

	public TransaccionCuentaAporte() {
	}

	public TransaccionCuentaAporte(BigDecimal idTransaccionCuentaAporte,
			HistorialCaja historialCaja, Date fecha, Date hora,
			BigDecimal numeroOperacion, BigDecimal monto,
			BigDecimal anioAfecta, BigDecimal estado,
			BigDecimal saldoDisponible, Tipotransaccionbancaria tipoTransaccion) {
		this.idTransaccionCuentaAporte = idTransaccionCuentaAporte;
		this.historialCaja = historialCaja;
		this.fecha = fecha;
		this.hora = hora;
		this.numeroOperacion = numeroOperacion;
		this.monto = monto;
		this.anioAfecta = anioAfecta;
		this.estado = estado;
		this.saldoDisponible = saldoDisponible;
		this.tipoTransaccion = tipoTransaccion;
	}

	public TransaccionCuentaAporte(BigDecimal idTransaccionCuentaAporte,
			HistorialCaja historialCaja, Date fecha, Date hora,
			BigDecimal numeroOperacion, BigDecimal monto, BigDecimal mesAfecta,
			BigDecimal anioAfecta, BigDecimal estado,
			BigDecimal saldoDisponible, String referencia, String observacion,
			Tipotransaccionbancaria tipoTransaccion) {
		this.idTransaccionCuentaAporte = idTransaccionCuentaAporte;
		this.historialCaja = historialCaja;
		this.fecha = fecha;
		this.hora = hora;
		this.numeroOperacion = numeroOperacion;
		this.monto = monto;
		this.mesAfecta = mesAfecta;
		this.anioAfecta = anioAfecta;
		this.estado = estado;
		this.saldoDisponible = saldoDisponible;
		this.referencia = referencia;
		this.observacion = observacion;
		this.tipoTransaccion = tipoTransaccion;
	}

	@Id
	@Column(name = "ID_TRANSACCION_CUENTA_APORTE", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getIdTransaccionCuentaAporte() {
		return this.idTransaccionCuentaAporte;
	}

	public void setIdTransaccionCuentaAporte(
			BigDecimal idTransaccionCuentaAporte) {
		this.idTransaccionCuentaAporte = idTransaccionCuentaAporte;
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
	@JoinColumn(name = "ID_CUENTAAPORTE", nullable = false)
	public CuentaAporte getCuentaAporte() {
		return this.cuentaAporte;
	}

	public void setCuentaAporte(CuentaAporte cuentaAporte) {
		this.cuentaAporte = cuentaAporte;
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
	public BigDecimal getNumeroOperacion() {
		return this.numeroOperacion;
	}

	public void setNumeroOperacion(BigDecimal numeroOperacion) {
		this.numeroOperacion = numeroOperacion;
	}

	@Column(name = "MONTO", nullable = false, precision = 18)
	public BigDecimal getMonto() {
		return this.monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	@Column(name = "MES_AFECTA", precision = 22, scale = 0)
	public BigDecimal getMesAfecta() {
		return this.mesAfecta;
	}

	public void setMesAfecta(BigDecimal mesAfecta) {
		this.mesAfecta = mesAfecta;
	}

	@Column(name = "ANIO_AFECTA", nullable = false, precision = 22, scale = 0)
	public BigDecimal getAnioAfecta() {
		return this.anioAfecta;
	}

	public void setAnioAfecta(BigDecimal anioAfecta) {
		this.anioAfecta = anioAfecta;
	}

	@Column(name = "ESTADO", nullable = false, precision = 22, scale = 0)
	public BigDecimal getEstado() {
		return this.estado;
	}

	public void setEstado(BigDecimal estado) {
		this.estado = estado;
	}

	@Column(name = "SALDO_DISPONIBLE", nullable = false, precision = 18)
	public BigDecimal getSaldoDisponible() {
		return this.saldoDisponible;
	}

	public void setSaldoDisponible(BigDecimal saldoDisponible) {
		this.saldoDisponible = saldoDisponible;
	}

	@Column(name = "REFERENCIA", length = 140, columnDefinition = "nvarchar2")
	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	@Column(name = "OBSERVACION", length = 100, columnDefinition = "nvarchar2")
	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TIPO_TRANSACCION", nullable = false, length = 16, columnDefinition = "nvarchar2")
	public Tipotransaccionbancaria getTipoTransaccion() {
		return this.tipoTransaccion;
	}

	public void setTipoTransaccion(Tipotransaccionbancaria tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

}
