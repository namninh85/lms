package vn.wse.lms.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the course_registration database table.
 * 
 */
@Entity
@Table(name="course_registration")
@NamedQuery(name="CourseRegistration.findAll", query="SELECT c FROM CourseRegistration c")
public class CourseRegistration implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="COURSE_REGISTRATION_ID")
	private Long courseRegistrationId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ID")
	private Course course;

	private String email;

	@Column(name="USER_ID")
	private Long userId;
	
	@Formula("(select u.FULL_NAME from users u where u.USER_ID = USER_ID)")
	private String fullName;
	
	public CourseRegistration() {
	}
	
	public Long getCourseRegistrationId() {
		return courseRegistrationId;
	}

	public void setCourseRegistrationId(Long courseRegistrationId) {
		this.courseRegistrationId = courseRegistrationId;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getUserId() {
		return this.userId;
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