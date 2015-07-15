/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean.Session;

import Dao.TUsuarioDao;
import HibernateUtil.HibernateUtil;
import Models.Tusuario;
import Util.seguridad.Encrypt;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;

/**
 *
 * @author j.reyes
 */
@ManagedBean
@SessionScoped
public class MbSsession {

    /**
     * Creates a new instance of MbSsession
     */
    private String correoElectronico;
    private String contrasenia;
    private boolean inside;
    private Session session;

    public MbSsession() {
    }

    @PostConstruct
    public void init() {
        this.correoElectronico = "";
        this.contrasenia = "";
        this.inside = false;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String login() {
        this.session = null;
        String path = "/index";
        TUsuarioDao usuarioDao = null;

        try {
            usuarioDao = new TUsuarioDao();

            this.session = HibernateUtil.getSessionFactory().openSession();

            Tusuario usuario = usuarioDao.getByCorreoElectronico(this.correoElectronico, session);

            if (usuario != null) {
                if (usuario.getContrasenia().equals(Encrypt.sha512(this.contrasenia))) {
                    path = "/usuario/ver";
                    inside = true;
                    showInformation("ingreso el usuario");
                    return null;
                }
            }

            showError("usuario o contraseña invalido");

        } catch (Exception error) {
            throw new RuntimeException("", error);

        } finally {
            if (this.session != null) {
                this.session.close();
            }

            if (usuarioDao != null) {
                usuarioDao = null;
            }

            return path;
        }
    }

    public String cerrarSesion() {
        init();
        return "/index";
    }

    private void showError(String text) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", text));
    }

    private void showInformation(String text) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información:", text));
    }

    public boolean isInside() {
        return inside;
    }

    public void setInside(boolean inside) {
        this.inside = inside;
    }
}
