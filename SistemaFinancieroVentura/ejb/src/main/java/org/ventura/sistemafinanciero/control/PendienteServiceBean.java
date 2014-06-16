package org.ventura.sistemafinanciero.control;

import java.math.BigInteger;
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
import org.ventura.sistemafinanciero.entity.BovedaCaja;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.PendienteCaja;
import org.ventura.sistemafinanciero.entity.dto.VoucherPendienteCaja;
import org.ventura.sistemafinanciero.service.PendienteService;

@Named
@Stateless
@Remote(PendienteService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PendienteServiceBean extends AbstractServiceBean<PendienteCaja> implements PendienteService {

	@Inject
	private DAO<Object, PendienteCaja> pendienteCajaDAO;
	
	@Override
	public PendienteCaja getPendiente(BigInteger id) {
		PendienteCaja pendiente = pendienteCajaDAO.find(id);
		Moneda moneda = pendiente.getMoneda();
		Hibernate.initialize(moneda);
		return pendiente;
	}	
	
	@Override
	protected DAO<Object, PendienteCaja> getDAO() {
		return this.pendienteCajaDAO;
	}
		
	@Override
	public VoucherPendienteCaja getVoucherPendienteCaja(BigInteger idPendienteCaja) {
		VoucherPendienteCaja voucherPendienteCaja = new VoucherPendienteCaja();		
		
		//recuperando pendiente
		PendienteCaja pendientecaja = pendienteCajaDAO.find(idPendienteCaja);
		Caja caja = pendientecaja.getHistorialCaja().getCaja();
		Set<BovedaCaja> list = caja.getBovedaCajas();
		Agencia agencia = null;
		for (BovedaCaja bovedaCaja : list) {
			agencia = bovedaCaja.getBoveda().getAgencia();
			break;
		}
			
		//poniendo los datos del pendiente
		voucherPendienteCaja.setAgenciaDenominacion(agencia.getDenominacion());
		voucherPendienteCaja.setAgenciaAbreviatura(agencia.getAbreviatura());
		voucherPendienteCaja.setCajaDenominacion(caja.getDenominacion());
		voucherPendienteCaja.setCajaAbreviatura(caja.getAbreviatura());
		voucherPendienteCaja.setIdPendienteCaja(idPendienteCaja);	
		voucherPendienteCaja.setMonto(pendientecaja.getMonto());
		voucherPendienteCaja.setObservacion(pendientecaja.getObservacion());
		voucherPendienteCaja.setFecha(pendientecaja.getFecha());
		voucherPendienteCaja.setHora(pendientecaja.getHora());
		voucherPendienteCaja.setTipoPendiente(pendientecaja.getTipoPendiente());
		voucherPendienteCaja.setTrabajador(pendientecaja.getTrabajador());
			
		Moneda moneda = pendientecaja.getMoneda();
		Hibernate.initialize(moneda);
		
		voucherPendienteCaja.setMoneda(moneda);
		
		return voucherPendienteCaja;
	}
}
