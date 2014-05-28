package org.ventura.sistemafinanciero.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Beneficiario;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;

@Remote
public interface CuentaBancariaService extends AbstractService<CuentaBancaria>{

	Set<CuentaBancaria> findByFilterText(String filterText);

	BigInteger createCuentaAhorro(BigInteger idMoneda, TipoPersona tipoPersona, BigInteger idPersona, int cantRetirantes, List<BigInteger> titulares, List<Beneficiario> beneficiarios) throws RollbackFailureException;

	BigInteger createCuentaCorriente(BigInteger idMoneda, TipoPersona tipoPersona,
			BigInteger idPersona, int cantRetirantes,
			List<BigInteger> titulares, List<Beneficiario> beneficiarios) throws RollbackFailureException;

	BigInteger createCuentaPlazoFijo(BigInteger idMoneda, TipoPersona tipoPersona,
			BigInteger idPersona, int cantRetirantes, BigDecimal monto, int periodo, BigDecimal tasaInteres,
			List<BigInteger> titulares, List<Beneficiario> beneficiarios) throws RollbackFailureException;

	
}
