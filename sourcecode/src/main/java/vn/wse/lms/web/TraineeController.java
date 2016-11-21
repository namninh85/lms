package vn.wse.lms.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.social.google.api.Google;
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
import vn.wse.lms.bean.TraineeSectionBean;
import vn.wse.lms.entity.Course;
import vn.wse.lms.entity.Material;
import vn.wse.lms.entity.Material.MaterialType;
import vn.wse.lms.entity.Section;
import vn.wse.lms.entity.Topic;
import vn.wse.lms.entity.TraineeMaterial;
import vn.wse.lms.entity.TraineeQuiz;
import vn.wse.lms.entity.TraineeQuizDetail;
import vn.wse.lms.entity.TraineeTopic;
import vn.wse.lms.entity.Users;
import vn.wse.lms.service.CourseService;
import vn.wse.lms.service.TopicService;
import vn.wse.lms.service.TraineeService;
import vn.wse.lms.service.UserService;
import vn.wse.lms.util.Utils;

@Controller
@RequestMapping("/trainee")
public class TraineeController {
	@Autowired
	private AppContext appContext;
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	TraineeService traineeService;
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	TopicService topicService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	Google google;
	
	@RequestMapping("/trainee-dashboard")
	public String trainerDashboard(HttpServletRequest request) { 
		return "/trainee/trainee-dashboard";
    }
	
	@RequestMapping("/")
	public String trainerCourses(HttpServletRequest request, Model model) { 
		return "/course/layout";
    }
	
	
	@RequestMapping("/all-courses1")
	public String allCourses(HttpServletRequest request, Model model) { 
		return "/course/courses::content";
    }
	
	
	@RequestMapping("/my-courses")
	public String trainerCourses(HttpServletRequest request) { 
		return "/trainee/my-courses";
    }
	
	@RequestMapping("/my-history")
	public String myHistory(HttpServletRequest request) { 
		return "/trainee/my-history";
    }

	@RequestMapping("/browse-courses")
	public String browserCourses(HttpServletRequest request,
			Model model,
			@RequestParam(value = "currentPage", required = false) Integer page
			) { 
		
		return "/trainee/courses::content";
    }
	
	@RequestMapping("/take-course/{id}")
	public String takeCourse(Model model,
			RedirectAttributes redirectAttributes,
			@PathVariable Long id, @RequestParam(value = "error", required = false) String error) {
		if (!traineeService.isRegistered(id, appContext.getUserProfile().getId())) {
			return "redirect:/trainee/view-course/"+ id ;

		};
		
		Course course = courseService.findById(id);
		// init assignment for first time
		traineeService.initCourseForTrainee(appContext.getUserProfile().getId(), course);
		
		List<TraineeSectionBean> traineeSectionBeans = new ArrayList<TraineeSectionBean>();
		List<TraineeTopic> traineeTopics = traineeService.getTraineeTopicsByCourse(id, appContext.getUserProfile().getId());
		Map<Long, TraineeTopic> topicMap = new HashMap<Long, TraineeTopic>(); 
		for (TraineeTopic tt : traineeTopics) {
			topicMap.put(tt.getTopic().getTopicId(), tt);
		}
		int idx = 0;
		if (course.getSections() != null) {			
			for (Section section : course.getSections()) {
				TraineeSectionBean sectionBean = new TraineeSectionBean();
				sectionBean.setSection(section);
				List<TraineeTopic> traineeTopicsList = new ArrayList<TraineeTopic>();
				
				if (section.getTopics() != null) {
					for (Topic topic : section.getTopics()) {
						TraineeTopic traineeTopic = topicMap.get(topic.getTopicId());
						if (traineeTopic == null) {
							traineeTopic = new TraineeTopic();
							traineeTopic.setUserId(appContext.getUserProfile().getId());
							traineeTopic.setProgress(0l);
							traineeTopic.setCourseId(course.getCourseId());
							traineeTopic.setSectionId(section.getSectionId());
							traineeTopic.setTopic(topic);
							traineeTopic.setLock(topic.getLocked());
							traineeService.saveTraineeTopic(traineeTopic);
							
						}
						if (idx ==0) {
							traineeTopic.setLock(false);
						}
						idx ++;
						traineeTopicsList.add(traineeTopic);
					}
				}
				sectionBean.setTraineeTopics(traineeTopicsList);
				traineeSectionBeans.add(sectionBean);
			}
		}
		List<Users> trainerList = courseService.getAllCourseTrainer(course.getCourseId());
		model.addAttribute("trainerList", trainerList);
		model.addAttribute("course", course);
		model.addAttribute("sections",	traineeSectionBeans);
		model.addAttribute("registered", traineeService.isRegistered(id, appContext.getUserProfile().getId()));
		model.addAttribute("error", error);
		if ("403".equals(error)) {	
			
			model.addAttribute("message", "You don't have permission to access this topic. Please register this course!");
		}
		if ("04".equals(error)) {	
			
			model.addAttribute("message", "This topic is not opened yet!");
		}
		return "/trainee/take-course";
    }
	
	@RequestMapping("/view-course/{id}")
	public String viewCourse(Model model,
			RedirectAttributes redirectAttributes,
			@PathVariable Long id, @RequestParam(value = "error", required = false) String error) {
		if (traineeService.isRegistered(id, appContext.getUserProfile().getId())) {
			return "redirect:/trainee/take-course/"+ id ;

		};
		Course course = courseService.findById(id);
		
		List<Users> trainerList = courseService.getAllCourseTrainer(course.getCourseId());
		model.addAttribute("trainerList", trainerList);
		model.addAttribute("course", course);
		model.addAttribute("registered", traineeService.isRegistered(id, appContext.getUserProfile().getId()));
		model.addAttribute("error", error);
		if ("403".equals(error)) {	
			
			model.addAttribute("message", "You don't have permission to access this topic. Please register this course!");
		}
		if ("04".equals(error)) {	
			
			model.addAttribute("message", "This topic is not opened yet!");
		}
		return "/trainee/view-course";
    }
	
	@RequestMapping("/view-topic/{courseId}/{topicId}")
	public String viewTopic(Model model,
			RedirectAttributes redirectAttributes,
			@PathVariable Long courseId, @PathVariable Long topicId ) {
		Topic topic = topicService.findById(topicId);
		Course course = courseService.findById(courseId);
		if (!traineeService.isRegistered(courseId, appContext.getUserProfile().getId())) {
			return "redirect:/trainee/view-course/"+ courseId + "?error=403";
		}
		
		if (topic.getStartDate() != null && topic.getStartDate().after(Calendar.getInstance().getTime())) {
			return "redirect:/trainee/take-course/"+ courseId + "?error=04";
		}
		
		// update trainee status
		traineeService.updateStatus(appContext.getUserProfile().getId(), courseId, topic);
		traineeService.updateTrainingProgress(appContext.getUserProfile().getId(), courseId, topic.getTopicId());
				
		model.addAttribute("course", course);
		//get all material in topics
		model.addAttribute("materials", traineeService.getMaterials(topic, courseId, appContext.getUserProfile().getId()));
		List<Users> trainerList = courseService.getAllCourseTrainer(courseId);
		model.addAttribute("trainerList", trainerList);
		model.addAttribute("topic", topic);
		//get next topic
		TraineeTopic nextTopic = traineeService.nextTopic(course, topicId, appContext.getUserProfile().getId()); 
		if ( nextTopic != null) {
			model.addAttribute("nextTopic", nextTopic);
		}
		
		
		
		return "/trainee/view-topic";
    }
	
	
	@RequestMapping("/download/material/{courseId}/{materialId}")
	public void downloadMaterial(Model model,
			RedirectAttributes redirectAttributes,
			@PathVariable Long courseId, @PathVariable Long materialId ,HttpServletRequest request, HttpServletResponse response) {
		try {
		Course course = courseService.findById(courseId);
		Material material = null;
		if (course != null) {
			material = courseService.getMaterial(materialId);
		}
		
		
		
		if (material != null && MaterialType.FILE.getStringValue().equals(material.getType()) && material.getPath() != null) {
			FileInputStream fis = new FileInputStream(Utils.getFolderCourse() + material.getPath());
			response.setContentType("application/text");
			response.addHeader("Content-Disposition",
					"attachment; filename=\"" + material.getPath());
			org.apache.commons.io.IOUtils.copy(fis,
					response.getOutputStream());
			response.flushBuffer();
			fis.close();
			traineeService.updateMaterialStatus(appContext.getUserProfile().getId(), courseId, material, 1, 0, 0);
			traineeService.updateTrainingProgress(appContext.getUserProfile().getId(), courseId, material.getTopic().getTopicId());
			
		}
		/*
		if (material != null) {
			traineeService.updateMaterialStatus(appContext.getUserProfile().getId(), courseId, material, 1);
		}
		*/
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//return "/trainee/view-topic";
    }
	
	
	@RequestMapping("/take-quiz/{courseId}/{topicId}/{quizMaterialId}")
	public String takeQuiz(Model model,
			RedirectAttributes redirectAttributes,
			@PathVariable Long courseId, @PathVariable Long topicId, @PathVariable Long quizMaterialId ) {
		
		Course course = courseService.findById(courseId);
		// Generate Quiz
		long traineeQuizId = traineeService.generateQuiz(course, quizMaterialId, appContext.getUserProfile().getId());
		model.addAttribute("course", course);
		return "redirect:/trainee/proceed-quiz/"+ courseId + "/" + topicId + "/"+ quizMaterialId + "/" + traineeQuizId;
    }
	
	@RequestMapping("/proceed-quiz/{courseId}/{topicId}/{quizMaterialId}/{traineeQuizId}")
	public String proceedQuiz(Model model,
			RedirectAttributes redirectAttributes,
			@PathVariable Long courseId, @PathVariable Long topicId, @PathVariable Long quizMaterialId, @PathVariable Long traineeQuizId  ) {
		model.addAttribute("courseId", courseId );
		Course course = courseService.findById(courseId);
		Topic topic = topicService.findById(topicId);
		model.addAttribute("course", course);
		model.addAttribute("topic", topic);
		model.addAttribute("quizMaterialId", quizMaterialId );
		model.addAttribute("traineeQuizId", traineeQuizId );
		return "/trainee/take-quiz";
    }
	
	@RequestMapping("/next-question/{courseId}/{topicId}/{quizMaterialId}/{traineeQuizId}")
	public @ResponseBody QuizBean nextQuestion(Model model,
			RedirectAttributes redirectAttributes,
			@PathVariable Long courseId,  @PathVariable Long topicId, @PathVariable Long quizMaterialId, @PathVariable Long traineeQuizId  ) {
		QuizBean qb = new QuizBean();
		qb.setPendingList(traineeService.getPendingQuestion(traineeQuizId));
		List<TraineeQuizDetail> leftList = traineeService.getPendingQuestion(traineeQuizId);
		qb.setLeft(leftList.size());
		qb.setTotal(traineeService.getTotalQuestion(traineeQuizId));
		qb.setCorrect(traineeService.getTotalCorrect(traineeQuizId));
		qb.setWrong(traineeService.getTotalWrong(traineeQuizId));
		
		Material material = courseService.getMaterial(quizMaterialId);
		TraineeQuiz tq = traineeService.getTraineeQuiz(traineeQuizId);
		long time = 0;
		if (tq.getStartDate() != null ) {
			time = Calendar.getInstance().getTimeInMillis() - tq.getStartDate().getTime();
		}
		time = time/1000 % 60;
		Long totalTime = 100l;
		if (material.getQuizTemplate().getQuizTime() != null) {
			totalTime = material.getQuizTemplate().getQuizTime()*60l;
		}
		Long timeSpent = time;
		qb.setTimeLeft(totalTime.intValue() - timeSpent.intValue());
		if (qb.getTimeLeft() < 0) {
			return qb;
		}
		if (leftList.size() > 0) {
			QuestionAnswerBean qa = new QuestionAnswerBean();
			TraineeQuizDetail tqd = leftList.get(0);
			qa.setQuestion(tqd);
			qa.setAnsList(traineeService.getAnswerTemplates(tqd.getQuestionTemplateId()));
			qb.setCurrent(qa);
		}
		return qb;
    }
	
	
	@RequestMapping("/ans-question/{courseId}/{topicId}/{quizMaterialId}/{traineeQuizId}")
	public @ResponseBody QuizBean ansQuestion(Model model,
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "ans", required = false) String ans,
			@RequestParam(value = "traineeQuizDetailId", required = false) long traineeQuizDetailId,
			@PathVariable Long courseId, @PathVariable Long topicId, @PathVariable Long quizMaterialId, @PathVariable Long traineeQuizId  ) {
		
		AnsResult ansResult = traineeService.ansQuestion(traineeQuizDetailId, ans);
		QuizBean qb = new QuizBean();
		qb.setAnsResult(ansResult);
		
		/*
		qb.setPendingList(traineeService.getPendingQuestion(traineeQuizId));
		List<TraineeQuizDetail> leftList = traineeService.getPendingQuestion(traineeQuizId);
		
		Material material = courseService.getMaterial(quizMaterialId);
		TraineeQuiz tq = traineeService.getTraineeQuiz(traineeQuizId);
		long time = 0;
		if (tq.getStartDate() != null ) {
			time = Calendar.getInstance().getTimeInMillis() - tq.getStartDate().getTime();
		}
		time = time/1000 % 60;
		Long totalTime = 100l;
		if (material.getQuizTemplate().getQuizTime() != null) {
			totalTime = material.getQuizTemplate().getQuizTime()*60l;
		}
		Long timeSpent = time;
		qb.setTimeLeft(totalTime.intValue() - timeSpent.intValue());
		
		qb.setLeft(leftList.size());
		qb.setTotal(traineeService.getTotalQuestion(traineeQuizId));
		qb.setCorrect(traineeService.getTotalCorrect(traineeQuizId));
		qb.setWrong(traineeService.getTotalWrong(traineeQuizId));
		if (qb.getTimeLeft() < 0) {
			return qb;
		}
		
		if (leftList.size() > 0) {
			QuestionAnswerBean qa = new QuestionAnswerBean();
			TraineeQuizDetail tqd = leftList.get(0);
			qa.setQuestion(tqd);
			qa.setAnsList(traineeService.getAnswerTemplates(tqd.getQuestionTemplateId()));
			qb.setCurrent(qa);
		} 
		
		*/
		return qb;
    }
	
	
	@RequestMapping("/skip-question/{courseId}/{topicId}/{quizMaterialId}/{traineeQuizId}")
	public @ResponseBody QuizBean skipQuestion(Model model,
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "traineeQuizDetailId", required = false) long traineeQuizDetailId,
			@PathVariable Long courseId, @PathVariable Long topicId, @PathVariable Long quizMaterialId, @PathVariable Long traineeQuizId  ) {
		
		AnsResult ansResult = traineeService.skipQuestion(traineeQuizDetailId);
		QuizBean qb = new QuizBean();
		qb.setAnsResult(ansResult);
		
		
		return qb;
    }
	
	@RequestMapping("/ans-finish/{courseId}/{topicId}/{quizId}/{traineeQuizId}")
	public @ResponseBody QuizBean ansFinish(Model model,
			RedirectAttributes redirectAttributes,			
			@PathVariable Long courseId, @PathVariable Long topicId, @PathVariable Long quizId, @PathVariable Long traineeQuizId  ) {
		QuizBean qb = new QuizBean();
		Material material = courseService.getMaterial(quizId);
		TraineeQuiz traineeQuiz = traineeService.calculateScore(traineeQuizId, appContext.getUserProfile().getId());
		if (traineeQuiz == null) {
			return qb;
		}
		traineeService.updateMaterialStatus(appContext.getUserProfile().getId(), courseId, material, 1, traineeQuiz.getScore(), traineeQuizId);
		traineeService.updateTrainingProgress(appContext.getUserProfile().getId(), courseId, topicId);
		
		qb.setPendingList(traineeService.getPendingQuestion(traineeQuizId));
		List<TraineeQuizDetail> leftList = traineeService.getPendingQuestion(traineeQuizId);
		
		qb.setLeft(leftList.size());
		qb.setTotal(traineeService.getTotalQuestion(traineeQuizId));
		qb.setCorrect(traineeService.getTotalCorrect(traineeQuizId));
		qb.setWrong(traineeService.getTotalWrong(traineeQuizId));
		
		if (qb.getTimeLeft() < 0) {
			return qb;
		}
		
		if (leftList.size() > 0) {
			QuestionAnswerBean qa = new QuestionAnswerBean();
			TraineeQuizDetail tqd = leftList.get(0);
			qa.setQuestion(tqd);
			qa.setAnsList(traineeService.getAnswerTemplates(tqd.getQuestionTemplateId()));
			qb.setCurrent(qa);
		} 
		return qb;
    }
	
	
	@RequestMapping(value = "/getAll", method=RequestMethod.GET )
	public @ResponseBody List<Users> getAll() { 
		return  userService.getAllTrainee();
    }

	@RequestMapping(value= "/searchMyCourses", method = RequestMethod.GET)
	public @ResponseBody CoursesBean searchMyCourses(@RequestParam("page") Integer pageNumber, @RequestParam("count") Integer count){

		CoursesBean bean = new CoursesBean();
		if(pageNumber != null)
			bean.setPage(pageNumber);
		if( count != null)
			bean.setLimit(count);
		return courseService.searchRegisteredCourses(bean, appContext.getUserProfile().getId());
	}
	
	@RequestMapping(value= "/searchMyHistory", method = RequestMethod.GET)
	public @ResponseBody CoursesBean searchMyHistory(@RequestParam("page") Integer pageNumber, @RequestParam("count") Integer count){

		CoursesBean bean = new CoursesBean();
		if(pageNumber != null)
			bean.setPage(pageNumber);
		if( count != null)
			bean.setLimit(count);
		return courseService.searchMyHistory(bean, appContext.getUserProfile().getId());
	}
	
	
	@RequestMapping(value= "/searchPaging", method = RequestMethod.GET)
	public @ResponseBody CoursesBean searchPaging(@RequestParam("page") Integer pageNumber, @RequestParam("count") Integer count){

		CoursesBean bean = new CoursesBean();
		if(pageNumber != null)
			bean.setPage(pageNumber);
		if( count != null)
			bean.setLimit(count);
		return courseService.searchOpenCourses(bean);
	}
	
	public String getScore(long courseId, long materialId) {
		
		TraineeMaterial tm = traineeService.getTrainingMaterial(appContext.getUserProfile().getId(), courseId, materialId);
		if (tm != null && tm.getScore() != null) {
			return tm.getScore().toString();
		}
		return "-";
	}
	
	public int getProgress(long topicId) {
		return traineeService.getTopicProgress(appContext.getUserProfile().getId(), topicId);
	}
}
