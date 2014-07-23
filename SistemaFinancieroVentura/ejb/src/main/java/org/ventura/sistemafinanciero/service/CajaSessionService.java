package org.ventura.sistemafinanciero.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Beneficiario;
import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.HistorialCaja;
import org.ventura.sistemafinanciero.entity.HistorialTransaccionCaja;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.PendienteCaja;
import org.ventura.sistemafinanciero.entity.TransaccionBovedaCajaView;
import org.ventura.sistemafinanciero.entity.TransaccionCajaCaja;
import org.ventura.sistemafinanciero.entity.dto.GenericDetalle;
import org.ventura.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.entity.type.Tipotransaccioncompraventa;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;


@Remote
public interface CajaSessionService extends AbstractService<Caja> {	
	
	public Set<Moneda> getMonedas();
	public Set<GenericMonedaDetalle> getDetalleCaja();
	public List<TransaccionBovedaCajaView> getTransaccionesEnviadasBovedaCaja();
	public List<TransaccionBovedaCajaView> getTransaccionesRecibidasBovedaCaja();
	public Set<TransaccionCajaCaja> getTransaccionesEnviadasCajaCaja();
	public Set<TransaccionCajaCaja> getTransaccionesRecibidasCajaCaja();
	public Set<PendienteCaja> getPendientesCaja();
	public Set<HistorialCaja> getHistorialCaja(Date dateDesde, Date dateHasta);
	public List<HistorialTransaccionCaja> getHistorialTransaccion();
	
	public void cancelarTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja) throws RollbackFailureException;
	public void confirmarTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja)throws RollbackFailureException;
	
	//transacciones
	public BigInteger abrirCaja() throws RollbackFailureException;
	public Map<Boveda, BigDecimal> getDiferenciaSaldoCaja(Set<GenericMonedaDetalle> detalleCaja);
	public BigInteger cerrarCaja(Set<GenericMonedaDetalle> detalleCaja) throws RollbackFailureException;
	public BigInteger crearPendiente(BigInteger idBoveda, BigDecimal monto, String observacion) throws RollbackFailureException;
	public BigInteger crearTransaccionBovedaCaja(BigInteger idBoveda, Set<GenericDetalle> detalleTransaccion) throws RollbackFailureException;	
	public BigInteger crearTransaccionCajaCaja(BigInteger idCajadestino,BigInteger idMoneda, BigDecimal monto, String observacion) throws RollbackFailureException;	
	
	public BigInteger crearDepositoBancario(String numeroCuenta, BigDecimal monto, String referencia) throws RollbackFailureException;
	public BigInteger crearRetiroBancario(String numeroCuenta, BigDecimal monto, String referencia) throws RollbackFailureException;
	public BigInteger crearCompraVenta(Tipotransaccioncompraventa tipoTransaccion,BigInteger idMonedaRecibido, BigInteger idMonedaEntregado, BigDecimal montoRecibido, BigDecimal montoEntregado, BigDecimal tasaCambio, String referencia) throws RollbackFailureException;
	
	public BigInteger crearAporte(BigInteger idSocio, BigDecimal monto, int mes, int anio, String referencia) throws RollbackFailureException;
	public BigInteger retiroCuentaAporte(BigInteger idSocio) throws RollbackFailureException;
	public BigInteger crearTransferenciaBancaria(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal monto, String referencia) throws RollbackFailureException;
	
	public BigInteger[] crearCuentaBancariaPlazoFijoConDeposito(String codigo, BigInteger idMoneda, TipoPersona tipoPersona, BigInteger idPersona, int cantRetirantes, BigDecimal monto, int periodo, BigDecimal tasaInteres, List<BigInteger> titulares, List<Beneficiario> beneficiarios) throws RollbackFailureException;
	public BigInteger cancelarCuentaBancariaConRetiro(BigInteger id) throws RollbackFailureException;
	public BigInteger cancelarSocioConRetiro(BigInteger idSocio) throws RollbackFailureException;
	
	public void extornarTransaccion(BigInteger idTransaccion) throws RollbackFailureException;
	public List<HistorialTransaccionCaja> findAllView(String filterText) throws RollbackFailureException;
}
