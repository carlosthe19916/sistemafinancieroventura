package org.ventura.sistemafinanciero.service;

import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Boveda;


@Remote
public interface BovedaService extends AbstractService<Boveda> {

	public Set<Boveda> getBovedasByIdOficina(int idOficina);
		
}
