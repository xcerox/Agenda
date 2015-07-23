/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Dao.Exception.DaoExceptionSessionClose;
import Models.Tusuario;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author j.reyes
 */
public class UsuarioDao implements Dao.Interfaces.CrudUsuario {

    @Override
    public Tusuario getById(String id, Session session) throws RuntimeException {
        if (session.isOpen()) {
            return (Tusuario)session.load(Tusuario.class,id);
        } else {
            throw new DaoExceptionSessionClose();
        }
    }

    @Override
    public List<Tusuario> getAll(Session session) throws RuntimeException {
        if (session.isOpen()) {
            return session.getNamedQuery("@HQL.USUARIO.GET.ALL")
                    .list();
        } else {
            throw new DaoExceptionSessionClose();
        }
    }

    @Override
    public boolean delete(Tusuario entity, Session session) throws RuntimeException {
        if (session.isOpen()) {
            session.delete(entity);
            return true;
        } else {
            throw new DaoExceptionSessionClose();
        }
    }

    @Override
    public boolean update(Tusuario entity, Session session) throws RuntimeException {
        if (session.isOpen()) {
            session.update(entity);
            return true;
        } else {
            throw new DaoExceptionSessionClose();
        }
    }

    @Override
    public String insert(Tusuario entity, Session session) throws RuntimeException {
        if (session.isOpen()) {
            return (String) session.save(entity);
        } else {
            throw new DaoExceptionSessionClose();
        }
    }

    @Override
    public Tusuario getByCorreoElectronico(String correoElectronico, Session session) throws RuntimeException {
        if(session.isOpen()){
             return (Tusuario) session.createCriteria(Tusuario.class)
                     .add(Restrictions.eq("correoElectronico", correoElectronico))
                     .uniqueResult();
        }else
            throw new DaoExceptionSessionClose();
    }

}
