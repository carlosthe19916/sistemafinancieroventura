package org.ventura.sistemafinanciero.service;

import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.DetalleHistorialCaja;
import org.ventura.sistemafinanciero.entity.HistorialCaja;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.Socio;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;

@Remote
public interface CajaService extends AbstractService<Caja> {
	
	public Caja findByTrabajador(int idTrabajador) throws NonexistentEntityException;
	
	public HistorialCaja getHistorialActivo(int idCaja) throws NonexistentEntityException;
	
	public Map<Moneda, Set<DetalleHistorialCaja>> getDetalleCaja(int idCaja) throws NonexistentEntityException;
	
	public Set<DetalleHistorialCaja> getDetalleCajaByMoneda(int idHistorial, int idMoneda) throws NonexistentEntityException;

	
	public void abrirCaja(int idCaja);
	
}
