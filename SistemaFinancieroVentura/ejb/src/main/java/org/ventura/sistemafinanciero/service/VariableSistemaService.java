package org.ventura.sistemafinanciero.service;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.entity.VariableSistema;
import org.ventura.sistemafinanciero.entity.type.Variable;


@Remote
public interface VariableSistemaService extends AbstractService<VariableSistema> {

	public VariableSistema find(Variable variable);

	public BigDecimal getTasaCambio(BigInteger idMonedaRecibida, BigInteger idMonedaEntregada);
	
}
