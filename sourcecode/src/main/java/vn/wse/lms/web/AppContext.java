package vn.wse.lms.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.google.api.Google;
import org.springframework.stereotype.Component;

import vn.wse.lms.bean.UserDetailsBean;
import vn.wse.lms.entity.Users;
import vn.wse.lms.service.SocialMediaService;
import vn.wse.lms.service.UserService;
import vn.wse.lms.util.Utils;

@Component
public class AppContext {
	
	@Autowired
	Google google;
	
	@Autowired
	UserService userService;
	
	public String getUsername() {
        UserDetails userDetail = this.getUserProfile();
        if (userDetail != null) {
            return userDetail.getUsername();
        }
        return null;
    }
	
	
	@SuppressWarnings("unused")
	public UserDetailsBean getUserProfile() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetailsBean bean = null;
        if (context == null) {
            return null;
        }

        Authentication authen = context.getAuthentication();

        if (authen == null) {
            return null;
        }

        Object result = authen.getPrincipal();
        if (result == null) {
            return null;
        }
         
        if (result instanceof UserDetailsBean) {
        	bean = (UserDetailsBean) result;
        	if (!bean.isUpdatedProfile()) {
        		if (SocialMediaService.GOOGLE.equals(bean.getSocialSignInProvider())) {
        			Users user = userService.getByEmail(bean.getUsername());
        			//load picture 
        	    	try {
        				String picture = getPicture();
        				//InputStream is = downloadPicture(picture);
        				String avatar = user.getEmail() + ".jpg";
        				File newPicture = new File (Utils.getFolderImg() + avatar); 
        						FileUtils.copyURLToFile(new URL(picture), newPicture);
        				user.setAvatar(avatar);
        				userService.edit(user);
        				bean.setAvatar(avatar);
        			} catch (MalformedURLException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			} catch (IOException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        		}
        		bean.setUpdatedProfile(true);
        	}
        } else {
            return null;
        }
        return bean;
    }
	
	public String getEmbededUrl(String url) {
		String result  = url;
		if (url != null) {
			result = url.replaceAll("/watch?v=", "/embed/");
		
		}
		return result;
			
	}
	
	private String getPicture() {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet httpget = new HttpGet("https://www.googleapis.com/oauth2/v1/userinfo?alt=json");
			httpget.setHeader("Authorization", "Bearer " + google.getAccessToken());
			HttpResponse resp = client.execute(httpget);
			HttpEntity entity = resp.getEntity();
			InputStream is = entity.getContent();

			String body = IOUtils.toString(is);
			JSONObject jo = new JSONObject(body);
			String picture = jo.getString("picture");

			return picture;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private InputStream downloadPicture(String url) {
		InputStream is = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			httpget.setHeader("Authorization", "Bearer " + google.getAccessToken());
			HttpResponse resp = client.execute(httpget);
			HttpEntity entity = resp.getEntity();
			is = entity.getContent();
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return is;
		
	}
}
