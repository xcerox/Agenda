/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean.Session;

import Dao.UsuarioDao;
import Class.Util.Persistence.PersistenceUtil;
import Models.Tusuario;
import Class.Util.Security.EncryptUtil;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;

/**
 *
 * @author j.reyes
 */
@ManagedBean
@SessionScoped
public class MbSsession implements Serializable{

    /**
     * Creates a new instance of MbSsession
     */
    private String correoElectronico;
    private String contrasenia;
    
    private boolean inside;
    private Tusuario usuario;
    private Session session;

    public MbSsession() {
        HttpSession userSession = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        userSession.setMaxInactiveInterval(30*60);
    }   

    @PostConstruct
    public void init() {
        this.correoElectronico = "";
        this.contrasenia = "";
        this.inside = false;
        this.usuario = null;
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
        UsuarioDao usuarioDao = null;

        try {
            usuarioDao = new UsuarioDao();

            this.session = PersistenceUtil.getSessionFactory().openSession();

            this.usuario = usuarioDao.getByCorreoElectronico(this.correoElectronico, session);

            if (this.usuario != null) {
                if (this.usuario.getContrasenia().equals(EncryptUtil.sha512(this.contrasenia))) {
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

    public Tusuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Tusuario usuario) {
        this.usuario = usuario;
    }
}
