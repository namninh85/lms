package vn.wse.lms.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.google.api.Google;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.wse.lms.bean.UserDetailsBean;
import vn.wse.lms.entity.UserRole;
import vn.wse.lms.entity.Users;
import vn.wse.lms.exception.DuplicateEmailException;
import vn.wse.lms.repository.UserRepository;
import vn.wse.lms.util.SecurityUtil;
import vn.wse.lms.util.Utils;

@Service("userService")
@Transactional("transactionManager")
public class UserService implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserRepository userRepository;
	
	List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username,boolean isSocialLogin) throws UsernameNotFoundException {

		Users user = userRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		
		
		List<String> roles = userRepository.getRoles(username);
		if(isSocialLogin){
	        if(user.getSignInProvider() != null){
	        	String role = SecurityUtil.getRoleBySignInProvider(user.getSignInProvider().toString());
	        	if(StringUtils.isNotBlank(role)){        		
	        		roles.add(role);
	        	}
	        	
	        	
	        	
		    	
	        }	
		}
		
		/*
		if (!isSocialLogin && user.getSignInProvider() != null) {
			throw new UsernameNotFoundException("user not found");
		}
		*/
		//TODO: if user login by GOOGLE, we will has password default is SocialUser
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(passwordEncoder.encode("SocialUser"));
		}
		UserDetailsBean principal = UserDetailsBean.getBuilder()
	                .fullName(user.getFullName())
	                .id(user.getUserId())
	                .password(user.getPassword())
	                .avatar(user.getAvatar())
	                .roles(roles)
	                .socialSignInProvider(user.getSignInProvider())
	                .username(user.getEmail())
	                .build();
		
		

	        LOGGER.debug("Returning user details: {}", principal);

	        return principal;

	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return loadUserByUsername(username, false);
	}
	
	@Override
	public Users getByEmail(String username) {
		return userRepository.findByEmail(username);
	}

	public List<String> getRoles(String userName) throws Exception {
		try {

			List<String> response = userRepository.getRoles(userName);
			return response;
		} catch (Exception exception) {
			throw exception;
		}
	}
	
	public List<String> getAllRoles() throws Exception {
		try {

			List<String> response = userRepository.getAllRole();
			return response;
		} catch (Exception exception) {
			throw exception;
		}
	}

	private Authentication authenticate(Users account) {
		return new UsernamePasswordAuthenticationToken(createUser(account), null);
	}

	public void signin(Users account) {
		
		SecurityContextHolder.getContext().setAuthentication(authenticate(account));
	}

	private User createUser(Users account) {
		return new User(account.getEmail(), account.getPassword(), roles);
	}

	public List<Users> getAll() {
		return userRepository.getAll();
	}

	public List<Users> getAllTrainee() {
		return userRepository.getAllTrainee();
	}
	
	public List<Users> getAllTrainer() {
		return userRepository.getAllTrainer();
	}

	@Transactional
	public void add(Users users) {
		userRepository.add(users);
	}

	public void edit(Users users) {
		userRepository.edit(users);
	}
	
	public void updateUserAndRole(Users user) {
		
		userRepository.updateListUserRole(user);
	}

	
	public void delete(Long userId){
		userRepository.delete(userId);
	}

	public Users getById(Long id) {
		return userRepository.getById(id);
	}

	@Transactional
	public Users register(Users users) throws DuplicateEmailException {

		LOGGER.debug("Registering new user account with information: {}", users);
		Users exits = userRepository.findByEmail(users.getEmail());
		if (exits != null) {
			users.setPassword(exits.getPassword());
			if(StringUtils.isBlank(users.getPassword())){
				users.setPassword(passwordEncoder.encode("SocialUser"));
			}
			LOGGER.debug("Email: {} exists. Throwing exception.", users.getEmail());
			throw new DuplicateEmailException("The email address: " + users.getEmail() + " is already in use.");
		}

		LOGGER.debug("Email: {} does not exist. Continuing registration.", users.getEmail());


		LOGGER.debug("Persisting new user with information: {}", users);

		userRepository.add(users);
		return users;
	}

	private String encodePassword(Users dto) {
		String encodedPassword = null;

		if (StringUtils.isNotBlank(dto.getPassword())) {
			LOGGER.debug("Registration is normal registration. Encoding password.");
			encodedPassword = passwordEncoder.encode(dto.getPassword());
		}

		return encodedPassword;
	}
	
	public List<UserRole> getAllUserRole(){
		return userRepository.getAllUserRole();
	}
	
}
