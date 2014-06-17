package org.ventura.sistemafinanciero.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the ESTADOCUENTA_BANCARIA_VIEW database table.
 * 
 */
@Entity
@Table(name="ESTADOCUENTA_BANCARIA_VIEW")
@NamedQuery(name="EstadocuentaBancariaView.findAll", query="SELECT e FROM EstadocuentaBancariaView e")
public class EstadocuentaBancariaView implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal estado;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private Timestamp hora;

	@Column(name="ID_TRANSACCION_TRANSFERENCIA")
	private BigDecimal idTransaccionTransferencia;

	private BigDecimal monto;

	@Column(name="NUMERO_CUENTA")
	private String numeroCuenta;

	@Column(name="NUMERO_OPERACION")
	private BigDecimal numeroOperacion;

	private String referencia;

	@Column(name="SALDO_DISPONIBLE")
	private BigDecimal saldoDisponible;

	@Column(name="TIPO_TRANSACCION_TRANSFERENCIA")
	private String tipoTransaccionTransferencia;

	public EstadocuentaBancariaView() {
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

	public BigDecimal getIdTransaccionTransferencia() {
		return this.idTransaccionTransferencia;
	}

	public void setIdTransaccionTransferencia(BigDecimal idTransaccionTransferencia) {
		this.idTransaccionTransferencia = idTransaccionTransferencia;
	}

	public BigDecimal getMonto() {
		return this.monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getNumeroCuenta() {
		return this.numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public BigDecimal getNumeroOperacion() {
		return this.numeroOperacion;
	}

	public void setNumeroOperacion(BigDecimal numeroOperacion) {
		this.numeroOperacion = numeroOperacion;
	}

	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public BigDecimal getSaldoDisponible() {
		return this.saldoDisponible;
	}

	public void setSaldoDisponible(BigDecimal saldoDisponible) {
		this.saldoDisponible = saldoDisponible;
	}

	public String getTipoTransaccionTransferencia() {
		return this.tipoTransaccionTransferencia;
	}

	public void setTipoTransaccionTransferencia(String tipoTransaccionTransferencia) {
		this.tipoTransaccionTransferencia = tipoTransaccionTransferencia;
	}

}