package vn.wse.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.wse.lms.entity.Material;

public interface MaterialRepository extends JpaRepository<Material, Long>, MaterialRepositoryCustom {

	@Query(value="select m.quizTemplate.quizTemplateId from Material m where m.materialId =:materialId")
	public Long findQuizTemplateIdByMaterialId(@Param("materialId") Long materialId);
}
