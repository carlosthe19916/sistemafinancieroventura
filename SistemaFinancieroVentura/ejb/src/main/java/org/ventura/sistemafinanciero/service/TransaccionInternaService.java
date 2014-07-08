package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.dto.VoucherTransaccionBovedaCaja;
import org.ventura.sistemafinanciero.entity.dto.VoucherTransaccionCajaCaja;


@Remote
public interface TransaccionInternaService {
	
	public VoucherTransaccionBovedaCaja getVoucherTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja);
	
	public VoucherTransaccionCajaCaja getVoucherTransaccionCajaCaja(BigInteger idTransaccionCajaCaja);
	
}
