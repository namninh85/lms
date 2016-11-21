package vn.wse.lms.bean;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUser;
import org.springframework.stereotype.Component;

import vn.wse.lms.service.SocialMediaService;

@Component
@Scope("session")
public class UserDetailsBean extends SocialUser {

    private Long id;

    private String fullName;
    
    private String avatar;

    private List<String> roles;

    private SocialMediaService socialSignInProvider;
    
    private boolean isUpdatedProfile;

    public boolean isUpdatedProfile() {
		return isUpdatedProfile;
	}

	public void setUpdatedProfile(boolean isUpdatedProfile) {
		this.isUpdatedProfile = isUpdatedProfile;
	}

	public UserDetailsBean(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
    
    public static Builder getBuilder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public SocialMediaService getSocialSignInProvider() {
        return socialSignInProvider;
    }
	
    public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("username", getUsername())
                .append("fullName", fullName)
                .append("roles", roles)
                .append("socialSignInProvider", socialSignInProvider)
                .toString();
    }

    public static class Builder {

        private Long id;

        private String username;

        private String fullName;

        private String avatar;

        private String password;

        private List<String> roles;

        private SocialMediaService socialSignInProvider;

        private Set<GrantedAuthority> authorities;

        public Builder() {
            this.authorities = new HashSet<>();
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder roles(List<String> roles) {
            this.roles = roles;
           
            if(roles != null){
            	for (String role : roles) {				
            		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
            		this.authorities.add(authority);
            	}            	
            }

            return this;
        }

        public Builder socialSignInProvider(SocialMediaService socialSignInProvider) {
            this.socialSignInProvider = socialSignInProvider;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }
        
        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        
        public UserDetailsBean build() {
            UserDetailsBean user = new UserDetailsBean(username, password, authorities);

            user.id = id;
            user.fullName = fullName;
            user.roles = roles;
            user.socialSignInProvider = socialSignInProvider;
            user.avatar = avatar;

            return user;
        }
        
        
    }
}
