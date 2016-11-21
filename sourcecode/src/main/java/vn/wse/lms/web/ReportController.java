package vn.wse.lms.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.wse.lms.bean.CourseQuizBean;
import vn.wse.lms.bean.CoursesBean;
import vn.wse.lms.bean.QuizReportBean;
import vn.wse.lms.bean.TraineeBean;
import vn.wse.lms.bean.TraineeCourseBean;
import vn.wse.lms.entity.Comment;
import vn.wse.lms.entity.Course;
import vn.wse.lms.entity.Material;
import vn.wse.lms.entity.QuizTemplate;
import vn.wse.lms.entity.Section;
import vn.wse.lms.entity.Topic;
import vn.wse.lms.entity.TraineeMaterial;
import vn.wse.lms.entity.TraineeStatus;
import vn.wse.lms.entity.Users;
import vn.wse.lms.service.CourseService;
import vn.wse.lms.service.ReportService;
import vn.wse.lms.service.TraineeService;
import vn.wse.lms.service.UserService;

@Controller
@RequestMapping("/report")
public class ReportController {
	@Autowired
	private AppContext appContext;
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TraineeService traineeService;
	
	@Autowired
	ReportService reportService;
	
	@RequestMapping("/")
	public String adminLayoutTrainee(HttpServletRequest request, Model model) { 
		return "/report/layout";
    }
	
	@RequestMapping("/course-report")
	public String courseReport(HttpServletRequest request, Model model) { 
		return "/report/courses::content";
    }
	
	@RequestMapping(value = "/trainee-report", method = RequestMethod.GET)
	public String traineeReport(HttpServletRequest request, Model model) {
		return "/report/trainees::content";
    }
	
	@RequestMapping(value = "/quiz-report", method = RequestMethod.GET)
	public String quizReport(HttpServletRequest request, Model model) {
		return "/report/quiz-report::content";
    }
	
	@RequestMapping(value = "/course-detail", method = RequestMethod.GET)
	public String courseDetail(HttpServletRequest request, Model model) {
		return "/report/course-detail::content";
    }
	
	@RequestMapping("/trainee-detail")
	public String traineeDetail(HttpServletRequest request, Model model) { 
		return "/report/trainee-detail::content";
    }
	
	@RequestMapping(value =  "/courses", method = RequestMethod.GET)
	public @ResponseBody CoursesBean searchPaging(@RequestParam(value = "status", required = false) String status, @RequestParam("page") Integer pageNumber, @RequestParam("count") Integer count){

		CoursesBean bean = new CoursesBean();
		
		if(pageNumber != null)
			bean.setPage(pageNumber);
		if( count != null)
			bean.setLimit(count);
		String userName = appContext.getUsername();
		bean.setStatus(status);
		return courseService.searchCourses(bean, userName);
	}
	
	@RequestMapping(value =  "/trainees", method = RequestMethod.GET)
	public @ResponseBody TraineeBean searchTrainees(@RequestParam("page") Integer pageNumber, @RequestParam("count") Integer count, @RequestParam("courseId") long courseId){

		TraineeBean bean = new TraineeBean();
		bean.setCourseId(courseId);
		if(pageNumber != null)
			bean.setPage(pageNumber);
		if( count != null)
			bean.setLimit(count);
		return traineeService.searchTrainees(bean);
	}

	@RequestMapping(value =  "/getAllQuiz", method = RequestMethod.GET)
	public @ResponseBody List<CourseQuizBean> getAllQuiz(@RequestParam("courseId") long courseId){
		return reportService.getAllQuiz(courseId);
	}
	
	@RequestMapping(value =  "/getQuizSummary", method = RequestMethod.GET)
	public @ResponseBody QuizReportBean getQuizSummary(@RequestParam("courseId") long courseId, @RequestParam("materialId") long materialId){
		QuizReportBean bean = new QuizReportBean();
		bean = reportService.getQuizSummary(courseId, materialId);
		return bean;
	}


	@RequestMapping(value =  "/traineeDetailData", method = RequestMethod.GET)
	public @ResponseBody TraineeCourseBean getTraineeDetailData(@RequestParam("courseId") long courseId, @RequestParam("traineeId") long traineeId){
	
		TraineeCourseBean traineeCourseBean = new TraineeCourseBean();
		Course course = courseService.findById(courseId);
		traineeCourseBean.setCourse(course);
		TraineeStatus status = traineeService.getCurrentStatus(traineeId, courseId);
		traineeCourseBean.setTraineeStatus(status);
		if (course != null && course.getSections() != null) {
			for (Section section: course.getSections()) {
				if (section.getTopics() != null) {
					for (Topic topic: section.getTopics()) {
						if (topic.getMaterials() != null) {
							List<Material> tmp = new ArrayList<Material>();
							for (Material material : topic.getMaterials()) {
								
								if (material.getType().equals("4")) {
								TraineeMaterial tm = traineeService.getTrainingMaterial(traineeId, courseId, material.getMaterialId());
								QuizTemplate qt = material.getQuizTemplate();
								if (tm.getTraineeQuiz() != null && tm.getTraineeQuiz().getPercentScore() != null &&  tm.getTraineeQuiz().getPercentScore() >= 100*qt.getPassingGrade()) {
									tm.setHighScore(true);
								} else {
									tm.setHighScore(false);
								}
								material.setTraineeMaterial(tm);
								
								tmp.add(material);
								} 
								
								
							}
							topic.setMaterials(tmp);
						}
					}
				}
			}
		}
		Users user = userService.getById(traineeId);
		traineeCourseBean.setTrainee(user);
		TraineeStatus traineeStatus = traineeService.getCurrentStatus(traineeId, courseId);
		traineeCourseBean.setTraineeStatus(traineeStatus);
		return traineeCourseBean;
	}
	
	@RequestMapping(value = "/saveAssessment", method=RequestMethod.POST)
	public @ResponseBody TraineeCourseBean saveTrainee(@RequestBody TraineeCourseBean traineeCourse) { 
		if (traineeCourse != null && traineeCourse.getTraineeStatus() != null) {
//			if (traineeCourse.getTraineeStatus().getTraineeTopicId() != null) {
//				traineeService.updateStatus(traineeCourse.getTraineeStatus());
//			} else {
//				TraineeStatus status = traineeCourse.getTraineeStatus();			
//				traineeService.updateStatus(status);
//			}
			TraineeStatus status = traineeCourse.getTraineeStatus();
		    if (status.getCourseId() == null) {
		    	status.setCourseId(traineeCourse.getCourse().getCourseId());
		    }
		    if (status.getUserId() == null) {
		    	status.setUserId(traineeCourse.getTrainee().getUserId());
		    }
			traineeService.updateStatus(traineeCourse.getTraineeStatus());
		}
		return traineeCourse;
	}

	@RequestMapping(value = "/addComment", method=RequestMethod.POST)
	public @ResponseBody Boolean addComment(@RequestBody Comment comment) { 
		comment.setCommentDate(Calendar.getInstance().getTime());
		comment.setUserComment(appContext.getUserProfile().getFullName());
		reportService.addComment(comment);
		return true;
	}
	
	@RequestMapping(value =  "/getComments", method = RequestMethod.GET)
	public @ResponseBody List<Comment> getComments(@RequestParam("courseId") long courseId, @RequestParam("traineeId") long traineeId){
		return reportService.getComments(courseId, traineeId);
	}
	
	
	@RequestMapping(value =  "/quizReportDetail", method = RequestMethod.GET)
	public @ResponseBody TraineeCourseBean quizReportDetail(@RequestParam("courseId") long courseId){
		return null;
	}
}
