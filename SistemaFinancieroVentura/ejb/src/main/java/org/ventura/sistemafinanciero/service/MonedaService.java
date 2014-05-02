package org.ventura.sistemafinanciero.service;

import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.MonedaDenominacion;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;

@Remote
public interface MonedaService extends AbstractService<Moneda> {
	
	public Set<MonedaDenominacion> getDenominaciones(int idMoneda) throws NonexistentEntityException;
	
}
