package vn.wse.lms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.wse.lms.bean.CourseQuizBean;
import vn.wse.lms.bean.QuizReportBean;
import vn.wse.lms.bean.QuizTemplateBean;
import vn.wse.lms.bean.TraineeScoreBean;
import vn.wse.lms.entity.Comment;
import vn.wse.lms.entity.Course;
import vn.wse.lms.entity.Material;
import vn.wse.lms.entity.QuizTemplate;
import vn.wse.lms.entity.TraineeQuiz;
import vn.wse.lms.entity.TraineeQuizDetail;
import vn.wse.lms.repository.CommentRepository;
import vn.wse.lms.repository.CourseRepository;
import vn.wse.lms.repository.MaterialRepository;
import vn.wse.lms.repository.TraineeQuizRepository;
import vn.wse.lms.service.ReportService;
import vn.wse.lms.util.Constant;

@Service("reportService")
@Transactional(readOnly=true)
public class ReportServiceImpl implements ReportService {

	private Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	MaterialRepository materialRepository;
	
	@Autowired
	private TraineeQuizRepository traineeQuizRepository;
	
	@Autowired CourseRepository courseRepository;
	@Override
	public List<Comment> getComments(long courseId, long userId) {
		return commentRepository.getComments(userId, courseId);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void addComment(Comment comment) {
		commentRepository.save(comment);
		
	}
	
	@Override
	public List<CourseQuizBean> getAllQuiz(long courseId) {
		return materialRepository.getAllQuiz(courseId);
	}
	
	@Override
	public QuizTemplateBean getQuizReport(QuizTemplateBean bean, long courseId, Material material, QuizTemplate quizTemplate) {
		List<TraineeScoreBean> traineeScores = new ArrayList<TraineeScoreBean>();
		traineeScores = materialRepository.getTraineeScores(courseId, material.getMaterialId());
		
		int[] rightAns = new int[quizTemplate.getQuestionTemplates().size()];
		for (TraineeScoreBean score : traineeScores) {
			if (score.getTraineeQuizId() != null) {
				TraineeQuiz tq = traineeQuizRepository.findOne(score.getTraineeQuizId());
				if (tq != null) {
					score.setPercent(tq.getPercentScore());
					List<TraineeQuizDetail> detailList =traineeQuizRepository.getAllQuizDetail(tq.getTraineeQuizId()); 
					score.setQuizDetail(detailList);
					
					//count number of right ans
					for (int i = 0; i < quizTemplate.getQuestionTemplates().size(); i++) {
						if (detailList != null && detailList.size() > i  ) {
							if (detailList.get(i).getResult() != null && detailList.get(i).getResult() > 0) {
								rightAns[i] = rightAns[i] + 1;
							}
						}
					}
				}
			}
		}
		
		List<String> rankList = new ArrayList<String>();
		for (int i = 0; i < quizTemplate.getQuestionTemplates().size(); i++) {
			double d = 0;
			if (traineeScores.size() ==0) {
				d = 0;
			} else {
				d = 100*rightAns[i]/traineeScores.size();
			}
			if (traineeScores.size() == 0) {
				rankList.add("1");
			} else {
				if (d >= Constant.QUESTION_LOW_RANK) {
					rankList.add("1");
				}else {					
					if (quizTemplate.getQuestionTemplates().get(i).getRequired().equals("1")) {
						rankList.add("0");
					} else {
						rankList.add("1");
					}
				}
			}
		}
		
		bean.setRankList(rankList);
		
		
		return bean;
	}
	
	@Override
	public QuizReportBean getQuizSummary(long courseId, long materialId) {
		Material material = materialRepository.findOne(materialId);
		QuizTemplate quizTemplate = material.getQuizTemplate();
		
		QuizReportBean bean = materialRepository.getQuizSummary(courseId, materialId, quizTemplate);
		bean.setQuizTitle(material.getMaterialName());
		Course course = courseRepository.findOne(courseId);
		int totalTrainee = course.getTrainees().size();
		bean.setTotalTrainees(totalTrainee);
		if (totalTrainee > 0) {
			int p = 100*bean.getCountedSubmission()/totalTrainee;
			bean.setCountedSubmissionPercent(p);;
			
		}
		bean.setPossiblePoint(quizTemplate.getTotalPoint());
		Double avgPercent = 100*bean.getAveragePoint()/quizTemplate.getTotalPoint();
		bean.setPercentPoint(avgPercent.intValue());
		
		bean.setQuestionTemplate(quizTemplate.getQuestionTemplates());
		List<TraineeScoreBean> traineeScores = new ArrayList<TraineeScoreBean>();
		traineeScores = materialRepository.getTraineeScores(courseId, materialId);
		int[] rightAns = new int[quizTemplate.getQuestionTemplates().size()];
		for (TraineeScoreBean score : traineeScores) {
			if (score.getTraineeQuizId() != null) {
				TraineeQuiz tq = traineeQuizRepository.findOne(score.getTraineeQuizId());
				if (tq != null) {
					score.setPercent(tq.getPercentScore());
					List<TraineeQuizDetail> detailList =traineeQuizRepository.getAllQuizDetail(tq.getTraineeQuizId()); 
					score.setQuizDetail(detailList);
					
					//count number of right ans
					for (int i = 0; i < quizTemplate.getQuestionTemplates().size(); i++) {
						if (detailList != null && detailList.size() > i  ) {
							if (detailList.get(i).getResult() != null && detailList.get(i).getResult() > 0) {
								rightAns[i] = rightAns[i] + 1;
							}
						}
					}
				}
			}
		}
		bean.setTraineeScores(traineeScores);
		List<Double> percentRightAns = new ArrayList<Double>();
		List<String> rankList = new ArrayList<String>();
		int low = 0;
		if (traineeScores.size() > 0) {
			for (int i = 0; i < quizTemplate.getQuestionTemplates().size(); i++) {
				double d = 100*rightAns[i]/traineeScores.size();
				percentRightAns.add(d);
				if (d >= Constant.QUESTION_LOW_RANK) {
					rankList.add("1");
				}else {					
					rankList.add("0");
					if (quizTemplate.getQuestionTemplates().get(i).getRequired().equals("1")) {
						low = low + 1;
					}
				}
			}
		}
		bean.setTotalLowQuestion(low);
		if (quizTemplate.getQuestionTemplates().size() > 0) {
			bean.setLowQuestionPercent(100*low/quizTemplate.getQuestionTemplates().size());
		}
		bean.setSummaryList(percentRightAns);
		bean.setRankList(rankList);
		return bean;
	}
	
}
