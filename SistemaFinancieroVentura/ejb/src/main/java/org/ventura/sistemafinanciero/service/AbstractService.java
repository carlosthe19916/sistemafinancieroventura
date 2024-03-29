package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
import java.util.List;

import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;

public interface AbstractService<T> {

	public T create(T t) throws PreexistingEntityException;
	
	public void update(BigInteger id, T t) throws NonexistentEntityException, PreexistingEntityException;

	public void delete(BigInteger id) throws NonexistentEntityException, RollbackFailureException;

	public T findById(BigInteger id);
	
	public List<T> findAll();

	public List<T> findRange(int[] range);

	public int count();
	
}
