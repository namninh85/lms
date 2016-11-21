package vn.wse.lms.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import vn.wse.lms.entity.Topic;
import vn.wse.lms.service.TopicService;

@Controller
@RequestMapping("/topic")
public class TopicController {
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private UserIdSource userIdSource;
	
	@RequestMapping("/edit")
	public String course(HttpServletRequest request, Model model) { 
		return "topic/topic-edit::content";
    }
	
	@RequestMapping("/filepicker")
	public String picker(HttpServletRequest request, Model model) { 
		return "/topic/filepicker";
    }
	
	@RequestMapping(value= "/{topicId}", method = RequestMethod.GET)
	public @ResponseBody Topic findById(@PathVariable("topicId") Long topicId){
		Topic topic = topicService.findById(topicId);
		return topic;
	}
	
	@RequestMapping(value = "/update", method=RequestMethod.POST)
	public @ResponseBody String update(@RequestBody Topic topic, WebRequest request) { 
		try {
			topicService.save(topic);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return topic.getTopicId() != null ? topic.getTopicId().toString() : null;
	}
}
