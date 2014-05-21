package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;

@Remote
public interface TrabajadorService extends AbstractService<Trabajador>{

	public Trabajador findByUsuario(BigInteger idusuario);
	
	public Caja findByTrabajador(BigInteger idTrabajador) throws NonexistentEntityException;
	
	public Agencia getAgencia(BigInteger idTrabajador) throws NonexistentEntityException;

}
