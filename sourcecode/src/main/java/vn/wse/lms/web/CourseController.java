package vn.wse.lms.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.wse.lms.bean.CoursesBean;
import vn.wse.lms.entity.Course;
import vn.wse.lms.service.CourseService;
import vn.wse.lms.service.TopicService;

@Controller
@RequestMapping("/course")
public class CourseController {

	@Autowired CourseService courseService;
	
	@Autowired TopicService topicService;
	
	@Autowired
	AppContext appContext;
	
	@RequestMapping("/")
	public String trainerCourses(HttpServletRequest request, Model model) { 
		return "/course/layout";
    }
	
	@RequestMapping("/list")
	public String courses(HttpServletRequest request, Model model) { 
		return "course/courses::content";
    }
	
	@RequestMapping("/edit")
	public String course(HttpServletRequest request, Model model) { 
		return "course/course-edit::content";
    }
	
	@RequestMapping(value= "/getAll", method = RequestMethod.GET)
	public @ResponseBody List<Course> getAll(){
		return courseService.getAll();
	}
	
	@RequestMapping(value= "/searchPaging", method = RequestMethod.GET)
	public @ResponseBody CoursesBean searchPaging(@RequestParam("page") Integer pageNumber, @RequestParam("count") Integer count){

		CoursesBean bean = new CoursesBean();
		if(pageNumber != null)
			bean.setPage(pageNumber);
		if( count != null)
			bean.setLimit(count);
		String userName = appContext.getUsername();
		
		return courseService.searchCourses(bean, userName);
	}
	
	@RequestMapping(value= "/search-courses", method = RequestMethod.GET)
	public @ResponseBody List<Course> searchCourse(){
		return courseService.getAll();
	}
	
	@RequestMapping(value= "/{courseId}", method = RequestMethod.GET)
	public @ResponseBody Course findById(@PathVariable("courseId") Long courseId){
		Course course = courseService.findById(courseId);
		return course;
	}
	
	@RequestMapping(value = "/update", method=RequestMethod.POST)
	public @ResponseBody String update(@RequestBody Course course) throws Exception { 
		course.setStatus(Course.CourseStatus.ACTIVE.isValue());
		courseService.save(course);
		return course.getCourseId() != null ? course.getCourseId().toString() : null;
	}
	
	@RequestMapping(value = "/detele/{courseId}", method=RequestMethod.GET)
	public @ResponseBody void update(@PathVariable("courseId") Long courseId) throws Exception { 
		Course course=	courseService.findById(courseId);
		course.setStatus(Course.CourseStatus.INACTIVE.isValue());
		courseService.save(course);
	}
	
	@RequestMapping(value = "/clone", method=RequestMethod.POST)
	public @ResponseBody String clone(@RequestBody Course courseClone) throws Exception { 
		courseClone = courseService.cloneCourse(courseClone);
		
		
		return courseClone.getCourseId() != null ? courseClone.getCourseId().toString() : null;
	}
}
