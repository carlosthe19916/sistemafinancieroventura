package org.ventura.sistemafinanciero.service;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Trabajador;

@Remote
public interface TrabajadorService extends AbstractService<Trabajador>{

	public Trabajador findByUsuario(int idusuario);

}
