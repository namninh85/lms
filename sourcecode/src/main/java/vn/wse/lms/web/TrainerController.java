package vn.wse.lms.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.wse.lms.entity.Users;
import vn.wse.lms.service.UserService;

@Controller
@RequestMapping("/trainer")
public class TrainerController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping("/trainer-dashboard")
	public String trainerDashboard(HttpServletRequest request) { 
		return "/trainer/trainer-dashboard";
    }

	@RequestMapping(value = "/getAll", method=RequestMethod.GET )
	public @ResponseBody List<Users> getAll() { 
		return  userService.getAllTrainer();
    }
	
	
	

}
