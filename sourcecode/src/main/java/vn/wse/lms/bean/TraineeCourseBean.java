package vn.wse.lms.bean;

import java.io.Serializable;

import vn.wse.lms.entity.Course;
import vn.wse.lms.entity.TraineeStatus;
import vn.wse.lms.entity.Users;

public class TraineeCourseBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5514631944775184836L;
	private Course course;
	private Users trainee;
	private TraineeStatus traineeStatus;
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public Users getTrainee() {
		return trainee;
	}
	public void setTrainee(Users trainee) {
		this.trainee = trainee;
	}
	public TraineeStatus getTraineeStatus() {
		return traineeStatus;
	}
	public void setTraineeStatus(TraineeStatus traineeStatus) {
		this.traineeStatus = traineeStatus;
	}
	
	
}
