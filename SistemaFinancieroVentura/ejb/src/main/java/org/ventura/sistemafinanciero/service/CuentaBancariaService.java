package org.ventura.sistemafinanciero.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Beneficiario;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.CuentaBancariaView;
import org.ventura.sistemafinanciero.entity.EstadocuentaBancariaView;
import org.ventura.sistemafinanciero.entity.Titular;
import org.ventura.sistemafinanciero.entity.dto.VoucherTransaccionBancaria;
import org.ventura.sistemafinanciero.entity.dto.VoucherTransferenciaBancaria;
import org.ventura.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;

@Remote
public interface CuentaBancariaService extends AbstractService<CuentaBancaria> {	
	
	public CuentaBancariaView findView(BigInteger idCuentaBancaria);
	
	/**
     * Devuelve la lista de cuentas bancarias que cumplan con los datos 
     * de condiciones enviados
     *
     * @return List<CuentaBancariaView> lista de cuentas bancarias.
     */
	public List<CuentaBancariaView> findAllView();
	
	/**
     * Devuelve la lista de cuentas bancarias que cumplan con los datos 
     * de condiciones enviados
     *
     * @param  tipoCuenta TipoCuentaBancaria[] lista de tipos de cuentas bancarias.
     * @return List<CuentaBancariaView> lista de cuentas bancarias.
     */
	public List<CuentaBancariaView> findAllView(TipoCuentaBancaria[] tipoCuenta);
	
	/**
     * Devuelve la lista de cuentas bancarias que cumplan con los datos 
     * de condiciones enviados
     *
     * @param  tipoCuenta TipoCuentaBancaria[] lista de tipos de cuentas bancarias.
     * @param  persona TipoPersona[] para indicar el tipo de personas incluidas en el resultado. 
     * @return List<CuentaBancariaView> lista de cuentas bancarias.
     */
	public List<CuentaBancariaView> findAllView(TipoCuentaBancaria[] tipoCuenta, TipoPersona[] persona);
	
	/**
     * Devuelve la lista de cuentas bancarias que cumplan con los datos 
     * de condiciones enviados
     *
     * @param  tipoCuenta TipoCuentaBancaria[] lista de tipos de cuentas bancarias.
     * @param  persona TipoPersona[] para indicar el tipo de personas incluidas en el resultado.
     * @param  estadoCuenta EstadoCuentaBancaria[] para indicar el tipo de estado de cuenta en el resultado  
     * @return List<CuentaBancariaView> lista de cuentas bancarias.
     */
	public List<CuentaBancariaView> findAllView(TipoCuentaBancaria[] tipoCuenta, TipoPersona[] persona, EstadoCuentaBancaria[] estadoCuenta);
			
	/**
     * Devuelve la lista de cuentas bancarias que cumplan con los datos 
     * de condiciones enviados
     *
     * @param  tipoCuenta TipoCuentaBancaria[] lista de tipos de cuentas bancarias.
     * @param  persona TipoPersona[] para indicar el tipo de personas incluidas en el resultado.
     * @param  estadoCuenta EstadoCuentaBancaria[] para indicar el tipo de estado de cuenta en el resultado  
     * @param  range BigInteger[] para indicar el rango de resultados.   
     * @return List<CuentaBancariaView> lista de cuentas bancarias.
     */
	public List<CuentaBancariaView> findAllView(TipoCuentaBancaria[] tipoCuenta, TipoPersona[] persona, EstadoCuentaBancaria[] estadoCuenta, BigInteger offset, BigInteger limit);
	
	/**
     * Devuelve la lista de cuentas bancarias que cumplan con los datos 
     * de condiciones enviados
     * @param  filterText String texto a buscar.
     * @return List<CuentaBancariaView> lista de cuentas bancarias.
     */
	public List<CuentaBancariaView> findAllView(String filterText);
	
	/**
     * Devuelve la lista de cuentas bancarias que cumplan con los datos 
     * de condiciones enviados
     *
     * @param  filterText String texto a buscar.
     * @param  tipoCuenta TipoCuentaBancaria[] lista de tipos de cuentas bancarias.
     * @return List<CuentaBancariaView> lista de cuentas bancarias.
     */
	public List<CuentaBancariaView> findAllView(String filterText, TipoCuentaBancaria[] tipoCuenta);
	
	/**
     * Devuelve la lista de cuentas bancarias que cumplan con los datos 
     * de condiciones enviados
     *
     * @param  filterText String texto a buscar.
     * @param  tipoCuenta TipoCuentaBancaria[] lista de tipos de cuentas bancarias.
     * @param  persona TipoPersona[] para indicar el tipo de personas incluidas en el resultado. 
     * @return List<CuentaBancariaView> lista de cuentas bancarias.
     */
	public List<CuentaBancariaView> findAllView(String filterText, TipoCuentaBancaria[] tipoCuenta, TipoPersona[] persona);
	
	/**
     * Devuelve la lista de cuentas bancarias que cumplan con los datos 
     * de condiciones enviados
     *
     * @param  filterText String texto a buscar.
     * @param  tipoCuenta TipoCuentaBancaria[] lista de tipos de cuentas bancarias.
     * @param  persona TipoPersona[] para indicar el tipo de personas incluidas en el resultado.
     * @param  estadoCuenta EstadoCuentaBancaria[] para indicar el tipo de estado de cuenta en el resultado  
     * @return List<CuentaBancariaView> lista de cuentas bancarias.
     */
	public List<CuentaBancariaView> findAllView(String filterText, TipoCuentaBancaria[] tipoCuenta, TipoPersona[] persona, EstadoCuentaBancaria[] estadoCuenta);
	
	/**
     * Devuelve la lista de cuentas bancarias que cumplan con los datos 
     * de condiciones enviados
     *
     * @param  filterText String texto a buscar.
     * @param  tipoCuenta TipoCuentaBancaria[] lista de tipos de cuentas bancarias.
     * @param  persona TipoPersona[] para indicar el tipo de personas incluidas en el resultado.
     * @param  estadoCuenta EstadoCuentaBancaria[] para indicar el tipo de estado de cuenta en el resultado  
     * @param  range BigInteger[] para indicar el rango de resultados.   
     * @return List<CuentaBancariaView> lista de cuentas bancarias.
     */
	public List<CuentaBancariaView> findAllView(String filterText, TipoCuentaBancaria[] tipoCuenta, TipoPersona[] persona, EstadoCuentaBancaria[] estadoCuenta, BigInteger offset, BigInteger limit);		
		
	public BigInteger crearCuentaBancaria(TipoCuentaBancaria tipoCuentaBancaria,String codigoAgencia, BigInteger idMoneda, BigDecimal tasaInteres, TipoPersona tipoPersona,  BigInteger idPersona,Integer periodo, int cantRetirantes, List<BigInteger> titulares, List<Beneficiario> beneficiarios) throws RollbackFailureException;	
	
	public void congelarCuentaBancaria(BigInteger idCuentaBancaria) throws RollbackFailureException;
	
	public void descongelarCuentaBancaria(BigInteger idCuentaBancaria) throws RollbackFailureException;
	
	public void recalcularCuentaPlazoFijo(BigInteger idCuenta, int periodo, BigDecimal tasaInteres) throws RollbackFailureException;
	
	public BigInteger[] renovarCuentaPlazoFijo(BigInteger idCuenta, int periodo, BigDecimal tasaInteres) throws RollbackFailureException;
	
	public void capitalizarCuenta(BigInteger idCuentaBancaria) throws RollbackFailureException;
	
	public void cancelarCuentaBancaria(BigInteger id) throws RollbackFailureException;		
						
	public Set<Titular> getTitulares(BigInteger idCuentaBancaria, boolean mode);
	
	public Set<Beneficiario> getBeneficiarios(BigInteger idCuentaBancaria);

	public BigInteger addBeneficiario(BigInteger id, Beneficiario beneficiario) throws RollbackFailureException;
	
	public BigInteger addTitular(BigInteger idCuenta, Titular titular) throws RollbackFailureException;
	
	public VoucherTransaccionBancaria getVoucherCuentaBancaria(BigInteger idTransaccionBancaria);
	
	public VoucherTransferenciaBancaria getVoucherTransferenciaBancaria(BigInteger idTransferencia);

	public List<EstadocuentaBancariaView> getEstadoCuenta(BigInteger idCuenta, Date dateDesde, Date dateHasta);

	public CuentaBancariaView find(String numeroCuenta);
	
	public Agencia getAgencia(BigInteger idCuentaBancaria);

}
