package org.ventura.sistemafinanciero.service;

import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.MonedaDenominacion;
import org.ventura.sistemafinanciero.entity.dto.GenericDetalle;

@Remote
public interface MonedaService extends AbstractService<Moneda> {
	
	public Moneda findByDenominacion(String denominacion);
	
	public Moneda findBySimbolo(String simbolo);
	
	public List<MonedaDenominacion> getDenominaciones(int idMoneda);
	
	public Set<GenericDetalle> getGenericDenominaciones(int idMoneda);
	
}
