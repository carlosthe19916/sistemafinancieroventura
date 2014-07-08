package org.ventura.sistemafinanciero.control;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Hibernate;
import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.entity.BovedaCaja;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.TransaccionCompraVenta;
import org.ventura.sistemafinanciero.entity.dto.VoucherCompraVenta;
import org.ventura.sistemafinanciero.service.CajaService;

@Named
@Stateless
@Remote(CajaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CajaServiceBean extends AbstractServiceBean<Caja> implements CajaService {	
	
	@Inject private DAO<Object, Caja> cajaDAO;
	@Inject private DAO<Object, TransaccionCompraVenta> transaccionCopraVentaDAO;
	
	@Override
	public Set<Boveda> getBovedasByCaja(BigInteger idCaja) {
		Set<Boveda> result = null;
		Caja caja = cajaDAO.find(idCaja);
		if(caja != null){
			result = new HashSet<Boveda>();
			Set<BovedaCaja> bovedaCajas= caja.getBovedaCajas();
			for (BovedaCaja bovedaCaja : bovedaCajas) {
				Boveda boveda = bovedaCaja.getBoveda();
				Moneda moneda = boveda.getMoneda();				
				Hibernate.initialize(boveda);
				Hibernate.initialize(moneda);
				result.add(boveda);
			}
		}	
		return result;
	}

	@Override
	protected DAO<Object, Caja> getDAO() {
		return this.cajaDAO;
	}

	@Override
	public VoucherCompraVenta getVoucherCompraVenta( BigInteger idTransaccionCompraVenta) {
		
		VoucherCompraVenta voucherCompraVenta = new VoucherCompraVenta();
				
		// recuperando transaccion
		TransaccionCompraVenta compraVenta = transaccionCopraVentaDAO.find(idTransaccionCompraVenta);

		Caja caja = compraVenta.getHistorialCaja().getCaja();
		Set<BovedaCaja> list = caja.getBovedaCajas();
		Agencia agencia = null;
		for (BovedaCaja bovedaCaja : list) {
			agencia = bovedaCaja.getBoveda().getAgencia();
			break;
		}
						
		//Poniendo datos de transaccion
		Moneda monedaEntregada = compraVenta.getMonedaEntregada();
		Moneda monedaRecibida = compraVenta.getMonedaRecibida();
		Hibernate.initialize(monedaEntregada);
		Hibernate.initialize(monedaRecibida);
		
		voucherCompraVenta.setIdCompraVenta(compraVenta.getIdTransaccionCompraVenta());
		voucherCompraVenta.setFecha(compraVenta.getFecha());
		voucherCompraVenta.setHora(compraVenta.getHora());
		voucherCompraVenta.setMonedaEntregada(monedaEntregada);
		voucherCompraVenta.setMonedaRecibida(monedaRecibida);
		voucherCompraVenta.setMontoRecibido(compraVenta.getMontoRecibido());
		voucherCompraVenta.setMontoEntregado(compraVenta.getMontoEntregado());
		voucherCompraVenta.setEstado(compraVenta.getEstado());
		
		voucherCompraVenta.setNumeroOperacion(compraVenta.getNumeroOperacion());
		voucherCompraVenta.setObservacion(compraVenta.getObservacion());
		voucherCompraVenta.setReferencia(compraVenta.getReferencia());
		voucherCompraVenta.setTipoCambio(compraVenta.getTipoCambio());
		voucherCompraVenta.setTipoTransaccion(compraVenta.getTipoTransaccion());												
				
		//Poniendo datos de agencia		
		voucherCompraVenta.setAgenciaDenominacion(agencia.getDenominacion());
		voucherCompraVenta.setAgenciaAbreviatura(agencia.getAbreviatura());
				
		//Poniendo datos de caja
		voucherCompraVenta.setCajaDenominacion(caja.getDenominacion());
		voucherCompraVenta.setCajaAbreviatura(caja.getAbreviatura());
						
		return voucherCompraVenta;
	}
		
	
}
