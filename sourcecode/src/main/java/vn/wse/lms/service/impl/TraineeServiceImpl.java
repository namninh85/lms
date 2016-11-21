package vn.wse.lms.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.wse.lms.bean.AnsResult;
import vn.wse.lms.bean.MaterialBean;
import vn.wse.lms.bean.TraineeBean;
import vn.wse.lms.entity.AnswerTemplate;
import vn.wse.lms.entity.Course;
import vn.wse.lms.entity.Material;
import vn.wse.lms.entity.QuestionTemplate;
import vn.wse.lms.entity.QuizTemplate;
import vn.wse.lms.entity.Section;
import vn.wse.lms.entity.Topic;
import vn.wse.lms.entity.TraineeMaterial;
import vn.wse.lms.entity.TraineeQuiz;
import vn.wse.lms.entity.TraineeQuizDetail;
import vn.wse.lms.entity.TraineeStatus;
import vn.wse.lms.entity.TraineeTopic;
import vn.wse.lms.entity.Users;
import vn.wse.lms.repository.CourseRegistrationRepository;
import vn.wse.lms.repository.CourseRepository;
import vn.wse.lms.repository.MaterialRepository;
import vn.wse.lms.repository.QuestionTemplateRepository;
import vn.wse.lms.repository.QuizTemplateRepository;
import vn.wse.lms.repository.SearchRepository;
import vn.wse.lms.repository.TopicRepository;
import vn.wse.lms.repository.TraineeMaterialRepository;
import vn.wse.lms.repository.TraineeQuizDetailRepository;
import vn.wse.lms.repository.TraineeQuizRepository;
import vn.wse.lms.repository.TraineeRepository;
import vn.wse.lms.repository.TraineeStatusRepository;
import vn.wse.lms.repository.TraineeTopicRepository;
import vn.wse.lms.service.TraineeService;
import vn.wse.lms.util.Constant;
import vn.wse.lms.util.NumberUtils;
import vn.wse.lms.util.ScoreUtil;

@Service("traineeService")
@Transactional(readOnly=true)
public class TraineeServiceImpl implements TraineeService {

	@Autowired
	private TraineeRepository traineeRepository;
	
	@Autowired
	private CourseRegistrationRepository courseRegistrationRepository;
	
	@Autowired
	private TraineeStatusRepository traineeStatusRepository;
	
	@Autowired
	private TraineeMaterialRepository traineeMaterialRepository;
	
	@Autowired
	private MaterialRepository materialRepository;
	
	@Autowired
	private QuestionTemplateRepository questionTemplateRepository;
	
	@Autowired
	private QuizTemplateRepository quizTemplateRepository;

	@Autowired
	private TraineeQuizRepository traineeQuizRepository;
	
	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private TraineeQuizDetailRepository traineeQuizDetailRepository;
	
	@Autowired
	private SearchRepository searchRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private TraineeTopicRepository traineeTopicRepository;
	@Override
	public List<Users> getAll(){
		return traineeRepository.findAll();
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public long generateQuiz(Course course, long materialId, long traineeId) {
		// TODO Auto-generated method stub
		//select quiz template
		Material material = materialRepository.findOne(materialId);
		List<TraineeMaterial> traineeMaterialList = traineeMaterialRepository.findCurrentStatus(traineeId, course.getCourseId(), material.getMaterialId());
		QuizTemplate quizTemplate = material.getQuizTemplate();
		long result = 0;
		if (quizTemplate != null) {
			//get latest
			List<TraineeQuiz> quizList = traineeQuizRepository.getAllCurrentQuiz(course.getCourseId(), traineeId, quizTemplate.getQuizTemplateId());
			long noRetried = 0;
			if (quizList.size() > 0) {
				if (quizList.get(0).getNoRetried() != null) {
					noRetried = 1 + quizList.get(0).getNoRetried().longValue();
				}
			}
			// update old quiz
			for (TraineeQuiz tq : quizList) {
				tq.setStatus(1l);
				traineeQuizRepository.save(tq);
			}
			
			//create new
			TraineeQuiz traineeQuiz = new TraineeQuiz();
			traineeQuiz.setCourseId(course.getCourseId());
			traineeQuiz.setQuizTemplateId(quizTemplate.getQuizTemplateId());
			traineeQuiz.setUserId(traineeId);
			traineeQuiz.setStatus(0l);
			traineeQuiz.setNoRetried(noRetried);
			
			
			long numAttemp = 0;
			if (quizTemplate.getNumAttempt() != null && quizTemplate.getNumAttempt() > 0) {
				numAttemp = quizTemplate.getNumAttempt();
			}
			long retryLeft = numAttemp;
			if (traineeMaterialList.size() > 0) {
				TraineeMaterial tm = traineeMaterialList.get(0);
				if (tm.getNoViewed() != null) {
					retryLeft = numAttemp - tm.getNoViewed();
				}
			}
			
			traineeQuiz.setRetryLeft(retryLeft);
			
			traineeQuiz.setStartDate(Calendar.getInstance().getTime());
			traineeQuiz = traineeQuizRepository.save(traineeQuiz);
			result = traineeQuiz.getTraineeQuizId();
			// detail
			List<QuestionTemplate> questions = quizTemplate.getQuestionTemplates();
			for (int i = 0; i < questions.size(); i++) {
				QuestionTemplate qt = questions.get(i);
				TraineeQuizDetail traineeQuizDetail = new TraineeQuizDetail();
				traineeQuizDetail.setQuestionTemplateId(qt.getQuestionTemplateId());
				traineeQuizDetail.setTraineeQuizId(traineeQuiz.getTraineeQuizId());
				traineeQuizDetail.setQuestion(qt.getQuestion());
				traineeQuizDetail.setQuestionType(qt.getQuestionType());
				traineeQuizDetail.setSectionIndex(qt.getSectionIndex());
				traineeQuizDetail.setSectionTitle(qt.getSectionTitle());
				traineeQuizDetail.setVideoLink(qt.getVideoLink());
				traineeQuizDetail.setRequired(qt.getRequired());
				traineeQuiz.setStartDate(Calendar.getInstance().getTime());
				traineeQuizDetailRepository.save(traineeQuizDetail);
			}
			
			
			
		}
		return result;
	}


	@Override
	public List<TraineeQuizDetail> getPendingQuestion(long traineeQuizId) {
		return traineeQuizRepository.getPendingQuestion(traineeQuizId);
	}
	
	@Override
	public long getTotalQuestion(long traineeQuizId) {
		return traineeQuizRepository.TotalQuestion(traineeQuizId);
	}
	
	@Override
	public long getTotalCorrect(long traineeQuizId) {
		return traineeQuizRepository.TotalCorrect(traineeQuizId);
	}
	
	@Override
	public long getTotalWrong(long traineeQuizId) {
		return traineeQuizRepository.TotalWrong(traineeQuizId);
	}
	
	@Override
	public long getTotalScore(long traineeQuizId) {
		Long sc = traineeQuizRepository.TotalScore(traineeQuizId);
		if (sc == null) {
			return 0l;
		}
		return sc;
	}
	
	
	@Override
	public List<TraineeQuizDetail> getLeftQuestion(long traineeQuizId) {
		return traineeQuizRepository.getLeftQuestion(traineeQuizId);
	}
	
	@Override
	public List<AnswerTemplate> getAnswerTemplates(long questionTemplateId) {
		return traineeQuizRepository.getAnsTemplate(questionTemplateId);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public AnsResult ansQuestion(long traineeQuizDetailId, String ans) {
		AnsResult ansResult = new AnsResult();
		try {
			TraineeQuizDetail traineeQuizDetail = traineeQuizDetailRepository.findOne(traineeQuizDetailId);
			QuestionTemplate questionTemplate = questionTemplateRepository.findOne(traineeQuizDetail.getQuestionTemplateId());
			//calculate score
			if (Constant.QUESTION_TEXT.equals(traineeQuizDetail.getQuestionType())) {
				traineeQuizDetail.setScore(0l);
				traineeQuizDetail.setResult(-1l);
			} else if (Constant.QUESTION_ORDERING.equals(traineeQuizDetail.getQuestionType())) {
				traineeQuizDetail.setScore(ScoreUtil.scoreList(questionTemplate, ans));
				if (traineeQuizDetail.getScore().intValue() > 0) {
					traineeQuizDetail.setResult(1l);
				} else {
					traineeQuizDetail.setResult(0l);
				}
			} 
			
			else {
				traineeQuizDetail.setScore(ScoreUtil.score(questionTemplate, ans));
				if (traineeQuizDetail.getScore().intValue() > 0) {
					traineeQuizDetail.setResult(1l);
				} else {
					traineeQuizDetail.setResult(0l);
				}
				
			}
			
			ansResult.setResult(String.valueOf(traineeQuizDetail.getResult()));
			ansResult.setRightAns(questionTemplate.getRightAnswer());
			ansResult.setTrainerFeedback(questionTemplate.getTrainerFeedback());
			
			traineeQuizDetail.setTraineeAnwser(ans);
			traineeQuizDetailRepository.saveAndFlush(traineeQuizDetail);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ansResult;
		
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public AnsResult skipQuestion(long traineeQuizDetailId) {
		AnsResult ansResult = new AnsResult();
		try {
			TraineeQuizDetail traineeQuizDetail = traineeQuizDetailRepository.findOne(traineeQuizDetailId);
			traineeQuizDetail.setResult(-2l);
			traineeQuizDetail.setScore(0l);
			traineeQuizDetailRepository.saveAndFlush(traineeQuizDetail);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ansResult;
		
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void updateStatus(long traineeId, long courseId, Topic topic) {
		try {
			List<TraineeStatus> traineeStatusList = traineeStatusRepository.findCurrentStatus(traineeId, courseId);
			TraineeStatus status = new TraineeStatus();
			if (traineeStatusList.size() > 0) {
				status = traineeStatusList.get(0);
				status.setCurrentSectionId(topic.getSectionId());
				status.setCurrentTopicId(topic.getTopicId());
				
			} else {
				
				status.setCurrentSectionId(topic.getSectionId());
				status.setCurrentTopicId(topic.getTopicId());
				status.setCourseId(courseId);
				status.setUserId(traineeId);
			}
			traineeStatusRepository.save(status);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void updateStatus(TraineeStatus status) {
		try {
			
			traineeStatusRepository.save(status);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public TraineeStatus getCurrentStatus(long traineeId, long courseId) {
		try {
			List<TraineeStatus> traineeStatusList = traineeStatusRepository.findCurrentStatus(traineeId, courseId);
			TraineeStatus status = new TraineeStatus();
			if (traineeStatusList.size() > 0) {
				status = traineeStatusList.get(0);
				
			} 
			return status;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void updateMaterialStatus(long traineeId, long courseId, Material material, long increaseView, long totalScore, long traineeQuizId) {
		try {
			List<TraineeMaterial> traineeMaterialList = traineeMaterialRepository.findCurrentStatus(traineeId, courseId, material.getMaterialId());
			
			TraineeMaterial status = new TraineeMaterial();
			if (traineeMaterialList.size() > 0) {
				status = traineeMaterialList.get(0);
				status.setStatus(100l);
				status.setTopicId(material.getTopic().getTopicId());
				long noViewed = increaseView + NumberUtils.defaultLongValue(status.getNoViewed());
				status.setNoViewed(noViewed);
				status.setScore(totalScore);
				status.setSubmissionDate(Calendar.getInstance().getTime());
				if (traineeQuizId > 0) {
					status.setTraineeQuizId(traineeQuizId);					
				}
			} else {
				
				status.setStatus(100l);
				long noViewed = increaseView ;//+ NumberUtils.defaultLongValue(status.getNoViewed());
				status.setNoViewed(noViewed);
				status.setCourseId(courseId);
				status.setTopicId(material.getTopic().getTopicId());
				status.setUserId(traineeId);
				status.setMaterialId(material.getMaterialId());
				status.setScore(totalScore);
				status.setSubmissionDate(Calendar.getInstance().getTime());
				if (traineeQuizId > 0) {
					status.setTraineeQuizId(traineeQuizId);
				}
			}
			traineeMaterialRepository.save(status);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean isRegistered(long courseId, long traineeId) {
		// TODO Auto-generated method stub
		int count = courseRegistrationRepository.countByUserIdAndCourseId(traineeId, courseId);
		if (count > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public int getTopicProgress(long traineeId, long topicId) {
		long total = topicRepository.totalMaterial( topicId);
		long finished = topicRepository.totalFinished(traineeId, topicId);
		if (total != 0) {
			Long result = (finished*100)/total;
			return result.intValue();
			
		}
		return 0;
	}

	@Override
	public TraineeBean searchTrainees(TraineeBean traineeBean) {
		return searchRepository.searchTrainees(traineeBean);
	}

	@Override
	public TraineeMaterial getTrainingMaterial(long traineeId, long courseId, long materialId) {
		List<TraineeMaterial> traineeMaterialList = traineeMaterialRepository.findCurrentStatus(traineeId, courseId, materialId);
		TraineeMaterial status = new TraineeMaterial();
		if (traineeMaterialList.size() > 0) {
			status = traineeMaterialList.get(0);
		}
		return status;
	}
	
	@Override
	public List<TraineeTopic> getTraineeTopicsByCourse(long courseId, long traineeId) {
		return traineeTopicRepository.findAllByCourse(traineeId, courseId);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void saveTraineeTopic(TraineeTopic traineeTopic) {
		traineeTopicRepository.save(traineeTopic);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void initCourseForTrainee(long traineeId, Course course) {
		if (course != null) {
			List<TraineeStatus> traineeStatusList = traineeStatusRepository.findCurrentStatus(traineeId, course.getCourseId());
			if (traineeStatusList.size() > 0) {
				return;
			}
			TraineeStatus ts = new TraineeStatus();
			ts.setUserId(traineeId);
			ts.setCourseId(course.getCourseId());
			ts.setStartDate(Calendar.getInstance().getTime());
			ts.setStatus(Constant.STATUS_INPROGRESS);
			traineeStatusRepository.save(ts);
			
			List<Section> sections = course.getSections();
			if (sections != null) {
				for (Section section : sections) {
					List<Topic> topics = section.getTopics();
					if (topics != null) {
						for (Topic topic : topics) {
							TraineeTopic traineeTopic = new TraineeTopic();
							traineeTopic.setUserId(traineeId);
							traineeTopic.setProgress(0l);
							traineeTopic.setCourseId(course.getCourseId());
							traineeTopic.setSectionId(section.getSectionId());
							traineeTopic.setTopic(topic);
							traineeTopic.setLock(topic.getLocked());
							traineeTopicRepository.save(traineeTopic);
						}
					}
					
				}
			}
			
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public int updateTrainingProgress(long traineeId, long courseId, long topicId) {
		List<TraineeTopic> list = traineeTopicRepository.findAllByTopic(traineeId, courseId, topicId);
		TraineeTopic current = new TraineeTopic();
		if (list.size() > 0) {
			current = list.get(0);
		} else {
			return -1;
		}
		if (current.getProgress() != null && current.getProgress() > 100l) {
			//do nothing
		}else {
			long total = topicRepository.totalMaterial( topicId);
			long finished = topicRepository.totalFinished(traineeId, topicId);
			long progress = 0;
			if (total==0) {
				progress = 100l;
			} else {
				progress = (finished*100)/total;
			}
			current.setProgress(progress);
			if (progress >= 100l) {
				//unlock next topic
				Course course = courseRepository.findOne(courseId);
				TraineeTopic next = nextTopic(course, topicId, traineeId);
				if (next != null) {
					next.setLock(false);
					traineeTopicRepository.save(next);
				}
			}
			
		}
		traineeTopicRepository.save(current);
		
		return 0;
	}
	
	
	@Override
	public TraineeTopic nextTopic(Course course, long topicId, long traineeId) {
		Topic nextTopic = null;
		Topic tmp = null;
		Map<Long, Topic> map = new HashMap<Long, Topic>();
		List<Section> sections = course.getSections();
		if (sections != null) {
			for (Section section : sections) {
				
				List<Topic> topics = section.getTopics();
				if (topics != null) {
					for (Topic topic : topics) {
							if (tmp != null) {
								map.put(tmp.getTopicId(), topic);
							}
							tmp = topic;
					}
				}
			}
		}
		nextTopic = map.get(topicId);
		if (nextTopic == null) {
			return null;
		}
		List<TraineeTopic> list = traineeTopicRepository.findAllByTopic(traineeId, course.getCourseId(), nextTopic.getTopicId());
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public List<MaterialBean> getMaterials(Topic topic, long courseId, long traineeId) {
		List<MaterialBean> result = new ArrayList<MaterialBean>();
		if (topic != null && topic.getMaterials() != null) {
			List<TraineeMaterial> tms = traineeMaterialRepository.findByTopic(traineeId, courseId, topic.getTopicId());
			Map<Long, TraineeMaterial> map = new HashMap<Long, TraineeMaterial>();
			for (TraineeMaterial tm : tms) {
				map.put(tm.getMaterialId(), tm);
			}
			for (Material material : topicRepository.getMaterialByTopic(topic.getTopicId())) {
				MaterialBean bean = new MaterialBean();
				bean.setMaterial(material);
				if (map.get(material.getMaterialId()) != null) {
					bean.setTraineeMaterial(map.get(material.getMaterialId()));
				}
				result.add(bean);
				
			}
		}
		return result;
	}

	@Override
	public QuizTemplate getQuizTemplate(long quizTemplateId) {
		return  quizTemplateRepository.findOne(quizTemplateId);
		
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public TraineeQuiz calculateScore(long traineeQuizId, long traineeId) {
		TraineeQuiz traineeQuiz = traineeQuizRepository.findOne(traineeQuizId);
		QuizTemplate quizTemplate = quizTemplateRepository.findOne(traineeQuiz.getQuizTemplateId());
		
		if (traineeQuiz != null && traineeQuiz.getRetryLeft() != null && traineeQuiz.getRetryLeft() < 0) {
			return null;
		}
		//if (traineeQuiz)
		long totalScore = getTotalScore(traineeQuizId);
		
		traineeQuiz.setScore(totalScore);
		traineeQuiz.setResult(Constant.QUIZ_RESULT_FAILED);
		
		double percentScore = 0;
		if (quizTemplate.getTotalPoint() > 0) {
			percentScore= totalScore/quizTemplate.getTotalPoint();
		}
		traineeQuiz.setPercentScore(((Double) (percentScore * 100)).longValue());
		
		if (percentScore > quizTemplate.getPassingGrade() ) {
			traineeQuiz.setResult(Constant.QUIZ_RESULT_PASS);
		}
		
		long retryLeft = traineeQuiz.getRetryLeft() == null? 0: traineeQuiz.getRetryLeft();
		if (retryLeft > 0) {
			traineeQuiz.setRetryLeft(retryLeft-1);
		};
		
		traineeQuizRepository.save(traineeQuiz);
		return traineeQuiz;
	}

	@Override
	public TraineeQuiz getTraineeQuiz(long traineeQuizId) {
		return traineeQuizRepository.findOne(traineeQuizId);
	}
	
	
	
}
