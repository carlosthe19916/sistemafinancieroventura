package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TransaccionBovedaCaja generated by hbm2java
 */
@Entity
@Table(name = "TRANSACCION_BOVEDA_CAJA", schema = "BDSISTEMAFINANCIERO")
public class TransaccionBovedaCaja implements java.io.Serializable {

	private BigDecimal idTransaccionBovedaCaja;
	private HistorialBoveda historialBoveda;
	private HistorialCaja historialCaja;
	private Date fecha;
	private Serializable hora;
	private BigDecimal saldoDisponibleOrigen;
	private String observacion;
	private BigDecimal estadoSolicitud;
	private BigDecimal estadoConfirmacion;
	private String origen;
	private BigDecimal saldoDisponibleDestino;
	private Set transaccionBovedaCajaDetalls = new HashSet(0);

	public TransaccionBovedaCaja() {
	}

	public TransaccionBovedaCaja(BigDecimal idTransaccionBovedaCaja,
			HistorialBoveda historialBoveda, HistorialCaja historialCaja,
			Date fecha, Serializable hora, BigDecimal saldoDisponibleOrigen,
			BigDecimal estadoSolicitud, BigDecimal estadoConfirmacion,
			BigDecimal saldoDisponibleDestino) {
		this.idTransaccionBovedaCaja = idTransaccionBovedaCaja;
		this.historialBoveda = historialBoveda;
		this.historialCaja = historialCaja;
		this.fecha = fecha;
		this.hora = hora;
		this.saldoDisponibleOrigen = saldoDisponibleOrigen;
		this.estadoSolicitud = estadoSolicitud;
		this.estadoConfirmacion = estadoConfirmacion;
		this.saldoDisponibleDestino = saldoDisponibleDestino;
	}

	public TransaccionBovedaCaja(BigDecimal idTransaccionBovedaCaja,
			HistorialBoveda historialBoveda, HistorialCaja historialCaja,
			Date fecha, Serializable hora, BigDecimal saldoDisponibleOrigen,
			String observacion, BigDecimal estadoSolicitud,
			BigDecimal estadoConfirmacion, String origen,
			BigDecimal saldoDisponibleDestino, Set transaccionBovedaCajaDetalls) {
		this.idTransaccionBovedaCaja = idTransaccionBovedaCaja;
		this.historialBoveda = historialBoveda;
		this.historialCaja = historialCaja;
		this.fecha = fecha;
		this.hora = hora;
		this.saldoDisponibleOrigen = saldoDisponibleOrigen;
		this.observacion = observacion;
		this.estadoSolicitud = estadoSolicitud;
		this.estadoConfirmacion = estadoConfirmacion;
		this.origen = origen;
		this.saldoDisponibleDestino = saldoDisponibleDestino;
		this.transaccionBovedaCajaDetalls = transaccionBovedaCajaDetalls;
	}

	@Id
	@Column(name = "ID_TRANSACCION_BOVEDA_CAJA", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getIdTransaccionBovedaCaja() {
		return this.idTransaccionBovedaCaja;
	}

	public void setIdTransaccionBovedaCaja(BigDecimal idTransaccionBovedaCaja) {
		this.idTransaccionBovedaCaja = idTransaccionBovedaCaja;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_HISTORIAL_BOVEDA", nullable = false)
	public HistorialBoveda getHistorialBoveda() {
		return this.historialBoveda;
	}

	public void setHistorialBoveda(HistorialBoveda historialBoveda) {
		this.historialBoveda = historialBoveda;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_HISTORIAL_CAJA", nullable = false)
	public HistorialCaja getHistorialCaja() {
		return this.historialCaja;
	}

	public void setHistorialCaja(HistorialCaja historialCaja) {
		this.historialCaja = historialCaja;
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
	public Serializable getHora() {
		return this.hora;
	}

	public void setHora(Serializable hora) {
		this.hora = hora;
	}

	@Column(name = "SALDO_DISPONIBLE_ORIGEN", nullable = false, precision = 18)
	public BigDecimal getSaldoDisponibleOrigen() {
		return this.saldoDisponibleOrigen;
	}

	public void setSaldoDisponibleOrigen(BigDecimal saldoDisponibleOrigen) {
		this.saldoDisponibleOrigen = saldoDisponibleOrigen;
	}

	@Column(name = "OBSERVACION", length = 140)
	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@Column(name = "ESTADO_SOLICITUD", nullable = false, precision = 22, scale = 0)
	public BigDecimal getEstadoSolicitud() {
		return this.estadoSolicitud;
	}

	public void setEstadoSolicitud(BigDecimal estadoSolicitud) {
		this.estadoSolicitud = estadoSolicitud;
	}

	@Column(name = "ESTADO_CONFIRMACION", nullable = false, precision = 22, scale = 0)
	public BigDecimal getEstadoConfirmacion() {
		return this.estadoConfirmacion;
	}

	public void setEstadoConfirmacion(BigDecimal estadoConfirmacion) {
		this.estadoConfirmacion = estadoConfirmacion;
	}

	@Column(name = "ORIGEN", length = 12)
	public String getOrigen() {
		return this.origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	@Column(name = "SALDO_DISPONIBLE_DESTINO", nullable = false, precision = 18)
	public BigDecimal getSaldoDisponibleDestino() {
		return this.saldoDisponibleDestino;
	}

	public void setSaldoDisponibleDestino(BigDecimal saldoDisponibleDestino) {
		this.saldoDisponibleDestino = saldoDisponibleDestino;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "transaccionBovedaCaja")
	public Set<TransaccionBovedaCajaDetall> getTransaccionBovedaCajaDetalls() {
		return this.transaccionBovedaCajaDetalls;
	}

	public void setTransaccionBovedaCajaDetalls(Set transaccionBovedaCajaDetalls) {
		this.transaccionBovedaCajaDetalls = transaccionBovedaCajaDetalls;
	}

}
