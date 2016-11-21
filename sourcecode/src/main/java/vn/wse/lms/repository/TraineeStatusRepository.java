package vn.wse.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.wse.lms.entity.TraineeStatus;

public interface TraineeStatusRepository extends JpaRepository<TraineeStatus, Long>  {

	@Query(value="select traineeStatus from TraineeStatus traineeStatus where userId = :userId  and courseId = :courseId ")
	List<TraineeStatus> findCurrentStatus(@Param("userId") long userId, @Param("courseId") long courseId);
	
	
}
