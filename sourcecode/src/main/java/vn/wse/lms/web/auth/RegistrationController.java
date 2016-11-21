package vn.wse.lms.web.auth;


import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import vn.wse.lms.entity.Role;
import vn.wse.lms.entity.Users;
import vn.wse.lms.exception.DuplicateEmailException;
import vn.wse.lms.service.SocialMediaService;
import vn.wse.lms.service.UserService;
import vn.wse.lms.util.SecurityUtil;

/**
 * @author Petri Kainulainen
 */
@Controller
@SessionAttributes("user")
public class RegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

    protected static final String ERROR_CODE_EMAIL_EXIST = "NotExist.user.email";
    protected static final String MODEL_NAME_REGISTRATION_DTO = "user";
    protected static final String VIEW_NAME_REGISTRATION_PAGE = "register";

    private UserService userService;
    
    @Autowired
	private PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserService service) {
        this.userService = service;
    }

    /**
     * Renders the registration page.
     */
    @RequestMapping(value = "/user/register", method = RequestMethod.GET)
    public String showRegistrationForm(WebRequest request, Model model) {
        LOGGER.debug("Rendering registration page.");

        Connection<?> connection = ProviderSignInUtils.getConnection(request);

        Users registration = createRegistrationDTO(connection);
        LOGGER.debug("Rendering registration form with information: {}", registration);

        model.addAttribute(MODEL_NAME_REGISTRATION_DTO, registration);

        return VIEW_NAME_REGISTRATION_PAGE;
    }
    
    
    @RequestMapping(value = "/user/register/local", method = RequestMethod.GET)
    public String showRegistrationLocalForm(WebRequest request, Model model) {
        LOGGER.debug("Rendering registration page.");

        Users registration = createRegistrationDTO(null);
        LOGGER.debug("Rendering registration form with information: {}", registration);

        model.addAttribute(MODEL_NAME_REGISTRATION_DTO, registration);

        return "register-local";
    }
    

    /**
     * Creates the form object used in the registration form.
     * @param connection
     * @return  If a user is signing in by using a social provider, this method returns a form
     *          object populated by the values given by the provider. Otherwise this method returns
     *          an empty form object (normal form registration).
     */
    private Users createRegistrationDTO(Connection<?> connection) {
        Users dto = new Users();

        if (connection != null) {
            UserProfile socialMediaProfile = connection.fetchUserProfile();
            dto.setEmail(socialMediaProfile.getEmail());
            dto.setFullName(socialMediaProfile.getFirstName() + " " + socialMediaProfile.getLastName());

            ConnectionKey providerKey = connection.getKey();
            dto.setSignInProvider(SocialMediaService.valueOf(providerKey.getProviderId().toUpperCase()));
            
            Users currentUsr = userService.getByEmail(dto.getEmail());
            if (currentUsr != null) {
            	dto.setAvatar(currentUsr.getAvatar());
            	dto.setUserId(currentUsr.getUserId());
            }
        }

        return dto;
    }

    /**
     * Processes the form submissions of the registration form.
     * @throws Exception 
     */
    @RequestMapping(value ="/user/register", method = RequestMethod.POST)
    public String registerUserAccount(@Valid @ModelAttribute(value="user") Users userAccountData,
                                      BindingResult result,
                                      WebRequest request) throws Exception {
        LOGGER.debug("Registering user account with information: {}", userAccountData);
        if (result.hasErrors()) {
            LOGGER.debug("Validation errors found. Rendering form view.");
            return VIEW_NAME_REGISTRATION_PAGE;
        }

        LOGGER.debug("No validation errors found. Continuing registration process.");

        Users registered = createUserAccount(userAccountData, result, false);

        //If email address was already found from the database, render the form view.
        if (registered == null) {
            LOGGER.debug("An email address was found from the database. Rendering form view.");
            return VIEW_NAME_REGISTRATION_PAGE;
        }

        LOGGER.debug("Registered user account with information: {}", registered);
        //Logs the user in.
        List<String> roles =   userService.getRoles(registered.getEmail());
       
        if(registered.getSignInProvider() != null){
        	String role = SecurityUtil.getRoleBySignInProvider(registered.getSignInProvider().toString());
        	if(StringUtils.isNotBlank(role)){        		
        		roles.add(role);
        	}
        }
        	
        SecurityUtil.logInUser(registered, roles);
        LOGGER.debug("User {} has been signed in", registered);
        //If the user is signing in by using a social provider, this method call stores
        //the connection to the UserConnection table. Otherwise, this method does not
        //do anything.
        ProviderSignInUtils.handlePostSignUp(registered.getEmail(), request);

        return "redirect:/index";
    }

    /**
     * Processes the form submissions of the registration form.
     * @throws Exception 
     */
    @RequestMapping(value ="/user/register/local", method = RequestMethod.POST)
    public String registerLocalUserAccount(@Valid @ModelAttribute(value="user") Users userAccountData,
                                      BindingResult result,
                                      WebRequest request) throws Exception {
        LOGGER.debug("Registering user account with information: {}", userAccountData);
        if (result.hasErrors()) {
            LOGGER.debug("Validation errors found. Rendering form view.");
            return "register-local";
        }

        LOGGER.debug("No validation errors found. Continuing registration process.");

        //check user exists
        Users us = userService.getByEmail(userAccountData.getEmail());
        if (us != null) {
        	return "register-local";
        } else {
        	userAccountData.setPassword(passwordEncoder.encode(userAccountData.getPassword()));
        	
        	userAccountData.setStatus("Active");
        	userAccountData.setUserType(1);
        	userService.add(userAccountData);
        }
       
        //Logs the user in.
        List<String> roles =   userService.getRoles(userAccountData.getEmail());
       
        SecurityUtil.logInUser(userAccountData, roles);

        return "redirect:/index";
    }
    
    /**
     * Creates a new user account by calling the service method. If the email address is found
     * from the database, this method adds a field error to the email field of the form object.
     */
    private Users createUserAccount(Users userAccountData, BindingResult result, boolean isLocal) {
        LOGGER.debug("Creating user account with information: {}", userAccountData);
        Users registered = null;

        try {
        	if(userAccountData.getSignInProvider() == null) {
        		userAccountData.setPassword(passwordEncoder.encode(userAccountData.getPassword()));
        	}
        	userAccountData.setShortDescription(userAccountData.getShortDescription());
        	userAccountData.setStatus("Active");
        	userAccountData.setUserType(1);
        	
        	
            registered = userService.register(userAccountData);
            
        }
        catch (DuplicateEmailException ex) {
            LOGGER.debug("An email address: {} exists.", userAccountData.getEmail());
            if (!isLocal) {
            	userService.edit(userAccountData);
            	registered = userAccountData;
            }
        }

        return registered;
    }


}
