package org.ventura.sistemafinanciero.control;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.AbstractService;

public abstract class AbstractServiceBean<T> implements AbstractService<T>{

	@Inject 
	protected Validator validator;

	protected abstract DAO<Object, T> getDAO();
	
	public T create(T t) throws PreexistingEntityException {
		T obj = null;
		try {
			Set<ConstraintViolation<T>> violations = validator.validate(t);
			if (!violations.isEmpty()) {
	            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
	        }	
			obj = getDAO().create(t);					
		} catch (EntityExistsException e) {		
			throw new PreexistingEntityException("Objeto ya existente en base de datos", e);
		}
		return obj;
	}
	
	public void update(BigInteger id, T t) throws NonexistentEntityException, PreexistingEntityException {		
		Set<ConstraintViolation<T>> violations = validator.validate(t);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
		T other = getDAO().find(id);
		if (other != null)
			getDAO().update(t);
		else
			throw new NonexistentEntityException("Objeto no existente");				
	}

	public void delete(BigInteger id) throws NonexistentEntityException, RollbackFailureException {		
		T t = getDAO().find(id);
		if (t != null)
			getDAO().delete(t);
		else
			throw new NonexistentEntityException("Objeto no existente");	
	}

	public T findById(BigInteger id) {
		T result = null;
		result = getDAO().find(id);		
		return result;
	}
	
	public List<T> findAll() {
		return getDAO().findAll();
	}

	public List<T> findRange(int[] range){
		return getDAO().findRange(range);	
	}

	public int count(){
		return getDAO().count();
	}
	
}
