package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * HistorialCaja generated by hbm2java
 */
@Entity
@Table(name = "HISTORIAL_CAJA", schema = "BDSISTEMAFINANCIERO")
@NamedQueries({ @NamedQuery(name = HistorialCaja.findByHistorialActivo, query = "SELECT c FROM HistorialCaja c WHERE c.caja.idCaja = :idcaja AND c.estado = true") })
public class HistorialCaja implements java.io.Serializable {

	public final static String findByHistorialActivo = "findByHistorialActivo";

	private int idHistorialCaja;
	private Caja caja;
	private Date fechaApertura;
	private Date fechaCierre;
	private Date horaApertura;
	private Date horaCierre;
	private int estado;
	private Set transaccionCajaCajasForIdCajaHistorialDestino = new HashSet(0);
	private Set transaccionCuentaAportes = new HashSet(0);
	private Set transaccionCompraVentas = new HashSet(0);
	private Set pendienteCajas = new HashSet(0);
	private Set transaccionCajaCajasForIdCajaHistorialOrigen = new HashSet(0);
	private Set transaccionBovedaCajas = new HashSet(0);
	private Set transaccionBancarias = new HashSet(0);
	private Set detalleHistorialCajas = new HashSet(0);
	private Set transferenciaBancarias = new HashSet(0);

	public HistorialCaja() {
	}

	public HistorialCaja(int idHistorialCaja, Caja caja,
			Date fechaApertura, Date horaApertura, boolean estado) {
		this.idHistorialCaja = idHistorialCaja;
		this.caja = caja;
		this.fechaApertura = fechaApertura;
		this.horaApertura = horaApertura;
		this.estado = (estado ? 1:0 );
	}

	public HistorialCaja(int idHistorialCaja, Caja caja,
			Date fechaApertura, Date fechaCierre, Date horaApertura,
			Date horaCierre, boolean estado,
			Set transaccionCajaCajasForIdCajaHistorialDestino,
			Set transaccionCuentaAportes, Set transaccionCompraVentas,
			Set pendienteCajas,
			Set transaccionCajaCajasForIdCajaHistorialOrigen,
			Set transaccionBovedaCajas, Set transaccionBancarias,
			Set detalleHistorialCajas, Set transferenciaBancarias) {
		this.idHistorialCaja = idHistorialCaja;
		this.caja = caja;
		this.fechaApertura = fechaApertura;
		this.fechaCierre = fechaCierre;
		this.horaApertura = horaApertura;
		this.horaCierre = horaCierre;
		this.estado = (estado ? 1:0 );
		this.transaccionCajaCajasForIdCajaHistorialDestino = transaccionCajaCajasForIdCajaHistorialDestino;
		this.transaccionCuentaAportes = transaccionCuentaAportes;
		this.transaccionCompraVentas = transaccionCompraVentas;
		this.pendienteCajas = pendienteCajas;
		this.transaccionCajaCajasForIdCajaHistorialOrigen = transaccionCajaCajasForIdCajaHistorialOrigen;
		this.transaccionBovedaCajas = transaccionBovedaCajas;
		this.transaccionBancarias = transaccionBancarias;
		this.detalleHistorialCajas = detalleHistorialCajas;
		this.transferenciaBancarias = transferenciaBancarias;
	}

	@Id
	@Column(name = "ID_HISTORIAL_CAJA", unique = true, nullable = false, precision = 22, scale = 0)
	public int getIdHistorialCaja() {
		return this.idHistorialCaja;
	}

	public void setIdHistorialCaja(int idHistorialCaja) {
		this.idHistorialCaja = idHistorialCaja;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CAJA", nullable = false)
	public Caja getCaja() {
		return this.caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_APERTURA", nullable = false, length = 7)
	public Date getFechaApertura() {
		return this.fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_CIERRE", length = 7)
	public Date getFechaCierre() {
		return this.fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	@Column(name = "HORA_APERTURA", nullable = false)
	public Date getHoraApertura() {
		return this.horaApertura;
	}

	public void setHoraApertura(Date horaApertura) {
		this.horaApertura = horaApertura;
	}

	@Column(name = "HORA_CIERRE")
	public Date getHoraCierre() {
		return this.horaCierre;
	}

	public void setHoraCierre(Date horaCierre) {
		this.horaCierre = horaCierre;
	}

	@Column(name = "ESTADO", nullable = false, precision = 22, scale = 0)
	public boolean getEstado() {
		return (this.estado == 1 ? true : false);
	}

	public void setEstado(boolean estado) {
		this.estado = (estado ? 1 : 0);
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "historialCajaByIdCajaHistorialDestino")
	public Set<TransaccionCajaCaja> getTransaccionCajaCajasForIdCajaHistorialDestino() {
		return this.transaccionCajaCajasForIdCajaHistorialDestino;
	}

	public void setTransaccionCajaCajasForIdCajaHistorialDestino(
			Set transaccionCajaCajasForIdCajaHistorialDestino) {
		this.transaccionCajaCajasForIdCajaHistorialDestino = transaccionCajaCajasForIdCajaHistorialDestino;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "historialCaja")
	public Set<TransaccionCuentaAporte> getTransaccionCuentaAportes() {
		return this.transaccionCuentaAportes;
	}

	public void setTransaccionCuentaAportes(Set transaccionCuentaAportes) {
		this.transaccionCuentaAportes = transaccionCuentaAportes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "historialCaja")
	public Set<TransaccionCompraVenta> getTransaccionCompraVentas() {
		return this.transaccionCompraVentas;
	}

	public void setTransaccionCompraVentas(Set transaccionCompraVentas) {
		this.transaccionCompraVentas = transaccionCompraVentas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "historialCaja")
	public Set<PendienteCaja> getPendienteCajas() {
		return this.pendienteCajas;
	}

	public void setPendienteCajas(Set pendienteCajas) {
		this.pendienteCajas = pendienteCajas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "historialCajaByIdCajaHistorialOrigen")
	public Set<TransaccionCajaCaja> getTransaccionCajaCajasForIdCajaHistorialOrigen() {
		return this.transaccionCajaCajasForIdCajaHistorialOrigen;
	}

	public void setTransaccionCajaCajasForIdCajaHistorialOrigen(
			Set transaccionCajaCajasForIdCajaHistorialOrigen) {
		this.transaccionCajaCajasForIdCajaHistorialOrigen = transaccionCajaCajasForIdCajaHistorialOrigen;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "historialCaja")
	public Set<TransaccionBovedaCaja> getTransaccionBovedaCajas() {
		return this.transaccionBovedaCajas;
	}

	public void setTransaccionBovedaCajas(Set transaccionBovedaCajas) {
		this.transaccionBovedaCajas = transaccionBovedaCajas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "historialCaja")
	public Set<TransaccionBancaria> getTransaccionBancarias() {
		return this.transaccionBancarias;
	}

	public void setTransaccionBancarias(Set transaccionBancarias) {
		this.transaccionBancarias = transaccionBancarias;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "historialCaja")
	public Set<DetalleHistorialCaja> getDetalleHistorialCajas() {
		return this.detalleHistorialCajas;
	}

	public void setDetalleHistorialCajas(Set detalleHistorialCajas) {
		this.detalleHistorialCajas = detalleHistorialCajas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "historialCaja")
	public Set<TransferenciaBancaria> getTransferenciaBancarias() {
		return this.transferenciaBancarias;
	}

	public void setTransferenciaBancarias(Set transferenciaBancarias) {
		this.transferenciaBancarias = transferenciaBancarias;
	}

}
