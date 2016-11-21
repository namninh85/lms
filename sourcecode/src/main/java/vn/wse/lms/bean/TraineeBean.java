package vn.wse.lms.bean;

import vn.wse.lms.entity.Users;

public class TraineeBean extends AbstractBean<Users> {
	private long courseId;
	
	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}
	
	
}
