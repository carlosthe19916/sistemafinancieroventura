package org.ventura.sistemafinanciero.control;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashSet;
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
	
	@EJB
	private PersonaNaturalService personaNaturalService;
	@EJB
	private PersonaJuridicaService personaJuridicaService;
	
	@Override
	public Set<SocioView> findByFilterText(String filterText) {
		if (filterText == null)
			return new HashSet<SocioView>();
		if (filterText.isEmpty() || filterText.trim().isEmpty()) {
			return new HashSet<SocioView>();
		}
		List<SocioView> list = null;
		QueryParameter queryParameter = QueryParameter.with("filtertext", '%' + filterText.toUpperCase() + '%');
		list = socioViewDAO.findByNamedQuery(SocioView.FindByFilterTextSocioView, queryParameter.parameters(), 1000);
		return new HashSet<SocioView>(list);
	}
	
	@Override
	public Set<SocioView> findAllView() {
		List<SocioView> list = socioViewDAO.findAll();
		return new HashSet<>(list);
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
			TipoDocumento documento = persona.getTipoDocumento();
			Hibernate.initialize(documento);
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
	public BigInteger create(TipoPersona tipoPersona, BigInteger idDocSocio,
			String numDocSocio, BigInteger idDocApoderado,
			String numDocApoderado) throws RollbackFailureException {
		PersonaNatural personaNatural = null;
		PersonaJuridica personaJuridica = null;
		PersonaNatural apoderado = personaNaturalService.findByTipoNumeroDocumento(idDocApoderado, numDocApoderado);
		Calendar calendar = Calendar.getInstance();
		switch (tipoPersona) {
		case NATURAL:
			personaNatural = personaNaturalService.findByTipoNumeroDocumento(idDocSocio, numDocSocio);
			if(personaNatural == null)
				throw new RollbackFailureException("Persona para socio no encontrado");
			break;
		case JURIDICA:
			personaJuridica = personaJuridicaService.findByTipoNumeroDocumento(idDocSocio, numDocSocio);
			break;
		default:
			throw new RollbackFailureException("Tipo de persona no valido");
		}
						
		//verificar si el socio ya existe
		Socio socio = findSocio(tipoPersona, idDocSocio,numDocSocio);
		if(socio != null){
			if(!socio.getEstado()){
				CuentaAporte aporte = socio.getCuentaAporte();
				if(aporte != null){
					CuentaAporte cuentaAporte = new CuentaAporte();
					cuentaAporte.setEstadoCuenta(EstadoCuentaAporte.ACTIVO);
					cuentaAporte.setMoneda(ProduceObject.getMonedaPrincipal());
					cuentaAporte.setSaldo(BigDecimal.ZERO);
					cuentaAporte.setSocios(null);
					cuentaAporteDAO.create(cuentaAporte);
					
					socio.setCuentaAporte(cuentaAporte);
					socio.setApoderado(apoderado);
					socioDAO.update(socio);
				} else {
					throw new RollbackFailureException("Socio ya existente, y tiene cuenta aportes activa");
				}
			} else {
				throw new RollbackFailureException("Socio ya existente, no se puede registrar nuevamente");
			}
		} else {			
			CuentaAporte cuentaAporte = new CuentaAporte();
			cuentaAporte.setEstadoCuenta(EstadoCuentaAporte.ACTIVO);
			cuentaAporte.setMoneda(ProduceObject.getMonedaPrincipal());
			cuentaAporte.setSaldo(BigDecimal.ZERO);
			cuentaAporte.setSocios(null);
			cuentaAporteDAO.create(cuentaAporte);					
			
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

}
