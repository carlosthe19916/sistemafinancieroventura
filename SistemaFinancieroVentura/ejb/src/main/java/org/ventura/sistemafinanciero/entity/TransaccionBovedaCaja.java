package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.ventura.sistemafinanciero.entity.type.TransaccionBovedaCajaOrigen;
import org.ventura.sistemafinanciero.entity.type.Variable;

/**
 * TransaccionBovedaCaja generated by hbm2java
 */
@Entity
@Table(name = "TRANSACCION_BOVEDA_CAJA", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "transaccionbovedacaja")
@XmlAccessorType(XmlAccessType.NONE)
public class TransaccionBovedaCaja implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger idTransaccionBovedaCaja;
	private HistorialBoveda historialBoveda;
	private HistorialCaja historialCaja;
	private Date fecha;
	private Date hora;
	private BigDecimal saldoDisponibleOrigen;
	private String observacion;
	private int estadoSolicitud;
	private int estadoConfirmacion;
	private TransaccionBovedaCajaOrigen origen;
	private BigDecimal saldoDisponibleDestino;
	private Set transaccionBovedaCajaDetalls = new HashSet(0);

	public TransaccionBovedaCaja() {
	}

	public TransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja,
			HistorialBoveda historialBoveda, HistorialCaja historialCaja,
			Date fecha, Date hora, BigDecimal saldoDisponibleOrigen,
			boolean estadoSolicitud, boolean estadoConfirmacion,
			BigDecimal saldoDisponibleDestino) {
		this.idTransaccionBovedaCaja = idTransaccionBovedaCaja;
		this.historialBoveda = historialBoveda;
		this.historialCaja = historialCaja;
		this.fecha = fecha;
		this.hora = hora;
		this.saldoDisponibleOrigen = saldoDisponibleOrigen;
		this.estadoSolicitud = (estadoSolicitud ? 1 : 0);
		this.estadoConfirmacion = (estadoConfirmacion ? 1 : 0);
		this.saldoDisponibleDestino = saldoDisponibleDestino;
	}

	public TransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja,
			HistorialBoveda historialBoveda, HistorialCaja historialCaja,
			Date fecha, Date hora, BigDecimal saldoDisponibleOrigen,
			String observacion, boolean estadoSolicitud,
			boolean estadoConfirmacion, TransaccionBovedaCajaOrigen origen,
			BigDecimal saldoDisponibleDestino, Set transaccionBovedaCajaDetalls) {
		this.idTransaccionBovedaCaja = idTransaccionBovedaCaja;
		this.historialBoveda = historialBoveda;
		this.historialCaja = historialCaja;
		this.fecha = fecha;
		this.hora = hora;
		this.saldoDisponibleOrigen = saldoDisponibleOrigen;
		this.observacion = observacion;
		this.estadoSolicitud = (estadoSolicitud ? 1 : 0);
		this.estadoConfirmacion = (estadoConfirmacion ? 1 : 0);
		this.origen = origen;
		this.saldoDisponibleDestino = saldoDisponibleDestino;
		this.transaccionBovedaCajaDetalls = transaccionBovedaCajaDetalls;
	}

	@XmlElement(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "ID_TRANSACCION_BOVEDA_CAJA", unique = true, nullable = false, precision = 22, scale = 0)
	public BigInteger getIdTransaccionBovedaCaja() {
		return this.idTransaccionBovedaCaja;
	}

	public void setIdTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja) {
		this.idTransaccionBovedaCaja = idTransaccionBovedaCaja;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_HISTORIAL_BOVEDA", nullable = false)
	public HistorialBoveda getHistorialBoveda() {
		return this.historialBoveda;
	}

	public void setHistorialBoveda(HistorialBoveda historialBoveda) {
		this.historialBoveda = historialBoveda;
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
	@Column(name = "SALDO_DISPONIBLE_ORIGEN", nullable = false, precision = 18)
	public BigDecimal getSaldoDisponibleOrigen() {
		return this.saldoDisponibleOrigen;
	}

	public void setSaldoDisponibleOrigen(BigDecimal saldoDisponibleOrigen) {
		this.saldoDisponibleOrigen = saldoDisponibleOrigen;
	}

	@XmlElement
	@Column(name = "OBSERVACION", length = 140, columnDefinition = "nvarchar2")
	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@XmlElement
	@Column(name = "ESTADO_SOLICITUD", nullable = false, precision = 22, scale = 0)
	public boolean getEstadoSolicitud() {
		return (this.estadoSolicitud == 1 ? true : false);
	}

	public void setEstadoSolicitud(boolean estadoSolicitud) {
		this.estadoSolicitud = (estadoSolicitud ? 1 : 0);
	}

	@XmlElement
	@Column(name = "ESTADO_CONFIRMACION", nullable = false, precision = 22, scale = 0)
	public boolean getEstadoConfirmacion() {
		return (this.estadoConfirmacion == 1 ? true : false);
	}

	public void setEstadoConfirmacion(boolean estadoConfirmacion) {
		this.estadoConfirmacion = (estadoConfirmacion ? 1 : 0);
	}

	@XmlElement
	@Enumerated(value = EnumType.STRING)
	@Column(name = "ORIGEN", length = 12, columnDefinition = "nvarchar2")
	public TransaccionBovedaCajaOrigen getOrigen() {
		return this.origen;
	}

	public void setOrigen(TransaccionBovedaCajaOrigen origen) {
		this.origen = origen;
	}

	@XmlElement
	@Column(name = "SALDO_DISPONIBLE_DESTINO", nullable = false, precision = 18)
	public BigDecimal getSaldoDisponibleDestino() {
		return this.saldoDisponibleDestino;
	}

	public void setSaldoDisponibleDestino(BigDecimal saldoDisponibleDestino) {
		this.saldoDisponibleDestino = saldoDisponibleDestino;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "transaccionBovedaCaja")
	public Set<TransaccionBovedaCajaDetalle> getTransaccionBovedaCajaDetalls() {
		return this.transaccionBovedaCajaDetalls;
	}

	public void setTransaccionBovedaCajaDetalls(Set transaccionBovedaCajaDetalls) {
		this.transaccionBovedaCajaDetalls = transaccionBovedaCajaDetalls;
	}

}
