package vn.wse.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.wse.lms.entity.Material;
import vn.wse.lms.entity.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long>, TopicRepositoryCustom {

	@Query(value = "select count(*) from material where topic_id = :topicId and type in ('2', '4')" , nativeQuery = true)
	public Long totalMaterial(@Param("topicId")Long topicId);
	
	@Query(value = "select count(*) from trainee_material where  user_id = :userId and topic_id = :topicId and status = 100" , nativeQuery = true)
	public Long totalFinished(@Param("userId")Long userId, @Param("topicId")Long topicId);
	
	@Query(value = "select m from Material m where  m.topic.topicId = :topicId order by m.position ")
	public List<Material> getMaterialByTopic(@Param("topicId")Long topicId);
}
