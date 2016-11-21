package vn.wse.lms.web.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogoutController {
	
	@RequestMapping(value = "/logout1")
	public String logout() {
        return "logout"; 
	}
	
}
