package org.ventura.sistemafinanciero.entity;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.ventura.sistemafinanciero.dao.QueryParameter;
import org.ventura.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * The persistent class for the CUENTA_BANCARIA_VIEW database table.
 * 
 */
@Entity
@Table(name = "HISTORIAL_APORTES_VIEW", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "historial_aportes_view")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({ @NamedQuery(name = HistorialAportesView.findByIdSocioAndFecha, query = "SELECT h from HistorialAportesView h WHERE h.idSocio = :idSocio") })
public class HistorialAportesView implements Serializable {

	private static final long serialVersionUID = 1L;

	// public final static String FindByFilterTextCuentaBancariaView =
	// "CuentaBancariaView.FindByFilterTextCuentaBancariaView";
	public final static String findByIdSocioAndFecha = "HistorialAportesView.findByIdSocioAndFecha";

	private BigInteger idSocio;
	private int anio;
	private int mes;
	private BigDecimal monto;

	public HistorialAportesView() {
	}

	@XmlElement(name = "id")
	@Id
	@Column(name = "ID_SOCIO", unique = true, nullable = false)
	public BigInteger getIdSocio() {
		return idSocio;
	}

	public void setIdSocio(BigInteger idSocio) {
		this.idSocio = idSocio;
	}

	@XmlElement
	@Column(name = "ANIO", precision = 22, scale = 0)
	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	@XmlElement
	@Column(name = "MES", precision = 22, scale = 0)
	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	@XmlTransient
	@Column(name = "MONTO", precision = 22, scale = 0)
	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

}