package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.PendienteCaja;

@Remote
public interface PendienteService extends AbstractService<PendienteCaja> {	
	
	public PendienteCaja getPendiente(BigInteger id);
	
}
