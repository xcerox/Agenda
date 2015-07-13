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
import Dao.TUsuarioDao;
import org.hibernate.Session;
import HibernateUtil.HibernateUtil;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    private Tusuario usuario;
    private List<Tusuario> usuarios;
    private TUsuarioDao daoUsuario;

    private String txtContraseniaRepita;

    private Session session;
    private Transaction transaction;

    public MbRUsuario() {
    }

    @PostConstruct
    private void init() {
        this.usuario = new Tusuario();
        this.usuario.setCodigoUsuario("");
        this.usuario.setSexo(true);
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
        if (!this.usuario.getContrasenia().equals(this.txtContraseniaRepita)) {
            showError("Las contraseñas no coinciden");
            result = false;
        }
        return result;
    }

    private boolean checkExistCorreoElectronico(TUsuarioDao daoTUsuario) {
        boolean result = false;
        if (daoTUsuario.getByCorreoElectronico(this.usuario.getCorreoElectronico(), this.session) != null) {
            result = true;
        }
        return result;
    }

    public void register() throws RuntimeException {
        if (isSamePass()) {
            cleanConnection();

            try {
                daoUsuario = new TUsuarioDao();

                this.session = HibernateUtil.getSessionFactory().openSession();
                this.transaction = this.session.beginTransaction();

                if (!checkExistCorreoElectronico(this.daoUsuario)) {
                    this.usuario.setContrasenia(Encrypt.sha512(this.usuario.getContrasenia()));
                    this.daoUsuario.insert(this.usuario, this.session);
                    this.transaction.commit();

                    showInformation("Usuario agregado.");
                    init();
                } else {
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

                if (this.daoUsuario != null) {
                    daoUsuario = null;
                }

            }
        }
    }

    public List<Tusuario> getAll() {
        cleanConnection();

        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.daoUsuario = new TUsuarioDao();

            return this.daoUsuario.getAll(this.session);
        } catch (Exception error) {
            throw new RuntimeException("Error al buscar los datos.", error);
        } finally {
            if (this.session != null) {
                this.session.close();
            }

            if (this.daoUsuario != null) {
                daoUsuario = null;
            }
        }
    }

    public Tusuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Tusuario tusuario) {
        this.usuario = tusuario;
    }

    public String getTxtContraseniaRepita() {
        return txtContraseniaRepita;
    }

    public void setTxtContraseniaRepita(String txtContraseniaRepita) {
        this.txtContraseniaRepita = txtContraseniaRepita;
    }

}
