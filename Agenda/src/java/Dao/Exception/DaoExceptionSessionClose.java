/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao.Exception;

/**
 *
 * @author j.reyes
 */
public class DaoExceptionSessionClose extends RuntimeException{
    private final static String ALERT = "La sesion esta cerada.";
    
    public DaoExceptionSessionClose() {
        this(ALERT);
    }

    public DaoExceptionSessionClose(Throwable cause) {
        this(ALERT,cause);
    }
    
    public DaoExceptionSessionClose(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoExceptionSessionClose(String message) {
        super(message);
    }
    
}
