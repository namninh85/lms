package vn.wse.lms.bean;

import java.io.Serializable;
import java.util.List;

import vn.wse.lms.entity.QuestionTemplate;
import vn.wse.lms.entity.QuizSection;
import vn.wse.lms.entity.QuizTemplate;

public class QuizTemplateBean implements Serializable {
	private QuizTemplate quizTemplate;
	private List<String> rankList;
	private List<QuizSection> quizSections;
	private List<QuestionTemplate> questionTemplates;
	public QuizTemplate getQuizTemplate() {
		return quizTemplate;
	}
	public void setQuizTemplate(QuizTemplate quizTemplate) {
		this.quizTemplate = quizTemplate;
	}
	public List<QuizSection> getQuizSections() {
		return quizSections;
	}
	public void setQuizSections(List<QuizSection> quizSections) {
		this.quizSections = quizSections;
	}
	public List<QuestionTemplate> getQuestionTemplates() {
		return questionTemplates;
	}
	public void setQuestionTemplates(List<QuestionTemplate> questionTemplates) {
		this.questionTemplates = questionTemplates;
	}
	public List<String> getRankList() {
		return rankList;
	}
	public void setRankList(List<String> rankList) {
		this.rankList = rankList;
	}
	
	
}
