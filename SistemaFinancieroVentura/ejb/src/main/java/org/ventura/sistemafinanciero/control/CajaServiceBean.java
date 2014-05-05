package org.ventura.sistemafinanciero.control;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.dao.QueryParameter;
import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.entity.BovedaCaja;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.DetalleHistorialCaja;
import org.ventura.sistemafinanciero.entity.HistorialCaja;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.MonedaDenominacion;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.TrabajadorCaja;
import org.ventura.sistemafinanciero.exception.IllegalResultException;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.service.CajaService;
import org.ventura.sistemafinanciero.service.MonedaService;

@Named
@Stateless
@Remote(CajaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CajaServiceBean extends AbstractServiceBean<Caja> implements CajaService {

	private Logger LOGGER = LoggerFactory.getLogger(CajaService.class);

	@Inject private DAO<Object, Caja> cajaDAO;
	@Inject private DAO<Object, HistorialCaja> historialCajaDAO;
	//@Inject private DAO<Object, Deta> historialCajaDAO;
	@Inject private DAO<Object, Trabajador> trabajadorDAO;
	
	@EJB private MonedaService monedaService;

	@Override
	public Caja findByTrabajador(int idTrabajador) throws NonexistentEntityException{
		Caja result = null;
		try {
			Trabajador trabajador = trabajadorDAO.find(idTrabajador);
			if(trabajador == null)
				throw new NonexistentEntityException("Trabajador no existente");
			Set<TrabajadorCaja> cajas = trabajador.getTrabajadorCajas();
			if(cajas.size() >= 2)
				throw new IllegalResultException("Trabajador tiene " + cajas.size() + " asignadas");
			for (TrabajadorCaja trabajadorCaja : cajas) {
				result = trabajadorCaja.getCaja();				
				break;
			}
		} catch (IllegalResultException e) {
			LOGGER.error(e.getMessage(), e.getLocalizedMessage(), e.getCause());
		}	
		Hibernate.initialize(result);
		return result;
	}

	@Override
	public HistorialCaja getHistorialActivo(int idCaja) throws NonexistentEntityException {
		HistorialCaja cajaHistorial = null;
		try {
			Caja caja = cajaDAO.find(idCaja);
			if(caja == null)
				throw new NonexistentEntityException("Caja no encontrada");
			
			QueryParameter queryParameter = QueryParameter.with("idcaja", idCaja);
			List<HistorialCaja> list = historialCajaDAO.findByNamedQuery(HistorialCaja.findByHistorialActivo, queryParameter.parameters());
			if(list.size() >= 2){
				throw new IllegalResultException("La caja tiene mas de un historial activo");
			} else {
				for (HistorialCaja c : list) {
					cajaHistorial = c;
				}
			}
		} catch (IllegalResultException e) {
			LOGGER.error(e.getMessage(), e.getLocalizedMessage(), e.getCause());
		}
		return cajaHistorial;
	}

	@Override
	public Map<Moneda, Set<DetalleHistorialCaja>> getDetalleCaja(int idCaja) throws NonexistentEntityException {
		Map<Moneda, Set<DetalleHistorialCaja>> result = null;			
		Caja caja = cajaDAO.find(idCaja);
		if (caja == null)
			throw new NonexistentEntityException("Caja no encontrada");

		HistorialCaja cajaHistorial = this.getHistorialActivo(idCaja);
		Set<BovedaCaja> bovedas = caja.getBovedaCajas();
		result = new HashMap<Moneda, Set<DetalleHistorialCaja>>();

		for (BovedaCaja bovedaCaja : bovedas) {
			Boveda boveda = bovedaCaja.getBoveda();
			Moneda moneda = boveda.getMoneda();

			Set<MonedaDenominacion> denominacionesExistentes = monedaService.getDenominaciones(moneda.getIdMoneda());
			TreeSet<DetalleHistorialCaja> detalleCaja;
			TreeSet<DetalleHistorialCaja> denominacionesCantidad = new TreeSet<DetalleHistorialCaja>();
			if (cajaHistorial != null)
				detalleCaja = (TreeSet<DetalleHistorialCaja>) getDetalleCajaByMoneda(cajaHistorial.getIdHistorialCaja(), moneda.getIdMoneda());
			else
				detalleCaja = new TreeSet<DetalleHistorialCaja>();
			for (MonedaDenominacion monedaDenominacion : denominacionesExistentes) {
				DetalleHistorialCaja detalle = new DetalleHistorialCaja();
				if (this.contains(detalleCaja, monedaDenominacion)) {
					DetalleHistorialCaja h = detalleCaja.tailSet(detalle, true).first();
					detalle.setCantidad(h.getCantidad());
					detalle.setMonedaDenominacion(monedaDenominacion);
				} else {
					detalle.setCantidad(0);
					detalle.setMonedaDenominacion(monedaDenominacion);
				}
				denominacionesCantidad.add(detalle);
			}
			result.put(moneda, denominacionesCantidad);
		}
		return result;
	}

	protected boolean contains(Collection<DetalleHistorialCaja> collection, MonedaDenominacion moneda) {
		boolean result = false;
		for (DetalleHistorialCaja cajaHistorialDetalle : collection) {
			MonedaDenominacion other = cajaHistorialDetalle.getMonedaDenominacion();
			if(moneda.equals(other))
				return true;
		}
		return result;
	}
	
	@Override
	public Set<DetalleHistorialCaja> getDetalleCajaByMoneda(int idHistorial, int idMoneda) throws NonexistentEntityException {
		TreeSet<DetalleHistorialCaja> result = null;
		HistorialCaja historial = historialCajaDAO.find(idHistorial);
		Moneda moneda = monedaService.findById(idMoneda);
		if (historial == null)
			throw new NonexistentEntityException("Caja Historial Detalle no encontrado");
		if (moneda == null)
			throw new NonexistentEntityException("Moneda no encontrada");
		Set<DetalleHistorialCaja> detalle = historial.getDetalleHistorialCajas();
		result = new TreeSet<DetalleHistorialCaja>();
		for (DetalleHistorialCaja cajaHistorialDetalle : detalle) {
			if (cajaHistorialDetalle.getMonedaDenominacion().getMoneda().equals(moneda)) {
				result.add(cajaHistorialDetalle);
			}
		}
		return result;
	}

	@Override
	public void abrirCaja(int idCaja) {
		/*Caja caja = cajaDAO.find(idCaja);
		if(!caja.getAbierto()){
			//throw 
		}
		Set<BovedaCaja> bovedaCajas = caja.getBovedaCajas();
		for (BovedaCaja bovedaCaja : bovedaCajas) {
			Boveda boveda = bovedaCaja.getBoveda();
			if(!boveda.getAbierto())
				throw new Exception();
		}
		
		HistorialCaja historialCaja = this.getHistorialActivo(caja.getIdCaja());		
		if(historialCaja != null){
			//detalleHistorialCaja = historialCaja.getDetalleHistorialCajas();
		} else {
			Calendar calendar = Calendar.getInstance();
			
			historialCaja = new HistorialCaja();
			historialCaja.setCaja(caja);
			historialCaja.setFechaApertura(calendar.getTime());
			historialCaja.setHoraApertura(calendar.getTime());
			historialCaja.setEstado(true);			
			historialCajaDAO.create(historialCaja);
			
			for (BovedaCaja bovedaCaja : bovedaCajas) {
				Moneda moneda = bovedaCaja.getBoveda().getMoneda();
				Set<MonedaDenominacion> denominaciones = monedaService.getDenominaciones(moneda.getIdMoneda());
				for (MonedaDenominacion monedaDenominacion : denominaciones) {
					DetalleHistorialCaja detalleHistorialCaja = new DetalleHistorialCaja();
					detalleHistorialCaja.setCantidad(0);
					detalleHistorialCaja.setHistorialCaja(historialCaja);
					detalleHistorialCaja.setMonedaDenominacion(monedaDenominacion);
					
				}
			}
		}*/
	}
	
	@Override
	protected DAO<Object, Caja> getDAO() {
		return this.cajaDAO;
	}

}
