package vn.wse.lms.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the trainee_topic database table.
 * 
 */
@Entity
@Table(name="trainee_topic")
@NamedQuery(name="TraineeTopic.findAll", query="SELECT t FROM TraineeTopic t")
public class TraineeTopic implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TRAINEE_TOPIC_ID")
	private Long traineeTopicId;

	@Column(name="COURSE_ID")
	private Long courseId;

	private Long progress;

	@Column(name="SECTION_ID")
	private Long sectionId;

	@Column(name="`LOCK`")
	private Boolean lock;

	@Column(name="USER_ID")
	private Long userId;

	@ManyToOne()
	@JoinColumn(name = "TOPIC_ID")
	private Topic topic;
	
	public TraineeTopic() {
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

	public Long getProgress() {
		return this.progress;
	}

	public void setProgress(Long progress) {
		this.progress = progress;
	}

	public Long getSectionId() {
		return this.sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public Boolean getLock() {
		return lock;
	}

	public void setLock(Boolean lock) {
		this.lock = lock;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}