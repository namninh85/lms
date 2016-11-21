package vn.wse.lms.service;

import java.util.List;

import vn.wse.lms.bean.AnsResult;
import vn.wse.lms.bean.MaterialBean;
import vn.wse.lms.bean.TraineeBean;
import vn.wse.lms.entity.AnswerTemplate;
import vn.wse.lms.entity.Course;
import vn.wse.lms.entity.Material;
import vn.wse.lms.entity.QuizTemplate;
import vn.wse.lms.entity.Topic;
import vn.wse.lms.entity.TraineeMaterial;
import vn.wse.lms.entity.TraineeQuiz;
import vn.wse.lms.entity.TraineeQuizDetail;
import vn.wse.lms.entity.TraineeStatus;
import vn.wse.lms.entity.TraineeTopic;
import vn.wse.lms.entity.Users;

public interface TraineeService {
    List<Users> getAll();
    

    public long generateQuiz(Course course, long materialId, long traineeId) ;


	List<TraineeQuizDetail> getPendingQuestion(long traineeQuizId);


	List<TraineeQuizDetail> getLeftQuestion(long traineeQuizId);


	List<AnswerTemplate> getAnswerTemplates(long questionTemplateId);


	AnsResult ansQuestion(long traineeQuizDetailId, String ans);


	long getTotalQuestion(long traineeQuizId);
	
	TraineeQuiz getTraineeQuiz (long traineeQuizId);
	


	long getTotalCorrect(long traineeQuizId);

	
	boolean isRegistered(long courseId, long traineeId);
	

	long getTotalWrong(long traineeQuizId);


	void updateStatus(long traineeId, long courseId, Topic topic);


	void updateMaterialStatus(long traineeId, long courseId, Material material, long increaseView, long totalScore, long traineeQuizId);
	
	int getTopicProgress(long traineeId, long topicId) ;
	
	int updateTrainingProgress(long traineeId, long courseId, long topicId) ;
	
	TraineeBean searchTrainees(TraineeBean traineeBean);


	TraineeStatus getCurrentStatus(long traineeId, long courseId);
	public TraineeMaterial getTrainingMaterial(long traineeId, long courseId, long materialId);


	long getTotalScore(long traineeQuizId);


	void updateStatus(TraineeStatus status);


	void initCourseForTrainee(long traineeId, Course course);


	List<TraineeTopic> getTraineeTopicsByCourse(long courseId, long traineeId);


	void saveTraineeTopic(TraineeTopic traineeTopic);


	public TraineeTopic nextTopic(Course course, long topicId, long traineeId);


	List<MaterialBean> getMaterials(Topic topic, long courseId, long traineeId);
	
	TraineeQuiz calculateScore(long traineeQuizId, long traineeId);


	AnsResult skipQuestion(long traineeQuizDetailId);


	QuizTemplate getQuizTemplate(long quizTemplateId);
}
