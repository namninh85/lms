package vn.wse.lms.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the course_trainer database table.
 * 
 */
@Entity
@Table(name = "course_trainer")
@NamedQuery(name = "CourseTrainer.findAll", query = "SELECT c FROM CourseTrainer c")
public class CourseTrainer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "COURSE_TRAINER_ID")
	private Long courseTrainerId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ID")
	private Course course;

	@Column(name = "TRAINER_EMAIL")
	private String email;

	@Column(name = "TRAINER_ID")
	private Long userId;

	@Formula("(select u.FULL_NAME from users u where u.USER_ID = TRAINER_ID)")
	private String fullName;

	public CourseTrainer() {
	}

	public Long getCourseTrainerId() {
		return this.courseTrainerId;
	}

	public void setCourseTrainerId(Long courseTrainerId) {
		this.courseTrainerId = courseTrainerId;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}