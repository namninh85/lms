package vn.wse.lms.bean;

import java.io.Serializable;
import java.util.List;

import vn.wse.lms.entity.Course;
import vn.wse.lms.entity.QuestionTemplate;
import vn.wse.lms.entity.TraineeStatus;
import vn.wse.lms.entity.Users;

public class QuizReportBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5514631944775184836L;
	
	private String quizTitle;
	private double possiblePoint;
	private double averagePoint;
	private double percentPoint;
	private int countedSubmission;
	private int totalTrainees;
	private double countedSubmissionPercent;
	private int numOfLowScoreQuestion;
	private double numOfLowScoreQuestionPercent;
	
	private List<QuestionTemplate> questionTemplate;
	private List<Double> summaryList;
	private List<String> rankList;
	private List<TraineeScoreBean> traineeScores;

	private int totalLowQuestion;
	private int lowQuestionPercent;
	
	public double getPossiblePoint() {
		return possiblePoint;
	}

	public void setPossiblePoint(double possiblePoint) {
		this.possiblePoint = possiblePoint;
	}

	public double getAveragePoint() {
		return averagePoint;
	}

	public void setAveragePoint(double averagePoint) {
		this.averagePoint = averagePoint;
	}

	public int getCountedSubmission() {
		return countedSubmission;
	}

	public void setCountedSubmission(int countedSubmission) {
		this.countedSubmission = countedSubmission;
	}

	public int getNumOfLowScoreQuestion() {
		return numOfLowScoreQuestion;
	}

	public void setNumOfLowScoreQuestion(int numOfLowScoreQuestion) {
		this.numOfLowScoreQuestion = numOfLowScoreQuestion;
	}

	public List<TraineeScoreBean> getTraineeScores() {
		return traineeScores;
	}

	public void setTraineeScores(List<TraineeScoreBean> traineeScores) {
		this.traineeScores = traineeScores;
	}

	public List<QuestionTemplate> getQuestionTemplate() {
		return questionTemplate;
	}

	public void setQuestionTemplate(List<QuestionTemplate> questionTemplate) {
		this.questionTemplate = questionTemplate;
	}

	public List<Double> getSummaryList() {
		return summaryList;
	}

	public void setSummaryList(List<Double> summaryList) {
		this.summaryList = summaryList;
	}

	public double getPercentPoint() {
		return percentPoint;
	}

	public void setPercentPoint(double percentPoint) {
		this.percentPoint = percentPoint;
	}

	public double getNumOfLowScoreQuestionPercent() {
		return numOfLowScoreQuestionPercent;
	}

	public void setNumOfLowScoreQuestionPercent(double numOfLowScoreQuestionPercent) {
		this.numOfLowScoreQuestionPercent = numOfLowScoreQuestionPercent;
	}

	public double getCountedSubmissionPercent() {
		return countedSubmissionPercent;
	}

	public void setCountedSubmissionPercent(double countedSubmissionPercent) {
		this.countedSubmissionPercent = countedSubmissionPercent;
	}

	public int getTotalTrainees() {
		return totalTrainees;
	}

	public void setTotalTrainees(int totalTrainees) {
		this.totalTrainees = totalTrainees;
	}

	public List<String> getRankList() {
		return rankList;
	}

	public void setRankList(List<String> rankList) {
		this.rankList = rankList;
	}

	public String getQuizTitle() {
		return quizTitle;
	}

	public void setQuizTitle(String quizTitle) {
		this.quizTitle = quizTitle;
	}

	public int getLowQuestionPercent() {
		return lowQuestionPercent;
	}

	public void setLowQuestionPercent(int lowQuestionPercent) {
		this.lowQuestionPercent = lowQuestionPercent;
	}

	public int getTotalLowQuestion() {
		return totalLowQuestion;
	}

	public void setTotalLowQuestion(int totalLowQuestion) {
		this.totalLowQuestion = totalLowQuestion;
	}
	
	
	
	
}
