/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean.Request;

import Class.Util.Security.EncryptUtil;
import Models.Tusuario;
import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import Dao.UsuarioDao;
import org.hibernate.Session;
import Class.Util.Persistence.PersistenceUtil;
import ManagedBean.Session.MbSsession;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Transaction;

/**
 *
 * @author j.reyes
 */
@RequestScoped
@ManagedBean
public class MbRUsuario {

    /**
     * Creates a new instance of MbRUsuario
     */
    private Tusuario usuario;

    private List<Tusuario> usuarios;
    private UsuarioDao usuarioDao;

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

    private boolean checkExistCorreoElectronico(UsuarioDao daoTUsuario) {
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
                usuarioDao = new UsuarioDao();

                this.session = PersistenceUtil.getSessionFactory().openSession();
                this.transaction = this.session.beginTransaction();

                if (!checkExistCorreoElectronico(this.usuarioDao)) {
                    this.usuario.setContrasenia(EncryptUtil.sha512(this.usuario.getContrasenia()));
                    this.usuarioDao.insert(this.usuario, this.session);
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

                if (this.usuarioDao != null) {
                    usuarioDao = null;
                }

            }
        }
    }

    public List<Tusuario> getAll() {
        cleanConnection();

        try {
            this.session = PersistenceUtil.getSessionFactory().openSession();
            this.usuarioDao = new UsuarioDao();

            return this.usuarioDao.getAll(this.session);
        } catch (Exception error) {
            throw new RuntimeException("Error al buscar los datos.", error);
        } finally {
            if (this.session != null) {
                this.session.close();
            }

            if (this.usuarioDao != null) {
                usuarioDao = null;
            }
        }
    }

    public void updateUser(Tusuario tusuario) {
        cleanConnection();

        try {
            this.session = PersistenceUtil.getSessionFactory().openSession();
            this.usuarioDao = new UsuarioDao();
            this.usuarioDao.update(tusuario, this.session);
            showInformation("Se edito Correctamente");
        } catch (Exception error) {
            throw new RuntimeException("Error al buscar los datos.", error);
        } finally {
            if (this.session != null) {
                this.session.close();
            }

            if (this.usuarioDao != null) {
                usuarioDao = null;
            }
        }
    }

    public void loadMyUser() throws RuntimeException {
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        MbSsession mbSsession = (MbSsession) ((httpSession != null) ? httpSession.getAttribute("mbSsession") : null);

        if (mbSsession != null) {
            this.usuario = mbSsession.getUsuario();
        } else {
            throw new RuntimeException("La session no existe");
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

    public List<Tusuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Tusuario> usuarios) {
        this.usuarios = usuarios;
    }

}
