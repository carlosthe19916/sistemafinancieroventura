package org.ventura.sistemafinanciero.control;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.dao.QueryParameter;
import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.entity.BovedaCaja;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.DetalleHistorialBoveda;
import org.ventura.sistemafinanciero.entity.DetalleHistorialCaja;
import org.ventura.sistemafinanciero.entity.HistorialBoveda;
import org.ventura.sistemafinanciero.entity.HistorialCaja;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.MonedaDenominacion;
import org.ventura.sistemafinanciero.entity.PendienteCaja;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.TrabajadorCaja;
import org.ventura.sistemafinanciero.entity.TrabajadorUsuario;
import org.ventura.sistemafinanciero.entity.TransaccionBovedaCaja;
import org.ventura.sistemafinanciero.entity.TransaccionBovedaCajaDetalle;
import org.ventura.sistemafinanciero.entity.TransaccionCajaCaja;
import org.ventura.sistemafinanciero.entity.Usuario;
import org.ventura.sistemafinanciero.entity.dto.GenericDetalle;
import org.ventura.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.ventura.sistemafinanciero.entity.type.TipoPendiente;
import org.ventura.sistemafinanciero.entity.type.TransaccionBovedaCajaOrigen;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.CajaSessionService;
import org.ventura.sistemafinanciero.service.MonedaService;
import org.ventura.sistemafinanciero.util.AllowedTo;
import org.ventura.sistemafinanciero.util.EntityManagerProducer;
import org.ventura.sistemafinanciero.util.Guard;
import org.ventura.sistemafinanciero.util.Permission;

@Stateless
@Named
@Interceptors(Guard.class)
@Remote(CajaSessionService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CajaSessionServiceBean extends AbstractServiceBean<Caja> implements CajaSessionService {

	@Resource
	private SessionContext context;

	@Inject
	private EntityManagerProducer em;
	
	@Inject
    private Validator validator;
	
	@Inject
	private DAO<Object, Usuario> usuarioDAO;
	@Inject
	private DAO<Object, Boveda> bovedaDAO;
	@Inject
	private DAO<Object, Caja> cajaDAO;
	@Inject
	private DAO<Object, HistorialCaja> historialCajaDAO;
	@Inject 
	private DAO<Object, DetalleHistorialCaja> detalleHistorialCajaDAO;
	@Inject 
	private DAO<Object, PendienteCaja> pendienteCajaDAO;
	@Inject 
	private DAO<Object, BovedaCaja> bovedaCajaDAO;
	@Inject 
	private DAO<Object, HistorialBoveda> historialBovedaDAO;
	
	@Inject 
	private DAO<Object, TransaccionBovedaCaja> transaccionBovedaCajaDAO;
	//@Inject 
	//private DAO<Object, TransaccionCajaCaja> transaccionCajaCajaDAO;
	@Inject 
	private DAO<Object, TransaccionBovedaCajaDetalle> detalleTransaccionBovedaCajaDAO;
	
	@EJB
	private MonedaService monedaService;
	
	private Logger LOGGER = LoggerFactory.getLogger(CajaSessionService.class);

	private Usuario getUsuario(){
		String username = context.getCallerPrincipal().getName();
		Usuario usuario = null;
		QueryParameter queryParameter = QueryParameter.with("username",username);
		List<Usuario> result = usuarioDAO.findByNamedQuery(Usuario.findByUsername, queryParameter.parameters());
		for (Usuario u : result) {
			usuario = u;
			break;
		}
		return usuario;
	}
	
	private Trabajador getTrabajador(){
		String username = context.getCallerPrincipal().getName();
		Usuario usuario = null;
		Trabajador trabajador = null;
		QueryParameter queryParameter = QueryParameter.with("username",username);
		List<Usuario> result = usuarioDAO.findByNamedQuery(Usuario.findByUsername, queryParameter.parameters());
		for (Usuario u : result) {
			usuario = u;
			break;
		}
		Set<TrabajadorUsuario> listTrabajadores = usuario.getTrabajadorUsuarios();
		for (TrabajadorUsuario trabajadorUsuario : listTrabajadores) {
			trabajador = trabajadorUsuario.getTrabajador();
			break;
		}
		return trabajador;
	}
	
	private Caja getCaja(){
		String username = context.getCallerPrincipal().getName();
		Caja caja = null;
		Usuario usuario = null;
		Trabajador trabajador = null;
		QueryParameter queryParameter = QueryParameter.with("username",username);
		List<Usuario> result = usuarioDAO.findByNamedQuery(Usuario.findByUsername, queryParameter.parameters());
		for (Usuario u : result) {
			usuario = u;
			break;
		}
		Set<TrabajadorUsuario> listTrabajadores = usuario.getTrabajadorUsuarios();
		for (TrabajadorUsuario trabajadorUsuario : listTrabajadores) {
			trabajador = trabajadorUsuario.getTrabajador();
			break;
		}
		Set<TrabajadorCaja> cajas = trabajador.getTrabajadorCajas();
		for (TrabajadorCaja trabajadorCaja : cajas) {
			caja = trabajadorCaja.getCaja();
			break;
		}
		return caja;
	}
	
	private HistorialCaja getHistorialActivo() {
		Caja caja = getCaja();
		HistorialCaja cajaHistorial = null;
		QueryParameter queryParameter = QueryParameter.with("idcaja", caja.getIdCaja());
		List<HistorialCaja> list = historialCajaDAO.findByNamedQuery(HistorialCaja.findByHistorialActivo, queryParameter.parameters());
		for (HistorialCaja c : list) {
			cajaHistorial = c;
			break;
		}
		return cajaHistorial;
	}
	
	@Override
	@AllowedTo(Permission.CERRADO)
	public BigInteger abrirCaja() throws RollbackFailureException {
		Caja caja = getCaja();
		Trabajador trabajador = getTrabajador();
		if(trabajador == null)
			throw new RollbackFailureException("No se encontró un trabajador para la caja");
		
		Set<BovedaCaja> bovedaCajas = caja.getBovedaCajas();
		for (BovedaCaja bovedaCaja : bovedaCajas) {
			Boveda boveda = bovedaCaja.getBoveda();
			if(!boveda.getAbierto())
				throw new RollbackFailureException("Debe de abrir las bovedas asociadas a la caja("+boveda.getDenominacion()+")");
		}		
			
		try {
			HistorialCaja historialCajaOld = this.getHistorialActivo();			
			
			//abriendo caja
			caja.setAbierto(true);
			caja.setEstadoMovimiento(true);
			Set<ConstraintViolation<Caja>> violationsCaja = validator.validate(caja);
			if (!violationsCaja.isEmpty()) {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsCaja));
			} else {
				cajaDAO.update(caja);	
			}
			
					
			if(historialCajaOld != null) {
				historialCajaOld.setEstado(false);
				Set<ConstraintViolation<HistorialCaja>> violationsHistorialOld = validator.validate(historialCajaOld);
				if (!violationsHistorialOld.isEmpty()) {
					throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsHistorialOld));
				} else {
					historialCajaDAO.update(historialCajaOld);		
				}							
			}
			
			Calendar calendar = Calendar.getInstance();	
			HistorialCaja historialCajaNew = new HistorialCaja();
			historialCajaNew.setCaja(caja);
			historialCajaNew.setFechaApertura(calendar.getTime());
			historialCajaNew.setHoraApertura(calendar.getTime());
			historialCajaNew.setEstado(true);			
			historialCajaNew.setTrabajador(trabajador.getPersonaNatural().getApellidoPaterno()+" "+trabajador.getPersonaNatural().getApellidoMaterno()+","+trabajador.getPersonaNatural().getNombres());
			Set<ConstraintViolation<HistorialCaja>> violationsHistorialNew = validator.validate(historialCajaNew);
			if (!violationsHistorialNew.isEmpty()) {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsHistorialNew));
			} else {
				historialCajaDAO.create(historialCajaNew);
			}	
			
			
			if(historialCajaOld != null){
				Set<DetalleHistorialCaja> detalleHistorialCajas = historialCajaOld.getDetalleHistorialCajas();
				for (DetalleHistorialCaja detalleHistorialCaja : detalleHistorialCajas) {
					this.em.getEm().detach(detalleHistorialCaja);
					detalleHistorialCaja.setIdDetalleHistorialCaja(null);
					detalleHistorialCaja.setHistorialCaja(historialCajaNew);
										
					Set<ConstraintViolation<DetalleHistorialCaja>> violationsHistorialDetalle = validator.validate(detalleHistorialCaja);
					if (!violationsHistorialDetalle.isEmpty()) {
						throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsHistorialDetalle));
					} else {
						detalleHistorialCajaDAO.create(detalleHistorialCaja);
					}	
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
						
						Set<ConstraintViolation<DetalleHistorialCaja>> violationsHistorialDetalle = validator.validate(detalleHistorialCaja);
						if (!violationsHistorialDetalle.isEmpty()) {
							throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsHistorialDetalle));
						} else {
							detalleHistorialCajaDAO.create(detalleHistorialCaja);
						}							
					}
				}
			}
			
			return historialCajaNew.getIdHistorialCaja();
		} catch (ConstraintViolationException e) {
			LOGGER.error(e.getMessage(), e.getCause(), e.getLocalizedMessage());
			return BigInteger.ONE.negate();
		}
	}

	@Override
	public Map<Boveda, BigDecimal> getDiferenciaSaldoCaja(Set<GenericMonedaDetalle> detalleCaja){
		Map<Boveda, BigDecimal> result = new HashMap<Boveda, BigDecimal>();
		Caja caja = getCaja();
		Set<BovedaCaja> bovedas = caja.getBovedaCajas();
		for (BovedaCaja bovedaCaja : bovedas) {
			Moneda moneda = bovedaCaja.getBoveda().getMoneda();
			for (GenericMonedaDetalle detalle : detalleCaja) {
				if (moneda.equals(detalle.getMoneda())) {
					if (bovedaCaja.getSaldo().compareTo(detalle.getTotal()) != 0) {
						Boveda boveda = bovedaCaja.getBoveda();
						Hibernate.initialize(boveda);
						BigDecimal diferencia = bovedaCaja.getSaldo().subtract(detalle.getTotal());
						result.put(boveda, diferencia);
					}
					break;
				}
			}
		}
		return result;
	}
	
	@Override
	@AllowedTo(Permission.ABIERTO)
	public BigInteger cerrarCaja(Set<GenericMonedaDetalle> detalleCaja) throws RollbackFailureException {
		Caja caja = getCaja();
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
			HistorialCaja historialCaja = this.getHistorialActivo();
			historialCaja.setEstado(true);
			historialCaja.setFechaCierre(calendar.getTime());
			historialCaja.setHoraCierre(calendar.getTime());
			
			Set<ConstraintViolation<HistorialCaja>> violationsHistorial = validator.validate(historialCaja);
			if (!violationsHistorial.isEmpty()) {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsHistorial));
			} else {
				historialCajaDAO.update(historialCaja);
			}
			
			
			//cerrando caja
			caja.setAbierto(false);
			caja.setEstadoMovimiento(false);
			Set<ConstraintViolation<Caja>> violationsCaja = validator.validate(caja);
			if (!violationsHistorial.isEmpty()) {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsCaja));
			} else {
				cajaDAO.update(caja);
			}
			
			
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
										
										Set<ConstraintViolation<DetalleHistorialCaja>> violationsDetalle = validator.validate(dhc);
										if (!violationsHistorial.isEmpty()) {
											throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsDetalle));
										} else {
											detalleHistorialCajaDAO.update(dhc);
										}										
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
			return historialCaja.getIdHistorialCaja();
		} catch (ConstraintViolationException e) {
			LOGGER.error(e.getMessage(), e.getCause(), e.getLocalizedMessage());
			return BigInteger.ONE.negate();
		}
	}

	@Override
	@AllowedTo(Permission.ABIERTO)
	public BigInteger crearPendiente(BigInteger idBoveda, BigDecimal monto, String observacion) throws RollbackFailureException {
		Caja caja = getCaja();
		Boveda boveda = bovedaDAO.find(idBoveda);
		if(boveda == null)
			throw new RollbackFailureException("Boveda no encontrada");
		
		Set<BovedaCaja> bovedasCajas = caja.getBovedaCajas();
		BovedaCaja bovedaCajaTransaccion = null;
		for (BovedaCaja bovedaCaja : bovedasCajas) {
			Boveda bov = bovedaCaja.getBoveda();
			if(bov.equals(boveda)){
				bovedaCajaTransaccion = bovedaCaja;
				break;
			}				
		}
		if(bovedaCajaTransaccion == null)
			throw new RollbackFailureException("La caja y la boveda seleccionados no estan relacionados");
		
		//obteniendo el historial de la caja
		HistorialCaja historialCaja;
	
		historialCaja = this.getHistorialActivo();
		
		Calendar calendar = Calendar.getInstance();
		PendienteCaja pendienteCaja = new PendienteCaja();
		pendienteCaja.setFecha(calendar.getTime());
		pendienteCaja.setHora(calendar.getTime());
		pendienteCaja.setHistorialCaja(historialCaja);
		pendienteCaja.setMoneda(boveda.getMoneda());
		pendienteCaja.setMonto(monto);
		pendienteCaja.setTipoPendiente(monto.compareTo(BigDecimal.ZERO) >= 1 ? TipoPendiente.SOBRANTE : TipoPendiente.SOBRANTE);
		pendienteCaja.setObservacion(observacion);
		pendienteCajaDAO.create(pendienteCaja);
		
		//modificando el saldo de boveda
		BigDecimal saldoActual = bovedaCajaTransaccion.getSaldo();
		BigDecimal montoTransaccion = monto;
		BigDecimal saldoFinal = saldoActual.add(montoTransaccion);
		bovedaCajaTransaccion.setSaldo(saldoFinal);
		bovedaCajaDAO.update(bovedaCajaTransaccion);
		
		return pendienteCaja.getIdPendienteCaja();
			
	}

	@Override
	@AllowedTo(Permission.ABIERTO)
	public BigInteger crearTransaccionBovedaCaja(BigInteger idBoveda,Set<GenericDetalle> detalleTransaccion)throws RollbackFailureException {
		Boveda boveda = bovedaDAO.find(idBoveda);
		Caja caja = getCaja();
		if(boveda == null || caja == null)
			throw new RollbackFailureException("Caja o Boveda no encontrada");
		Moneda moneda = boveda.getMoneda();
		HistorialCaja historialCaja = null;
		HistorialBoveda historialBoveda = null;
				

		//obteniendo historial de boveda
		QueryParameter queryParameter1 = QueryParameter.with("idboveda", idBoveda);
		List<HistorialBoveda> listHistBovedas = historialBovedaDAO.findByNamedQuery(HistorialBoveda.findByHistorialActivo, queryParameter1.parameters());
		if(listHistBovedas.size() > 1){
			throw new RollbackFailureException("La boveda tiene mas de un historial activo");
		} else {
			for (HistorialBoveda hist : listHistBovedas) {
				historialBoveda = hist;
			}
		}
		
		//obteniendo historial de caja
		QueryParameter queryParameter2 = QueryParameter.with("idcaja", caja.getIdCaja());
		List<HistorialCaja> list = historialCajaDAO.findByNamedQuery(HistorialCaja.findByHistorialActivo, queryParameter2.parameters());
		if(list.size() > 1){
			throw new RollbackFailureException("La caja tiene mas de un historial activo");
		} else {
			for (HistorialCaja c : list) {
				historialCaja = c;
			}
		}	
		
		//determinando los saldos
		BigDecimal totalTransaccion = BigDecimal.ZERO;
		BigDecimal totalBoveda = BigDecimal.ZERO;
		BigDecimal totalCajaByMoneda = BigDecimal.ZERO;
		Set<DetalleHistorialBoveda> detHistBoveda = historialBoveda.getDetalleHistorialBovedas();
		Set<BovedaCaja> bovedasCajas = caja.getBovedaCajas();
		for (GenericDetalle detalle : detalleTransaccion) {
			BigDecimal subtotal = detalle.getSubtotal();
			totalTransaccion = totalTransaccion.add(subtotal);
		}
		for (DetalleHistorialBoveda detalle : detHistBoveda) {
			BigInteger cantidad = detalle.getCantidad();
			BigDecimal valor = detalle.getMonedaDenominacion().getValor();
			BigDecimal subtotal = valor.multiply(new BigDecimal(cantidad));
			totalBoveda = totalBoveda.add(subtotal);
		}
		totalBoveda = totalBoveda.subtract(totalTransaccion);
		for (BovedaCaja bovedaCaja : bovedasCajas) {
			Boveda bovedaCaj = bovedaCaja.getBoveda();
			if(bovedaCaj.equals(boveda)){
				totalCajaByMoneda = bovedaCaja.getSaldo();
				break;
			}
		}
		totalCajaByMoneda = totalCajaByMoneda.add(totalTransaccion);
		
		//creando la transaccion
		TransaccionBovedaCaja transaccionBovedaCaja = new TransaccionBovedaCaja();
		Calendar calendar = Calendar.getInstance();
		
		transaccionBovedaCaja.setEstadoConfirmacion(false);
		transaccionBovedaCaja.setEstadoSolicitud(true);
		transaccionBovedaCaja.setFecha(calendar.getTime());
		transaccionBovedaCaja.setHora(calendar.getTime());
		transaccionBovedaCaja.setHistorialBoveda(historialBoveda);
		transaccionBovedaCaja.setHistorialCaja(historialCaja);
		transaccionBovedaCaja.setOrigen(TransaccionBovedaCajaOrigen.CAJA);
		transaccionBovedaCaja.setSaldoDisponibleOrigen(totalCajaByMoneda);
		transaccionBovedaCaja.setSaldoDisponibleDestino(totalBoveda);
		transaccionBovedaCajaDAO.create(transaccionBovedaCaja);
		
		//creando el detalle de transaccion
		List<MonedaDenominacion> denominaciones = monedaService.getDenominaciones(moneda.getIdMoneda());
		for (GenericDetalle detalle : detalleTransaccion) {
			TransaccionBovedaCajaDetalle det = new TransaccionBovedaCajaDetalle();
			det.setCantidad(detalle.getCantidad());				
			det.setTransaccionBovedaCaja(transaccionBovedaCaja);
			for (MonedaDenominacion monedaDenominacion : denominaciones) {
				BigDecimal valorDenominacion = monedaDenominacion.getValor();
				BigDecimal valorDetalle = detalle.getValor();
				if(valorDenominacion.compareTo(valorDetalle) == 0){
					det.setMonedaDenominacion(monedaDenominacion);
					break;
				}
			}
			detalleTransaccionBovedaCajaDAO.create(det);
		}	
		
		return transaccionBovedaCaja.getIdTransaccionBovedaCaja();

	}

	@Override
	@AllowedTo(Permission.ABIERTO)
	public BigInteger crearTransaccionCajaCaja(BigInteger idCajadestino, BigInteger idMoneda,BigDecimal monto, String observacion)throws RollbackFailureException {
		return null;	
	}

	@Override	
	protected DAO<Object, Caja> getDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Moneda> getMonedas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<GenericMonedaDetalle> getDetalleCaja() {
		Set<GenericMonedaDetalle> result = null;				
		Caja caja = getCaja();
		if (caja == null)
			return null;			
		//recuperando el historial activo
		HistorialCaja cajaHistorial = getHistorialActivo();
					
		//recorrer por todas las bovedas
		Set<BovedaCaja> bovedas = caja.getBovedaCajas();
		result = new HashSet<GenericMonedaDetalle>();					
		for (BovedaCaja bovedaCaja : bovedas) {
			Boveda boveda = bovedaCaja.getBoveda();
			Moneda moneda = boveda.getMoneda();
			Hibernate.initialize(moneda);
			GenericMonedaDetalle genericMonedaDetalle = new GenericMonedaDetalle(moneda);				
			//recorrer todas las denominaciones existentes en la base de datos
			List<MonedaDenominacion> denominacionesExistentes = monedaService.getDenominaciones(moneda.getIdMoneda());	
			for (MonedaDenominacion m : denominacionesExistentes) {
				GenericDetalle detalle = new GenericDetalle(m.getValor(), BigInteger.ZERO);
				genericMonedaDetalle.addElementDetalleReplacingIfExist(detalle);
				genericMonedaDetalle.setMoneda(m.getMoneda());
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
	public Set<TransaccionBovedaCaja> getTransaccionesEnviadasBovedaCaja() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<TransaccionBovedaCaja> getTransaccionesRecibidasBovedaCaja() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<TransaccionCajaCaja> getTransaccionesEnviadasCajaCaja() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<TransaccionCajaCaja> getTransaccionesRecibidasCajaCaja() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<PendienteCaja> getPendientesCaja() {		
		HistorialCaja historial = getHistorialActivo();
		Set<PendienteCaja> result = historial.getPendienteCajas();
		for (PendienteCaja pendienteCaja : result) {
			Moneda moneda = pendienteCaja.getMoneda();
			Hibernate.initialize(pendienteCaja);
			Hibernate.initialize(moneda);
		}
		return result;

	}
	
}
