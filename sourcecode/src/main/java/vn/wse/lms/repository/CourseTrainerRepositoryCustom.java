package vn.wse.lms.repository;

import java.util.List;

import vn.wse.lms.entity.CourseTrainer;


public interface CourseTrainerRepositoryCustom {
	
	public void deleteCourseTrainerNotInListByCourseId(List<CourseTrainer> courseTrainers,Long courseId);

	void deleteAllCourseTrainerByCourseId(Long courseId);
}