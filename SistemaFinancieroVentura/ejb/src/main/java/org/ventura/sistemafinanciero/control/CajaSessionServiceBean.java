package org.ventura.sistemafinanciero.control;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBAccessException;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.dao.QueryParameter;
import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Beneficiario;
import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.entity.BovedaCaja;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.CuentaAporte;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.DetalleHistorialBoveda;
import org.ventura.sistemafinanciero.entity.DetalleHistorialCaja;
import org.ventura.sistemafinanciero.entity.HistorialBoveda;
import org.ventura.sistemafinanciero.entity.HistorialCaja;
import org.ventura.sistemafinanciero.entity.HistorialTransaccionCaja;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.MonedaDenominacion;
import org.ventura.sistemafinanciero.entity.PendienteCaja;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.Socio;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.TrabajadorCaja;
import org.ventura.sistemafinanciero.entity.TrabajadorUsuario;
import org.ventura.sistemafinanciero.entity.TransaccionBancaria;
import org.ventura.sistemafinanciero.entity.TransaccionBovedaCaja;
import org.ventura.sistemafinanciero.entity.TransaccionBovedaCajaDetalle;
import org.ventura.sistemafinanciero.entity.TransaccionCajaCaja;
import org.ventura.sistemafinanciero.entity.TransaccionCompraVenta;
import org.ventura.sistemafinanciero.entity.TransaccionCuentaAporte;
import org.ventura.sistemafinanciero.entity.TransferenciaBancaria;
import org.ventura.sistemafinanciero.entity.Usuario;
import org.ventura.sistemafinanciero.entity.dto.GenericDetalle;
import org.ventura.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.ventura.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoPendiente;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.entity.type.Tipotransaccionbancaria;
import org.ventura.sistemafinanciero.entity.type.Tipotransaccioncompraventa;
import org.ventura.sistemafinanciero.entity.type.TransaccionBovedaCajaOrigen;
import org.ventura.sistemafinanciero.exception.IllegalResultException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.CajaSessionService;
import org.ventura.sistemafinanciero.service.CuentaBancariaService;
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
	
	@Inject
	private DAO<Object, CuentaBancaria> cuentaBancariaDAO;
	@Inject
	private DAO<Object, TransaccionBancaria> transaccionBancariaDAO;
	@Inject
	private DAO<Object, TransaccionCuentaAporte> transaccionCuentaAporteDAO;
	@Inject
	private DAO<Object, Moneda> monedaDAO;
	@Inject
	private DAO<Object, Socio> socioDAO;
	@Inject
	private DAO<Object, CuentaAporte> cuentaAporteDAO;
	@Inject
	private DAO<Object, TransferenciaBancaria> transferenciaBancariaDAO;
	@Inject
	private DAO<Object, TransaccionCompraVenta> transaccionCompraVentaDAO;
	@Inject
	private DAO<Object, HistorialTransaccionCaja> historialTransaccionCajaDAO;		
	
	@EJB
	private MonedaService monedaService;
	@EJB
	private CuentaBancariaService cuentaBancariaService;
	
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
	
	private BigInteger getNumeroOperacion(){
		Agencia agencia = new Agencia();
		Caja caja = this.getCaja();
		Set<BovedaCaja> lisBC = caja.getBovedaCajas();
		for (BovedaCaja bovedaCaja : lisBC) {
			agencia = bovedaCaja.getBoveda().getAgencia();
		}

		Query query = em
				.getEm()
				.createNativeQuery(
						"SELECT T.Numero_Operacion AS numero_operacion FROM Transaccion_Bancaria T Inner Join Historial_Caja HC on (HC.id_historial_caja = T.Id_Historial_Caja) Inner Join Caja C on (C.Id_Caja = Hc.Id_Caja) Inner Join Boveda_Caja BC on (BC.id_caja = C.Id_Caja) Inner Join Boveda B on (B.id_boveda = Bc.Id_Boveda) Where B.Id_Agencia = :idagencia and T.Fecha = (select to_char(systimestamp,'dd/mm/yy') from dual) "
						+ "union "
						+ "select Tcv.Numero_Operacion AS numero_operacion From Transaccion_Compra_Venta TCV Inner Join Historial_Caja HC on (HC.id_historial_caja = Tcv.Id_Historial_Caja) Inner Join Caja C on (C.Id_Caja = Hc.Id_Caja) Inner Join Boveda_Caja BC on (BC.id_caja = C.Id_Caja) Inner Join Boveda B on (B.id_boveda = Bc.Id_Boveda) Where B.Id_Agencia = :idagencia and Tcv.Fecha = (select to_char(systimestamp,'dd/mm/yy') from dual) "
						+ "union "
						+ "select Tb.Numero_Operacion AS numero_operacion From Transferencia_Bancaria TB Inner Join Historial_Caja HC on (HC.id_historial_caja = Tb.Id_Historial_Caja) Inner Join Caja C on (C.Id_Caja = Hc.Id_Caja) Inner Join Boveda_Caja BC on (BC.id_caja = C.Id_Caja) Inner Join Boveda B on (B.id_boveda = Bc.Id_Boveda) Where B.Id_Agencia = :idagencia and Tb.Fecha = (select to_char(systimestamp,'dd/mm/yy') from dual) "
						+ "union "
						+ "select Ap.Numero_Operacion AS numero_operacion from Transaccion_Cuenta_Aporte AP Inner Join Historial_Caja HC on (HC.id_historial_caja = AP.id_historial_caja) Inner Join Caja C on (HC.Id_Caja = C.Id_Caja) Inner Join Boveda_Caja BC on (BC.id_caja = C.id_caja) Inner Join Boveda B on (B.id_boveda = BC.id_boveda) where B.Id_Agencia = :idagencia and ap.Fecha = (select to_char(systimestamp,'dd/mm/yy') from dual) ORDER BY numero_operacion DESC");
		query.setParameter("idagencia", agencia.getIdAgencia());
		
		List<BigDecimal> list = query.getResultList();
		if(list.size() == 0) {
			return BigInteger.ONE;	
		}			
		else {
			BigDecimal op = list.get(0);
			BigInteger numero_operacion = op.toBigInteger();
			return numero_operacion.add(BigInteger.ONE);
		}
	}
	
	private void actualizarSaldoCaja(BigDecimal monto, BigInteger idMoneda) throws RollbackFailureException{
		Moneda monedaTransaccion = monedaDAO.find(idMoneda);
		Caja caja = this.getCaja();
		Set<BovedaCaja> bovedasCajas = caja.getBovedaCajas();
		for (BovedaCaja bovedaCaja : bovedasCajas) {
			Moneda monedaBoveda = bovedaCaja.getBoveda().getMoneda();
			if(monedaTransaccion.equals(monedaBoveda)){
				BigDecimal saldoActual = bovedaCaja.getSaldo();
				BigDecimal saldoFinal = saldoActual.add(monto);
				if(saldoFinal.compareTo(BigDecimal.ZERO) >= 0){
					bovedaCaja.setSaldo(saldoFinal);
					bovedaCajaDAO.update(bovedaCaja);
				} else {
					throw new RollbackFailureException("Saldo menor a cero, no se puede modificar saldo de caja");
				}
				break;
			}				
		}
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
			historialCajaNew.setTrabajador(trabajador.getPersonaNatural().getApellidoPaterno()+" "+trabajador.getPersonaNatural().getApellidoMaterno()+", "+trabajador.getPersonaNatural().getNombres());
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
		Trabajador trabajador = getTrabajador();
		if(trabajador == null)
			throw new RollbackFailureException("No se encontró un trabajador para la caja");
		
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
		pendienteCaja.setTipoPendiente(monto.compareTo(BigDecimal.ZERO) >= 1 ? TipoPendiente.FALTANTE : TipoPendiente.SOBRANTE);
		pendienteCaja.setObservacion(observacion);
		pendienteCaja.setTrabajador(trabajador.getPersonaNatural().getApellidoPaterno()+" "+trabajador.getPersonaNatural().getApellidoMaterno()+", "+trabajador.getPersonaNatural().getNombres());
		pendienteCajaDAO.create(pendienteCaja);
		
		//modificando el saldo de boveda
		BigDecimal saldoActual = bovedaCajaTransaccion.getSaldo();
		BigDecimal montoTransaccion = (monto.negate());
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
		HistorialCaja historialCaja = getHistorialActivo();
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
		if(historialBoveda == null)
			throw new RollbackFailureException("Boveda no tiene historiales activos");
		
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
		return this.cajaDAO;
	}

	@Override
	public Set<Moneda> getMonedas() {
		Caja caja = getCaja();
		Set<BovedaCaja> bovedaCajas = caja.getBovedaCajas();
		Set<Moneda> result = new HashSet<>();
		for (BovedaCaja bovedaCaja : bovedaCajas) {
			Boveda boveda = bovedaCaja.getBoveda();
			Moneda moneda = boveda.getMoneda();
			Hibernate.initialize(moneda);
			result.add(moneda);
		}
		return result;
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
					Moneda monedaHistorial = d.getMonedaDenominacion().getMoneda();
					if(monedaHistorial.equals(moneda)){
						GenericDetalle detalle = new GenericDetalle(d.getMonedaDenominacion().getValor(), d.getCantidad());
						genericMonedaDetalle.addElementDetalleReplacingIfExist(detalle);	
					}					
				};
			}			
			result.add(genericMonedaDetalle);
		}		
		return result;
	}

	@Override
	public Set<TransaccionBovedaCaja> getTransaccionesEnviadasBovedaCaja() {
		HistorialCaja historial = getHistorialActivo();
		Set<TransaccionBovedaCaja> enviados = historial.getTransaccionBovedaCajas();
		Set<TransaccionBovedaCaja> result = new HashSet<>();
		for (TransaccionBovedaCaja transaccionBovedaCaja : enviados) {
			if(transaccionBovedaCaja.getOrigen().equals(TransaccionBovedaCajaOrigen.CAJA))
				result.add(transaccionBovedaCaja);
		}
		Hibernate.initialize(result);
		return result;
	}

	@Override
	public Set<TransaccionBovedaCaja> getTransaccionesRecibidasBovedaCaja() {
		HistorialCaja historial = getHistorialActivo();
		Set<TransaccionBovedaCaja> enviados = historial.getTransaccionBovedaCajas();
		Set<TransaccionBovedaCaja> result = new HashSet<>();
		for (TransaccionBovedaCaja transaccionBovedaCaja : enviados) {
			if(transaccionBovedaCaja.getOrigen().equals(TransaccionBovedaCajaOrigen.BOVEDA))
				result.add(transaccionBovedaCaja);
		}
		Hibernate.initialize(result);
		return result;
	}

	@Override
	public Set<TransaccionCajaCaja> getTransaccionesEnviadasCajaCaja() {
		HistorialCaja historial = getHistorialActivo();
		Set<TransaccionCajaCaja> enviados = historial.getTransaccionCajaCajasForIdCajaHistorialOrigen();		
		Hibernate.initialize(enviados);
		return enviados;
	}

	@Override
	public Set<TransaccionCajaCaja> getTransaccionesRecibidasCajaCaja() {
		HistorialCaja historial = getHistorialActivo();
		Set<TransaccionCajaCaja> recibidos = historial.getTransaccionCajaCajasForIdCajaHistorialDestino();		
		Hibernate.initialize(recibidos);
		return recibidos;
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

	@Override
	public Set<HistorialCaja> getHistorialCaja(Date dateDesde, Date dateHasta) {
		Caja caja = getCaja();		
		QueryParameter queryParameter = QueryParameter.with("idcaja", caja.getIdCaja()).and("desde", dateDesde).and("hasta", dateHasta);		
		List<HistorialCaja> list = historialCajaDAO.findByNamedQuery(HistorialCaja.findByHistorialDateRange, queryParameter.parameters());		
		return new HashSet<HistorialCaja>(list);
	}
	
	@Override
	public List<HistorialTransaccionCaja> getHistorialTransaccion() {
		HistorialCaja historial = this.getHistorialActivo();
		QueryParameter queryParameter = QueryParameter.with("idHistorialCaja", historial.getIdHistorialCaja());
		List<HistorialTransaccionCaja> list = historialTransaccionCajaDAO.findByNamedQuery(HistorialTransaccionCaja.findByHistorialCaja, queryParameter.parameters());		
		return list;
	}
	
	@Override
	@AllowedTo(Permission.ABIERTO)
	public BigInteger crearDepositoBancario(String numeroCuenta,
			BigDecimal monto, String referencia)
			throws RollbackFailureException {
		
		CuentaBancaria cuentaBancaria = null;
		if(monto.compareTo(BigDecimal.ZERO) != 1){
			throw new RollbackFailureException("Monto invalido para transaccion");
		}
		try {
			QueryParameter queryParameter = QueryParameter.with("numerocuenta", numeroCuenta);
			List<CuentaBancaria> list = cuentaBancariaDAO.findByNamedQuery(CuentaBancaria.findByNumeroCuenta, queryParameter.parameters());
			if(list.size() == 1)
				cuentaBancaria = list.get(0);
			else
				throw new IllegalResultException("Existen mas de una cuenta con el numero de cuenta indicado");
		} catch (IllegalResultException e) {
			LOGGER.error(e.getMessage(), e.getCause(), e.getLocalizedMessage());
			throw new EJBAccessException("Error de inconsistencia de datos");
		}
		
		switch (cuentaBancaria.getEstado()) {
		case CONGELADO:
			throw new RollbackFailureException("Cuenta CONGELADA, no se pueden realizar transacciones");			
		case INACTIVO:
			throw new RollbackFailureException("Cuenta INACTIVO, no se pueden realizar transacciones");
		default:
			break;
		}
		
		//obteniendo datos de caja en session
		HistorialCaja historialCaja = this.getHistorialActivo();
		Trabajador trabajador = this.getTrabajador();
		PersonaNatural natural = trabajador.getPersonaNatural();
		
		//obteniendo saldo disponible de cuenta
		BigDecimal saldoDisponible = cuentaBancaria.getSaldo().add(monto);
		cuentaBancaria.setSaldo(saldoDisponible);
		if(cuentaBancaria.getTipoCuentaBancaria().equals(TipoCuentaBancaria.PLAZO_FIJO)){
			cuentaBancaria.setEstado(EstadoCuentaBancaria.CONGELADO);
		}
		cuentaBancariaDAO.update(cuentaBancaria);
		
		Calendar calendar = Calendar.getInstance();
		
		TransaccionBancaria transaccionBancaria = new TransaccionBancaria();
		transaccionBancaria.setCuentaBancaria(cuentaBancaria);
		transaccionBancaria.setEstado(true);
		transaccionBancaria.setFecha(calendar.getTime());
		transaccionBancaria.setHora(calendar.getTime());
		transaccionBancaria.setHistorialCaja(historialCaja);
		transaccionBancaria.setMonto(monto);
		transaccionBancaria.setNumeroOperacion(this.getNumeroOperacion());
		transaccionBancaria.setObservacion("Doc:"+natural.getTipoDocumento().getAbreviatura()+"/"+natural.getNumeroDocumento()+"Trabajador:"+natural.getApellidoPaterno()+" "+natural.getApellidoMaterno()+","+natural.getNombres());
		transaccionBancaria.setReferencia(referencia);
		transaccionBancaria.setSaldoDisponible(saldoDisponible);
		transaccionBancaria.setTipoTransaccion(Tipotransaccionbancaria.DEPOSITO);
		transaccionBancaria.setMoneda(cuentaBancaria.getMoneda());
		transaccionBancariaDAO.create(transaccionBancaria);				
		//actualizar saldo caja
		this.actualizarSaldoCaja(monto, cuentaBancaria.getMoneda().getIdMoneda());
		
		return transaccionBancaria.getIdTransaccionBancaria();		
	}

	@Override
	@AllowedTo(Permission.ABIERTO)
	public BigInteger crearRetiroBancario(String numeroCuenta,
			BigDecimal monto, String referencia)
			throws RollbackFailureException {
		CuentaBancaria cuentaBancaria = null;
		if(monto.compareTo(BigDecimal.ZERO) != -1){
			throw new RollbackFailureException("Monto invalido para transaccion");
		}
		try {
			QueryParameter queryParameter = QueryParameter.with("numerocuenta", numeroCuenta);
			List<CuentaBancaria> list = cuentaBancariaDAO.findByNamedQuery(CuentaBancaria.findByNumeroCuenta, queryParameter.parameters());
			if(list.size() == 1)
				cuentaBancaria = list.get(0);
			else
				throw new IllegalResultException("Existen mas de una cuenta con el numero de cuenta indicado");
		} catch (IllegalResultException e) {
			LOGGER.error(e.getMessage(), e.getCause(), e.getLocalizedMessage());
			throw new EJBAccessException("Error de inconsistencia de datos");
		}
		
		switch (cuentaBancaria.getEstado()) {
		case CONGELADO:
			throw new RollbackFailureException("Cuenta CONGELADA, no se pueden realizar transacciones");			
		case INACTIVO:
			throw new RollbackFailureException("Cuenta INACTIVO, no se pueden realizar transacciones");
		default:
			break;
		}
		
		//obteniendo datos de caja en session
		HistorialCaja historialCaja = this.getHistorialActivo();
		Trabajador trabajador = this.getTrabajador();
		PersonaNatural natural = trabajador.getPersonaNatural();
		
		//obteniendo saldo disponible de cuenta
		BigDecimal saldoDisponible = cuentaBancaria.getSaldo().add(monto);
		if(saldoDisponible.compareTo(BigDecimal.ZERO) == -1){
			throw new RollbackFailureException("Saldo insuficiente para transaccion");
		} else {
			cuentaBancaria.setSaldo(saldoDisponible);
			cuentaBancariaDAO.update(cuentaBancaria);
		}
		
		Calendar calendar = Calendar.getInstance();
		
		TransaccionBancaria transaccionBancaria = new TransaccionBancaria();
		transaccionBancaria.setCuentaBancaria(cuentaBancaria);
		transaccionBancaria.setEstado(true);
		transaccionBancaria.setFecha(calendar.getTime());
		transaccionBancaria.setHora(calendar.getTime());
		transaccionBancaria.setHistorialCaja(historialCaja);
		transaccionBancaria.setMonto(monto);
		transaccionBancaria.setNumeroOperacion(this.getNumeroOperacion());
		transaccionBancaria.setObservacion("Doc:"+natural.getTipoDocumento().getAbreviatura()+"/"+natural.getNumeroDocumento()+"Trabajador:"+natural.getApellidoPaterno()+" "+natural.getApellidoMaterno()+","+natural.getNombres());
		transaccionBancaria.setReferencia(referencia);
		transaccionBancaria.setSaldoDisponible(saldoDisponible);
		transaccionBancaria.setTipoTransaccion(Tipotransaccionbancaria.RETIRO);
		transaccionBancaria.setMoneda(cuentaBancaria.getMoneda());
		transaccionBancariaDAO.create(transaccionBancaria);				
		//actualizar saldo caja
		this.actualizarSaldoCaja(monto, cuentaBancaria.getMoneda().getIdMoneda());
		
		return transaccionBancaria.getIdTransaccionBancaria();	
	}

	@Override
	@AllowedTo(Permission.ABIERTO)
	public BigInteger crearAporte(BigInteger idSocio, BigDecimal monto,
			int mes, int anio, String referencia)
			throws RollbackFailureException {
		Socio socio = socioDAO.find(idSocio);
		if(socio == null)
			throw new RollbackFailureException("Socio no encontrado");
		CuentaAporte cuentaAporte = socio.getCuentaAporte();
		if(cuentaAporte == null)
			throw new RollbackFailureException("Socio no tiene cuenta de aportes");
				
		if(monto.compareTo(BigDecimal.ZERO) != 1){
			throw new RollbackFailureException("Monto invalido para transaccion");
		}	
		
		switch (cuentaAporte.getEstadoCuenta()) {
		case CONGELADO:
			throw new RollbackFailureException("Cuenta CONGELADA, no se pueden realizar transacciones");			
		case INACTIVO:
			throw new RollbackFailureException("Cuenta INACTIVO, no se pueden realizar transacciones");
		default:
			break;
		}
		
		//obteniendo datos de caja en session
		HistorialCaja historialCaja = this.getHistorialActivo();
		Trabajador trabajador = this.getTrabajador();
		PersonaNatural natural = trabajador.getPersonaNatural();
		
		//obteniendo saldo disponible de cuenta
		BigDecimal saldoDisponible = cuentaAporte.getSaldo().add(monto);
		cuentaAporte.setSaldo(saldoDisponible);
		cuentaAporteDAO.update(cuentaAporte);
		
		Calendar calendar = Calendar.getInstance();
		
		TransaccionCuentaAporte transaccionCuentaAporte = new TransaccionCuentaAporte();
		transaccionCuentaAporte.setAnioAfecta(anio);
		transaccionCuentaAporte.setMesAfecta(mes);
		transaccionCuentaAporte.setCuentaAporte(cuentaAporte);
		transaccionCuentaAporte.setEstado(true);
		transaccionCuentaAporte.setFecha(calendar.getTime());
		transaccionCuentaAporte.setHistorialCaja(historialCaja);
		transaccionCuentaAporte.setHora(calendar.getTime());
		transaccionCuentaAporte.setMonto(monto);
		transaccionCuentaAporte.setNumeroOperacion(this.getNumeroOperacion());
		transaccionCuentaAporte.setReferencia(referencia);
		transaccionCuentaAporte.setObservacion("Doc:"+natural.getTipoDocumento().getAbreviatura()+"/"+natural.getNumeroDocumento()+"Trabajador:"+natural.getApellidoPaterno()+" "+natural.getApellidoMaterno()+","+natural.getNombres());
		transaccionCuentaAporte.setSaldoDisponible(saldoDisponible);
		transaccionCuentaAporte.setTipoTransaccion(Tipotransaccionbancaria.DEPOSITO);
		
		transaccionCuentaAporteDAO.create(transaccionCuentaAporte);	
		
		//actualizar saldo caja
		this.actualizarSaldoCaja(monto, cuentaAporte.getMoneda().getIdMoneda());
		
		return transaccionCuentaAporte.getIdTransaccionCuentaAporte();	
	}
	
	@Override
	public BigInteger retiroCuentaAporte(BigInteger idSocio)
			throws RollbackFailureException {
		Socio socio = socioDAO.find(idSocio);
		if(socio == null)
			throw new RollbackFailureException("Socio no encontrado");
		CuentaAporte cuentaAporte = socio.getCuentaAporte();
		if(cuentaAporte == null)
			throw new RollbackFailureException("Socio no tiene cuenta de aportes");						
		
		switch (cuentaAporte.getEstadoCuenta()) {
		case CONGELADO:
			throw new RollbackFailureException("Cuenta CONGELADA, no se pueden realizar transacciones");			
		case INACTIVO:
			throw new RollbackFailureException("Cuenta INACTIVO, no se pueden realizar transacciones");
		default:
			break;
		}
		
		//obteniendo datos de caja en session
		HistorialCaja historialCaja = this.getHistorialActivo();
		Trabajador trabajador = this.getTrabajador();
		PersonaNatural natural = trabajador.getPersonaNatural();
		
		//obteniendo saldo disponible de cuenta
		BigDecimal montoTransaccion = cuentaAporte.getSaldo().negate();
		BigDecimal saldoDisponible = cuentaAporte.getSaldo().add(montoTransaccion);
		cuentaAporte.setSaldo(saldoDisponible);		
		cuentaAporteDAO.update(cuentaAporte);
		
		Calendar calendar = Calendar.getInstance();
		
		TransaccionCuentaAporte transaccionCuentaAporte = new TransaccionCuentaAporte();
		transaccionCuentaAporte.setCuentaAporte(cuentaAporte);
		transaccionCuentaAporte.setEstado(true);
		transaccionCuentaAporte.setFecha(calendar.getTime());
		transaccionCuentaAporte.setHistorialCaja(historialCaja);
		transaccionCuentaAporte.setHora(calendar.getTime());
		transaccionCuentaAporte.setMonto(montoTransaccion);
		transaccionCuentaAporte.setNumeroOperacion(this.getNumeroOperacion());
		transaccionCuentaAporte.setReferencia("Cancelacion de cuenta");
		transaccionCuentaAporte.setObservacion("Doc:"+natural.getTipoDocumento().getAbreviatura()+"/"+natural.getNumeroDocumento()+"Trabajador:"+natural.getApellidoPaterno()+" "+natural.getApellidoMaterno()+","+natural.getNombres());
		transaccionCuentaAporte.setSaldoDisponible(saldoDisponible);
		transaccionCuentaAporte.setTipoTransaccion(Tipotransaccionbancaria.RETIRO);
		
		transaccionCuentaAporteDAO.create(transaccionCuentaAporte);	
		
		//actualizar saldo caja
		this.actualizarSaldoCaja(montoTransaccion, cuentaAporte.getMoneda().getIdMoneda());
		
		return transaccionCuentaAporte.getIdTransaccionCuentaAporte();	
	}

	@Override
	public BigInteger crearTransferenciaBancaria(String numeroCuentaOrigen,
			String numeroCuentaDestino, BigDecimal monto, String referencia)
			throws RollbackFailureException {
		
		CuentaBancaria cuentaBancariaOrigen = null;
		CuentaBancaria cuentaBancariaDestino = null;
		
		if(monto.compareTo(BigDecimal.ZERO) != 1){
			throw new RollbackFailureException("Monto invalido para transaccion");
		}
		try {
			QueryParameter queryParameter1 = QueryParameter.with("numerocuenta", numeroCuentaOrigen);
			List<CuentaBancaria> list1 = cuentaBancariaDAO.findByNamedQuery(CuentaBancaria.findByNumeroCuenta, queryParameter1.parameters());
			QueryParameter queryParameter2 = QueryParameter.with("numerocuenta", numeroCuentaDestino);
			List<CuentaBancaria> list2 = cuentaBancariaDAO.findByNamedQuery(CuentaBancaria.findByNumeroCuenta, queryParameter2.parameters());
			
			if(list1.size() == 1)
				cuentaBancariaOrigen = list1.get(0);
			else
				throw new IllegalResultException("Existen mas de una cuenta con el numero de cuenta indicado");
			if(list2.size() == 1)
				cuentaBancariaDestino = list2.get(0);
			else
				throw new IllegalResultException("Existen mas de una cuenta con el numero de cuenta indicado");						
		} catch (IllegalResultException e) {
			LOGGER.error(e.getMessage(), e.getCause(), e.getLocalizedMessage());
			throw new EJBAccessException("Error de inconsistencia de datos");
		}
		
		switch (cuentaBancariaOrigen.getEstado()) {
		case CONGELADO:
			throw new RollbackFailureException("Cuenta CONGELADA, no se pueden realizar transacciones");			
		case INACTIVO:
			throw new RollbackFailureException("Cuenta INACTIVO, no se pueden realizar transacciones");
		default:
			break;
		}
		switch (cuentaBancariaDestino.getEstado()) {
		case CONGELADO:
			throw new RollbackFailureException("Cuenta CONGELADA, no se pueden realizar transacciones");			
		case INACTIVO:
			throw new RollbackFailureException("Cuenta INACTIVO, no se pueden realizar transacciones");
		default:
			break;
		}
		
		//obteniendo datos de caja en session
		HistorialCaja historialCaja = this.getHistorialActivo();
		Trabajador trabajador = this.getTrabajador();
		PersonaNatural natural = trabajador.getPersonaNatural();
		
		//obteniendo saldo disponible de cuenta
		BigDecimal saldoDisponibleOrigen = cuentaBancariaOrigen.getSaldo().subtract(monto);
		BigDecimal saldoDisponibleDestino = cuentaBancariaDestino.getSaldo().add(monto);
		if(saldoDisponibleOrigen.compareTo(BigDecimal.ZERO) == -1)
			throw new RollbackFailureException("Saldo insuficiente para transferencia");
		
		cuentaBancariaOrigen.setSaldo(saldoDisponibleOrigen);
		cuentaBancariaDestino.setSaldo(saldoDisponibleDestino);
		if(cuentaBancariaDestino.getTipoCuentaBancaria().equals(TipoCuentaBancaria.PLAZO_FIJO)){
			cuentaBancariaDestino.setEstado(EstadoCuentaBancaria.CONGELADO);
		}
		cuentaBancariaDAO.update(cuentaBancariaOrigen);
		cuentaBancariaDAO.update(cuentaBancariaDestino);
		
		Calendar calendar = Calendar.getInstance();
		
		TransferenciaBancaria transferenciaBancaria = new TransferenciaBancaria();
		transferenciaBancaria.setIdTransferenciaBancaria(null);
		transferenciaBancaria.setCuentaBancariaOrigen(cuentaBancariaOrigen);
		transferenciaBancaria.setCuentaBancariaDestino(cuentaBancariaDestino);
		transferenciaBancaria.setEstado(true);
		transferenciaBancaria.setFecha(calendar.getTime());
		transferenciaBancaria.setHora(calendar.getTime());
		transferenciaBancaria.setHistorialCaja(historialCaja);
		transferenciaBancaria.setMonto(monto);
		transferenciaBancaria.setNumeroOperacion(this.getNumeroOperacion());
		transferenciaBancaria.setObservacion("Doc:"+natural.getTipoDocumento().getAbreviatura()+"/"+natural.getNumeroDocumento()+"Trabajador:"+natural.getApellidoPaterno()+" "+natural.getApellidoMaterno()+","+natural.getNombres());
		transferenciaBancaria.setReferencia(referencia);
		transferenciaBancaria.setSaldoDisponibleOrigen(saldoDisponibleOrigen);
		transferenciaBancaria.setSaldoDisponibleDestino(saldoDisponibleDestino);
		
		transferenciaBancariaDAO.create(transferenciaBancaria);
		return transferenciaBancaria.getIdTransferenciaBancaria();	
	}

	@Override
	public BigInteger crearCompraVenta(Tipotransaccioncompraventa tipoTransaccion,
			BigInteger idMonedaRecibido,
			BigInteger idMonedaEntregado, BigDecimal montoRecibido,
			BigDecimal montoEntregado, BigDecimal tasaCambio, String referencia)
			throws RollbackFailureException {
		Moneda monedaRecibida = monedaDAO.find(idMonedaRecibido);
		Moneda monedaEntregada = monedaDAO.find(idMonedaEntregado);
		if(monedaRecibida == null || monedaEntregada == null)
			throw new RollbackFailureException("Monedas no encontradas");
		
		Calendar calendar = Calendar.getInstance();		
		
		HistorialCaja historialCaja = this.getHistorialActivo();
		Trabajador trabajador = this.getTrabajador();
		PersonaNatural natural = trabajador.getPersonaNatural();
		
		TransaccionCompraVenta transaccionCompraVenta = new TransaccionCompraVenta();
		transaccionCompraVenta.setIdTransaccionCompraVenta(null);
		transaccionCompraVenta.setEstado(true);
		transaccionCompraVenta.setFecha(calendar.getTime());
		transaccionCompraVenta.setHora(calendar.getTime());
		transaccionCompraVenta.setHistorialCaja(historialCaja);
		transaccionCompraVenta.setMonedaEntregada(monedaEntregada);
		transaccionCompraVenta.setMonedaRecibida(monedaRecibida);
		transaccionCompraVenta.setMontoEntregado(montoEntregado);
		transaccionCompraVenta.setMontoRecibido(montoRecibido);
		transaccionCompraVenta.setNumeroOperacion(this.getNumeroOperacion());
		transaccionCompraVenta.setObservacion("Doc:"+natural.getTipoDocumento().getAbreviatura()+"/"+natural.getNumeroDocumento()+"Trabajador:"+natural.getApellidoPaterno()+" "+natural.getApellidoMaterno()+","+natural.getNombres());
		transaccionCompraVenta.setReferencia(referencia);
		transaccionCompraVenta.setTipoCambio(tasaCambio);
		transaccionCompraVenta.setTipoTransaccion(tipoTransaccion);
		
		transaccionCompraVentaDAO.create(transaccionCompraVenta);
		
		return transaccionCompraVenta.getIdTransaccionCompraVenta();
	}

	@Override
	public BigInteger[] crearCuentaBancariaPlazoFijoConDeposito(String codigo,
			BigInteger idMoneda, TipoPersona tipoPersona, BigInteger idPersona,
			int cantRetirantes, BigDecimal monto, int periodo,
			BigDecimal tasaInteres, List<BigInteger> titulares,
			List<Beneficiario> beneficiarios) throws RollbackFailureException {
		
		BigInteger idCuentaBancaria = cuentaBancariaService.crearCuentaBancaria(TipoCuentaBancaria.PLAZO_FIJO, 
				codigo, idMoneda, tasaInteres, tipoPersona, idPersona, new Integer(periodo),
				cantRetirantes, titulares, beneficiarios);
		CuentaBancaria cuentaBancaria = cuentaBancariaDAO.find(idCuentaBancaria);
		String numeroCuenta = cuentaBancaria.getNumeroCuenta();
		BigInteger idTransaccion = crearDepositoBancario(numeroCuenta, monto, "APERTURA CUENTA BANCARIA PLAZO FIJO");
		
		return new BigInteger[]{idCuentaBancaria,idTransaccion};
	}
	
	@Override
	public BigInteger cancelarCuentaBancariaConRetiro(BigInteger idCuentaBancaria) throws RollbackFailureException {
		CuentaBancaria cuentaBancaria = cuentaBancariaDAO.find(idCuentaBancaria);
		if(cuentaBancaria == null)
			throw new RollbackFailureException("Cuenta bancaria no encontrada");
		if(!cuentaBancaria.getEstado().equals(EstadoCuentaBancaria.ACTIVO))
			throw new RollbackFailureException("La cuenta no esta activa, no se puede cancelar");
		if(cuentaBancaria.getTipoCuentaBancaria().equals(TipoCuentaBancaria.PLAZO_FIJO)){
			if(cuentaBancaria.getFechaCierre().compareTo(Calendar.getInstance().getTime()) == 1)
				throw new RollbackFailureException("La cuenta PLAZO_FIJO tiene fecha de cierre aun no vencida");
		}	
		
		cuentaBancariaService.capitalizarCuenta(idCuentaBancaria);
		
		String numeroCuenta = cuentaBancaria.getNumeroCuenta();
		BigDecimal monto = cuentaBancaria.getSaldo().negate();
		String referencia = "RETIRO POR CANCELACION DE CUENTA";
		
		BigInteger idTransaccion = crearRetiroBancario(numeroCuenta, monto, referencia);		
		cuentaBancariaService.cancelarCuentaBancaria(idCuentaBancaria);
		return idTransaccion;
	}
	
}
