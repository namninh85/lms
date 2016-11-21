package vn.wse.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.wse.lms.entity.TraineeTopic;

public interface TraineeTopicRepository extends JpaRepository<TraineeTopic, Long>  {
	
	@Query(value="select traineeTopic from TraineeTopic traineeTopic where userId = :userId  and courseId = :courseId ")
	List<TraineeTopic> findAllByCourse(@Param("userId") long userId, @Param("courseId") long courseId);
	
	@Query(value="select traineeTopic from TraineeTopic traineeTopic where userId = :userId  and courseId = :courseId and topic.topicId=:topicId")
	List<TraineeTopic> findAllByTopic(@Param("userId") long userId, @Param("courseId") long courseId,@Param("topicId") long topicId);
}
