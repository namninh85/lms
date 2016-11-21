package vn.wse.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.wse.lms.entity.CourseRegistration;


public interface CourseRegistrationRepositoryCustom {
	
	public void deleteCourseRegistrationNotInListByCourseId(List<CourseRegistration> courseRegistrations,Long courseId);

	void deleteAllCourseRegistrationByCourseId(Long courseId);
}