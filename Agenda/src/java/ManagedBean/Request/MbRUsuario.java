/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean.Request;

import Util.seguridad.Encrypt;
import Pojos.Tusuario;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import Dao.daoTusuario;
import org.hibernate.Session;
import HibernateUtil.HibernateUtil;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;

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
    private List<Tusuario> tusuarios;

    private String txtContraseniaRepita;

    private Session session;
    private Transaction transaction;

    public MbRUsuario() {
    }

    @PostConstruct
    private void init() {
        this.tusuario = new Tusuario();
        this.tusuario.setCodigoUsuario("");
        this.tusuario.setSexo(true);
        this.txtContraseniaRepita = "";
    }

    private void cleanConnection() {
        this.session = null;
        this.transaction = null;
    }

    private void showError(String text) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", text));
    }

    private void showInformation(String text) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información:", text));
    }

    private boolean isSamePass() {
        boolean result = true;
        if (!this.tusuario.getContrasenia().equals(this.txtContraseniaRepita)) {
            showError("Las contraseñas no coinciden");
            result = false;
        }
        return result;
    }

    private boolean checkExistCorreoElectronico(daoTusuario daoTUsuario) {
        boolean result = false;
        if (daoTUsuario.getByCorreoElectronico(this.tusuario.getCorreoElectronico(), this.session) != null) {
            result = true;
        }
        return result;
    }

    public void register() throws RuntimeException {
        if (isSamePass()) {
            cleanConnection();

            try {
                daoTusuario daoTusuario = new daoTusuario();
                
                this.session = HibernateUtil.getSessionFactory().openSession();
                this.transaction = this.session.beginTransaction();
                
                if (!checkExistCorreoElectronico(daoTusuario)) {
                    this.tusuario.setContrasenia(Encrypt.sha512(this.tusuario.getContrasenia()));
                    daoTusuario.insert(this.tusuario, this.session);
                    this.transaction.commit();

                    showInformation("Usuario agregado.");
                    init();
                } else {
                    daoTusuario = null;
                    showError("Este correo ya ha sido usado para registrarse");
                }
            } catch (Exception error) {
                if (this.transaction != null) {
                    this.transaction.rollback();
                }

                throw new RuntimeException("Error al registrar usuario", error);

            } finally {
                if (this.session != null) {
                    this.session.close();
                }

            }
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
