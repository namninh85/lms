package vn.wse.lms.bean;

import java.util.List;

import vn.wse.lms.entity.AnswerTemplate;
import vn.wse.lms.entity.TraineeQuizDetail;

public class QuestionAnswerBean {
	private TraineeQuizDetail question;
	private String type;
	private List<AnswerTemplate> ansList;
	public TraineeQuizDetail getQuestion() {
		return question;
	}
	public void setQuestion(TraineeQuizDetail question) {
		this.question = question;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<AnswerTemplate> getAnsList() {
		return ansList;
	}
	public void setAnsList(List<AnswerTemplate> ansList) {
		this.ansList = ansList;
	}
	
	
}
