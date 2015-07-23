/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class.Helper.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author j.reyes
 */
@FacesValidator("email")
public class EmailValidatorHelper implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        HtmlInputText htmlInputText = (HtmlInputText) component;
        String label;
                
        if(htmlInputText.getLabel().trim().isEmpty() || htmlInputText.getLabel() == null)
            label = htmlInputText.getId();
        else
            label = htmlInputText.getLabel();
               
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher((CharSequence)value);
        
        if(!matcher.matches())
             throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", label + ": es incorrecto"));
    }
    
}
