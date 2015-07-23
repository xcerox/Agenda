/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class.Helper.Validator;

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
@FacesValidator("empty")

public class EmptyValidatorHelper implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        HtmlInputText htmlInputText = (HtmlInputText) component;
        String label;
                
        if(htmlInputText.getLabel().trim().isEmpty() || htmlInputText.getLabel() == null)
            label = htmlInputText.getId();
        else
            label = htmlInputText.getLabel();
        
        if(value.toString().trim().isEmpty())
             throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", label + ": Es un campo Obligatorio"));
    }
    
}
