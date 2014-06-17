package org.ventura.sistemafinanciero.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the ESTADOCUENTA_APORTES_VIEW database table.
 * 
 */
@Entity
@Table(name="ESTADOCUENTA_APORTES_VIEW")
@NamedQuery(name="EstadocuentaAportesView.findAll", query="SELECT e FROM EstadocuentaAportesView e")
public class EstadocuentaAportesView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ESTADO_TRANSACCION")
	private BigDecimal estadoTransaccion;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_TRANSACCION")
	private Date fechaTransaccion;

	@Column(name="HORA_TRANSACCION")
	private Timestamp horaTransaccion;

	@Column(name="ID_TRANSACCION")
	private BigDecimal idTransaccion;

	@Column(name="MONTO_TRANSACCON")
	private BigDecimal montoTransaccon;

	@Column(name="NUMERO_CUENTA_APORTE")
	private String numeroCuentaAporte;

	@Column(name="NUMERO_OPERACION")
	private BigDecimal numeroOperacion;

	@Column(name="REFERECIA_TRANSACCION")
	private String refereciaTransaccion;

	@Column(name="SALDO_DISPONIBLE")
	private BigDecimal saldoDisponible;

	@Column(name="TIPO_TRANSACCION")
	private String tipoTransaccion;

	public EstadocuentaAportesView() {
	}

	public BigDecimal getEstadoTransaccion() {
		return this.estadoTransaccion;
	}

	public void setEstadoTransaccion(BigDecimal estadoTransaccion) {
		this.estadoTransaccion = estadoTransaccion;
	}

	public Date getFechaTransaccion() {
		return this.fechaTransaccion;
	}

	public void setFechaTransaccion(Date fechaTransaccion) {
		this.fechaTransaccion = fechaTransaccion;
	}

	public Timestamp getHoraTransaccion() {
		return this.horaTransaccion;
	}

	public void setHoraTransaccion(Timestamp horaTransaccion) {
		this.horaTransaccion = horaTransaccion;
	}

	public BigDecimal getIdTransaccion() {
		return this.idTransaccion;
	}

	public void setIdTransaccion(BigDecimal idTransaccion) {
		this.idTransaccion = idTransaccion;
	}

	public BigDecimal getMontoTransaccon() {
		return this.montoTransaccon;
	}

	public void setMontoTransaccon(BigDecimal montoTransaccon) {
		this.montoTransaccon = montoTransaccon;
	}

	public String getNumeroCuentaAporte() {
		return this.numeroCuentaAporte;
	}

	public void setNumeroCuentaAporte(String numeroCuentaAporte) {
		this.numeroCuentaAporte = numeroCuentaAporte;
	}

	public BigDecimal getNumeroOperacion() {
		return this.numeroOperacion;
	}

	public void setNumeroOperacion(BigDecimal numeroOperacion) {
		this.numeroOperacion = numeroOperacion;
	}

	public String getRefereciaTransaccion() {
		return this.refereciaTransaccion;
	}

	public void setRefereciaTransaccion(String refereciaTransaccion) {
		this.refereciaTransaccion = refereciaTransaccion;
	}

	public BigDecimal getSaldoDisponible() {
		return this.saldoDisponible;
	}

	public void setSaldoDisponible(BigDecimal saldoDisponible) {
		this.saldoDisponible = saldoDisponible;
	}

	public String getTipoTransaccion() {
		return this.tipoTransaccion;
	}

	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

}