package vn.wse.lms.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the trainee_learn_log database table.
 * 
 */
@Entity
@Table(name="trainee_learn_log")
@NamedQuery(name="TraineeLearnLog.findAll", query="SELECT t FROM TraineeLearnLog t")
public class TraineeLearnLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TRAINEE_LEARN_LOG_ID")
	private int traineeLearnLogId;

	@Column(name="COURSE_ID")
	private int courseId;

	@Column(name="MATERIAL_ID")
	private int materialId;

	@Column(name="SECTION_ID")
	private int sectionId;

	private String status;

	@Column(name="TOPIC_ID")
	private int topicId;

	@Column(name="USER_ID")
	private int userId;

	public TraineeLearnLog() {
	}

	public int getTraineeLearnLogId() {
		return this.traineeLearnLogId;
	}

	public void setTraineeLearnLogId(int traineeLearnLogId) {
		this.traineeLearnLogId = traineeLearnLogId;
	}

	public int getCourseId() {
		return this.courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}

	public int getSectionId() {
		return this.sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTopicId() {
		return this.topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}