/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author j.reyes
 * @param <T>
 */
public interface CrudBase<T,Id> {
    public Id insert(T entity, Session session)throws RuntimeException;
    public boolean update(T entity, Session session)throws RuntimeException;
    public boolean delete(T entity, Session session)throws RuntimeException;
    public List<T> getAll( Session session)throws RuntimeException;
    public T getById(Id id, Session session)throws RuntimeException;
}
