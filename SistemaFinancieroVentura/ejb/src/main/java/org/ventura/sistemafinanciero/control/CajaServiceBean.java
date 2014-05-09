package org.ventura.sistemafinanciero.control;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import org.ventura.sistemafinanciero.entity.dto.GenericDetalle;
import org.ventura.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.ventura.sistemafinanciero.exception.IllegalResultException;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.CajaService;
import org.ventura.sistemafinanciero.service.MonedaService;

@Named
@Stateless
@Remote(CajaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CajaServiceBean extends AbstractServiceBean<Caja> implements CajaService {

	private Logger LOGGER = LoggerFactory.getLogger(CajaService.class);

	@PersistenceContext
	private EntityManager em;
	
	@Inject private DAO<Object, Caja> cajaDAO;
	@Inject private DAO<Object, HistorialCaja> historialCajaDAO;
	@Inject private DAO<Object, DetalleHistorialCaja> detalleHistorialCajaDAO;
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
				Hibernate.initialize(result);
				break;
			}
		} catch (IllegalResultException e) {
			LOGGER.error(e.getMessage(), e.getLocalizedMessage(), e.getCause());
		}			
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
	public Set<GenericMonedaDetalle> getDetalleCaja(int idCaja)throws NonexistentEntityException {
		Set<GenericMonedaDetalle> result = null;			
		Caja caja = cajaDAO.find(idCaja);
		if (caja == null)
			throw new NonexistentEntityException("Caja no encontrada");

		HistorialCaja cajaHistorial = this.getHistorialActivo(idCaja);
		Set<BovedaCaja> bovedas = caja.getBovedaCajas();
		result = new HashSet<GenericMonedaDetalle>();
				
		//recorrer por todas las bovedas
		for (BovedaCaja bovedaCaja : bovedas) {
			Boveda boveda = bovedaCaja.getBoveda();
			Moneda moneda = boveda.getMoneda();

			GenericMonedaDetalle genericMonedaDetalle = new GenericMonedaDetalle(moneda);
			
			//recorrer todas las denominaciones existentes en la base de datos
			Set<MonedaDenominacion> denominacionesExistentes = monedaService.getDenominaciones(moneda.getIdMoneda());	
			for (MonedaDenominacion m : denominacionesExistentes) {
				GenericDetalle detalle = new GenericDetalle(m.getValor(), BigInteger.ZERO);
				genericMonedaDetalle.addElementDetalleReplacingIfExist(detalle);
			}	
			
			//si tiene historiales activos reemplazar por cantidades
			if(cajaHistorial != null){
				for (DetalleHistorialCaja d : cajaHistorial.getDetalleHistorialCajas()) {
					GenericDetalle detalle = new GenericDetalle(d.getMonedaDenominacion().getValor(), d.getCantidad());
					genericMonedaDetalle.addElementDetalleReplacingIfExist(detalle);
				};
			}			
			result.add(genericMonedaDetalle);
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
	public void abrirCaja(int idCaja) throws NonexistentEntityException, RollbackFailureException {
		Caja caja = cajaDAO.find(idCaja);
		if(caja.getAbierto()){
			throw new RollbackFailureException("Caja abierta, imposible abrirla nuevamente");
		}
		Set<BovedaCaja> bovedaCajas = caja.getBovedaCajas();
		for (BovedaCaja bovedaCaja : bovedaCajas) {
			Boveda boveda = bovedaCaja.getBoveda();
			if(!boveda.getAbierto())
				throw new RollbackFailureException("Debe de abrir las bovedas asociadas a la caja("+boveda.getDenominacion()+")");
		}
		
		try {
			//abriendo caja
			caja.setAbierto(true);
			caja.setEstadoMovimiento(true);
			cajaDAO.update(caja);
			
			HistorialCaja historialCajaOld = this.getHistorialActivo(caja.getIdCaja());
			if(historialCajaOld != null) {
				historialCajaOld.setEstado(false);
				historialCajaDAO.update(historialCajaOld);				
			}
			
			Calendar calendar = Calendar.getInstance();	
			HistorialCaja historialCajaNew = new HistorialCaja();
			historialCajaNew.setCaja(caja);
			historialCajaNew.setFechaApertura(calendar.getTime());
			historialCajaNew.setHoraApertura(calendar.getTime());
			historialCajaNew.setEstado(true);			
			historialCajaDAO.create(historialCajaNew);
			
			if(historialCajaOld != null){
				Set<DetalleHistorialCaja> detalleHistorialCajas = historialCajaNew.getDetalleHistorialCajas();
				for (DetalleHistorialCaja detalleHistorialCaja : detalleHistorialCajas) {
					em.detach(detalleHistorialCaja);
					detalleHistorialCaja.setHistorialCaja(historialCajaNew);
					detalleHistorialCajaDAO.create(detalleHistorialCaja);
				}
			} else {
				for (BovedaCaja bovedaCaja : bovedaCajas) {
					Moneda moneda = bovedaCaja.getBoveda().getMoneda();
					Set<MonedaDenominacion> denominaciones = monedaService.getDenominaciones(moneda.getIdMoneda());
					for (MonedaDenominacion monedaDenominacion : denominaciones) {
						DetalleHistorialCaja detalleHistorialCaja = new DetalleHistorialCaja();						
						detalleHistorialCaja.setCantidad(BigInteger.ZERO);
						detalleHistorialCaja.setHistorialCaja(historialCajaNew);
						detalleHistorialCaja.setMonedaDenominacion(monedaDenominacion);
						detalleHistorialCajaDAO.create(detalleHistorialCaja);
					}
				}
			}
		} catch (Exception e) {
			throw new EJBException();
		}		
	}

	@Override
	public void cerrarCaja(int idCaja, Set<GenericMonedaDetalle> detalleCaja) throws NonexistentEntityException, RollbackFailureException {
		Caja caja = cajaDAO.find(idCaja);

		Set<BovedaCaja> bovedas = caja.getBovedaCajas();
		for (BovedaCaja bovedaCaja : bovedas) {
			Moneda moneda = bovedaCaja.getBoveda().getMoneda();
			for (GenericMonedaDetalle detalle : detalleCaja) {
				if (moneda.equals(detalle.getMoneda())) {
					if (bovedaCaja.getSaldo().compareTo(detalle.getTotal()) != 0) {
						throw new RollbackFailureException("El detalle enviado y el saldo en boveda no coinciden");
					}
					break;
				}
			}
		}
		try {
			// abriendo caja
			caja.setAbierto(false);
			caja.setEstadoMovimiento(false);
			cajaDAO.update(caja);

			Calendar calendar = Calendar.getInstance();
			HistorialCaja historialCaja = this.getHistorialActivo(caja.getIdCaja());
			historialCaja.setEstado(true);
			historialCaja.setFechaCierre(calendar.getTime());
			historialCaja.setHoraCierre(calendar.getTime());
			historialCajaDAO.update(historialCaja);
			
			//modificando el detalleCaja
			if(bovedas.size() == detalleCaja.size()){				
				for (BovedaCaja bovedaCaja : bovedas) {
					Moneda monedaBoveda = bovedaCaja.getBoveda().getMoneda();				
					for (GenericMonedaDetalle detalle : detalleCaja) {
						Moneda monedaUsuario = detalle.getMoneda();
						if(monedaBoveda.equals(monedaUsuario)){
							
							Set<DetalleHistorialCaja> detHistCaja = historialCaja.getDetalleHistorialCajas();
							Set<GenericDetalle> genDet= detalle.getDetalle();
							for (DetalleHistorialCaja dhc : detHistCaja) {
								MonedaDenominacion monedaDenom =  dhc.getMonedaDenominacion();
								BigDecimal valorMonedaDenom = monedaDenom.getValor();
								for (GenericDetalle genericDetalle : genDet) {
									if(genericDetalle.getValor().compareTo(valorMonedaDenom) == 0 && monedaDenom.getMoneda().equals(monedaUsuario)){										
										dhc.setCantidad(genericDetalle.getCantidad());
										detalleHistorialCajaDAO.update(dhc);
									}
								}
							}
							break;
						}
					}
				}
			} else {
				throw new RollbackFailureException("El numero de bovedas enviadas no coincide con el numero en base de datos");
			}	
		} catch (Exception e) {
			throw new EJBException();
		}
	}
	
	@Override
	protected DAO<Object, Caja> getDAO() {
		return this.cajaDAO;
	}

}
