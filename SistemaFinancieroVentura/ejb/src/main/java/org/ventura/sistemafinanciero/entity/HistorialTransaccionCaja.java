package org.ventura.sistemafinanciero.entity;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the HISTORIAL_TRANSACCION_CAJA database table.
 * 
 */
@Entity
@Table(name = "HISTORIAL_TRANSACCION_CAJA")
@NamedQuery(name = "HistorialTransaccionCaja.findAll", query = "SELECT h FROM HistorialTransaccionCaja h")
@XmlRootElement(name = "moneda")
@XmlAccessorType(XmlAccessType.NONE)
public class HistorialTransaccionCaja implements Serializable {

	private static final long serialVersionUID = 1L;

	HistorialTransaccionCajaId id;

	private Date fecha;

	private Date hora;

	private String moneda;

	private String monto;

	private String tipoTransaccion;

	private int estado;

	public HistorialTransaccionCaja() {
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "idHistorialCaja", column = @Column(name = "ID_HISTORIAL_CAJA", nullable = false, precision = 22, scale = 0)),
			@AttributeOverride(name = "numeroOperacion", column = @Column(name = "NUMERO_OPERACION", nullable = false, precision = 22, scale = 0)) })
	public HistorialTransaccionCajaId getId() {
		return id;
	}

	public void setId(HistorialTransaccionCajaId id) {
		this.id = id;
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
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA", nullable = false, length = 7)
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@XmlElement
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HORA", nullable = false)
	public Date getHora() {
		return this.hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	@XmlElement
	@Column(name = "MONEDA", nullable = false, length = 50, columnDefinition = "nvarchar2")
	public String getMoneda() {
		return this.moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	@XmlElement
	@Column(name = "MONTO", nullable = false, length = 60, columnDefinition = "nvarchar2")
	public String getMonto() {
		return this.monto;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	@XmlElement
	@Column(name = "TIPO_TRANSACCION", columnDefinition = "nvarchar2")
	public String getTipoTransaccion() {
		return this.tipoTransaccion;
	}

	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

}