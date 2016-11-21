package vn.wse.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.wse.lms.entity.Section;

public interface SectionRepository extends JpaRepository<Section, Long>, SectionRepositoryCustom {


}
