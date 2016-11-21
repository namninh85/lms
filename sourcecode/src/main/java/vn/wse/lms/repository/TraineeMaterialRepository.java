package vn.wse.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.wse.lms.entity.TraineeMaterial;

public interface TraineeMaterialRepository extends JpaRepository<TraineeMaterial, Long>  {

	@Query(value="select traineeMaterial from TraineeMaterial traineeMaterial where userId = :userId  and courseId = :courseId and materialId = :materialId ")
	List<TraineeMaterial> findCurrentStatus(@Param("userId") long userId, @Param("courseId") long courseId, @Param("materialId") long materialId);
	
	@Query(value="select traineeMaterial from TraineeMaterial traineeMaterial where userId = :userId  and courseId = :courseId and topicId = :topicId ")
	List<TraineeMaterial> findByTopic(@Param("userId") long userId, @Param("courseId") long courseId, @Param("topicId") long topicId);
	
	
}
