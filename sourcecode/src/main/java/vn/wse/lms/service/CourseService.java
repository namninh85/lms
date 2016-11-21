package vn.wse.lms.service;

import java.util.List;

import vn.wse.lms.bean.CoursesBean;
import vn.wse.lms.entity.Course;
import vn.wse.lms.entity.Material;
import vn.wse.lms.entity.Users;

public interface CourseService {

	List<Course> getAll();
	
	List<Course> getAllCoursesByUser(String username);
	
	Course findById(Long courseId);

	void save(Course course) throws Exception;
	
	CoursesBean searchCourses(CoursesBean coursesBean, String userName);
	
	CoursesBean searchRegisteredCourses(CoursesBean coursesBean, long traineeId);
	
	CoursesBean searchOpenCourses(CoursesBean coursesBean);

	List<Users> getAllCourseTrainer(long courseId);
	
	Material getMaterial(long materialId);

	CoursesBean searchMyHistory(CoursesBean coursesBean, long traineeId);

	Course cloneCourse(Course course) throws Exception;
}
