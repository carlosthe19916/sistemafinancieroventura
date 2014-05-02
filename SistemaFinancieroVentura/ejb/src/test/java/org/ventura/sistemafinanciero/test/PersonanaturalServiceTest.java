package org.ventura.sistemafinanciero.test;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ventura.sistemafinanciero.control.PersonanaturalServiceBean;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.service.PersonanaturalService;

import prueba.EntityManagerProducer;

@RunWith(Arquillian.class)
public class PersonanaturalServiceTest {

	@Inject
	PersonanaturalService personanaturalService;

	@Deployment
	public static JavaArchive createTestArchive() {
		return ShrinkWrap
				.create(JavaArchive.class)
				.addClass(PersonaNatural.class)
				.addClass(PersonanaturalService.class)
				.addClass(EntityManagerProducer.class)
				.addAsManifestResource("META-INF/test-persistence.xml",
						ArchivePaths.create("persistence.xml"))
				.addAsManifestResource("META-INF/beans.xml",
						ArchivePaths.create("beans.xml"));
				//.addAsResource("ValidationMessages.properties");
	}

	@Test
	public void prueba() {
		int s = personanaturalService.findAll().size();
		System.out.println(s);
	}
}
