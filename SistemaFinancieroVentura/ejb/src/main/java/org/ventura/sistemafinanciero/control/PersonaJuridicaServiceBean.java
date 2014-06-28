package org.ventura.sistemafinanciero.control;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.dao.QueryParameter;
import org.ventura.sistemafinanciero.entity.Accionista;
import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.TipoDocumento;
import org.ventura.sistemafinanciero.exception.IllegalResultException;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;
import org.ventura.sistemafinanciero.service.PersonaJuridicaService;

@Named
@Stateless
@Remote(PersonaJuridicaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PersonaJuridicaServiceBean extends AbstractServiceBean<PersonaJuridica> implements PersonaJuridicaService {

	private static Logger LOGGER = LoggerFactory.getLogger(PersonaJuridicaServiceBean.class);

	@Inject
	private DAO<Object, PersonaJuridica> personaJuridicaDAO;
	
	@Inject
	private DAO<Object, PersonaNatural> personaNaturalDAO;
	
	@Inject
	private DAO<Object, Accionista> accionistaDAO;
	
	@Inject
    private Validator validator;

	@Override
	public PersonaJuridica findById(BigInteger id){
		if(id == null)
			return null;
		PersonaJuridica persona = personaJuridicaDAO.find(id);
		if(persona != null){
			PersonaNatural representante = persona.getRepresentanteLegal();
			TipoDocumento documentoRepresentante = representante.getTipoDocumento();
			TipoDocumento documento = persona.getTipoDocumento();
			Set<Accionista> accionistas = persona.getAccionistas();			
			Hibernate.initialize(representante);
			Hibernate.initialize(documentoRepresentante);
			Hibernate.initialize(documento);
			for (Accionista accionista : accionistas) {
				PersonaNatural person = accionista.getPersonaNatural();
				TipoDocumento docPerson = person.getTipoDocumento();
				Hibernate.initialize(accionista);
				Hibernate.initialize(person);
				Hibernate.initialize(docPerson);
			}
		}
		return persona;
	}
	
	@Override
	public PersonaJuridica find(BigInteger idTipodocumento, String numerodocumento) {
		if(idTipodocumento == null || numerodocumento == null)
			return null;
		if(numerodocumento.isEmpty() || numerodocumento.trim().isEmpty())
			return null;
		PersonaJuridica result = null;
		try {
			QueryParameter queryParameter = QueryParameter.with("idtipodocumento",idTipodocumento).and("numerodocumento", numerodocumento);
			List<PersonaJuridica> list = personaJuridicaDAO.findByNamedQuery(PersonaJuridica.FindByTipoAndNumeroDocumento,queryParameter.parameters());
			if (list.size() > 1)
				throw new IllegalResultException("Se encontró mas de una persona con idDocumento:" + idTipodocumento + " y numero de documento:" + numerodocumento);
			else 
				for (PersonaJuridica personaJuridica : list) {
					result = personaJuridica;
					TipoDocumento tipoDocumento = result.getTipoDocumento();
					PersonaNatural representante = result.getRepresentanteLegal();
					TipoDocumento documentoRepre = representante.getTipoDocumento();
					Set<Accionista> accionistas = result.getAccionistas();
					Hibernate.initialize(representante);
					Hibernate.initialize(accionistas);
					Hibernate.initialize(tipoDocumento);
					Hibernate.initialize(documentoRepre);
				}
		} catch (IllegalResultException e) {
			LOGGER.error(e.getMessage(), e.getLocalizedMessage(), e.getCause());
		}
		return result;
	}
	
	@Override
	public List<PersonaJuridica> findAll(){
		return findAll(null, null);
	}

	@Override
	public List<PersonaJuridica> findAll(BigInteger offset, BigInteger limit) {
		return findAll(null, offset, limit);
	}

	@Override
	public List<PersonaJuridica> findAll(String filterText) {
		return findAll(filterText, null, null);
	}

	@Override
	public List<PersonaJuridica> findAll(String filterText, BigInteger offset, BigInteger limit) {
		List<PersonaJuridica> result = null;
		
		if(filterText == null)
			filterText = "";
		if(offset == null) {			
			offset = BigInteger.ZERO;			
		}
		offset = offset.abs();
		if(limit != null){
			limit = limit.abs();			
		}
		Integer offSetInteger = offset.intValue();
		Integer limitInteger = (limit != null ? limit.intValue() : null);
		
		QueryParameter queryParameter = QueryParameter.with("filtertext", '%' + filterText.toUpperCase() + '%');
		result = personaJuridicaDAO.findByNamedQuery(PersonaJuridica.FindByFilterText, queryParameter.parameters(), offSetInteger, limitInteger);			
		if(result != null){
			for (PersonaJuridica personaJuridica : result) {
				Set<Accionista> accionistas = personaJuridica.getAccionistas();
				PersonaNatural representante = personaJuridica.getRepresentanteLegal();
				TipoDocumento tipoDocumento = personaJuridica.getTipoDocumento();			
				Hibernate.initialize(representante);
				Hibernate.initialize(tipoDocumento);	
				for (Accionista accionista : accionistas) {				
					PersonaNatural p = accionista.getPersonaNatural();
					TipoDocumento doc = p.getTipoDocumento();
					Hibernate.initialize(accionista);
					Hibernate.initialize(p);
					Hibernate.initialize(doc);
				}
			}
		}
		return result;	
	}

	@Override
	public PersonaJuridica create(PersonaJuridica personaJuridica) throws PreexistingEntityException {	
		Set<ConstraintViolation<PersonaJuridica>> violations = validator.validate(personaJuridica);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

		TipoDocumento tipoDocumento = personaJuridica.getTipoDocumento();
		String numeroDocumento = personaJuridica.getNumeroDocumento();
		Set<Accionista> accionistas = personaJuridica.getAccionistas();
		
		Object obj = find(tipoDocumento.getIdTipoDocumento(), numeroDocumento);
		if (obj == null)
			personaJuridicaDAO.create(personaJuridica);
		else
			throw new PreexistingEntityException("La persona con el Tipo y Numero de documento ya existe");
		
		//crear accionistas		
		for (Accionista accionista : accionistas) {
			PersonaNatural personaNatural = accionista.getPersonaNatural();
			if(personaNatural != null){
				BigInteger idPersona = personaNatural.getIdPersonaNatural();
				if(idPersona != null){
					PersonaNatural persona = personaNaturalDAO.find(idPersona);
					if(persona != null){
						accionista.setPersonaJuridica(personaJuridica);			
						accionistaDAO.create(accionista);	
					} else {
						throw new EJBException("Accionista no encontrado");
					}
				}				
			}			
		}				
		return personaJuridica;		
	}
	
	@Override
	public void update(BigInteger id, PersonaJuridica personaJuridica) throws NonexistentEntityException, PreexistingEntityException {	
		Set<ConstraintViolation<PersonaJuridica>> violations = validator.validate(personaJuridica);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

		TipoDocumento tipoDocumento = personaJuridica.getTipoDocumento();
		String numeroDocumento = personaJuridica.getNumeroDocumento();
		Set<Accionista> accionistas = personaJuridica.getAccionistas();
		
		PersonaJuridica personaDB = find(tipoDocumento.getIdTipoDocumento(), numeroDocumento);
		PersonaJuridica personaById = personaJuridicaDAO.find(id);
		if(personaById == null)
			throw new PreexistingEntityException("Persona juridica no encontrada");
		
		personaJuridica.setIdPersonaJuridica(id);
		if (personaDB != null){
			if(personaById.getIdPersonaJuridica().equals(personaDB.getIdPersonaJuridica())){
				personaJuridicaDAO.update(personaJuridica);
			} else {
				throw new PreexistingEntityException("El document ya existe");
			}
		}			
		else {
			personaJuridicaDAO.update(personaJuridica);
		}			
		
		/*
		//crear accionistas		
		for (Accionista accionista : accionistas) {
			PersonaNatural personaNatural = accionista.getPersonaNatural();
			if(personaNatural != null){
				BigInteger idPersona = personaNatural.getIdPersonaNatural();
				if(idPersona != null){
					PersonaNatural persona = personaNaturalDAO.find(idPersona);
					if(persona != null){
						accionista.setPersonaJuridica(personaJuridica);			
						accionistaDAO.create(accionista);	
					} else {
						throw new RollbackFailureException("Accionista no encontrado");
					}
				}				
			}			
		}
			*/		
	}
	
	@Override
	protected DAO<Object, PersonaJuridica> getDAO() {
		return this.personaJuridicaDAO;
	}

}
