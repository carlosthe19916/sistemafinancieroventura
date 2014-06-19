package org.ventura.sistemafinanciero.control;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

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
import org.ventura.sistemafinanciero.entity.Accionista;
import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.CuentaAporte;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.Socio;
import org.ventura.sistemafinanciero.entity.SocioView;
import org.ventura.sistemafinanciero.entity.TipoDocumento;
import org.ventura.sistemafinanciero.entity.type.EstadoCuentaAporte;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.PersonaJuridicaService;
import org.ventura.sistemafinanciero.service.PersonaNaturalService;
import org.ventura.sistemafinanciero.service.SocioService;
import org.ventura.sistemafinanciero.util.ProduceObject;

@Named
@Stateless
@Remote(SocioService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SocioServiceBean extends AbstractServiceBean<Socio> implements SocioService {

	private static Logger LOGGER = LoggerFactory.getLogger(SocioServiceBean.class);

	@Inject
	private DAO<Object, Socio> socioDAO;
	@Inject
	private DAO<Object, SocioView> socioViewDAO;
	@Inject
	private DAO<Object, CuentaAporte> cuentaAporteDAO;
	@Inject
	private DAO<Object, CuentaBancaria> cuentaBancariaDAO;
	@Inject
	private DAO<Object, Agencia> agenciaDAO;
	
	@EJB
	private PersonaNaturalService personaNaturalService;
	@EJB
	private PersonaJuridicaService personaJuridicaService;		
	
	@Override
	public List<SocioView> findByFilterText(String filterText, BigInteger[] range, Boolean modeSocio, Boolean modeEstado) {
		List<SocioView> result = null;	
		if (filterText == null)
			return new ArrayList<SocioView>();
		if (filterText.isEmpty() || filterText.trim().isEmpty()) {
			return new ArrayList<SocioView>();
		}
		if(modeSocio == null)
			modeSocio = true;
		
		List<Boolean> listEstado = new ArrayList<>();
		if(modeEstado != null){
			listEstado.add(modeEstado);
		} else {
			listEstado.add(true);
			listEstado.add(false);
		}		
		QueryParameter queryParameter = QueryParameter.with("modeEstado", listEstado).and("filtertext", '%' + filterText.toUpperCase() + '%');	
		if(range != null){
			if(range.length != 2)
				return null;
			if(range[0] == null || range[1] == null)
				return null;
			if(range[0].compareTo(BigInteger.ZERO) < 0 )
				return null;
			if(range[1].compareTo(BigInteger.ZERO) < 0 )
				return null;
			if(range[0].compareTo(range[1]) == 0)
				return new ArrayList<>();
			int[] rangeInt = {range[0].intValue(),range[1].intValue()};						
									
			if(modeSocio){				
				result = socioViewDAO.findByNamedQuery(SocioView.FindByFilterTextSocioView, queryParameter.parameters(), rangeInt);
			} else {
				result = socioViewDAO.findByNamedQuery(SocioView.FindByFilterTextSocioViewAllHaveCuentaAporte, queryParameter.parameters(), rangeInt);
			}						
		} else {
			if(modeSocio){
				result = socioViewDAO.findByNamedQuery(SocioView.FindByFilterTextSocioView, queryParameter.parameters());
			} else {
				result = socioViewDAO.findByNamedQuery(SocioView.FindByFilterTextSocioViewAllHaveCuentaAporte, queryParameter.parameters());
			}				
		}			
		return result;
	}
	
	@Override
	public List<SocioView> findAllView(BigInteger[] range, Boolean modeSocio, Boolean modeEstado) {	
		List<SocioView> result = null;	
		if(modeSocio == null)
			modeSocio = true;
		
		List<Boolean> listEstado = new ArrayList<>();
		if(modeEstado != null){
			listEstado.add(modeEstado);
		} else {
			listEstado.add(true);
			listEstado.add(false);
		}		
		QueryParameter queryParameter = QueryParameter.with("modeEstado", listEstado);		
		if(range != null){
			if(range.length != 2)
				return null;
			if(range[0] == null || range[1] == null)
				return null;
			if(range[0].compareTo(BigInteger.ZERO) < 0 )
				return null;
			if(range[1].compareTo(BigInteger.ZERO) < 0 )
				return null;
			if(range[0].compareTo(range[1]) == 0)
				return new ArrayList<>();
			int[] rangeInt = {range[0].intValue(),range[1].intValue()};						
									
			if(modeSocio){
				//todos
				result = socioViewDAO.findByNamedQuery(SocioView.findAll, queryParameter.parameters(), rangeInt);
			} else {
				//con cuenta aporte
				result = socioViewDAO.findByNamedQuery(SocioView.FindAllHaveCuentaAporte, queryParameter.parameters(), rangeInt);
			}						
		} else {
			if(modeSocio){
				//todos
				result = socioViewDAO.findByNamedQuery(SocioView.findAll, queryParameter.parameters());
			} else {
				//con cuenta aporte
				result = socioViewDAO.findByNamedQuery(SocioView.FindAllHaveCuentaAporte, queryParameter.parameters());
			}				
		}			
		return result;
	}
	
	@Override
	public Socio findSocio(TipoPersona tipoPersona, BigInteger idTipoDoc,String numDoc) {
		switch (tipoPersona) {
		case NATURAL:
			QueryParameter queryParameter1 = QueryParameter.with("idtipodocumento", idTipoDoc).and("numerodocumento", numDoc);
			List<Socio> list1 = socioDAO.findByNamedQuery(Socio.FindByPNTipoAndNumeroDocumento ,queryParameter1.parameters());
			if(list1.size() == 1)
				return list1.get(0);
			break;
		case JURIDICA:
			QueryParameter queryParameter2 = QueryParameter.with("idtipodocumento", idTipoDoc).and("numerodocumento", numDoc);
			List<Socio> list2 = socioDAO.findByNamedQuery(Socio.FindByPJTipoAndNumeroDocumento ,queryParameter2.parameters());
			if(list2.size() == 1)
				return list2.get(0);
			break;
		default:
			return null;
		}
		return null;
	}
	
	@Override
	public PersonaNatural getPersonaNatural(BigInteger idSocio) {
		Socio socio = socioDAO.find(idSocio);
		if(socio == null)
			return null;
		PersonaNatural persona = socio.getPersonaNatural();
		if(persona != null) {
			Hibernate.initialize(persona);
			TipoDocumento documento = persona.getTipoDocumento();
			Hibernate.initialize(documento);
		}		
		return persona;
	}
	
	@Override
	public PersonaJuridica getPersonaJuridica(BigInteger idSocio) {
		Socio socio = socioDAO.find(idSocio);
		if(socio == null)
			return null;
		PersonaJuridica persona = socio.getPersonaJuridica();
		if(persona != null) {
			Hibernate.initialize(persona);
		
			Set<Accionista> accionistas = persona.getAccionistas();
			PersonaNatural personaNatural = persona.getRepresentanteLegal();
			TipoDocumento tipoDocumento = persona.getTipoDocumento();
			Hibernate.initialize(accionistas);
			Hibernate.initialize(personaNatural);
			Hibernate.initialize(tipoDocumento);
		}
		return persona;
	}
	
	@Override
	public PersonaNatural getApoderado(BigInteger idSocio) {
		Socio socio = socioDAO.find(idSocio);
		if(socio == null)
			return null;
		PersonaNatural persona = socio.getApoderado();
		if(persona != null){
			Hibernate.initialize(persona);
			TipoDocumento documento = persona.getTipoDocumento();
			Hibernate.initialize(documento);
		}			
		return persona;
	}
	
	@Override
	public CuentaAporte getCuentaAporte(BigInteger idSocio) {
		Socio socio = socioDAO.find(idSocio);
		if(socio == null)
			return null;
		CuentaAporte cuentaAporte = socio.getCuentaAporte();
		if(cuentaAporte != null){
			Hibernate.initialize(cuentaAporte);
			Moneda moneda = cuentaAporte.getMoneda();
			Hibernate.initialize(moneda);
		}
			
		return cuentaAporte;
	}
	
	@Override
	public Set<CuentaBancaria> getCuentasBancarias(BigInteger idSocio) {
		Socio socio = socioDAO.find(idSocio);
		if(socio == null)
			return null;
		Set<CuentaBancaria> cuentas =  socio.getCuentaBancarias();
		for (CuentaBancaria cuentaBancaria : cuentas) {
			Hibernate.initialize(cuentaBancaria);
			Moneda moneda = cuentaBancaria.getMoneda();
			Hibernate.initialize(moneda);
		}
		return cuentas;
	}
	
	@Override
	public BigInteger create(BigInteger idAgencia, TipoPersona tipoPersona, BigInteger idDocSocio,
			String numDocSocio, BigInteger idDocApoderado,
			String numDocApoderado) throws RollbackFailureException {
		PersonaNatural personaNatural = null;
		PersonaJuridica personaJuridica = null;
		PersonaNatural apoderado = null;
		Agencia agencia = agenciaDAO.find(idAgencia);
		
		if(agencia == null)
			throw new RollbackFailureException("Agencia no encontrada");
		
		if(idDocApoderado != null && numDocApoderado != null) {
			apoderado = personaNaturalService.findByTipoNumeroDocumento(idDocApoderado, numDocApoderado);
			if(apoderado == null)
				throw new RollbackFailureException("Apoderado no encontrado");
		}
			
		Calendar calendar = Calendar.getInstance();
		switch (tipoPersona) {
		case NATURAL:
			personaNatural = personaNaturalService.findByTipoNumeroDocumento(idDocSocio, numDocSocio);
			if(personaNatural == null)
				throw new RollbackFailureException("Persona para socio no encontrado");
			if(apoderado != null)
				if(personaNatural.equals(apoderado))
					throw new RollbackFailureException("Apoderado y socio deben de ser diferentes");
			break;
		case JURIDICA:
			personaJuridica = personaJuridicaService.findByTipoNumeroDocumento(idDocSocio, numDocSocio);
			if(personaJuridica == null)
				throw new RollbackFailureException("Persona para socio no encontrado");
			if(apoderado != null)
				if(personaJuridica.getRepresentanteLegal().equals(apoderado))
					throw new RollbackFailureException("Apoderado y representante legal deben de ser diferentes");
			break;
		default:
			throw new RollbackFailureException("Tipo de persona no valido");
		}		
		
		//verificar si el socio ya existe
		Socio socio = findSocio(tipoPersona, idDocSocio,numDocSocio);
		if(socio != null) {		
			if(socio.getEstado()) {
				CuentaAporte aporte = socio.getCuentaAporte();
				if (aporte == null) {
					CuentaAporte cuentaAporte = new CuentaAporte();
					cuentaAporte.setNumeroCuenta(agencia.getCodigo());
					cuentaAporte.setEstadoCuenta(EstadoCuentaAporte.ACTIVO);
					cuentaAporte.setMoneda(ProduceObject.getMonedaPrincipal());
					cuentaAporte.setSaldo(BigDecimal.ZERO);
					cuentaAporte.setSocios(null);
					cuentaAporteDAO.create(cuentaAporte);

					String numeroCuenta = ProduceObject.getNumeroCuenta(cuentaAporte);
					cuentaAporte.setNumeroCuenta(numeroCuenta);
					cuentaAporteDAO.update(cuentaAporte);

					socio.setCuentaAporte(cuentaAporte);
					socio.setApoderado(apoderado);
					socioDAO.update(socio);
				} else {
					throw new RollbackFailureException("Socio ya existente, y tiene cuenta aportes activa");
				}
			} else {
				CuentaAporte cuentaAporte = new CuentaAporte();
				cuentaAporte.setNumeroCuenta(agencia.getCodigo());
				cuentaAporte.setEstadoCuenta(EstadoCuentaAporte.ACTIVO);
				cuentaAporte.setMoneda(ProduceObject.getMonedaPrincipal());
				cuentaAporte.setSaldo(BigDecimal.ZERO);
				cuentaAporte.setSocios(null);
				cuentaAporteDAO.create(cuentaAporte);					
				
				String numeroCuenta = ProduceObject.getNumeroCuenta(cuentaAporte);
				cuentaAporte.setNumeroCuenta(numeroCuenta);
				cuentaAporteDAO.update(cuentaAporte);
				
				socio = new Socio();
				socio.setPersonaNatural(personaNatural);
				socio.setPersonaJuridica(personaJuridica);
				socio.setApoderado(apoderado);
				socio.setCuentaAporte(cuentaAporte);
				socio.setEstado(true);
				socio.setFechaInicio(calendar.getTime());
				socio.setFechaFin(null);
				socioDAO.create(socio);
			}			
		} else {			
			CuentaAporte cuentaAporte = new CuentaAporte();
			cuentaAporte.setNumeroCuenta(agencia.getCodigo());
			cuentaAporte.setEstadoCuenta(EstadoCuentaAporte.ACTIVO);
			cuentaAporte.setMoneda(ProduceObject.getMonedaPrincipal());
			cuentaAporte.setSaldo(BigDecimal.ZERO);
			cuentaAporte.setSocios(null);
			cuentaAporteDAO.create(cuentaAporte);					
			
			String numeroCuenta = ProduceObject.getNumeroCuenta(cuentaAporte);
			cuentaAporte.setNumeroCuenta(numeroCuenta);
			cuentaAporteDAO.update(cuentaAporte);
			
			socio = new Socio();
			socio.setPersonaNatural(personaNatural);
			socio.setPersonaJuridica(personaJuridica);
			socio.setApoderado(apoderado);
			socio.setCuentaAporte(cuentaAporte);
			socio.setEstado(true);
			socio.setFechaInicio(calendar.getTime());
			socio.setFechaFin(null);
			socioDAO.create(socio);
		}				
		
		return socio.getIdSocio();
	}
	
	@Override
	protected DAO<Object, Socio> getDAO() {
		return this.socioDAO;
	}

	@Override
	public Socio findSocioByCuenta(BigInteger idCuentaBancaria) {
		CuentaBancaria cuentaBancaria = cuentaBancariaDAO.find(idCuentaBancaria);
		if(cuentaBancaria == null)
			return null;
		Socio socio = cuentaBancaria.getSocio();
		PersonaNatural personaNatural = socio.getPersonaNatural();
		PersonaJuridica personaJuridica = socio.getPersonaJuridica();
		Hibernate.initialize(socio);
		if(personaNatural != null){
			TipoDocumento documento = personaNatural.getTipoDocumento();
			Hibernate.initialize(personaNatural);	
			Hibernate.initialize(documento);
		}			
		if(personaJuridica != null){
			TipoDocumento documento = personaJuridica.getTipoDocumento();
			Set<Accionista> accionistas = personaJuridica.getAccionistas();
			Hibernate.initialize(personaJuridica);	
			Hibernate.initialize(documento);
			Hibernate.initialize(accionistas);
		}	 	
		return socio;
	}

	

	

	

}
