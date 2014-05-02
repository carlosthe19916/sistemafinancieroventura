package org.ventura.sistemafinanciero.control;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.dao.QueryParameter;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.TipoDocumento;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.exception.IllegalResultException;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;
import org.ventura.sistemafinanciero.service.PersonanaturalService;

@Named
@Stateless
@Remote(PersonanaturalService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PersonanaturalServiceBean extends AbstractServiceBean<PersonaNatural> implements PersonanaturalService {

	//private static Logger LOGGER = LoggerFactory.getLogger(PersonanaturalServiceBean.class);

	@Inject
	private DAO<Object, PersonaNatural> personanaturalDAO;

	//@Inject
	//private DAO<Object, Trabajador> trabajadorDAO;
	
	//@Inject
   // private Validator validator;

	@Override
	public void create(PersonaNatural personanatural) throws PreexistingEntityException {	
		/*Set<ConstraintViolation<PersonaNatural>> violations = validator.validate(personanatural);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

		TipoDocumento tipoDocumento = personanatural.getTipoDocumento();
		String numeroDocumento = personanatural.getNumeroDocumento();

		Object obj = findByTipoNumeroDocumento(tipoDocumento.getIdTipoDocumento(), numeroDocumento);
		if (obj == null)
			personanaturalDAO.create(personanatural);
		else
			throw new PreexistingEntityException("La persona con el Tipo y Numero de documento ya existe");*/
		
	}
	
	@Override
	public void update(int idPersona, PersonaNatural persona) throws NonexistentEntityException, PreexistingEntityException {
	/*	Set<ConstraintViolation<PersonaNatural>> violations = validator.validate(persona);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}		
		PersonaNatural personaNaturalFromDB = personanaturalDAO.find(idPersona);
		if (personaNaturalFromDB == null)
			throw new NonexistentEntityException("La persona con id " + idPersona + " no fue encontrado");
					
		TipoDocumento tipoDocumento = persona.getTipoDocumento();
		PersonaNatural p = this.findByTipoNumeroDocumento(tipoDocumento.getIdTipoDocumento(), persona.getNumeroDocumento());
		if(p != null)
			if(p.getId() != idPersona)
				throw new PreexistingEntityException("Tipo y numero de documento ya existente");
				
		persona.setIdPersonaNatural(idPersona);
		personanaturalDAO.update(persona);*/	
	}
	
	@Override
	public PersonaNatural findByTipoNumeroDocumento(int idTipodocumento, String numerodocumento) {		
		if(numerodocumento == null)
			return null;
		if(numerodocumento.isEmpty() || numerodocumento.trim().isEmpty())
			return null;
		PersonaNatural result = null;
		try {
			QueryParameter queryParameter = QueryParameter.with("idtipodocumento",idTipodocumento).and("numerodocumento", numerodocumento);
			List<PersonaNatural> list = personanaturalDAO.findByNamedQuery(PersonaNatural.FindByTipoAndNumeroDocumento, queryParameter.parameters());
			if (list.size() >= 1)
				throw new IllegalResultException("Se encontró mas de una persona con idDocumento:" + idTipodocumento + " y numero de documento:" + numerodocumento);
			else 
				for (PersonaNatural personaNatural : list) {
					result = personaNatural;
				}
		} catch (IllegalResultException e) {
		//	LOGGER.error(e.getMessage(), e.getLocalizedMessage(), e.getCause());
		}
		return result;
	}

	@Override
	public Set<PersonaNatural> findByFilterText(String filterText) {		
		if(filterText == null)
			return new HashSet<PersonaNatural>();
		if(filterText.isEmpty() || filterText.trim().isEmpty()){
			return new HashSet<PersonaNatural>();
		}
		List<PersonaNatural> list = null;
		QueryParameter queryParameter = QueryParameter.with("filtertext", filterText.toLowerCase());
		list = personanaturalDAO.findByNamedQuery(PersonaNatural.FindByFilterText, queryParameter.parameters(), 1000);												
		return new HashSet<PersonaNatural>(list);
	}
	
	@Override
	public PersonaNatural findByTrabajador(int idTrabajador) {
		//Trabajador trabajador = trabajadorDAO.find(idTrabajador);
		/*if(trabajador == null)
			return null;
		else
			return trabajador.getPersonaNatural();*/
		return null;
	}

	@Override
	protected DAO<Object, PersonaNatural> getDAO() {
		return personanaturalDAO;
	}

}
