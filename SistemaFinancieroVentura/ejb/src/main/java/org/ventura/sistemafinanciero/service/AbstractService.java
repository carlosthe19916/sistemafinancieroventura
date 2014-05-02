package org.ventura.sistemafinanciero.service;

import java.util.List;

import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;

public interface AbstractService<T> {

	public void create(T t) throws PreexistingEntityException;
	
	public void update(int id, T t) throws NonexistentEntityException, PreexistingEntityException;

	public void delete(int id) throws NonexistentEntityException;

	public T findById(int id);
	
	public List<T> findAll();

	public List<T> findRange(int[] range);

	public int count();
	
}
