package org.ventura.sistemafinanciero.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.DetalleHistorialCaja;
import org.ventura.sistemafinanciero.entity.HistorialCaja;
import org.ventura.sistemafinanciero.entity.TransaccionBovedaCaja;
import org.ventura.sistemafinanciero.entity.dto.CajaCierreMoneda;
import org.ventura.sistemafinanciero.entity.dto.GenericDetalle;
import org.ventura.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.ventura.sistemafinanciero.entity.dto.ResumenOperacionesCaja;
import org.ventura.sistemafinanciero.exception.IllegalResultException;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;

@Remote
public interface CajaService extends AbstractService<Caja> {
	
	//caja
	public HistorialCaja getHistorialActivo(int idCaja) throws NonexistentEntityException;
	public Set<Boveda> getBovedasByCaja(int idCaja);
	public Set<GenericMonedaDetalle> getDetalleCaja(int idCaja);
	public Set<DetalleHistorialCaja> getDetalleCajaByMoneda(int idHistorial, int idMoneda);

	//voucher caja
	public Set<CajaCierreMoneda> getVoucherCierreCaja(int caja, Date fechaApertura) throws NonexistentEntityException;
	
	public ResumenOperacionesCaja getResumenOperacionesCaja(int caja, Date fechaApertura) throws NonexistentEntityException, IllegalResultException;
	
	//historial caja
	public List<HistorialCaja> getHistorialCaja(int idCaja, Date desde, Date hasta);
	
	//transacciones
	public Set<TransaccionBovedaCaja> getTransaccionesEnviadasBovedaCaja(int idCaja, int idHistorialCaja);
	public Set<TransaccionBovedaCaja> getTransaccionesRecibidasBovedaCaja(int idCaja, int idHistorialCaja);
	 
	//transacciones
	public void abrirCaja(int idCaja) throws NonexistentEntityException , RollbackFailureException;
	public void cerrarCaja(int idCaja, Set<GenericMonedaDetalle> detalleCaja) throws NonexistentEntityException, RollbackFailureException;
	public void crearTransaccionBovedaCaja(int idBoveda, int idCaja, Set<GenericDetalle> detalleTransaccion)throws RollbackFailureException;
}
