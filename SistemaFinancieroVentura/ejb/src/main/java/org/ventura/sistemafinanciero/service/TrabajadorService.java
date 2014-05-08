package org.ventura.sistemafinanciero.service;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;

@Remote
public interface TrabajadorService extends AbstractService<Trabajador>{

	public Trabajador findByUsuario(int idusuario);
	
	public Agencia getAgencia(int idTrabajador) throws NonexistentEntityException;

}
