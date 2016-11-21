package vn.wse.lms.bean;

import java.util.List;

import vn.wse.lms.entity.TraineeQuizDetail;

public class QuizBean {
	private long total;
	private long correct;
	private long wrong;
	private long left;
	private int timeLeft;
	private List<TraineeQuizDetail> pendingList;
	private QuestionAnswerBean current;
	private AnsResult ansResult;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getCorrect() {
		return correct;
	}
	public void setCorrect(long correct) {
		this.correct = correct;
	}
	public long getWrong() {
		return wrong;
	}
	public void setWrong(long wrong) {
		this.wrong = wrong;
	}
	public long getLeft() {
		return left;
	}
	public void setLeft(long left) {
		this.left = left;
	}
	public List<TraineeQuizDetail> getPendingList() {
		return pendingList;
	}
	public void setPendingList(List<TraineeQuizDetail> pendingList) {
		this.pendingList = pendingList;
	}
	public QuestionAnswerBean getCurrent() {
		return current;
	}
	public void setCurrent(QuestionAnswerBean current) {
		this.current = current;
	}
	public int getTimeLeft() {
		return timeLeft;
	}
	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}
	public AnsResult getAnsResult() {
		return ansResult;
	}
	public void setAnsResult(AnsResult ansResult) {
		this.ansResult = ansResult;
	}
	
	
}
