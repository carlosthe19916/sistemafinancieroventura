package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.MonedaDenominacion;
import org.ventura.sistemafinanciero.entity.dto.GenericDetalle;

@Remote
public interface MonedaService extends AbstractService<Moneda> {
	
	public List<MonedaDenominacion> getDenominaciones(BigInteger idMoneda);
	
	public Set<GenericDetalle> getGenericDenominaciones(BigInteger idMoneda);
	
}
