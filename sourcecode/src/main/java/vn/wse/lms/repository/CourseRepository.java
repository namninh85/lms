package vn.wse.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.wse.lms.entity.Course;
import vn.wse.lms.entity.Users;

public interface CourseRepository extends JpaRepository<Course, Long>  {
	@Query("SELECT user FROM CourseTrainer ct, Users user where ct.userId = user.userId and ct.course.courseId=:courseId ")
	public List<Users> getAllCourseTrainer(@Param("courseId")Long courseId);
	
	@Query("SELECT course FROM Course course order by course.courseId desc ")
	public List<Course> getAllCourses();

	@Query("SELECT c FROM Course c INNER JOIN c.trainers ct WHERE ct.email = :userName order by c.courseId desc ")
	public List<Course> findCoursesByTrainer(@Param("userName")String  userName);
}
