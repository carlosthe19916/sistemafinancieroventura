package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Caja;


@Remote
public interface AgenciaService extends AbstractService<Agencia> {
	
	public Set<Caja> getCajas(BigInteger idAgencia);
	
}
