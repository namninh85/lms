package vn.wse.lms.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import vn.wse.lms.entity.Topic;
import vn.wse.lms.repository.MaterialRepository;
import vn.wse.lms.repository.TopicRepositoryCustom;

public class TopicRepositoryImpl implements TopicRepositoryCustom {
	
	@PersistenceContext(unitName="punit")
	EntityManager entityManager;
	
	@Autowired 
	private MaterialRepository materialRepository;
	
	private static Logger logger = LoggerFactory.getLogger(TopicRepositoryImpl.class);

	@Override
	public void deleteTopicNotInListBySectionId(List<Topic> topics, Long sectionId) {
		if(topics != null){
			List<Long> topicIds = new ArrayList<Long>(); 
			
			for (Topic topic : topics) {
				if(topic.getTopicId() != null)
					topicIds.add(topic.getTopicId());
			}
			
			if(topics.size() >0){
				try {					
					Query query = entityManager.createQuery("from Topic t where t.sectionId =:sectionId and topicId NOT IN :topicIds ");
					query.setParameter("sectionId", sectionId);
					query.setParameter("topicIds", topicIds);
					
					List<Topic> results	=	query.getResultList();
				
					if(results != null){
						topicNeedToDelete(results);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void topicNeedToDelete(List<Topic> results) {
		for (Topic topic : results) {
			materialRepository.deleteAllMaterialByTopicId(topic.getTopicId());
			Query query = entityManager.createQuery("delete from Topic t where t.topicId=:topicId");
			query.setParameter("topicId", topic.getTopicId());
			query.executeUpdate();
			
		}
		entityManager.flush();
	}

	

	@Override
	public void deleteAllTopicBySectionId(Long sectionId) {
		try {
			Query query = entityManager.createQuery("from Topic t where t.sectionId =:sectionId");
			query.setParameter("sectionId", sectionId);
			
			List<Topic> results	=	query.getResultList();
		
			if(results != null)
				topicNeedToDelete(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	

}
