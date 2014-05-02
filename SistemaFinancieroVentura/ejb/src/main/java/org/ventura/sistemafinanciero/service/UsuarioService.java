package org.ventura.sistemafinanciero.service;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Usuario;

@Remote
public interface UsuarioService extends AbstractService<Usuario>{

	public Usuario findByUsername(String username);

}
