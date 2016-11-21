package vn.wse.lms.repository;

import vn.wse.lms.bean.CoursesBean;
import vn.wse.lms.bean.TraineeBean;

public interface SearchRepository {
	CoursesBean searchCourses(CoursesBean coursesBean,String userName);

	CoursesBean searchRegisteredCourses(CoursesBean coursesBean, long traineeId);
	
	TraineeBean searchTrainees(TraineeBean traineeBean);

	CoursesBean searchOpenCourses(CoursesBean coursesBean);

	CoursesBean searchMyHistory(CoursesBean coursesBean, long traineeId);
}
