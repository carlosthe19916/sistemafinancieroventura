package org.ventura.sistemafinanciero.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the HISTORIAL_TRANSACCION_CAJA database table.
 * 
 */
@Entity
@Table(name="HISTORIAL_TRANSACCION_CAJA")
@NamedQuery(name="HistorialTransaccionCaja.findAll", query="SELECT h FROM HistorialTransaccionCaja h")
public class HistorialTransaccionCaja implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal estado;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private Timestamp hora;

	@Column(name="ID_HISTORIAL_CAJA")
	private BigDecimal idHistorialCaja;

	private String moneda;

	private String monto;

	@Column(name="NUMERO_OPERACION")
	private BigDecimal numeroOperacion;

	@Column(name="TIPO_TRANSACCION")
	private String tipoTransaccion;

	public HistorialTransaccionCaja() {
	}

	public BigDecimal getEstado() {
		return this.estado;
	}

	public void setEstado(BigDecimal estado) {
		this.estado = estado;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Timestamp getHora() {
		return this.hora;
	}

	public void setHora(Timestamp hora) {
		this.hora = hora;
	}

	public BigDecimal getIdHistorialCaja() {
		return this.idHistorialCaja;
	}

	public void setIdHistorialCaja(BigDecimal idHistorialCaja) {
		this.idHistorialCaja = idHistorialCaja;
	}

	public String getMoneda() {
		return this.moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getMonto() {
		return this.monto;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public BigDecimal getNumeroOperacion() {
		return this.numeroOperacion;
	}

	public void setNumeroOperacion(BigDecimal numeroOperacion) {
		this.numeroOperacion = numeroOperacion;
	}

	public String getTipoTransaccion() {
		return this.tipoTransaccion;
	}

	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

}