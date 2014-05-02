package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * PersonaNatural generated by hbm2java
 */
@Entity
@Table(name = "PERSONA_NATURAL", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "personanatural")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({
		@NamedQuery(name = PersonaNatural.FindAll, query = "SELECT p FROM PersonaNatural p"),
		@NamedQuery(name = PersonaNatural.FindByTipoAndNumeroDocumento, query = "SELECT p FROM PersonaNatural p WHERE p.tipoDocumento.idTipoDocumento = :idtipodocumento AND p.numeroDocumento = :numerodocumento"),
		@NamedQuery(name = PersonaNatural.FindByFilterText, query = "SELECT p FROM PersonaNatural p WHERE p.numeroDocumento = :filtertext OR LOWER(CONCAT(p.apellidoPaterno,' ', p.apellidoMaterno,' ',p.nombres)) LIKE '%:filtertext%'") })
public class PersonaNatural implements java.io.Serializable {

	public final static String FindByTipoAndNumeroDocumento = "FindByTipoAndNumeroDocumento";
	public final static String FindByFilterText = "FindByFilterText";
	public final static String FindAll = "FindAll";

	private BigDecimal idPersonaNatural;
	private TipoDocumento tipoDocumento;
	private String numeroDocumento;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String nombres;
	private Date fechaNacimiento;
	private String sexo;
	private String estadoCivil;
	private String ocupacion;
	private String direccion;
	private String referencia;
	private String telefono;
	private String celular;
	private String email;
	private String ubigeo;
	private String codigoPais;
	private Set titulars = new HashSet(0);
	private Set personaJuridicas = new HashSet(0);
	private Set accionistas = new HashSet(0);
	private Set socios = new HashSet(0);
	private Set trabajadors = new HashSet(0);

	public PersonaNatural() {
	}

	public PersonaNatural(BigDecimal idPersonaNatural,
			TipoDocumento tipoDocumento, String numeroDocumento,
			String apellidoPaterno, String apellidoMaterno, String nombres,
			Date fechaNacimiento, String sexo) {
		this.idPersonaNatural = idPersonaNatural;
		this.tipoDocumento = tipoDocumento;
		this.numeroDocumento = numeroDocumento;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.nombres = nombres;
		this.fechaNacimiento = fechaNacimiento;
		this.sexo = sexo;
	}

	public PersonaNatural(BigDecimal idPersonaNatural,
			TipoDocumento tipoDocumento, String numeroDocumento,
			String apellidoPaterno, String apellidoMaterno, String nombres,
			Date fechaNacimiento, String sexo, String estadoCivil,
			String ocupacion, String direccion, String referencia,
			String telefono, String celular, String email, String ubigeo,
			String codigoPais, Set titulars, Set personaJuridicas,
			Set accionistas, Set socios, Set trabajadors) {
		this.idPersonaNatural = idPersonaNatural;
		this.tipoDocumento = tipoDocumento;
		this.numeroDocumento = numeroDocumento;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.nombres = nombres;
		this.fechaNacimiento = fechaNacimiento;
		this.sexo = sexo;
		this.estadoCivil = estadoCivil;
		this.ocupacion = ocupacion;
		this.direccion = direccion;
		this.referencia = referencia;
		this.telefono = telefono;
		this.celular = celular;
		this.email = email;
		this.ubigeo = ubigeo;
		this.codigoPais = codigoPais;
		this.titulars = titulars;
		this.personaJuridicas = personaJuridicas;
		this.accionistas = accionistas;
		this.socios = socios;
		this.trabajadors = trabajadors;
	}

	@XmlElement
	@Id
	@Column(name = "ID_PERSONA_NATURAL", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getIdPersonaNatural() {
		return this.idPersonaNatural;
	}

	public void setIdPersonaNatural(BigDecimal idPersonaNatural) {
		this.idPersonaNatural = idPersonaNatural;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TIPO_DOCUMENTO", nullable = false)
	public TipoDocumento getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	@XmlElement
	@Column(name = "NUMERO_DOCUMENTO", nullable = false, length = 40)
	public String getNumeroDocumento() {
		return this.numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	@XmlElement
	@Column(name = "APELLIDO_PATERNO", nullable = false, length = 120)
	public String getApellidoPaterno() {
		return this.apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	@XmlElement
	@Column(name = "APELLIDO_MATERNO", nullable = false, length = 120)
	public String getApellidoMaterno() {
		return this.apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	@XmlElement
	@Column(name = "NOMBRES", nullable = false, length = 140)
	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	@XmlElement
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_NACIMIENTO", nullable = false, length = 7)
	public Date getFechaNacimiento() {
		return this.fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	@XmlElement
	@Column(name = "SEXO", nullable = false, length = 20)
	public String getSexo() {
		return this.sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	@XmlElement
	@Column(name = "ESTADO_CIVIL", length = 20)
	public String getEstadoCivil() {
		return this.estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	@XmlElement
	@Column(name = "OCUPACION", length = 60)
	public String getOcupacion() {
		return this.ocupacion;
	}

	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	@XmlElement
	@Column(name = "DIRECCION", length = 140)
	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@XmlElement
	@Column(name = "REFERENCIA", length = 100)
	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	@XmlElement
	@Column(name = "TELEFONO", length = 40)
	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	@XmlElement
	@Column(name = "CELULAR", length = 40)
	public String getCelular() {
		return this.celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	@XmlElement
	@Column(name = "EMAIL", length = 140)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "UBIGEO", length = 12)
	public String getUbigeo() {
		return this.ubigeo;
	}

	public void setUbigeo(String ubigeo) {
		this.ubigeo = ubigeo;
	}

	@Column(name = "CODIGO_PAIS", length = 6)
	public String getCodigoPais() {
		return this.codigoPais;
	}

	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personaNatural")
	public Set<Titular> getTitulars() {
		return this.titulars;
	}

	public void setTitulars(Set titulars) {
		this.titulars = titulars;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personaNatural")
	public Set<PersonaJuridica> getPersonaJuridicas() {
		return this.personaJuridicas;
	}

	public void setPersonaJuridicas(Set personaJuridicas) {
		this.personaJuridicas = personaJuridicas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personaNatural")
	public Set<Accionista> getAccionistas() {
		return this.accionistas;
	}

	public void setAccionistas(Set accionistas) {
		this.accionistas = accionistas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personaNatural")
	public Set<Socio> getSocios() {
		return this.socios;
	}

	public void setSocios(Set socios) {
		this.socios = socios;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personaNatural")
	public Set<Trabajador> getTrabajadors() {
		return this.trabajadors;
	}

	public void setTrabajadors(Set trabajadors) {
		this.trabajadors = trabajadors;
	}

}
