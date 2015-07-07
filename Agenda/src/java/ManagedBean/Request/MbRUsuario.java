/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean.Request;

import Pojos.Tusuario;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import Dao.daoTusuario;
import org.hibernate.Session;
import HibernateUtil.HibernateUtil;
import org.hibernate.Transaction;

/**
 *
 * @author j.reyes
 */
@ManagedBean
@RequestScoped
public class MbRUsuario {

    /**
     * Creates a new instance of MbRUsuario
     */
    private Tusuario tusuario;
    private String txtContraseniaRepita;
    private Session session;
    private Transaction transaction;

    public MbRUsuario() {
    }

    @PostConstruct
    private void init() {
        tusuario = new Tusuario();
        tusuario.setSexo(true);
    }

    private void cleanConnection() {
        session = null;
        transaction = null;
    }

    public String register() throws RuntimeException {

        try {
            cleanConnection();
            daoTusuario daoTusuario = new daoTusuario();

            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            daoTusuario.insert(tusuario, session);
            transaction.commit();

            return "/usuario/registrar";
        } catch (Exception e) {
            return "#";
        }

    }

    public Tusuario getTusuario() {
        return tusuario;
    }

    public void setTusuario(Tusuario tusuario) {
        this.tusuario = tusuario;
    }

    public String getTxtContraseniaRepita() {
        return txtContraseniaRepita;
    }

    public void setTxtContraseniaRepita(String txtContraseniaRepita) {
        this.txtContraseniaRepita = txtContraseniaRepita;
    }

}
