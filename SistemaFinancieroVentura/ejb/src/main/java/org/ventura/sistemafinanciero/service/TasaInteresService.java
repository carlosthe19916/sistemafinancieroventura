package org.ventura.sistemafinanciero.service;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.TasaInteres;

@Remote
public interface TasaInteresService extends AbstractService<TasaInteres>{

	public BigDecimal getTasaInteresCuentaPlazoFijo(BigInteger idMoneda, int periodo, BigDecimal monto);

	public BigDecimal getTasaInteresCuentaAhorro(BigInteger idMoneda);

	public BigDecimal getTasaInteresCuentaCorriente(BigInteger idMoneda);

	public BigDecimal getInteresGenerado(BigDecimal monto, int periodo, BigDecimal tasaInteres);

}
