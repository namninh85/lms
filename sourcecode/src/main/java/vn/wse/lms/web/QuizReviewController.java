package vn.wse.lms.web;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.wse.lms.bean.AnsResult;
import vn.wse.lms.bean.CoursesBean;
import vn.wse.lms.bean.QuestionAnswerBean;
import vn.wse.lms.bean.QuizBean;
import vn.wse.lms.bean.QuizTemplateBean;
import vn.wse.lms.entity.Course;
import vn.wse.lms.entity.Material;
import vn.wse.lms.entity.QuizTemplate;
import vn.wse.lms.entity.Topic;
import vn.wse.lms.entity.TraineeMaterial;
import vn.wse.lms.entity.TraineeQuiz;
import vn.wse.lms.entity.TraineeQuizDetail;
import vn.wse.lms.entity.Users;
import vn.wse.lms.service.CourseService;
import vn.wse.lms.service.ReportService;
import vn.wse.lms.service.TopicService;
import vn.wse.lms.service.TraineeService;
import vn.wse.lms.service.UserService;

@Controller
@RequestMapping("/quiz-review")
public class QuizReviewController {
	@Autowired
	private AppContext appContext;
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	TraineeService traineeService;
	
	@Autowired
	ReportService reportService;
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	TopicService topicService;
	
	@Autowired
	UserService userService;
	
	
	@RequestMapping("/{courseId}/{topicId}/{quizMaterialId}")
	public String reviewQuiz(Model model,
			RedirectAttributes redirectAttributes,
			@PathVariable Long courseId, @PathVariable Long topicId, @PathVariable Long quizMaterialId ) {
		
		//Course course = courseService.findById(courseId);
		// Generate Quiz
		
		
		model.addAttribute("courseId", courseId);
		model.addAttribute("topicId", topicId);
		model.addAttribute("quizMaterialId", quizMaterialId);
		//model.addAttribute("quizTemplate", quizTemplate);
		
		return "/report/quiz-review";
    }
	
	@RequestMapping("quiz/{courseId}/{topicId}/{quizMaterialId}")
	public @ResponseBody QuizTemplateBean getQuizTemplate(Model model,
			RedirectAttributes redirectAttributes,
			@PathVariable Long courseId,  @PathVariable Long topicId, @PathVariable Long quizMaterialId) {
		QuizTemplateBean qt = new QuizTemplateBean();
		Material material = courseService.getMaterial(quizMaterialId);
		QuizTemplate quizTemplate = material.getQuizTemplate();
		qt.setQuizTemplate(quizTemplate);
		qt.setQuestionTemplates(qt.getQuestionTemplates());
		
		//get report
		qt = reportService.getQuizReport(qt, courseId, material, quizTemplate);
		return qt;
    }
	
	
	
	
}
