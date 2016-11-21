package vn.wse.lms.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

public class SocialLoginUrlFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	//@Override
	
	/**
	public void doFilter1(ServletRequest req, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			 HttpServletRequest request = (HttpServletRequest) req;
			 HttpSession session = request.getSession(false);

			String currentUrl = request.getParameter("currentUrl");
			if(StringUtils.isNotBlank(currentUrl))
				request.getSession(true).setAttribute("currentUrl", currentUrl.split("/lms/")[1]);
			
			System.out.println("Filter: "+ currentUrl);
			
			chain.doFilter(request, response);
			
			
		} catch (Exception ex) {
			
		}
		
	}
	
	**/

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
	}

}
