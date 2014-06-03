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
public interface CuentaBancariaService extends AbstractService<CuentaBancaria> {

	public Set<CuentaBancaria> findByFilterText(String filterText);

	public BigInteger createCuentaAhorro(BigInteger idAgencia,
			BigInteger idMoneda, TipoPersona tipoPersona, BigInteger idPersona,
			int cantRetirantes, List<BigInteger> titulares,
			List<Beneficiario> beneficiarios) throws RollbackFailureException;

	public BigInteger createCuentaCorriente(BigInteger idAgencia, BigInteger idMoneda,
			TipoPersona tipoPersona, BigInteger idPersona, int cantRetirantes,
			List<BigInteger> titulares, List<Beneficiario> beneficiarios)
			throws RollbackFailureException;

	public BigInteger createCuentaPlazoFijo(BigInteger idAgencia, BigInteger idMoneda,
			TipoPersona tipoPersona, BigInteger idPersona, int cantRetirantes,
			BigDecimal monto, int periodo, BigDecimal tasaInteres,
			List<BigInteger> titulares, List<Beneficiario> beneficiarios)
			throws RollbackFailureException;

}
