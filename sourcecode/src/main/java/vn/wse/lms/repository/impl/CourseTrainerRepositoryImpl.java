package vn.wse.lms.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vn.wse.lms.entity.CourseTrainer;
import vn.wse.lms.repository.CourseTrainerRepositoryCustom;

public class CourseTrainerRepositoryImpl implements CourseTrainerRepositoryCustom {
	
	@PersistenceContext(unitName="punit")
	EntityManager entityManager;
	
	@Override
	public void deleteCourseTrainerNotInListByCourseId(List<CourseTrainer> courseTrainers,
			Long courseId) {
		if(courseTrainers != null){
			List<Long> userIds = new ArrayList<Long>(); 
			
			
			for (CourseTrainer courseTrainer : courseTrainers) {
				if(courseTrainer.getUserId() != null)
					userIds.add(courseTrainer.getUserId());
			}
			
			if(userIds.size() >0){
				try {					
					Query query = entityManager.createNativeQuery(" delete from course_trainer where COURSE_ID =:courseId and TRAINER_ID NOT IN :userIds ");
					query.setParameter("courseId", courseId);
					query.setParameter("userIds", userIds);
					query.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	@Override
	public void deleteAllCourseTrainerByCourseId(Long courseId) {
		try {					
			Query query = entityManager.createNativeQuery(" delete from course_trainer where COURSE_ID =:courseId ");
			query.setParameter("courseId", courseId);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
