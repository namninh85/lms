package vn.wse.lms.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.wse.lms.bean.CourseQuizBean;
import vn.wse.lms.bean.QuizReportBean;
import vn.wse.lms.bean.TraineeScoreBean;
import vn.wse.lms.entity.Material;
import vn.wse.lms.entity.QuestionTemplate;
import vn.wse.lms.entity.QuizTemplate;
import vn.wse.lms.repository.MaterialRepositoryCustom;

public class MaterialRepositoryImpl implements MaterialRepositoryCustom {
	
	@PersistenceContext(unitName="punit")
	EntityManager entityManager;
	
	private static Logger logger = LoggerFactory.getLogger(MaterialRepositoryImpl.class);

	@Override
	public void deleteMaterialNotInListByTopicId(List<Material> materials, Long topicId) {
		if(materials != null){
			List<Long> materialIds = new ArrayList<Long>(); 
			
			for (Material material : materials) {
				if(material.getMaterialId() != null)
					materialIds.add(material.getMaterialId());
			}
			
			if(materialIds.size() >0){
				try {					
					Query query = entityManager.createQuery("from Material m where m.topic.topicId =:topicId and materialId NOT IN :materialIds ");
					query.setParameter("topicId", topicId);
					query.setParameter("materialIds", materialIds);
					
					List<Material> results	=	query.getResultList();
				
					if(results != null)
						materialsNeedDelete(results);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void materialsNeedDelete(List<Material> results) {
		for (Material material : results) {
			Query queryHql = entityManager.createQuery("delete from Material m where m.materialId=:materialId");
			queryHql.setParameter("materialId", material.getMaterialId());
			queryHql.executeUpdate();
			
			if(material.getQuizTemplate() != null && material.getQuizTemplate().getQuizTemplateId() != null){
				Long quizTemplateId= material.getQuizTemplate().getQuizTemplateId();
				logger.info("delete quizTemplateId:"+ quizTemplateId);
				
				queryHql = entityManager.createQuery("from QuestionTemplate q where q.quizTemplate.quizTemplateId =:quizTemplateId");
				queryHql.setParameter("quizTemplateId", quizTemplateId);
				
				List<QuestionTemplate> questionTemplates	=	queryHql.getResultList();
				
				for (QuestionTemplate questionTemplate : questionTemplates) {
					//delete answer template
					if(questionTemplate.getQuestionTemplateId() != null){
						queryHql = entityManager.createQuery("delete from AnswerTemplate a where a.questionTemplate.questionTemplateId =:questionTemplateId");
						queryHql.setParameter("questionTemplateId", questionTemplate.getQuestionTemplateId());
						queryHql.executeUpdate();
					}
				}
				
				//delete question template
				queryHql = entityManager.createQuery("delete from QuestionTemplate q where q.quizTemplate.quizTemplateId =:quizTemplateId");
				queryHql.setParameter("quizTemplateId", quizTemplateId);
				queryHql.executeUpdate();
				
				queryHql = entityManager.createQuery("delete from QuizTemplate q where q.quizTemplateId=:quizTemplateId");
				queryHql.setParameter("quizTemplateId", quizTemplateId);
				queryHql.executeUpdate();
				
			}
			
			entityManager.flush();
		}
	}

	@Override
	public void deleteAllMaterialByTopicId(Long topicId) {
		try {
			Query query = entityManager.createQuery("from Material m where m.topic.topicId =:topicId");
			query.setParameter("topicId", topicId);
			
			List<Material> results	=	query.getResultList();
		
			if(results != null)
				materialsNeedDelete(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<CourseQuizBean> getAllQuiz(long courseId) {
		Session session = null;
		List<CourseQuizBean> result = new ArrayList<CourseQuizBean>();
		try {
			session = entityManager.unwrap(Session.class);
			
			
			String sql = "select se.SECTION_ID sectionId, se.TITLE sectionTitle, tp.topic_id topicId, tp.TITLE topicTitle, ma.MATERIAL_NAME quizTitle, ma.MATERIAL_ID materialId from material ma, topic tp, section se "
					+ " where tp.section_id = se.section_id and ma.topic_id = tp.topic_id and se.course_id = :courseId and ma.type='4' order by se.section_id  ";
			
						
				SQLQuery query = session.createSQLQuery(sql)
						.addScalar("sectionId", LongType.INSTANCE)
						.addScalar("sectionTitle", StringType.INSTANCE)
						.addScalar("topicId", LongType.INSTANCE)
						.addScalar("topicTitle", StringType.INSTANCE)
						.addScalar("quizTitle", StringType.INSTANCE)
						.addScalar("materialId", LongType.INSTANCE);
						
				query.setParameter("courseId", courseId);

				result =	query.setResultTransformer(Transformers.aliasToBean(CourseQuizBean.class)).list();
	
				
		} catch (Exception ex) {
			
		}
	
		return result;
	}
	
	
	@Override
	public QuizReportBean getQuizSummary(long courseId, long materialId, QuizTemplate qt) {
		Session session = null;
		QuizReportBean result = new QuizReportBean();
		try {
			session = entityManager.unwrap(Session.class);
			
			
			String sql = "select  avg(tq.score), count(1), 100*sum(tq.score)/count(1) from trainee_material tm, trainee_quiz tq where tm.TRAINEE_QUIZ_ID = tq.TRAINEE_QUIZ_ID and tm.course_id = :courseId and tm.material_id=:materialId and tm.status = 100 and tm.trainee_quiz_id is not null ";
					
				SQLQuery query = session.createSQLQuery(sql);
						
				query.setParameter("courseId", courseId);
				query.setParameter("materialId", materialId);

				List<Object[]> list =	(List<Object[]>)query.list();
				if (list.size() > 0) {
					Object[] objArr = list.get(0);
					if (objArr[0] != null) {
						BigDecimal avg = (BigDecimal) objArr[0];
						result.setAveragePoint(avg.doubleValue());
					}
					if (objArr[1] != null) {
						BigInteger bi = (BigInteger) objArr[1];
						result.setCountedSubmission(bi.intValue());
					}
					
					
					if (objArr[2] != null) {
						BigDecimal pe = (BigDecimal) objArr[2];
						result.setPercentPoint(pe.intValue());
					}
				}
				
				double score = 100*qt.getPassingGrade();
				int numofLowScore = getNumOfLowScore(courseId, materialId, score);
				if (result.getCountedSubmission() > 0) {
					Double percent = (double) (100*numofLowScore/result.getCountedSubmission());
					result.setNumOfLowScoreQuestionPercent(percent.intValue());
				}
				result.setNumOfLowScoreQuestion(numofLowScore);
	
				
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	
		return result;
	}
	
	private int getNumOfLowScore(long courseId, long materialId, double score) {
		Session session = null;
		try {
			session = entityManager.unwrap(Session.class);
			
			String sql = "select count(1) from trainee_material tm, trainee_quiz tq where tm.TRAINEE_QUIZ_ID = tq.TRAINEE_QUIZ_ID and tm.course_id = :courseId and tm.material_id=:materialId and tm.status = 100 and tm.trainee_quiz_id is not null and tq.percent_score < " + score;
						
				SQLQuery query = session.createSQLQuery(sql);
						
				query.setParameter("courseId", courseId);
				query.setParameter("materialId", materialId);

				List<BigInteger> list =	(List<BigInteger>)query.list();
				if (list.size() > 0) {
					BigInteger bi = (BigInteger) list.get(0);
					return bi.intValue();
				}
				
				
				
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	
		return 0;
	}

	@Override
	public List<TraineeScoreBean> getTraineeScores(long courseId, long materialId) {
		List<TraineeScoreBean> result = new ArrayList<TraineeScoreBean>();
		
		Session session = null;
		try {
			session = entityManager.unwrap(Session.class);
			
			String sql = "select tm.submission_date submissionTime, us.full_Name traineeName, tm.score totalPoint, tm.no_viewed timeSubmitted,  tm.trainee_Quiz_Id traineeQuizId from course_registration cr left join trainee_material tm on (cr.user_id = tm.user_id and cr.course_id = tm.course_id),  users us " 
					+ " where cr.user_id = us.user_id and cr.course_id = :courseId and tm.material_id = :materialId ";
			
						
				SQLQuery query = session.createSQLQuery(sql)
						.addScalar("submissionTime", DateType.INSTANCE)
						.addScalar("traineeName", StringType.INSTANCE)
						.addScalar("totalPoint", LongType.INSTANCE)
						.addScalar("timeSubmitted", LongType.INSTANCE)
						.addScalar("traineeQuizId", LongType.INSTANCE)
						;
						
				query.setParameter("courseId", courseId);
				query.setParameter("materialId", materialId);

				result =	query.setResultTransformer(Transformers.aliasToBean(TraineeScoreBean.class)).list();
				
		} catch (Exception ex) {
			
		}
	
		return result;
	}
}
