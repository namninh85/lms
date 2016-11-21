package vn.wse.lms.binding;

import java.text.SimpleDateFormat;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;


public class BindingInitializer implements WebBindingInitializer{
	
	@Override
	public void initBinder(WebDataBinder binder, WebRequest request) {
		//The date format to parse or output your dates
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		//Register it as custom editor for the Date type
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		
	}
	
}
