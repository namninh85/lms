package vn.wse.lms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * The persistent class for the trainee_material database table.
 * 
 */
@Entity
@Table(name="trainee_material")
@NamedQuery(name="TraineeMaterial.findAll", query="SELECT t FROM TraineeMaterial t")
public class TraineeMaterial implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TRAINEE_MATERIAL_ID")
	private Long traineeMaterialId;

	@Column(name="COURSE_ID")
	private Long courseId;
	
	@Column(name="TOPIC_ID")
	private Long topicId;

	@Column(name="MATERIAL_ID")
	private Long materialId;

	@Column(name="NO_VIEWED")
	private Long noViewed;
	
	@Column(name="SCORE")
	private Long score;
	
	@Column(name="TRAINEE_QUIZ_ID")
	private Long traineeQuizId;
	
	@Transient
	private boolean highScore;
	
	@ManyToOne
    @JoinColumn(name="TRAINEE_QUIZ_ID", insertable=false, updatable=false)
    private TraineeQuiz traineeQuiz;

	private Long status;

	@Column(name="USER_ID")
	private Long userId;

	@Column(name="SUBMISSION_DATE")
	@Temporal(TemporalType.DATE)
	private Date submissionDate;

	
	public TraineeMaterial() {
	}

	public Long getTraineeMaterialId() {
		return this.traineeMaterialId;
	}

	public void setTraineeMaterialId(Long traineeMaterialId) {
		this.traineeMaterialId = traineeMaterialId;
	}

	public Long getCourseId() {
		return this.courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	public Long getNoViewed() {
		return this.noViewed;
	}

	public void setNoViewed(Long noViewed) {
		this.noViewed = noViewed;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Long getTraineeQuizId() {
		return traineeQuizId;
	}

	public void setTraineeQuizId(Long traineeQuizId) {
		this.traineeQuizId = traineeQuizId;
	}

	public TraineeQuiz getTraineeQuiz() {
		return traineeQuiz;
	}

	public void setTraineeQuiz(TraineeQuiz traineeQuiz) {
		this.traineeQuiz = traineeQuiz;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public boolean getHighScore() {
		return highScore;
	}

	public void setHighScore(boolean highScore) {
		this.highScore = highScore;
	}
	
	
}