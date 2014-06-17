package org.ventura.sistemafinanciero.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Beneficiario;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.CuentaBancariaView;
import org.ventura.sistemafinanciero.entity.EstadocuentaBancariaView;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.Titular;
import org.ventura.sistemafinanciero.entity.dto.VoucherTransaccionBancaria;
import org.ventura.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;

@Remote
public interface CuentaBancariaService extends AbstractService<CuentaBancaria> {

	public Set<CuentaBancaria> findByFilterText(String filterText);
	
	public Set<CuentaBancariaView> findAllView();
	public List<CuentaBancariaView> findAllView(List<TipoPersona> tipoPersonaList, List<TipoCuentaBancaria> tipoCuentaList,
			List<EstadoCuentaBancaria> estadoCuentaList, List<Moneda> monedaList);

	public Set<CuentaBancariaView> findByFilterTextView(String filterText);

	public BigInteger createCuentaAhorro(BigInteger idAgencia,
			BigInteger idMoneda, BigDecimal tasaInteres, TipoPersona tipoPersona, 
			BigInteger idPersona, int cantRetirantes, List<BigInteger> titulares,
			List<Beneficiario> beneficiarios) throws RollbackFailureException;

	public BigInteger createCuentaCorriente(BigInteger idAgencia, 
			BigInteger idMoneda, BigDecimal tasaInteres, TipoPersona tipoPersona,
			BigInteger idPersona, int cantRetirantes, List<BigInteger> titulares, 
			List<Beneficiario> beneficiarios) throws RollbackFailureException;

	public BigInteger[] createCuentaPlazoFijo(BigInteger idAgencia, BigInteger idMoneda,
			TipoPersona tipoPersona, BigInteger idPersona, int cantRetirantes,
			BigDecimal monto, int periodo, BigDecimal tasaInteres,
			List<BigInteger> titulares, List<Beneficiario> beneficiarios)
			throws RollbackFailureException;

	/**mode representa titulares activos/inacativos o todos*/
	public Set<Titular> getTitulares(BigInteger idCuentaBancaria, boolean mode);
	public Set<Beneficiario> getBeneficiarios(BigInteger idCuentaBancaria);

	public BigInteger addBeneficiario(BigInteger id, Beneficiario beneficiario) throws RollbackFailureException;
	
	public BigInteger addTitular(BigInteger idCuenta, Titular titular) throws RollbackFailureException;
	
	public VoucherTransaccionBancaria getVoucherCuentaBancaria(BigInteger idTransaccionBancaria);

	public List<EstadocuentaBancariaView> getEstadoCuenta(BigInteger idCuenta, Date dateDesde, Date dateHasta);

}
