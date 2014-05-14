package org.ventura.sistemafinanciero.control;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javassist.NotFoundException;

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
import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.entity.BovedaCaja;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.DetalleHistorialCaja;
import org.ventura.sistemafinanciero.entity.HistorialCaja;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.MonedaDenominacion;
import org.ventura.sistemafinanciero.entity.PendienteCaja;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.TrabajadorCaja;
import org.ventura.sistemafinanciero.entity.TransaccionBancaria;
import org.ventura.sistemafinanciero.entity.TransaccionBovedaCaja;
import org.ventura.sistemafinanciero.entity.TransaccionCajaCaja;
import org.ventura.sistemafinanciero.entity.TransaccionCompraVenta;
import org.ventura.sistemafinanciero.entity.TransaccionCuentaAporte;
import org.ventura.sistemafinanciero.entity.VariableSistema;
import org.ventura.sistemafinanciero.entity.dto.CajaCierreMoneda;
import org.ventura.sistemafinanciero.entity.dto.GenericDetalle;
import org.ventura.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.ventura.sistemafinanciero.entity.dto.ResumenOperacionesCaja;
import org.ventura.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoPendiente;
import org.ventura.sistemafinanciero.entity.type.Tipotransaccionbancaria;
import org.ventura.sistemafinanciero.entity.type.Tipotransaccioncompraventa;
import org.ventura.sistemafinanciero.entity.type.TransaccionBovedaCajaOrigen;
import org.ventura.sistemafinanciero.entity.type.Variable;
import org.ventura.sistemafinanciero.exception.IllegalResultException;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.CajaService;
import org.ventura.sistemafinanciero.service.MonedaService;
import org.ventura.sistemafinanciero.service.VariableSistemaService;
import org.ventura.sistemafinanciero.util.EntityManagerProducer;

@Named
@Stateless
@Remote(CajaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CajaServiceBean extends AbstractServiceBean<Caja> implements CajaService {

	private Logger LOGGER = LoggerFactory.getLogger(CajaService.class);

	@Inject
	private EntityManagerProducer em;
	
	@Inject private DAO<Object, Caja> cajaDAO;
	@Inject private DAO<Object, HistorialCaja> historialCajaDAO;
	@Inject private DAO<Object, DetalleHistorialCaja> detalleHistorialCajaDAO;
	
	@EJB private VariableSistemaService variableSistemaService;
	@EJB private MonedaService monedaService;

	@Override
	public HistorialCaja getHistorialActivo(int idCaja) throws NonexistentEntityException {
		HistorialCaja cajaHistorial = null;
		try {
			Caja caja = cajaDAO.find(idCaja);
			if(caja == null)
				throw new NonexistentEntityException("Caja no encontrada");			
			QueryParameter queryParameter = QueryParameter.with("idcaja", idCaja);
			List<HistorialCaja> list = historialCajaDAO.findByNamedQuery(HistorialCaja.findByHistorialActivo, queryParameter.parameters());
			if(list.size() > 1){
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
	public Set<Boveda> getBovedasByCaja(int idCaja) {
		Set<Boveda> result = null;
		Caja caja = cajaDAO.find(idCaja);
		if(caja != null){
			result = new HashSet<Boveda>();
			Set<BovedaCaja> bovedaCajas= caja.getBovedaCajas();
			for (BovedaCaja bovedaCaja : bovedaCajas) {
				Boveda boveda = bovedaCaja.getBoveda();
				Hibernate.initialize(boveda);
				result.add(boveda);
			}
		}	
		return result;
	}

	@Override
	public Set<GenericMonedaDetalle> getDetalleCaja(int idCaja) {
		Set<GenericMonedaDetalle> result = null;				
		try {
			Caja caja = cajaDAO.find(idCaja);
			if (caja == null)
				return null;			
			//recuperando el historial activo
			HistorialCaja cajaHistorial = null;
			QueryParameter queryParameter = QueryParameter.with("idcaja", idCaja);
			List<HistorialCaja> list = historialCajaDAO.findByNamedQuery(HistorialCaja.findByHistorialActivo, queryParameter.parameters());
			if(list.size() > 1){
				throw new IllegalResultException("La caja tiene mas de un historial activo");
			} else {
				for (HistorialCaja c : list) {
					cajaHistorial = c;
				}			
			}				
			//recorrer por todas las bovedas
			Set<BovedaCaja> bovedas = caja.getBovedaCajas();
			result = new HashSet<GenericMonedaDetalle>();					
			for (BovedaCaja bovedaCaja : bovedas) {
				Boveda boveda = bovedaCaja.getBoveda();
				Moneda moneda = boveda.getMoneda();
				GenericMonedaDetalle genericMonedaDetalle = new GenericMonedaDetalle(moneda);				
				//recorrer todas las denominaciones existentes en la base de datos
				List<MonedaDenominacion> denominacionesExistentes = monedaService.getDenominaciones(moneda.getIdMoneda());	
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
		} catch (NullPointerException e) {
			LOGGER.error(e.getMessage(), e.getLocalizedMessage(), e.getCause());
		} catch (IllegalResultException e) {
			LOGGER.error(e.getMessage(), e.getLocalizedMessage(), e.getCause());
		} 			
		return result;
	}
	
	//duda
	@Override
	public Set<DetalleHistorialCaja> getDetalleCajaByMoneda(int idHistorial, int idMoneda){
		Set<DetalleHistorialCaja> result = null;
		HistorialCaja historial = historialCajaDAO.find(idHistorial);
		Moneda moneda = monedaService.findById(idMoneda);
		if (historial == null)
			return null;
		if (moneda == null)
			return null;
		Set<DetalleHistorialCaja> detalle = historial.getDetalleHistorialCajas();
		result = new HashSet<DetalleHistorialCaja>();
		for (DetalleHistorialCaja cajaHistDet : detalle) {
			Moneda monedaHist = cajaHistDet.getMonedaDenominacion().getMoneda(); 
			if (monedaHist.equals(moneda)) {
				result.add(cajaHistDet);
			}
		}
		return result;
	}

	@Override
	public List<HistorialCaja> getHistorialCaja(int idCaja, Date desde, Date hasta) {
		Caja caja = cajaDAO.find(idCaja);
		if(caja == null)
			return null;
		QueryParameter queryParameter = null;
		if(desde == null && hasta == null){
			Calendar begin = new GregorianCalendar(2000, Calendar.JANUARY, 1, 0, 0, 0);
			Calendar end = Calendar.getInstance();
			queryParameter = QueryParameter.with("idcaja", idCaja).and("desde", begin.getTime()).and("hasta", end.getTime());
		} 
		if(desde == null && hasta !=null){
			Calendar begin = new GregorianCalendar(2000, Calendar.JANUARY, 1, 0, 0, 0);
			queryParameter = QueryParameter.with("idcaja", idCaja).and("desde", begin.getTime()).and("hasta", hasta);
		}
		if(desde != null && hasta == null){			
			Calendar end = Calendar.getInstance();
			queryParameter = QueryParameter.with("idcaja", idCaja).and("desde", desde).and("hasta", end.getTime());
		} 
		if(desde != null && hasta !=null){
			queryParameter = QueryParameter.with("idcaja", idCaja).and("desde", desde).and("hasta", hasta);
		}			
		List<HistorialCaja> list = historialCajaDAO.findByNamedQuery(HistorialCaja.findByHistorialDateRange, queryParameter.parameters());		
		return list;
	}
	
	//falta verificar que la caja este abierta
	@Override
	public void abrirCaja(int idCaja) throws NonexistentEntityException, RollbackFailureException{
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
		//obteniendo el trabajador de la caja
		Trabajador trabajador = null;
		for (TrabajadorCaja trabCaj : caja.getTrabajadorCajas()) {
			trabajador = trabCaj.getTrabajador();
		}
		if(trabajador == null)
			throw new RollbackFailureException("No se encontro un trabajador asiganada a la caja");		
		try {
			HistorialCaja historialCajaOld = this.getHistorialActivo(caja.getIdCaja());			
			//abriendo caja
			caja.setAbierto(true);
			caja.setEstadoMovimiento(true);
			cajaDAO.update(caja);
					
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
			historialCajaNew.setTrabajador(trabajador.getPersonaNatural().getApellidoPaterno()+" "+trabajador.getPersonaNatural().getApellidoMaterno()+","+trabajador.getPersonaNatural().getNombres());
			historialCajaDAO.create(historialCajaNew);
			
			if(historialCajaOld != null){
				Set<DetalleHistorialCaja> detalleHistorialCajas = historialCajaOld.getDetalleHistorialCajas();
				for (DetalleHistorialCaja detalleHistorialCaja : detalleHistorialCajas) {
					this.em.getEm().detach(detalleHistorialCaja);
					detalleHistorialCaja.setIdDetalleHistorialCaja(null);
					detalleHistorialCaja.setHistorialCaja(historialCajaNew);
					detalleHistorialCajaDAO.create(detalleHistorialCaja);
				}
			} else {
				for (BovedaCaja bovedaCaja : bovedaCajas) {
					Moneda moneda = bovedaCaja.getBoveda().getMoneda();
					List<MonedaDenominacion> denominaciones = monedaService.getDenominaciones(moneda.getIdMoneda());
					for (MonedaDenominacion monedaDenominacion : denominaciones) {
						DetalleHistorialCaja detalleHistorialCaja = new DetalleHistorialCaja();								
						detalleHistorialCaja.setCantidad(BigInteger.ZERO);
						detalleHistorialCaja.setHistorialCaja(historialCajaNew);
						detalleHistorialCaja.setMonedaDenominacion(monedaDenominacion);
						detalleHistorialCajaDAO.create(detalleHistorialCaja);
					}
				}
			}
		} catch (NullPointerException e) {
			throw new EJBException();
		}		
	}

	//falta verificar que la caja este cerrada
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
			Calendar calendar = Calendar.getInstance();
			HistorialCaja historialCaja = this.getHistorialActivo(caja.getIdCaja());
			historialCaja.setEstado(true);
			historialCaja.setFechaCierre(calendar.getTime());
			historialCaja.setHoraCierre(calendar.getTime());
			historialCajaDAO.update(historialCaja);
			
			//cerrando caja
			caja.setAbierto(false);
			caja.setEstadoMovimiento(false);
			cajaDAO.update(caja);
			
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
	public Set<CajaCierreMoneda> getVoucherCierreCaja(int idCaja, Date fechaApertura) throws NonexistentEntityException {
		Caja caja = cajaDAO.find(idCaja);
		if(caja == null)
			return null;
		
		Set<CajaCierreMoneda> result;
		
		Agencia agencia = null;
		HistorialCaja historialCaja = null;
		
		//recuperando agencia
		Set<BovedaCaja> bovedaCajas = caja.getBovedaCajas();
		for (BovedaCaja bovedaCaja : bovedaCajas) {
			agencia = bovedaCaja.getBoveda().getAgencia();
			break;
		}
		if(agencia == null)
			return null;
		
		//recuperando el historial
		if(fechaApertura != null){
			QueryParameter queryParameter = QueryParameter.with("idcaja", idCaja).and("desde", fechaApertura).and("hasta", fechaApertura);
			List<HistorialCaja> list = historialCajaDAO.findByNamedQuery(HistorialCaja.findByHistorialDateRange, queryParameter.parameters());
			if(list.size() > 1)
				return null;
			else
				for (HistorialCaja hist : list) {
					historialCaja = hist;
				}			
			if(historialCaja == null)
				return null;
		} else {
			historialCaja = getHistorialActivo(idCaja);
		}		
		
		//recuperando el historial del dia anterior
		HistorialCaja historialAyer = null;
		QueryParameter queryParameter;
		if(fechaApertura != null){		
			queryParameter = QueryParameter.with("idcaja", idCaja).and("fecha", fechaApertura);			
		} else {
			queryParameter = QueryParameter.with("idcaja", idCaja).and("fecha", historialCaja.getFechaApertura());
		}
		List<HistorialCaja> list2 = historialCajaDAO.findByNamedQuery(HistorialCaja.findByHistorialDateRangePenultimo, queryParameter.parameters(), 2);			
		for (HistorialCaja hist : list2) {
			if(!historialCaja.equals(hist))
				historialAyer = hist;
		}
			
									
		//recuperando las monedas de la trasaccion
		Set<DetalleHistorialCaja> detalleHistorial = historialCaja.getDetalleHistorialCajas();	
		Set<Moneda> monedasTransaccion = new HashSet<Moneda>();
		for (DetalleHistorialCaja detHistcaja : detalleHistorial) {
			Moneda moneda = detHistcaja.getMonedaDenominacion().getMoneda();
			if(!monedasTransaccion.contains(moneda)){
				monedasTransaccion.add(moneda);
			}
		}
			
		//poniendo los datos por moneda
		result = new HashSet<CajaCierreMoneda>();
		for (Moneda moneda : monedasTransaccion) {
			CajaCierreMoneda cajaCierreMoneda = new CajaCierreMoneda();
			cajaCierreMoneda.setAgencia(agencia.getDenominacion());
			cajaCierreMoneda.setCaja(caja.getDenominacion());			
			cajaCierreMoneda.setFechaApertura(historialCaja.getFechaApertura());
			cajaCierreMoneda.setFechaCierre(historialCaja.getFechaCierre());
			cajaCierreMoneda.setHoraApertura(historialCaja.getHoraApertura());
			cajaCierreMoneda.setHoraCierre(historialCaja.getHoraCierre());
			cajaCierreMoneda.setMoneda(moneda);
			cajaCierreMoneda.setTrabajador(historialCaja.getTrabajador());
			
			BigDecimal saldoAyer = BigDecimal.ZERO;								
			BigDecimal entradas = BigDecimal.ZERO;
			BigDecimal salidas = BigDecimal.ZERO;
			BigDecimal porDevolver = BigDecimal.ZERO;
			BigDecimal sobrante = BigDecimal.ZERO;
			BigDecimal faltante = BigDecimal.ZERO;
			
			cajaCierreMoneda.setSaldoAyer(saldoAyer);
			cajaCierreMoneda.setEntradas(entradas);
			cajaCierreMoneda.setSalidas(salidas);
			cajaCierreMoneda.setPorDevolver(porDevolver);
			cajaCierreMoneda.setSobrante(sobrante);
			cajaCierreMoneda.setFaltante(faltante);
			
			/***********Añadiendo el detalle de una moneda***************/
			result.add(cajaCierreMoneda);
			
			//poniendo el detalle
			Set<GenericDetalle> detalle = new TreeSet<GenericDetalle>();		
			cajaCierreMoneda.setDetalle(detalle);
			for (DetalleHistorialCaja detHistcaja : detalleHistorial) {
				MonedaDenominacion denominacion = detHistcaja.getMonedaDenominacion();
				Moneda moneda2 = denominacion.getMoneda();
				BigInteger cantidad = detHistcaja.getCantidad();
				if(moneda.equals(moneda2)){
					if(cantidad.compareTo(BigInteger.ZERO) > 0){
						detalle.add(new GenericDetalle(denominacion.getValor(), detHistcaja.getCantidad()));
					}	
				}							
			}
			
			//recuperando saldo del dia anterior			
			if(historialAyer == null) {
				saldoAyer = BigDecimal.ZERO;
			}			
			else {
				for (DetalleHistorialCaja detHistCaja : historialAyer.getDetalleHistorialCajas()) {
					MonedaDenominacion denominacion = detHistCaja.getMonedaDenominacion();
					Moneda moneda2 = denominacion.getMoneda();
					if(moneda.equals(moneda2)){
						BigDecimal subTotal = denominacion.getValor().multiply(new BigDecimal(detHistCaja.getCantidad()));
						saldoAyer = saldoAyer.add(subTotal);
					}					
				}
			}
						
			//recuperando las operaciones del dia						
			Set<TransaccionBancaria> transBancarias = historialCaja.getTransaccionBancarias();
			Set<TransaccionCompraVenta> transComVent = historialCaja.getTransaccionCompraVentas();
			Set<TransaccionCuentaAporte> transCtaAport = historialCaja.getTransaccionCuentaAportes();
			for (TransaccionBancaria transBanc : transBancarias) {
				Moneda moneda2 = transBanc.getCuentaBancaria().getMoneda();
				if(moneda.equals(moneda2)){
					if(transBanc.getMonto().compareTo(BigDecimal.ZERO) >= 0)
						entradas = entradas.add(transBanc.getMonto());
					else
						salidas = salidas.add(transBanc.getMonto());
				}				
			}
			for (TransaccionCompraVenta transCompVent : transComVent) {
				Moneda monedaRecibida = transCompVent.getMonedaByIdMonedaRecibido();
				Moneda monedaEntregada = transCompVent.getMonedaByIdMonedaEntregado();
				if(moneda.equals(monedaRecibida)){
					entradas = entradas.add(transCompVent.getMontoRecibido());
				}
				if(moneda.equals(monedaEntregada)){
					salidas = salidas.add(transCompVent.getMontoEntregado());
				}
			}
			for (TransaccionCuentaAporte transAport : transCtaAport) {
				Moneda moneda2 = transAport.getCuentaAporte().getMoneda();
				if(moneda.equals(moneda2)){
					if(transAport.getMonto().compareTo(BigDecimal.ZERO) >= 0)
						entradas = entradas.add(transAport.getMonto());
					else
						salidas = salidas.add(transAport.getMonto());
				}				
			}
			
			//recuperando faltantes y sobrantes
			Set<PendienteCaja> listPendientes = historialCaja.getPendienteCajas();
			for (PendienteCaja pendiente : listPendientes) {
				Moneda moneda2 = pendiente.getMoneda();
				if(moneda.equals(moneda2)){
					if(pendiente.getMonto().compareTo(BigDecimal.ZERO) >= 0)
						sobrante = sobrante.add(pendiente.getMonto());
					else
						faltante = faltante.add(pendiente.getMonto());
				}
			}
		}
		
		return result;
	}

	@Override
	public ResumenOperacionesCaja getResumenOperacionesCaja(int idCaja,Date fechaApertura) throws NonexistentEntityException,IllegalResultException {
		Caja caja = cajaDAO.find(idCaja);
		if(caja == null)
			throw new NonexistentEntityException("Caja no encontrada");
		
		Agencia agencia = null;
		HistorialCaja historialCaja = null;
		
		//recuperando agencia
		Set<BovedaCaja> bovedaCajas = caja.getBovedaCajas();
		for (BovedaCaja bovedaCaja : bovedaCajas) {
			agencia = bovedaCaja.getBoveda().getAgencia();
			break;
		}
		if(agencia == null)
			throw new NonexistentEntityException("La caja no tiene una agencia asignada");
		
		//recuperando el historial
		if(fechaApertura != null){
			QueryParameter queryParameter = QueryParameter.with("idcaja", idCaja).and("desde", fechaApertura).and("hasta", fechaApertura);
			List<HistorialCaja> list = historialCajaDAO.findByNamedQuery(HistorialCaja.findByHistorialDateRange, queryParameter.parameters());
			if(list.size() > 1)
				throw new IllegalResultException("Existe mas de un historial para la fecha dada");
			else
				for (HistorialCaja hist : list) {
					historialCaja = hist;
				}			
			if(historialCaja == null)
				throw new NonexistentEntityException("No existe un historial de caja para la fecha dada");
		} else {
			historialCaja = getHistorialActivo(idCaja);
		}		
			
		
		
		ResumenOperacionesCaja result = new ResumenOperacionesCaja();
		
		int depositosAhorro = 0;
		int retirosAhorro = 0;
		int depositosCorriente= 0;
		int retirosCorriente = 0;
		int depositosPlazoFijo= 0;
		int retirosPlazoFijo = 0;
		int depositosAporte= 0;
		int retirosAporte = 0;
		
		int compra = 0;
		int venta = 0;
		
		int depositosMayorCuantia = 0;
		int retirosMayorCuantia = 0;
		int compraVentaMayorCuantia = 0;
		
		int transCajaCajaRecibido = 0;
		int transCajaCajaEnviado = 0;
		int transBovedaCajaRecibido = 0;
		int transBovedaCajaEnviado = 0;
		
		int pendienteFaltante = 0;
		int pendienteSobrante = 0;
		
		//recuperando las operaciones del dia						
		Set<TransaccionBancaria> transBancarias = historialCaja.getTransaccionBancarias();
		Set<TransaccionCompraVenta> transComVent = historialCaja.getTransaccionCompraVentas();
		Set<TransaccionCuentaAporte> transCtaAport = historialCaja.getTransaccionCuentaAportes();
		
		Set<TransaccionCajaCaja> transCajaCajaEnviados = historialCaja.getTransaccionCajaCajasForIdCajaHistorialOrigen();
		Set<TransaccionCajaCaja> transCajaCajaRecibidos = historialCaja.getTransaccionCajaCajasForIdCajaHistorialDestino();
		Set<TransaccionBovedaCaja> transBovedaCaja = historialCaja.getTransaccionBovedaCajas();
		
		Set<PendienteCaja> transPendiente = historialCaja.getPendienteCajas();
		
		VariableSistema varSoles = variableSistemaService.find(Variable.TRANSACCION_MAYOR_CUANTIA_NUEVOS_SOLES);
		VariableSistema varDolares = variableSistemaService.find(Variable.TRANSACCION_MAYOR_CUANTIA_DOLARES);
		VariableSistema varEuros = variableSistemaService.find(Variable.TRANSACCION_MAYOR_CUANTIA_EUROS);
		
		for (TransaccionBancaria transBanc : transBancarias) {			
			TipoCuentaBancaria tipoCuenta = transBanc.getCuentaBancaria().getTipoCuentaBancaria();
			if(tipoCuenta.equals(TipoCuentaBancaria.AHORRO)){
				if(transBanc.getTipoTransaccion().equals(Tipotransaccionbancaria.DEPOSITO))
					depositosAhorro++;
				else
					retirosAhorro++;
			}
			if(tipoCuenta.equals(TipoCuentaBancaria.CORRIENTE)){
				if(transBanc.getTipoTransaccion().equals(Tipotransaccionbancaria.DEPOSITO))
					depositosCorriente++;
				else
					retirosCorriente++;
			}
			if(tipoCuenta.equals(TipoCuentaBancaria.PLAZO_FIJO)){
				if(transBanc.getTipoTransaccion().equals(Tipotransaccionbancaria.DEPOSITO))
					depositosPlazoFijo++;
				else
					retirosPlazoFijo++;
			}
			
			//mayor cuantia
			if(transBanc.getCuentaBancaria().getMoneda().getIdMoneda() == 1){
				if(transBanc.getMonto().abs().compareTo(varSoles.getValor()) >= 0)
					if(transBanc.getTipoTransaccion().equals(Tipotransaccionbancaria.DEPOSITO))
						depositosMayorCuantia++;
					else
						retirosMayorCuantia++;				
			}
			if(transBanc.getCuentaBancaria().getMoneda().getIdMoneda() == 2){
				if(transBanc.getMonto().abs().compareTo(varDolares.getValor()) >= 0)
					if(transBanc.getTipoTransaccion().equals(Tipotransaccionbancaria.DEPOSITO))
						depositosMayorCuantia++;
					else
						retirosMayorCuantia++;				
			}
			if(transBanc.getCuentaBancaria().getMoneda().getIdMoneda() == 3){
				if(transBanc.getMonto().abs().compareTo(varEuros.getValor()) >= 0)
					if(transBanc.getTipoTransaccion().equals(Tipotransaccionbancaria.DEPOSITO))
						depositosMayorCuantia++;
					else
						retirosMayorCuantia++;				
			}
			
		}
		for (TransaccionCompraVenta transCompraVenta : transComVent) {			
			if(transCompraVenta.equals(Tipotransaccioncompraventa.COMPRA))
				compra++;
			else
				venta++;
		}
		for (TransaccionCuentaAporte trans : transCtaAport) {			
			if(trans.getTipoTransaccion().equals(Tipotransaccionbancaria.DEPOSITO))
				depositosAporte++;
			else
				retirosAporte++;
		}
		
		
		transCajaCajaEnviado = transCajaCajaEnviados.size();
		transCajaCajaRecibido = transCajaCajaRecibidos.size();
		
		for (TransaccionBovedaCaja transBovCaj : transBovedaCaja) {
			if(transBovCaj.getEstadoSolicitud() && transBovCaj.getEstadoConfirmacion()){
				if(transBovCaj.getOrigen().equals(TransaccionBovedaCajaOrigen.CAJA))
					transBovedaCajaEnviado++;
				else
					transBovedaCajaRecibido++;
			}
		}
		
		for (PendienteCaja pendiente : transPendiente) {
			if(pendiente.getTipoPendiente().equals(TipoPendiente.FALTANTE))
				pendienteFaltante++;
			else
				pendienteSobrante++;
		}
		
		result.setAgencia(agencia.getDenominacion());
		result.setCaja(caja.getDenominacion());
		result.setFechaApertura(historialCaja.getFechaApertura());
		result.setHoraApertura(historialCaja.getHoraApertura());
		result.setFechaCierre(historialCaja.getFechaCierre());
		result.setHoraCierre(historialCaja.getHoraCierre());
		result.setTrabajador(historialCaja.getTrabajador());
		
		result.setDepositosAhorro(depositosAhorro);
		result.setRetirosAhorro(retirosAhorro);
		result.setDepositosCorriente(depositosCorriente);
		result.setRetirosCorriente(retirosCorriente);
		result.setDepositosPlazoFijo(depositosPlazoFijo);
		result.setRetirosPlazoFijo(retirosPlazoFijo);
		result.setDepositosAporte(depositosAporte);
		result.setRetirosAporte(retirosAporte);
		
		result.setCompra(compra);
		result.setVenta(venta);
		
		result.setDepositoMayorCuantia(depositosMayorCuantia);
		result.setRetiroMayorCuantia(retirosMayorCuantia);
		result.setCompraVentaMayorCuantia(compraVentaMayorCuantia);
		
		result.setEnviadoCajaCaja(transCajaCajaEnviado);
		result.setRecibidoCajaCaja(transCajaCajaRecibido);
		result.setEnviadoBovedaCaja(transBovedaCajaEnviado);
		result.setRecibidoBovedaCaja(transBovedaCajaRecibido);
		
		result.setPendienteFaltante(pendienteFaltante);
		result.setPendienteSobrante(pendienteSobrante);
		
		return result;
	}

	@Override
	protected DAO<Object, Caja> getDAO() {
		return this.cajaDAO;
	}
}
