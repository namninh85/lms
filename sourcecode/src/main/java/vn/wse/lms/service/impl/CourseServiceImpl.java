package vn.wse.lms.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.drive.DriveFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.services.drive.Drive;

import vn.wse.lms.bean.CoursesBean;
import vn.wse.lms.entity.AnswerTemplate;
import vn.wse.lms.entity.Course;
import vn.wse.lms.entity.CourseRegistration;
import vn.wse.lms.entity.CourseTrainer;
import vn.wse.lms.entity.Material;
import vn.wse.lms.entity.QuestionTemplate;
import vn.wse.lms.entity.QuizSection;
import vn.wse.lms.entity.QuizTemplate;
import vn.wse.lms.entity.Section;
import vn.wse.lms.entity.Topic;
import vn.wse.lms.entity.Users;
import vn.wse.lms.repository.CourseRegistrationRepository;
import vn.wse.lms.repository.CourseRepository;
import vn.wse.lms.repository.CourseTrainerRepository;
import vn.wse.lms.repository.MaterialRepository;
import vn.wse.lms.repository.QuestionTemplateRepository;
import vn.wse.lms.repository.SearchRepository;
import vn.wse.lms.repository.SectionRepository;
import vn.wse.lms.repository.TopicRepository;
import vn.wse.lms.service.CourseService;
import vn.wse.lms.service.Role;
import vn.wse.lms.service.UserService;
import vn.wse.lms.util.Constant;
import vn.wse.lms.util.GoogleSpreadSheetUtil;
import vn.wse.lms.util.Utils;
import vn.wse.lms.web.AppContext;

@Service("courseService")
@Transactional(readOnly=true)
public class CourseServiceImpl implements CourseService {
	private static Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private MaterialRepository materialRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SearchRepository searchRepository;
	
	@Autowired
	private CourseRegistrationRepository courseRegistrationRepository;
	
	@Autowired
	private CourseTrainerRepository courseTrainerRepository;
	
	@Autowired
	private SectionRepository sectioncRepository;
	
	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private QuestionTemplateRepository questionTemplateRepository;
	
	
	
	@Autowired
	AppContext appContext;
	
	@Autowired
	Google google;
	
	@Override
	public List<Course> getAll(){
		return courseRepository.getAllCourses();
	}

	@Override
	public Course findById(Long courseId) {
		Course course = courseRepository.findOne(courseId);
		return course;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void save(Course course) throws Exception {
		
		Long courseId = course.getCourseId();
		
		if(course.getSections() != null && course.getSections().size() > 0){
			if (courseId != null) 
				sectioncRepository.deleteSectionNotInListByCourseId(course.getSections(), courseId);
			
			
			for (Section section : course.getSections()) {
				
				//update topics
				if(section.getTopics() != null && section.getTopics().size() > 0){
					if(section.getSectionId() != null)
						topicRepository.deleteTopicNotInListBySectionId(section.getTopics(), section.getSectionId());
				}else{
					if(section.getSectionId() != null)
						topicRepository.deleteAllTopicBySectionId(section.getSectionId());
				}
				
				section.setCourse(course);
			}
		}else{
			if (courseId != null) {
				sectioncRepository.deleteAllSectionByCourseId(courseId);				
			}
		}
		
		//update trainees
		if(course.getTrainees() != null && course.getTrainees().size() >0){			
			if(courseId != null)
				courseRegistrationRepository.deleteCourseRegistrationNotInListByCourseId(course.getTrainees(), courseId);
		
			Iterator<CourseRegistration> trainees = course.getTrainees().iterator();
			while (trainees.hasNext()) {
				CourseRegistration courseRegistration = (CourseRegistration) trainees.next();
				int count =courseRegistrationRepository.countByUserIdAndCourseId(courseRegistration.getUserId(), courseId);
				if(count > 0)
					trainees.remove();
				else{
					courseRegistration.setCourse(course);
				}
			}
		}else{
			if(courseId != null)
				courseRegistrationRepository.deleteAllCourseRegistrationByCourseId(courseId);
		}
		
		//update trainers
		if(course.getTrainers() != null && course.getTrainers().size() >0){			
			if(courseId != null)
				courseTrainerRepository.deleteCourseTrainerNotInListByCourseId(course.getTrainers(), courseId);
		
			Iterator<CourseTrainer> trainers = course.getTrainers().iterator();
			while (trainers.hasNext()) {
				CourseTrainer courseTrainer = (CourseTrainer) trainers.next();
				int count = courseTrainerRepository.countByTrainerIdAndCourseId(courseTrainer.getUserId(), courseId);
				if(count > 0)
					trainers.remove();
				else{
					courseTrainer.setCourse(course);
				}
			}
		}else{
			if(courseId != null)
				courseTrainerRepository.deleteAllCourseTrainerByCourseId(courseId);
		}

		//update icon
		String oldIcon ="";
		String newIcon = course.getIcon();
		if (course.getCourseId() != null) {
			Course courseOld = courseRepository.findOne(course.getCourseId());
			oldIcon = courseOld.getIcon();
		}
			
		if(StringUtils.isNotBlank(newIcon) && !newIcon.equalsIgnoreCase(oldIcon) ){
			File fileOldIcon = new File(Utils.getFolderImg()+oldIcon);
			if(fileOldIcon.exists() && fileOldIcon.isFile())
				FileUtils.deleteQuietly(fileOldIcon);
			Utils.move(Constant.PREFIX_FILE_TEMP+newIcon, Utils.getFoldelTemp(), Utils.getFolderImg());
		}
		courseRepository.save(course);
		
		if(course.getCourseId() != null){
			course=	courseRepository.findOne(course.getCourseId());
			String courseCode = generateCode(course);
			course.setCourseCode(courseCode);
			courseRepository.save(course);
		}
	}

	private String generateCode(Course course) throws Exception{
		if(course.getCourseId() == null)
			throw new Exception("Generate course code fail");
		
		String category= course.getCategory();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		String date = dateFormat.format(new Date());
		String id = course.getCourseId().toString();
		
		String code="WSE" + "_" + category + "_" + date + "_" + id;	
		
		return code;
	}
	
	@Override
	public CoursesBean searchCourses(CoursesBean coursesBean, String userName) {
		return searchRepository.searchCourses(coursesBean, userName);
	}
	
	@Override
	public CoursesBean searchRegisteredCourses(CoursesBean coursesBean, long traineeId) {
		return searchRepository.searchRegisteredCourses(coursesBean, traineeId);
	}
	
	@Override
	public CoursesBean searchMyHistory(CoursesBean coursesBean, long traineeId) {
		return searchRepository.searchMyHistory(coursesBean, traineeId);
	}


	@Override
	public Material getMaterial(long materialId) {
		return materialRepository.findOne(materialId);
	}

	@Override
	public List<Users> getAllCourseTrainer(long courseId){
		return courseRepository.getAllCourseTrainer(courseId);
	}

	@Override
	public List<Course> getAllCoursesByUser(String username) {
		
		try {
			List<String> roles = userService.getRoles(username);
			
			if(roles.contains(Role.ADMIN.toString()))
				return getAll();
			else if(roles.contains(Role.TRAINER.toString())){
				return courseRepository.findCoursesByTrainer(username);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Course>();
	}

	@Override
	public CoursesBean searchOpenCourses(CoursesBean coursesBean) {
		return searchRepository.searchOpenCourses(coursesBean);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public Course cloneCourse(Course courseClone) throws Exception {// create
																	// Topic
																	// first
		List<Topic> listTopics = new ArrayList<Topic>();
		for (Section section : courseClone.getSections()) {
			for (Topic topic : section.getTopics()) {
				topic.setTopicId(null);
				topic.setSectionId(null);
				topic.setStartDate(courseClone.getStartDate());
				topic.setEndDate(courseClone.getEndDate());
				topic.setBelongSection(section.getTitle());
				listTopics.add(topic);
			}
		}

		for (Topic topic : listTopics) {
			topic.setTopicId(null);
			if (topic.getMaterials() != null) {
				for (Material material : topic.getMaterials()) {
					material.setMaterialId(null);
					material.setTopic(topic);
					material.setTraineeMaterial(null);
					if (material.getQuizTemplate() != null) {
						QuizTemplate quizTemplate = material.getQuizTemplate();
						quizTemplate.setQuizTemplateId(null);
						material.setQuizTemplate(quizTemplate);
						if (quizTemplate.getQuizSections() != null) {
							for (QuizSection quizSection : quizTemplate.getQuizSections()) {
								quizSection.setQuizSectionId(null);
								quizSection.setQuizTemplate(quizTemplate);
							}
						}

						if (quizTemplate.getQuestionTemplates() != null) {
							for (QuestionTemplate questionTemplate : quizTemplate.getQuestionTemplates()) {
								questionTemplate.setQuestionTemplateId(null);
								;
								questionTemplate.setQuizTemplate(quizTemplate);
								if (questionTemplate.getAnswerTemplates() != null) {
									for (AnswerTemplate answerTemplate : questionTemplate.getAnswerTemplates()) {
										answerTemplate.setAnswerTemplateId(null);
										answerTemplate.setQuestionTemplate(questionTemplate);
									}
								}
							}
						}

					}

				}
			}
			topicRepository.save(topic);
		}

		// Save Course

		courseClone.setCourseId(null);
		courseClone.setCreatedDate(new Date());
		courseClone.setUpdatedDate(null);

		for (Section section : courseClone.getSections()) {
			section.setSectionId(null);
			section.setCourse(courseClone);
			section.setStartDate(courseClone.getStartDate());
			section.setToDate(courseClone.getEndDate());
			section.setTopics(null);

		}
		courseClone.setTrainees(new ArrayList<>());

		for (CourseTrainer courseTrainer : courseClone.getTrainers()) {
			courseTrainer.setCourse(courseClone);
			courseTrainer.setCourseTrainerId(null);
		}

		courseClone.setStatus(Course.CourseStatus.ACTIVE.isValue());
		this.save(courseClone);

		// update topics to section
		for (Topic topic : listTopics) {
			for (Section section : courseClone.getSections()) {
				if (topic.getBelongSection() != null && section.getTitle() != null) {
					if (topic.getBelongSection().equalsIgnoreCase(section.getTitle())) {
						topic.setSectionId(section.getSectionId());
						this.saveTopic(topic);
					}
				}
			}
		}
		
		return courseClone;
	}
	
	
	/**
	 * saveTopic
	 * 
	 * @param topic
	 * @throws Exception
	 */
	public void saveTopic(Topic topic) throws Exception {
		String oldIcon ="";
		String newIcon = topic.getIcon();
		if (topic.getTopicId() != null) {
			Topic topicOld = topicRepository.findOne(topic.getTopicId());
			oldIcon = topicOld.getIcon();
		}
		
		if(topic.getMaterials() != null && topic.getMaterials().size() >0){
			//update marterials
			if(topic.getTopicId() != null)
				materialRepository.deleteMaterialNotInListByTopicId(topic.getMaterials(), topic.getTopicId());
			
			int index = 0;
			for (Material material : topic.getMaterials()) {
				material.setTopic(topic);
				material.setPosition(index);
				
				if(Material.MaterialType.QUIZ.getStringValue().equals(material.getType())){
					
					material.setQuizTemplate(material.getQuizTemplate());
					if(material.getQuizTemplate() != null && material.getQuizTemplate().getQuestionTemplates() != null){
						boolean isNewParser = true;
						for (QuestionTemplate questionTemplate : material.getQuizTemplate().getQuestionTemplates()) {
							if(questionTemplate.getQuestionTemplateId() != null)
								isNewParser = false;
						}
						
						if(isNewParser) {
							if(material.getMaterialId() != null){
								Long quizTemplateId = materialRepository.findQuizTemplateIdByMaterialId(material.getMaterialId());
								if(quizTemplateId != null){
									if(material.getQuizTemplate() != null){
										material.getQuizTemplate().setQuizTemplateId(quizTemplateId);
									}
									questionTemplateRepository.deleteQuestionTeamplateAndAnswerTemplateByQuizTeamplateId(quizTemplateId);
								}
							}
						}
						
						for (QuestionTemplate questionTemplate : material.getQuizTemplate().getQuestionTemplates()) {
							questionTemplate.setQuizTemplate(material.getQuizTemplate());
							if(questionTemplate.getAnswerTemplates() != null){
								for (AnswerTemplate answerTemplate : questionTemplate.getAnswerTemplates()) {
									answerTemplate.setQuestionTemplate(questionTemplate);
								}
							}
						}
					}
				}
				
				if(StringUtils.isNotBlank(material.getFileId())){
					String path = Utils.getFolderCourse();
					String fileName = material.getPath();
					
					if(!Utils.isFileExits(path, fileName)){
						
						Drive driveService = GoogleSpreadSheetUtil.getDriveService(); 
						DriveFile file = google.driveOperations().getFile(material.getFileId());
						
						if (file == null) {
							throw new Exception(material.getMaterialName()+" File not found");
						}
						
						double mb= file.getFileSize()/1024/1024;
						if(mb > 50)
							throw new Exception("File too large, choose file less than or equal 50Mb!");
						
						//download file from google drive
						String newFileName = GoogleSpreadSheetUtil.dowloadFile(driveService, file, google);
						
						material.setPath(newFileName);
					}
				}
				
				index++;
			}
		}else{
			if(topic.getTopicId() != null)
				materialRepository.deleteAllMaterialByTopicId(topic.getTopicId());
			
		}
		
		if(StringUtils.isNotBlank(newIcon) && !newIcon.equalsIgnoreCase(oldIcon) ){
			File fileOldIcon = new File(Utils.getFolderImg()+oldIcon);
			if(fileOldIcon.exists() && fileOldIcon.isFile())
				FileUtils.deleteQuietly(fileOldIcon);
			Utils.move(Constant.PREFIX_FILE_TEMP+newIcon, Utils.getFoldelTemp(), Utils.getFolderImg());
		}
		topicRepository.save(topic);

	}

}
