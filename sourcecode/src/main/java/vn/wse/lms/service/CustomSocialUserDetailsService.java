package vn.wse.lms.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

import vn.wse.lms.bean.UserDetailsBean;

/**
 * This class delegates requests forward to our UserDetailsService implementation.
 * This is possible because we use the username of the user as the account ID.
 *
 */
public class CustomSocialUserDetailsService implements SocialUserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomSocialUserDetailsService.class);

    private UserDetailsService userDetailsService;

    public CustomSocialUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Loads the username by using the account ID of the user.
     * @param userId    The account ID of the requested user.
     * @return  The information of the requested user.
     * @throws UsernameNotFoundException    Thrown if no user is found.
     * @throws DataAccessException
     */
    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
        LOGGER.debug("Loading user by user id: {}", userId);

        UserDetailsBean userDetails;
		try {
			boolean isSocialLogin = true;
			
			userDetails = (UserDetailsBean) userDetailsService.loadUserByUsername(userId,isSocialLogin );
			LOGGER.debug("Found user details: {}", userDetails);
		} catch (Exception e) {
			return null;
		}
        
        return (SocialUserDetails) userDetails;
    }
}
