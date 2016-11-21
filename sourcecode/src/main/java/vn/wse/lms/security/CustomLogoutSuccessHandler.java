package vn.wse.lms.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {	
	
	
	@Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		//String userName = ((UserProfile)authentication.getPrincipal()).getUsername();	    
	    //String url = request.getContextPath();
		
        setDefaultTargetUrl("/login");
        super.onLogoutSuccess(request, response, authentication);
        
    }
}
