package vn.wse.lms.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SocialLoginUrlInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String currentUrl = request.getParameter("currentUrl");
		if(StringUtils.isNotBlank(currentUrl))
			request.getSession(true).setAttribute("currentUrl", currentUrl.split("/lms/")[1]);
		
		System.out.println("Filter: "+ currentUrl);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
			request.getSession().removeAttribute("currentUrl");
	}
}
