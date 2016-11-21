package vn.wse.lms.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.wse.lms.bean.UsersBean;
import vn.wse.lms.entity.Course;
import vn.wse.lms.entity.Role;
import vn.wse.lms.entity.UserRole;
import vn.wse.lms.entity.Users;
import vn.wse.lms.service.UserService;
import vn.wse.lms.util.Constant;
import vn.wse.lms.util.Utils;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private AppContext appContext;
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	UserService userService;
	
	@RequestMapping("/user")
	public String adminLayoutTrainee(HttpServletRequest request, Model model) { 
		return "/admin/trainee/layout";
    }
	/*
	 * SEARCH
	 */
	@RequestMapping(value= "/user/getAllTrainer", method = RequestMethod.GET)
	public @ResponseBody List<Users> getAllByTrainer(){
		return userService.getAllTrainer();
	}
	@RequestMapping(value= "/user/getAllTrainee", method = RequestMethod.GET)
	public @ResponseBody List<Users> getAllByTrainee(){
		return userService.getAllTrainee();
	}
	
	/////////////////// TRAINEE /////////////////////////
	@RequestMapping("/user/list")
	public String trainees(HttpServletRequest request, Model model) { 
		return "admin/trainee/list-trainee::content";
    }
	
	@RequestMapping(value= "/user/getAll", method = RequestMethod.GET)
	public @ResponseBody List<Users> getAll(){
		return userService.getAll();
	}
	
	@RequestMapping(value= "/user/{userId}", method = RequestMethod.GET)
	public @ResponseBody Users getByIdTrainee(@PathVariable("userId") Long userId){
		Users users = userService.getById(userId);
		return users;
	}
	
	@RequestMapping("/user/edit")
	public String editTrainee(HttpServletRequest request, Model model) { 
		return "admin/trainee/edit-trainee::content";
    }
	
	@RequestMapping("/user/admin-changepwd")
	public String adminChangepwd(HttpServletRequest request, Model model) { 
		return "admin/trainee/admin-changepwd::content";
    }
	
	
	@RequestMapping("/user/new")
	public String addTrainee(HttpServletRequest request) { 
		return "admin/trainee/add-trainee::content";
    }
	
	@RequestMapping(value = "/user/save", method=RequestMethod.POST)
	public @ResponseBody String saveTrainee(@RequestBody Users user) { 
		//update icon
		InsertImage(user);
		if(user.getUserId() != null){
			if (user.getRolesNeedSave() != null){
				userService.updateUserAndRole(user);
			}
			userService.edit(user);
		}
		else{
			Users usr = userService.getByEmail(user.getEmail());
			if (usr != null) {
				return null;
			}
			user.setUserType(1);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setStatus("Active");

			userService.add(user);
		}
		
		return user.getUserId() != null ? user.getUserId().toString() : null;
	}
	
	@RequestMapping(value = "/user/changepwd", method=RequestMethod.POST)
	
	public @ResponseBody String changePassword(@RequestBody Users user) { 
		//update icon
		Users currentUser = userService.getById(user.getUserId());
		currentUser.setPassword(passwordEncoder.encode(user.getPassword()));
		userService.edit(currentUser);
		return user.getUserId() != null ? user.getUserId().toString() : null;
	}
	
	@RequestMapping(value = "/user/update", method=RequestMethod.POST)
	public @ResponseBody String updateTrainee(@RequestBody Users user) { 
		userService.edit(user);
		return user.getUserId() != null ? user.getUserId().toString() : null;
	}
	
	@RequestMapping(value = "/user/delete", method=RequestMethod.POST)
	public @ResponseBody void deleteTrainee(@RequestParam Long id) { 
		userService.delete(id);
	}
	
	/////////////////// TRAINER /////////////////////////
	@RequestMapping("/trainer")
	public String adminLayoutTrainer(HttpServletRequest request, Model model) { 
		return "/admin/trainer/layout";
    }
	
	@RequestMapping("/trainer/list")
	public String trainers(HttpServletRequest request, Model model) { 
		return "admin/trainer/list-trainer::content";
    }
	
	@RequestMapping(value= "/trainer/getAll", method = RequestMethod.GET)
	public @ResponseBody List<Users> getAllTrainer(){
		return userService.getAllTrainer();
	}
	
	@RequestMapping(value= "/trainer/{userId}", method = RequestMethod.GET)
	public @ResponseBody Users getByIdTrainer(@PathVariable("userId") Long userId){
		Users users = userService.getById(userId);
		return users;
	}
	
	@RequestMapping("/trainer/edit")
	public String editTrainer(HttpServletRequest request, Model model) { 
		return "admin/trainer/edit-trainer::content";
    }
	
	@RequestMapping(method=RequestMethod.GET, value="/trainer/new")
	public String addTrainer(HttpServletRequest request) { 
		return "admin/trainer/add-trainer::content";
    }
	
	@RequestMapping(value = "/trainer/save", method=RequestMethod.POST)
	public @ResponseBody String saveTrainer(@RequestBody Users user) { 
		InsertImage(user);
		if(user.getUserId() != null){
			if (user.getRolesNeedSave() != null){
				userService.updateUserAndRole(user);
			}
			userService.edit(user);
		}
		else{
			user.setUserType(0);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setStatus("Active");
			userService.add(user);
		}
		return user.getUserId() != null ? user.getUserId().toString() : null;
	}
	
	@RequestMapping(value = "/trainer/update", method=RequestMethod.POST)
	public @ResponseBody String updateTrainer(@RequestBody Users user) { 
		InsertImage(user);
		userService.edit(user);
		user.setShortDescription(user.getFullName());
		return user.getUserId() != null ? user.getUserId().toString() : null;
	}
	
	@RequestMapping(value = "/trainer/delete", method=RequestMethod.POST)
	public @ResponseBody void deleteTrainer(@RequestParam Long id) { 
		userService.delete(id);
	}
	
	/////////////////// PROFILE /////////////////////////
	@RequestMapping("/profile")
	public String adminLayoutProfile(HttpServletRequest request, Model model) { 
		return "/admin/profile/layout";
    }
	@RequestMapping(value= "/profile/{userId}", method = RequestMethod.GET)
	public @ResponseBody Users profile(@PathVariable("userId") Long userId){
		Users users = userService.getById(userId);
		return users;
	}
	@RequestMapping("/profile/edit")
	public String editProfile(HttpServletRequest request, Model model) { 
		return "admin/profile/edit-profile::content";
    }
	@RequestMapping("/profile/changepassword")
	public String changePassword(HttpServletRequest request, Model model) { 
		return "admin/profile/change-password::content";
    }
	@RequestMapping(value="/profile/changepassword", method=RequestMethod.POST)
	public @ResponseBody String changePasswordPost(@RequestBody UsersBean usersBean){ 
		
		 try {
			 Users user = userService.getById(appContext.getUserProfile().getId());
				if(passwordEncoder.matches((CharSequence)usersBean.getOldPassword(), user.getPassword()))
				{	//save
					if(usersBean.getNewPassword().equals(usersBean.getConfirmPassword()))
					{
						user.setPassword(passwordEncoder.encode(usersBean.getNewPassword()));
						userService.edit(user);
						return user.getUserId() != null ? user.getUserId().toString() : null;	
					}		
				}
		  } catch (Exception  e) {
		      e.printStackTrace();
		  }
		 return "{\"error\":\"error\"}";
	}
	@RequestMapping(value = "/profile/save", method=RequestMethod.POST)
	public @ResponseBody String updateProfile(@RequestBody Users user) { 
		InsertImage(user);
		if(user.getUserId() != null){
			userService.edit(user);
		}
		return user.getUserId() != null ? user.getUserId().toString() : null;
	}
	
	@RequestMapping("/profile/forgotpassword")
	public String forgotPassword(HttpServletRequest request, Model model) { 
		return "admin/profile/forgot-password::content";
    }
	
	@RequestMapping(value= "/user/getAllRolesByUserName/{userId}", method = RequestMethod.GET)
	public @ResponseBody List<Role> getAllRolesByUserName(@PathVariable("userId")Long userId) throws Exception{
		Users user = getByIdTrainee(userId);
		List<String> listRoleLeft =  userService.getRoles(user.getEmail());
		List<Role> lstRole = new ArrayList<Role>();
		for (String role : listRoleLeft) {
			Role r = new Role();
			r.setRoleId(role);
			r.setRoleName(role);
			lstRole.add(r);
		}
		return lstRole;
	}
	
	@RequestMapping(value= "/user/getAllRolesNotBelongByUserName/{userId}", method = RequestMethod.GET)
	
	public @ResponseBody List<Role> getAllRolesNotBelongByUserName(@PathVariable("userId")Long userId) throws Exception{
		Users user = getByIdTrainee(userId);
		List<String> listRoleLeft =  userService.getRoles(user.getEmail());
		List<String> listRoleRight = userService.getAllRoles();
		List<Role> lstRole = new ArrayList<Role>();
		for (String roleRight : listRoleRight) {
			if(!listRoleLeft.contains(roleRight)){
				Role r = new Role();
				r.setRoleId(roleRight);
				r.setRoleName(roleRight);
				lstRole.add(r);
			}          
		}
		return lstRole;
	}
	
	@RequestMapping(value= "/trainer/getAllRolesByUserName/{userId}", method = RequestMethod.GET)
	public @ResponseBody List<Role> getAllRolesByUserNameForTrainer(@PathVariable("userId")Long userId) throws Exception{
		return getAllRolesByUserName(userId);
	}
	
	@RequestMapping(value= "/trainer/getAllRolesNotBelongByUserName/{userId}", method = RequestMethod.GET)
	public @ResponseBody List<Role> getAllRolesNotBelongByUserNameForTrainer(@PathVariable("userId")Long userId) throws Exception{
		return getAllRolesNotBelongByUserName(userId);
	}
	
	private void InsertImage(Users user) {
		//update icon
				String oldIcon ="";
				String newIcon = user.getAvatar();
				if (user.getUserId() != null) {
					Users userOld = userService.getById(user.getUserId() );
					oldIcon = userOld.getAvatar();
				}
					
				if(StringUtils.isNotBlank(newIcon) && !newIcon.equalsIgnoreCase(oldIcon) ){
					File fileOldIcon = new File(Utils.getFolderImg()+oldIcon);
					if(fileOldIcon.exists() && fileOldIcon.isFile())
						FileUtils.deleteQuietly(fileOldIcon);
					Utils.move(Constant.PREFIX_FILE_TEMP+newIcon, Utils.getFoldelTemp(), Utils.getFolderImg());
				}
	}
}
