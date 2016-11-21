package vn.wse.lms.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class WelcomeController {
	@Autowired
	private AppContext appContext;
	
	@Autowired
	MessageSource messageSource;
	
	
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	@RequestMapping("/error404")
	public String error404(HttpServletRequest request) { 
		return "/error404";
    }
	
	@ResponseStatus(value=HttpStatus.FORBIDDEN)
	@RequestMapping("/error403")
	public String error403(HttpServletRequest request) { 
		return "/error403";
    }
	
	@RequestMapping("/")
	public String home(HttpServletRequest request) { 
		return "index";
    }
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
		if (request.isUserInRole("TRAINER")) {
			return "redirect:/course/#/list"; 
		} else if (request.isUserInRole("TRAINEE")) {
			return "redirect:/#/trainee/my-courses"; 
		}
		return "index";
    }
	
	@RequestMapping("/demo")
	public String demo(HttpServletRequest request) { 
		return "demo";
    }
	
	public void manualLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
          if (auth != null){    
             new SecurityContextLogoutHandler().logout(request, response, auth);
          }
        SecurityContextHolder.getContext().setAuthentication(null);
    }

}
