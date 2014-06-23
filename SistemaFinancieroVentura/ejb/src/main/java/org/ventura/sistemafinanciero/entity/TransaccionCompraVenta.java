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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.ventura.sistemafinanciero.entity.type.Tipotransaccioncompraventa;

/**
 * TransaccionCompraVenta generated by hbm2java
 */
@Entity
@Table(name = "TRANSACCION_COMPRA_VENTA", schema = "BDSISTEMAFINANCIERO")
public class TransaccionCompraVenta implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigInteger idTransaccionCompraVenta;
	private HistorialCaja historialCaja;
	private Moneda monedaRecibida;
	private Moneda monedaEntregada;
	private Date fecha;
	private Date hora;
	private BigInteger numeroOperacion;
	private BigDecimal montoRecibido;
	private BigDecimal montoEntregado;
	private BigDecimal tipoCambio;
	private String referencia;
	private int estado;
	private String observacion;
	private Tipotransaccioncompraventa tipoTransaccion;

	public TransaccionCompraVenta() {
	}

	public TransaccionCompraVenta(BigInteger idTransaccionCompraVenta,
			HistorialCaja historialCaja, Moneda monedaRecibida,
			Moneda monedaEntregada, Date fecha, Date hora,
			BigInteger numeroOperacion, BigDecimal montoRecibido,
			BigDecimal montoEntregado, BigDecimal tipoCambio, boolean estado,
			Tipotransaccioncompraventa tipoTransaccion) {
		this.idTransaccionCompraVenta = idTransaccionCompraVenta;
		this.historialCaja = historialCaja;
		this.monedaRecibida = monedaRecibida;
		this.monedaEntregada = monedaEntregada;
		this.fecha = fecha;
		this.hora = hora;
		this.numeroOperacion = numeroOperacion;
		this.montoRecibido = montoRecibido;
		this.montoEntregado = montoEntregado;
		this.tipoCambio = tipoCambio;
		this.estado = (estado ? 1 : 0);
		this.tipoTransaccion = tipoTransaccion;
	}

	public TransaccionCompraVenta(BigInteger idTransaccionCompraVenta,
			HistorialCaja historialCaja, Moneda monedaRecibida,
			Moneda monedaEntregada, Date fecha, Date hora,
			BigInteger numeroOperacion, BigDecimal montoRecibido,
			BigDecimal montoEntregado, BigDecimal tipoCambio,
			String referencia, boolean estado, String observacion,
			Tipotransaccioncompraventa tipoTransaccion) {
		this.idTransaccionCompraVenta = idTransaccionCompraVenta;
		this.historialCaja = historialCaja;
		this.monedaRecibida = monedaRecibida;
		this.monedaEntregada = monedaEntregada;
		this.fecha = fecha;
		this.hora = hora;
		this.numeroOperacion = numeroOperacion;
		this.montoRecibido = montoRecibido;
		this.montoEntregado = montoEntregado;
		this.tipoCambio = tipoCambio;
		this.referencia = referencia;
		this.estado = (estado ? 1 : 0);
		;
		this.observacion = observacion;
		this.tipoTransaccion = tipoTransaccion;
	}

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "ID_TRANSACCION_COMPRA_VENTA", unique = true, nullable = false, precision = 22, scale = 0)
	public BigInteger getIdTransaccionCompraVenta() {
		return this.idTransaccionCompraVenta;
	}

	public void setIdTransaccionCompraVenta(BigInteger idTransaccionCompraVenta) {
		this.idTransaccionCompraVenta = idTransaccionCompraVenta;
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
	@JoinColumn(name = "ID_MONEDA_RECIBIDO", nullable = false)
	public Moneda getMonedaRecibida() {
		return this.monedaRecibida;
	}

	public void setMonedaRecibida(Moneda monedaRecibida) {
		this.monedaRecibida = monedaRecibida;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MONEDA_ENTREGADO", nullable = false)
	public Moneda getMonedaEntregada() {
		return this.monedaEntregada;
	}

	public void setMonedaEntregada(Moneda monedaEntregada) {
		this.monedaEntregada = monedaEntregada;
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

	@Column(name = "MONTO_RECIBIDO", nullable = false, precision = 18)
	public BigDecimal getMontoRecibido() {
		return this.montoRecibido;
	}

	public void setMontoRecibido(BigDecimal montoRecibido) {
		this.montoRecibido = montoRecibido;
	}

	@Column(name = "MONTO_ENTREGADO", nullable = false, precision = 18)
	public BigDecimal getMontoEntregado() {
		return this.montoEntregado;
	}

	public void setMontoEntregado(BigDecimal montoEntregado) {
		this.montoEntregado = montoEntregado;
	}

	@Column(name = "TIPO_CAMBIO", nullable = false, precision = 5, scale = 3)
	public BigDecimal getTipoCambio() {
		return this.tipoCambio;
	}

	public void setTipoCambio(BigDecimal tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	@Column(name = "REFERENCIA", length = 140, columnDefinition = "nvarchar2")
	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	@Column(name = "ESTADO", nullable = false, precision = 22, scale = 0)
	public boolean getEstado() {
		return (this.estado == 1 ? true : false);
	}

	public void setEstado(boolean estado) {
		this.estado = (estado ? 1 : 0);
		;
	}

	@Column(name = "OBSERVACION", length = 100, columnDefinition = "nvarchar2")
	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TIPO_TRANSACCION", nullable = false, length = 12, columnDefinition = "nvarchar2")
	public Tipotransaccioncompraventa getTipoTransaccion() {
		return this.tipoTransaccion;
	}

	public void setTipoTransaccion(Tipotransaccioncompraventa tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

}
