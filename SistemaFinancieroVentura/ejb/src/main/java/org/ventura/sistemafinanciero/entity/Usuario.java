package org.ventura.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Usuario generated by hbm2java
 */
@Entity
@Table(name = "USUARIO", schema = "BDSISTEMAFINANCIERO")
public class Usuario implements java.io.Serializable {

	private BigDecimal idUsuario;
	private String username;
	private String password;
	private Set usuarioRols = new HashSet(0);
	private Set trabajadorUsuarios = new HashSet(0);

	public Usuario() {
	}

	public Usuario(BigDecimal idUsuario, String username) {
		this.idUsuario = idUsuario;
		this.username = username;
	}

	public Usuario(BigDecimal idUsuario, String username, String password,
			Set usuarioRols, Set trabajadorUsuarios) {
		this.idUsuario = idUsuario;
		this.username = username;
		this.password = password;
		this.usuarioRols = usuarioRols;
		this.trabajadorUsuarios = trabajadorUsuarios;
	}

	@Id
	@Column(name = "ID_USUARIO", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(BigDecimal idUsuario) {
		this.idUsuario = idUsuario;
	}

	@Column(name = "USERNAME", nullable = false, length = 60)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "PASSWORD", length = 140)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<UsuarioRol> getUsuarioRols() {
		return this.usuarioRols;
	}

	public void setUsuarioRols(Set usuarioRols) {
		this.usuarioRols = usuarioRols;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<TrabajadorUsuario> getTrabajadorUsuarios() {
		return this.trabajadorUsuarios;
	}

	public void setTrabajadorUsuarios(Set trabajadorUsuarios) {
		this.trabajadorUsuarios = trabajadorUsuarios;
	}

}
