package vn.wse.lms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the trainee_quiz database table.
 * 
 */
@Entity
@Table(name="trainee_quiz")
@NamedQuery(name="TraineeQuiz.findAll", query="SELECT t FROM TraineeQuiz t")
public class TraineeQuiz implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TRAINEE_QUIZ_ID")
	private Long traineeQuizId;

	@Column(name="COURSE_ID")
	private Long courseId;

	@Column(name="NO_RETRIED")
	private Long noRetried;

	@Column(name="QUIZ_TEMPLATE_ID")
	private Long quizTemplateId;

	@Column(name="RESULT")
	private String result;

	@Column(name="SCORE")
	private Long score;
	
	@Column(name="RETRY_LEFT")
	private Long retryLeft;
	
	@Column(name="PERCENT_SCORE")
	private Long percentScore;

	@Column(name="USER_ID")
	private Long userId;
	
	@Column(name="STATUS")
	private Long status;
	

	@Column(name="START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	public TraineeQuiz() {
	}

	public Long getTraineeQuizId() {
		return this.traineeQuizId;
	}

	public void setTraineeQuizId(Long traineeQuizId) {
		this.traineeQuizId = traineeQuizId;
	}

	public Long getCourseId() {
		return this.courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getNoRetried() {
		return this.noRetried;
	}

	public void setNoRetried(Long noRetried) {
		this.noRetried = noRetried;
	}

	public Long getQuizTemplateId() {
		return this.quizTemplateId;
	}

	public void setQuizTemplateId(Long quizTemplateId) {
		this.quizTemplateId = quizTemplateId;
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Long getScore() {
		return this.score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Long getPercentScore() {
		return percentScore;
	}

	public void setPercentScore(Long percentScore) {
		this.percentScore = percentScore;
	}

	public Long getRetryLeft() {
		return retryLeft;
	}

	public void setRetryLeft(Long retryLeft) {
		this.retryLeft = retryLeft;
	}

	
}