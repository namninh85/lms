package vn.wse.lms.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vn.wse.lms.entity.CourseRegistration;
import vn.wse.lms.repository.CourseRegistrationRepositoryCustom;

public class CourseRegistrationRepositoryImpl implements CourseRegistrationRepositoryCustom {
	
	@PersistenceContext(unitName="punit")
	EntityManager entityManager;
	
	@Override
	public void deleteCourseRegistrationNotInListByCourseId(List<CourseRegistration> courseRegistrations,
			Long courseId) {
		if(courseRegistrations != null){
			List<Long> userIds = new ArrayList<Long>(); 
			
			
			for (CourseRegistration courseRegistration : courseRegistrations) {
				if(courseRegistration.getUserId() != null)
					userIds.add(courseRegistration.getUserId());
			}
			
			if(userIds.size() >0){
				try {					
					Query query = entityManager.createNativeQuery(" delete from course_registration where COURSE_ID =:courseId and USER_ID NOT IN :userIds ");
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
	public void deleteAllCourseRegistrationByCourseId(Long courseId) {
		try {					
			Query query = entityManager.createNativeQuery(" delete from course_registration where COURSE_ID =:courseId ");
			query.setParameter("courseId", courseId);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
