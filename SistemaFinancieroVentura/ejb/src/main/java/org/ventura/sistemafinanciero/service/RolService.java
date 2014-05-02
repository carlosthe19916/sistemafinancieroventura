package org.ventura.sistemafinanciero.service;

import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Rol;

@Remote
public interface RolService extends AbstractService<Rol>{

	public Set<Rol> getRolesByUsuario(int idUsuario) throws Exception;

}
