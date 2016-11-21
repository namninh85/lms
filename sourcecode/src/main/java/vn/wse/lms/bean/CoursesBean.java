package vn.wse.lms.bean;

import vn.wse.lms.entity.Course;

public class CoursesBean extends AbstractBean<Course> {
	private String keyword;
	private String status;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
