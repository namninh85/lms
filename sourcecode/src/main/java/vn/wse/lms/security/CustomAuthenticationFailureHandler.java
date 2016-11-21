package vn.wse.lms.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
		
	@Autowired
	private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class); 

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		LOGGER.debug("Invalid login");
		
		String usernameParameter = usernamePasswordAuthenticationFilter.getUsernameParameter();
	    String userName = request.getParameter(usernameParameter);    
	    String url = request.getContextPath();
	    
		if(exception.getClass().isAssignableFrom(LockedException.class)) {
			response.sendRedirect(url + "/login?error=2");
		  } else {
			response.sendRedirect(url + "/login?error=1");
		  }
		
	}

}
