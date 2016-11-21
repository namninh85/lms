package vn.wse.lms.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import vn.wse.lms.entity.Users;

public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {
	UserDetails loadUserByUsername(String username, boolean isSocialLogin) throws UsernameNotFoundException;

	Users getByEmail(String username);
}
