package vn.wse.lms.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import vn.wse.lms.service.UserService;

@Component("userDetailsContextMapper")
public class UserDetailsContextMapper implements org.springframework.security.ldap.userdetails.UserDetailsContextMapper {

	@Autowired
	private UserService userService;
	

	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx,
			String username, Collection<? extends GrantedAuthority> authority) {
		boolean isSocialLogin = false;
		
		UserDetails userDetails= userService.loadUserByUsername(username, isSocialLogin);
		if (userDetails instanceof UserProfile) {
			UserProfile userProfile = (UserProfile) userDetails;
			userProfile.setFullName(ctx.getStringAttribute("displayName"));
			
			return userProfile;
		}
		
		return userDetails;
	}
	
	@Override
	public void mapUserToContext(UserDetails userDetails, DirContextAdapter ctx) {
		// TODO Auto-generated method stub
		
	}

	
}
