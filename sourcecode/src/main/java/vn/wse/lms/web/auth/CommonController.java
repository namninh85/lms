package vn.wse.lms.web.auth;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommonController {
	
	@RequestMapping(value = "/setUrl")
	public @ResponseBody String login(HttpServletRequest request,@RequestParam(value="view", required=false)String view,@RequestParam(value="url", required=false)String url) {
		String newUrl = "";
		if(StringUtils.isNotBlank(view))
			newUrl += "/"+view+"/#/";
		if(StringUtils.isNotBlank(url))
			newUrl +=url;
			
		if(StringUtils.isNotBlank(newUrl))	
			request.getSession(true).setAttribute("currentUrl", newUrl);
		
		return "success"; 
	}
	
}
