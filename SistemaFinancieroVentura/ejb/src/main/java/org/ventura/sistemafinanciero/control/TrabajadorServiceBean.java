package org.ventura.sistemafinanciero.control;

import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.TrabajadorUsuario;
import org.ventura.sistemafinanciero.entity.Usuario;
import org.ventura.sistemafinanciero.exception.IllegalResultException;
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
	public Trabajador findByUsuario(int idusuario) {
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
	protected DAO<Object, Trabajador> getDAO() {
		return trabajadorDAO;
	}

	
	

}
