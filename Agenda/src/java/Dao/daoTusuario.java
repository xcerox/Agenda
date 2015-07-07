/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Pojos.Tusuario;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author j.reyes
 */
public class daoTusuario implements Interface.crudTUsuario {

    @Override
    public Tusuario getById(String id, Session session) throws RuntimeException {
        if (session.isOpen()) {
            return (Tusuario)session.load(Tusuario.class,id);
        } else {
            throw new RuntimeException("Session Cerrada");
        }
    }

    @Override
    public List<Tusuario> getAll(Session session) throws RuntimeException {
        if (session.isOpen()) {
            return session.createCriteria(Tusuario.class).list();
        } else {
            throw new RuntimeException("Session Cerrada");
        }
    }

    @Override
    public boolean delete(Tusuario entity, Session session) throws RuntimeException {
        if (session.isOpen()) {
            session.delete(entity);
            return true;
        } else {
            throw new RuntimeException("Session Cerrada");
        }
    }

    @Override
    public boolean update(Tusuario entity, Session session) throws RuntimeException {
        if (session.isOpen()) {
            session.update(entity);
            return true;
        } else {
            throw new RuntimeException("Session Cerrada");
        }
    }

    @Override
    public String insert(Tusuario entity, Session session) throws RuntimeException {
        if (session.isOpen()) {
            return (String) session.save(entity);
        } else {
            throw new RuntimeException("Session Cerrada");
        }
    }

}
