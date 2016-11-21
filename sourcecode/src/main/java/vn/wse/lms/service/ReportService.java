package vn.wse.lms.service;

import java.util.List;

import vn.wse.lms.bean.CourseQuizBean;
import vn.wse.lms.bean.QuizReportBean;
import vn.wse.lms.bean.QuizTemplateBean;
import vn.wse.lms.entity.Comment;
import vn.wse.lms.entity.Material;
import vn.wse.lms.entity.QuizTemplate;

public interface ReportService {
    List<Comment> getComments(long courseId, long userId);
    
    void addComment(Comment comment) ;

	List<CourseQuizBean> getAllQuiz(long courseId);

	QuizReportBean getQuizSummary(long courseId, long materialId);

	QuizTemplateBean getQuizReport(QuizTemplateBean bean, long courseId, Material material, QuizTemplate quizTemplate);
    
}
