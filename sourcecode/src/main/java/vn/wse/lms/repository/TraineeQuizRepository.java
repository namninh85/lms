package vn.wse.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.wse.lms.entity.AnswerTemplate;
import vn.wse.lms.entity.TraineeQuiz;
import vn.wse.lms.entity.TraineeQuizDetail;

public interface TraineeQuizRepository extends JpaRepository<TraineeQuiz, Long> {

	@Query("SELECT tq FROM TraineeQuiz tq where tq.userId = :userId and tq.courseId=:courseId and tq.quizTemplateId = :quizTemplateId and tq.status=0 order by tq.traineeQuizId ")
	public List<TraineeQuiz> getAllCurrentQuiz(@Param("courseId")Long courseId, @Param("userId")Long userId, @Param("quizTemplateId")Long quizTemplateId);
	
	@Query("SELECT tq FROM TraineeQuizDetail tq where tq.traineeQuizId = :traineeQuizId ")
	public List<TraineeQuizDetail> getAllQuizDetail(@Param("traineeQuizId")Long traineeQuizId);
	
	@Query("SELECT tq FROM TraineeQuizDetail tq where tq.traineeQuizId = :traineeQuizId and tq.result is null ")
	public List<TraineeQuizDetail> getPendingQuestion(@Param("traineeQuizId")Long traineeQuizId);
	
	@Query("SELECT count(*) FROM TraineeQuizDetail tq where tq.traineeQuizId = :traineeQuizId ")
	public Long TotalQuestion(@Param("traineeQuizId")Long traineeQuizId);
	
	@Query("SELECT count(*) FROM TraineeQuizDetail tq where tq.traineeQuizId = :traineeQuizId and tq.result=1 ")
	public Long TotalCorrect(@Param("traineeQuizId")Long traineeQuizId);
	
	@Query("SELECT sum(tq.score) FROM TraineeQuizDetail tq where tq.traineeQuizId = :traineeQuizId ")
	public Long TotalScore(@Param("traineeQuizId")Long traineeQuizId);
	
	
	@Query("SELECT count(*) FROM TraineeQuizDetail tq where tq.traineeQuizId = :traineeQuizId and tq.result = 0 ")
	public Long TotalWrong(@Param("traineeQuizId")Long traineeQuizId);
	
	@Query("SELECT tq FROM TraineeQuizDetail tq where tq.traineeQuizId = :traineeQuizId and tq.result is null  ")
	public List<TraineeQuizDetail> getLeftQuestion(@Param("traineeQuizId")Long traineeQuizId);
	
	@Query("SELECT an FROM AnswerTemplate an where an.questionTemplate.questionTemplateId = :questionTemplateId ")
	public List<AnswerTemplate> getAnsTemplate(@Param("questionTemplateId")Long questionTemplateId);
}
