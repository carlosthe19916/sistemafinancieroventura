package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.dto.VoucherCompraVenta;

@Remote
public interface CajaService extends AbstractService<Caja> {
	
	public Set<Boveda> getBovedasByCaja(BigInteger idCaja);
	
	public VoucherCompraVenta getVoucherCompraVenta(BigInteger idTransaccionCompraVenta);

}
