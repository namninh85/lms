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
 * The persistent class for the trainee_status database table.
 * 
 */
@Entity
@Table(name="trainee_status")
@NamedQuery(name="TraineeStatus.findAll", query="SELECT t FROM TraineeStatus t")
public class TraineeStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TRAINEE_TOPIC_ID")
	private Long traineeTopicId;

	@Column(name="COURSE_ID")
	private Long courseId;

	@Column(name="CURRENT_MATERIAL_ID")
	private Long currentMaterialId;

	@Column(name="CURRENT_SECTION_ID")
	private Long currentSectionId;

	@Column(name="CURRENT_TOPIC_ID")
	private Long currentTopicId;

	private String status;

	@Column(name="USER_ID")
	private Long userId;
	
	@Column(name="FINAL_COMMENT")
	private String finalComment;
	
	@Column(name="CENTER")
	private String center;
	
	@Column(name="FINAL_ASSESSMENT")
	private String finalAssessment;
	
	@Temporal(TemporalType.DATE)
	@Column(name="START_DATE")
	private Date startDate;

	public TraineeStatus() {
	}

	public Long getTraineeTopicId() {
		return this.traineeTopicId;
	}

	public void setTraineeTopicId(Long traineeTopicId) {
		this.traineeTopicId = traineeTopicId;
	}

	public Long getCourseId() {
		return this.courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getCurrentMaterialId() {
		return this.currentMaterialId;
	}

	public void setCurrentMaterialId(Long currentMaterialId) {
		this.currentMaterialId = currentMaterialId;
	}

	public Long getCurrentSectionId() {
		return this.currentSectionId;
	}

	public void setCurrentSectionId(Long currentSectionId) {
		this.currentSectionId = currentSectionId;
	}

	public Long getCurrentTopicId() {
		return this.currentTopicId;
	}

	public void setCurrentTopicId(Long currentTopicId) {
		this.currentTopicId = currentTopicId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFinalComment() {
		return finalComment;
	}

	public void setFinalComment(String finalComment) {
		this.finalComment = finalComment;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getFinalAssessment() {
		return finalAssessment;
	}

	public void setFinalAssessment(String finalAssessment) {
		this.finalAssessment = finalAssessment;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	
}