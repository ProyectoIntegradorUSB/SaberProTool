package com.saberpro.dataaccess.dao;

import com.saberpro.dataaccess.api.JpaDaoImpl;
import com.saberpro.modelo.Facultad;
import com.saberpro.modelo.TipoPregunta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * A data access object (DAO) providing persistence and search support for
 * TipoPregunta entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 *
 * @see lidis.TipoPregunta
 */
@Scope("singleton")
@Repository("TipoPreguntaDAO")
public class TipoPreguntaDAO extends JpaDaoImpl<TipoPregunta, Long>
    implements ITipoPreguntaDAO {
    private static final Logger log = LoggerFactory.getLogger(TipoPreguntaDAO.class);
    @PersistenceContext
    private EntityManager entityManager;

    public static ITipoPreguntaDAO getFromApplicationContext(
        ApplicationContext ctx) {
        return (ITipoPreguntaDAO) ctx.getBean("TipoPreguntaDAO");
    }

	@Override
	public TipoPregunta findByNombre(String nombre) {
		return (TipoPregunta)entityManager.createQuery("SELECT tip FROM TipoPregunta tip WHERE UPPER(sinacentos(tip.nombre))=UPPER(sinacentos(:nombre))").setParameter("nombre",nombre).getSingleResult();
	}
}
