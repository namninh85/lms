package vn.wse.lms.security;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.google.api.Google;

import vn.wse.lms.entity.Users;
import vn.wse.lms.service.SocialMediaService;
import vn.wse.lms.service.UserService;
import vn.wse.lms.util.Constant;
import vn.wse.lms.util.Utils;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {	
	@Autowired
	private HttpServletRequest request;
	
	
	
	@Autowired
	private UserService userService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class); 
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,    
			Authentication authentication) throws IOException,ServletException {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String userName = ((User)auth.getPrincipal()).getUsername();	    
	    String contextPath = request.getContextPath();
	    
	   String currentUrl= request.getSession().getAttribute("currentUrl") != null ?  request.getSession().getAttribute("currentUrl").toString()  : null;
	   //request.getSession().setAttribute("userProfile", (User)auth.getPrincipal());
	   if(StringUtils.isNotEmpty(currentUrl)){
		   request.getSession().removeAttribute("currentUrl");
		   response.sendRedirect(contextPath+currentUrl);
	   }else{
		   response.sendRedirect(contextPath+"/index");
	   }
	}
	
	
}