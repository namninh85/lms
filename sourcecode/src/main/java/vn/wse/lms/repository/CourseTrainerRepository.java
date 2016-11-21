package vn.wse.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.wse.lms.entity.CourseTrainer;


public interface CourseTrainerRepository  extends JpaRepository<CourseTrainer, Long>, CourseTrainerRepositoryCustom  {

	@Query(value="select count(*) from course_trainer where TRAINER_ID = :trainerId  and COURSE_ID = :courseId ", nativeQuery = true)
	int countByTrainerIdAndCourseId(@Param("trainerId")Long trainerId,@Param("courseId") Long courseId);
	
}