package vn.wse.lms.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import vn.wse.lms.bean.UserDetailsBean;
import vn.wse.lms.entity.Users;

public class SecurityUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityUtil.class);

    public static void logInUser(Users user, List<String> roles) {
        LOGGER.info("Logging in user: {}", user);

        
        UserDetailsBean userDetails = UserDetailsBean.getBuilder()
                .fullName(user.getFullName())
                .id(user.getUserId())
                .password(user.getPassword())
                .roles(roles)
                .socialSignInProvider(user.getSignInProvider())
                .username(user.getEmail())
                .build();
        LOGGER.debug("Logging in principal: {}", userDetails);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LOGGER.info("User: {} has been logged in.", userDetails);
    }
    
    public static String getRoleBySignInProvider(String signInProvider){
    	for (vn.wse.lms.service.Role role : vn.wse.lms.service.Role.values()) {
			if(role.toString().indexOf(signInProvider) != -1)
				return role.toString();
		}
    	return null;
    }
}
