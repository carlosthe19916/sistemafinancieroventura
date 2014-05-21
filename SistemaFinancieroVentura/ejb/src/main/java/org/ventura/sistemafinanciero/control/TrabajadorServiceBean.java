package org.ventura.sistemafinanciero.control;

import java.math.BigInteger;
import java.util.Set;

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
import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.TrabajadorCaja;
import org.ventura.sistemafinanciero.entity.TrabajadorUsuario;
import org.ventura.sistemafinanciero.entity.Usuario;
import org.ventura.sistemafinanciero.exception.IllegalResultException;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.service.TrabajadorService;

@Named
@Stateless
@Remote(TrabajadorService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TrabajadorServiceBean extends AbstractServiceBean<Trabajador> implements TrabajadorService {

	private static Logger LOGGER = LoggerFactory.getLogger(TrabajadorServiceBean.class);

	@Inject private DAO<Object, Trabajador> trabajadorDAO;
	@Inject private DAO<Object, Usuario> usuarioDAO;

	@Override
	public Caja findByTrabajador(BigInteger idTrabajador) throws NonexistentEntityException{
		Caja result = null;
		try {
			Trabajador trabajador = trabajadorDAO.find(idTrabajador);
			if(trabajador == null)
				throw new NonexistentEntityException("Trabajador no existente");
			Set<TrabajadorCaja> cajas = trabajador.getTrabajadorCajas();
			if(cajas.size() >= 2)
				throw new IllegalResultException("Trabajador tiene " + cajas.size() + " asignadas");
			for (TrabajadorCaja trabajadorCaja : cajas) {
				result = trabajadorCaja.getCaja();	
				Hibernate.initialize(result);
				break;
			}
		} catch (IllegalResultException e) {
			LOGGER.error(e.getMessage(), e.getLocalizedMessage(), e.getCause());
		}			
		return result;
	}
	
	@Override
	public Trabajador findByUsuario(BigInteger idusuario) {
		Trabajador result = null;
		try {
			Usuario usuario = usuarioDAO.find(idusuario);			
			if (usuario != null) {
				Set<TrabajadorUsuario> listTrabajadores = usuario.getTrabajadorUsuarios();
				if (listTrabajadores.size() <= 1) {
					for (TrabajadorUsuario trabajadorUsuario : listTrabajadores) {
						result = trabajadorUsuario.getTrabajador();						
					}
				} else {
					throw new IllegalResultException("Usuario con mas de un trabajador asignado");
				}
			}
		} catch (IllegalResultException e) {
			LOGGER.error(e.getMessage(), e.getLocalizedMessage(), e.getCause());
		}
		return result;
	}

	@Override
	public Agencia getAgencia(BigInteger idTrabajador) throws NonexistentEntityException {
		Agencia result= null;
		Trabajador trabajador = super.findById(idTrabajador);
		if(trabajador != null){
			result = trabajador.getAgencia();
			Hibernate.initialize(result);
		} else {
			throw new NonexistentEntityException("trabajador no encontrado");
		}
		return result;
	}

	@Override
	protected DAO<Object, Trabajador> getDAO() {
		return trabajadorDAO;
	}


}
