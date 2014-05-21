package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.HistorialCaja;
import org.ventura.sistemafinanciero.entity.dto.CajaCierreMoneda;
import org.ventura.sistemafinanciero.entity.dto.ResumenOperacionesCaja;


@Remote
public interface HistorialCajaService extends AbstractService<HistorialCaja> {
	
	public Set<CajaCierreMoneda> getVoucherCierreCaja(BigInteger idHistorial);
	public ResumenOperacionesCaja getResumenOperacionesCaja(BigInteger idHistorial);
	
}
