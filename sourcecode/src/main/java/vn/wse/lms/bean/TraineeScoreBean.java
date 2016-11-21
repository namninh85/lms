package vn.wse.lms.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import vn.wse.lms.entity.TraineeQuizDetail;

public class TraineeScoreBean implements Serializable {
	
	private Date submissionTime;
	private String traineeName;
	private Long totalPoint;
	private Long percent;
	private Long timeSubmitted;
	private Long traineeQuizId;
	private List<TraineeQuizDetail> quizDetail;
	public Date getSubmissionTime() {
		return submissionTime;
	}
	public void setSubmissionTime(Date submissionTime) {
		this.submissionTime = submissionTime;
	}
	public String getTraineeName() {
		return traineeName;
	}
	public void setTraineeName(String traineeName) {
		this.traineeName = traineeName;
	}
	public Long getTotalPoint() {
		return totalPoint;
	}
	public void setTotalPoint(Long totalPoint) {
		this.totalPoint = totalPoint;
	}
	public Long getPercent() {
		return percent;
	}
	public void setPercent(Long percent) {
		this.percent = percent;
	}
	public Long getTimeSubmitted() {
		return timeSubmitted;
	}
	public void setTimeSubmitted(Long timeSubmitted) {
		this.timeSubmitted = timeSubmitted;
	}
	public List<TraineeQuizDetail> getQuizDetail() {
		return quizDetail;
	}
	public void setQuizDetail(List<TraineeQuizDetail> quizDetail) {
		this.quizDetail = quizDetail;
	}
	public Long getTraineeQuizId() {
		return traineeQuizId;
	}
	public void setTraineeQuizId(Long traineeQuizId) {
		this.traineeQuizId = traineeQuizId;
	}
	
	
	
}
