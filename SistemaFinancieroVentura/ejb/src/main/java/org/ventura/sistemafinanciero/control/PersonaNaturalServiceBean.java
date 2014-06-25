package org.ventura.sistemafinanciero.control;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.TipoDocumento;
import org.ventura.sistemafinanciero.exception.IllegalResultException;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;
import org.ventura.sistemafinanciero.service.PersonaNaturalService;

@Named
@Stateless
@Remote(PersonaNaturalService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PersonaNaturalServiceBean extends AbstractServiceBean<PersonaNatural> implements PersonaNaturalService {

	private static Logger LOGGER = LoggerFactory.getLogger(PersonaNaturalServiceBean.class);

	@Inject
	private DAO<Object, PersonaNatural> personaNaturalDAO;

	@Inject
	private Validator validator;	
	
	@Override
	public PersonaNatural findById(BigInteger id){
		if(id == null)
			return null;
		PersonaNatural persona = personaNaturalDAO.find(id);
		if(persona != null){
			TipoDocumento documento = persona.getTipoDocumento();
			Hibernate.initialize(documento);
		}		
		return persona;
	}

	@Override
	public PersonaNatural find(BigInteger idTipoDocumento, String numeroDocumento) {
		if(idTipoDocumento == null || numeroDocumento == null)
			return null;
		
		numeroDocumento = numeroDocumento.trim();
		if(numeroDocumento.isEmpty())
			return null;
		
		PersonaNatural result = null;
		try {
			QueryParameter queryParameter = QueryParameter.with("idTipoDocumento",idTipoDocumento).and("numeroDocumento", numeroDocumento);
			List<PersonaNatural> list = personaNaturalDAO.findByNamedQuery(PersonaNatural.FindByTipoAndNumeroDocumento, queryParameter.parameters());
			if (list.size() > 1)
				throw new IllegalResultException("Se encontró mas de una persona con idDocumento:" + idTipoDocumento + " y numero de documento:" + numeroDocumento);
			else 
				for (PersonaNatural personaNatural : list) {
					result = personaNatural;
					TipoDocumento tipoDocumento = result.getTipoDocumento();
					Hibernate.initialize(tipoDocumento);
				}
		} catch (IllegalResultException e) {
			LOGGER.error(e.getMessage(), e.getLocalizedMessage(), e.getCause());
		}
		return result;
	}

	@Override
	public List<PersonaNatural> findAll() {
		return findAll(null, null);
	}
	
	@Override
	public List<PersonaNatural> findAll(BigInteger offset, BigInteger limit) {
		return findAll(null, offset, limit);
	}

	@Override
	public List<PersonaNatural> findAll(String filterText) {
		return findAll(filterText, null, null);
	}

	@Override
	public List<PersonaNatural> findAll(String filterText, BigInteger offset, BigInteger limit) {
		List<PersonaNatural> result = null;
		
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
		
		QueryParameter queryParameter = QueryParameter.with("filterText", '%' + filterText.toUpperCase() + '%');
		result = personaNaturalDAO.findByNamedQuery(PersonaNatural.FindByFilterText, queryParameter.parameters(), offSetInteger, limitInteger);	
		
		if(result != null){
			for (PersonaNatural personaNatural : result) {
				TipoDocumento tipoDocumento = personaNatural.getTipoDocumento();
				Hibernate.initialize(tipoDocumento);
			}
		}
		return result;
	}
	
	public PersonaNatural create(PersonaNatural personanatural) throws PreexistingEntityException{
		Set<ConstraintViolation<PersonaNatural>> violations = validator.validate(personanatural);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
		TipoDocumento tipoDocumento = personanatural.getTipoDocumento();
		String numeroDocumento = personanatural.getNumeroDocumento();
		Object obj = this.find(tipoDocumento.getIdTipoDocumento(), numeroDocumento);
		if (obj == null)
			personaNaturalDAO.create(personanatural);
		else
			throw new PreexistingEntityException("La persona con el Tipo y Numero de documento ya existe");
		return personanatural;
	}
	
	@Override
	public void update(BigInteger idPersona, PersonaNatural persona) throws NonexistentEntityException, PreexistingEntityException {
		Set<ConstraintViolation<PersonaNatural>> violations = validator.validate(persona);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}		
		PersonaNatural personaNaturalFromDB = personaNaturalDAO.find(idPersona);
		if (personaNaturalFromDB == null)
			throw new NonexistentEntityException("La persona con id " + idPersona + " no fue encontrado");
					
		TipoDocumento tipoDocumento = persona.getTipoDocumento();
		PersonaNatural p = this.find(tipoDocumento.getIdTipoDocumento(), persona.getNumeroDocumento());
		if(p != null)
			if(p.getIdPersonaNatural() != idPersona)
				throw new PreexistingEntityException("Tipo y numero de documento ya existente");
				
		persona.setIdPersonaNatural(idPersona);
		personaNaturalDAO.update(persona);
	}
	
	@Override
	protected DAO<Object, PersonaNatural> getDAO() {
		return this.personaNaturalDAO;
	}

}
