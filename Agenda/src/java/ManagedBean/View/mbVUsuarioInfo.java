/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean.View;

import Class.Util.Persistence.PersistenceUtil;
import Dao.UsuarioDao;
import ManagedBean.Session.MbSsession;
import Models.Tusuario;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author j.reyes
 */
@ManagedBean
@ViewScoped
public class mbVUsuarioInfo implements Serializable {

    /**
     * Creates a new instance of mbVUsuarioInfo
     */
    private boolean viewMode;
    private Tusuario usuario;

    private Session session;
    private Transaction transaction;

    @ManagedProperty("#{mbSsession}")
    MbSsession mbSsession;

    public mbVUsuarioInfo() {
    }

    @PostConstruct
    private void init() {
        this.usuario = new Tusuario();
        this.viewMode = true;
        mbSsession.getUsuario().copy(this.usuario);
    }

    private void cleanConection() {
        session = null;
        transaction = null;
    }

    public void update() throws RuntimeException {
        if (!viewMode) {
            if (!mbSsession.getUsuario().equals(this.usuario)) {
                UsuarioDao usuarioDao = null;

                try {
                    this.session = PersistenceUtil.getSessionFactory().openSession();
                    usuarioDao = new UsuarioDao();
                    transaction = this.session.beginTransaction();
                    usuarioDao.update(this.usuario, this.session);
                    transaction.commit();

                    mbSsession.setUsuario(this.usuario);
                    viewMode = true;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Se actualizo correctamente"));
                } catch (Exception error) {
                    transaction.rollback();
                    throw new RuntimeException("Actualizando los datos del usuario", error);
                } finally {
                    if (this.session != null) {
                        this.session.close();
                    }

                    if (transaction != null) {
                        transaction = null;
                    }

                    if (usuarioDao != null) {
                        usuarioDao = null;
                    }
                }
            }
        }
    }

    public void cancel(){
       init();
    }
    
    public boolean isViewMode() {
        return viewMode;
    }

    public void setViewMode(boolean viewMode) {
        this.viewMode = viewMode;
    }

    public Tusuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Tusuario usuario) {
        this.usuario = usuario;
    }

    public MbSsession getMbSsession() {
        return mbSsession;
    }

    public void setMbSsession(MbSsession mbSsession) {
        this.mbSsession = mbSsession;
    }

}
