package vn.wse.lms.repository;

import java.util.List;

import vn.wse.lms.bean.CourseQuizBean;
import vn.wse.lms.bean.QuizReportBean;
import vn.wse.lms.bean.TraineeScoreBean;
import vn.wse.lms.entity.Material;
import vn.wse.lms.entity.QuizTemplate;

public interface MaterialRepositoryCustom {
	public void deleteMaterialNotInListByTopicId(List<Material> materials,Long topicId);

	public void deleteAllMaterialByTopicId(Long topicId);

	List<CourseQuizBean> getAllQuiz(long courseId);

	QuizReportBean getQuizSummary(long courseId, long materialId, QuizTemplate qt);

	List<TraineeScoreBean> getTraineeScores(long courseId, long materialId);

}
