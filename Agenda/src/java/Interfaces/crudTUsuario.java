/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Models.Tusuario;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author j.reyes
 */
public interface crudTUsuario extends crud<Tusuario, String> {

    @Override
    public Tusuario getById(String id, Session session) throws RuntimeException;
    
    public Tusuario getByCorreoElectronico(String correoElectronico, Session session) throws RuntimeException;

    @Override
    public List<Tusuario> getAll(Session session) throws RuntimeException;

    @Override
    public boolean delete(Tusuario entity, Session session) throws RuntimeException;

    @Override
    public boolean update(Tusuario entity, Session session) throws RuntimeException;

    @Override
    public String insert(Tusuario entity, Session session) throws RuntimeException;

}
