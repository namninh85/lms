package vn.wse.lms.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vn.wse.lms.repository.QuestionTemplateRepositoryCustom;

public class QuestionTemplateRepositoryImpl implements QuestionTemplateRepositoryCustom{
	@PersistenceContext(unitName="punit")
	EntityManager entityManager;
	@Override
	   public void deleteQuestionTeamplateAndAnswerTemplateByQuizTeamplateId(Long quizTeamplateId) throws Exception{
		Query query = null;
		
		query = entityManager.createNativeQuery("select QUESTION_TEMPLATE_ID from question_template where QUIZ_TEMPLATE_ID = :quizTeamplateId");
		query.setParameter("quizTeamplateId", quizTeamplateId);
		List<Object> questionTemplateIds =query.getResultList();
		if(questionTemplateIds == null || (questionTemplateIds != null && questionTemplateIds.size() == 0) )
			return;
		
		query = entityManager.createNativeQuery("delete from answer_template where QUESTION_TEMPLATE_ID in :questionTemplateIds");
		query.setParameter("questionTemplateIds", questionTemplateIds);
		query.executeUpdate();
		
		query = entityManager.createNativeQuery("delete from question_template where QUIZ_TEMPLATE_ID =:quizTeamplateId");
		query.setParameter("quizTeamplateId", quizTeamplateId);
		query.executeUpdate();
		
		
		entityManager.flush();
	}
}
