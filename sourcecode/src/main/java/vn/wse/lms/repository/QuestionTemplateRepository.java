package vn.wse.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.wse.lms.entity.QuestionTemplate;

public interface QuestionTemplateRepository extends JpaRepository<QuestionTemplate, Long>, QuestionTemplateRepositoryCustom{

}
