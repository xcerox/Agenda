/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class.Helper;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ManagedBean.Session.MbSsession;

/**
 *
 * @author j.reyes
 */
@WebFilter("*.xhtml")
public class SessionUrlFilterHelper implements Filter {

    FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
    }

    private boolean existInUrlFree(final String url) {
        boolean isExist = false;
        
        String[] urlsFree = new String[]{
            "/faces/index.xhtml",
            "faces/usuario/registrar.xhtml"
        };
        
        for (String urlfree : urlsFree) 
            if(url.contains(urlfree)){
                isExist = true;
                break;
            }
        
        return isExist;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession httpSession = httpRequest.getSession(true);
        MbSsession mbSsession = (MbSsession) ((httpSession != null) ? httpSession.getAttribute("mbSsession") : null);

        if ((mbSsession != null && mbSsession.isInside()) || existInUrlFree(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/faces/index.xhtml");
        }
    }

    @Override
    public void destroy() {
        this.config = null;
    }

}
