package vn.wse.lms.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import vn.wse.lms.entity.Section;
import vn.wse.lms.repository.SectionRepositoryCustom;
import vn.wse.lms.repository.TopicRepository;

public class SectionRepositoryImpl implements SectionRepositoryCustom {
	
	@PersistenceContext(unitName="punit")
	EntityManager entityManager;
	
	@Autowired
	TopicRepository topicRepository;
	
	private static Logger logger = LoggerFactory.getLogger(SectionRepositoryImpl.class);

	@Override
	public void deleteSectionNotInListByCourseId(List<Section> sections, Long courseId) {
		if(sections != null){
			List<Long> sectionIds = new ArrayList<Long>(); 
			
			for (Section section : sections) {
				if(section.getSectionId() != null)
					sectionIds.add(section.getSectionId());
			}
			
			if(sectionIds.size() >0){
				try {					
					Query query = entityManager.createQuery("from Section s where s.course.courseId =:courseId and sectionId NOT IN :sections ");
					query.setParameter("courseId", courseId);
					query.setParameter("sections", sectionIds);
					
					List<Section> results	=	query.getResultList();
				
					if(results != null){
						sectionsNeedDelete(results);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void sectionsNeedDelete(List<Section> results) {
		for (Section section : results) {
			topicRepository.deleteAllTopicBySectionId(section.getSectionId());
			Query query = entityManager.createQuery("delete from Section s where s.sectionId=:sectionId");
			query.setParameter("sectionId", section.getSectionId());
			query.executeUpdate();
		}
		entityManager.flush();
	}

	@Override
	public void deleteAllSectionByCourseId(Long courseId) {
		try {
			Query query = entityManager.createQuery("from Section s where s.course.courseId =:courseId");
			query.setParameter("courseId", courseId);
			
			List<Section> results	=	query.getResultList();
		
			if(results != null)
				sectionsNeedDelete(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
