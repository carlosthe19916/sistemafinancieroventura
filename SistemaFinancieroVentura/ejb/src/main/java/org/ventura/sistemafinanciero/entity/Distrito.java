package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * TipoDocumento generated by hbm2java
 */
@Entity
@Table(name = "DISTRITO", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "pais")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({
		@NamedQuery(name = Distrito.findByIdProvincia, query = "SELECT d FROM Distrito d WHERE d.provincia.idProvincia = :idprovincia Order By d.denominacion"),
		@NamedQuery(name = Distrito.findCodigoProvincia, query = "SELECT d FROM Distrito d INNER JOIN d.provincia p INNER JOIN p.departamento dep WHERE dep.codigo = :codigoDepartamento AND p.codigo = :codigoProvincia Order By d.denominacion") })
public class Distrito implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String findByIdProvincia = "Distrito.findByIdProvincia";
	public final static String findCodigoProvincia = "Distrito.findCodigoProvincia";

	private BigInteger idDistrito;
	private String denominacion;
	private String abreviatura;
	private String codigo;
	private Provincia provincia;

	public Distrito() {
	}

	@XmlElement(name = "id")
	@Id
	@Column(name = "ID_DISTRITO", unique = true, nullable = false, precision = 22, scale = 0)
	public BigInteger getIdDistrito() {
		return this.idDistrito;
	}

	public void setIdDistrito(BigInteger idProvincia) {
		this.idDistrito = idProvincia;
	}

	@XmlElement
	@Column(name = "DENOMINACION", length = 100, columnDefinition = "nvarchar2")
	public String getDenominacion() {
		return this.denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	@XmlElement
	@Column(name = "ABREVIATURA", length = 5, columnDefinition = "nvarchar2")
	public String getAbreviatura() {
		return this.abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	@XmlElement
	@Column(name = "CODIGO", length = 3, columnDefinition = "nvarchar2")
	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PROVINCIA", nullable = false)
	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

}
