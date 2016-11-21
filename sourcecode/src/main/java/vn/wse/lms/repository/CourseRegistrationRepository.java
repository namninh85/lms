package vn.wse.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.wse.lms.entity.CourseRegistration;


public interface CourseRegistrationRepository  extends JpaRepository<CourseRegistration, Long>, CourseRegistrationRepositoryCustom  {

	@Query(value="select count(*) from course_registration where USER_ID = :userId  and COURSE_ID = :courseId ", nativeQuery = true)
	int countByUserIdAndCourseId(@Param("userId")Long userId,@Param("courseId") Long courseId);
	
}