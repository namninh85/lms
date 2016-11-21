package vn.wse.lms.repository.impl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.SearchExecutor;
import org.springframework.stereotype.Repository;

import vn.wse.lms.bean.CoursesBean;
import vn.wse.lms.bean.TraineeBean;
import vn.wse.lms.bean.TraineeSectionBean;
import vn.wse.lms.entity.Course;
import vn.wse.lms.entity.Users;
import vn.wse.lms.repository.SearchRepository;
import vn.wse.lms.service.Role;
import vn.wse.lms.service.UserService;
import vn.wse.lms.util.Utils;
import vn.wse.lms.web.AppContext;

@Repository
public class SearchRepositoryImpl implements SearchRepository{

	@PersistenceContext(unitName="punit")
	EntityManager entityManager;
	
	@Autowired
	UserService userService;
	
	@Override
	public CoursesBean searchCourses(CoursesBean coursesBean,String userName) {
		Session session = null;
		try {
			session = entityManager.unwrap(Session.class);
			String join = "";
			String condition = "";
		
			try {
				if (coursesBean.getStatus() == null) {
					coursesBean.setStatus("1");
				}
				List<String> roles = userService.getRoles(userName);
				
				if(!roles.contains(Role.ADMIN.toString())){
					join += " LEFT JOIN course_trainer ct on c.COURSE_ID = ct.COURSE_ID  ";
					condition += " AND ( ct.TRAINER_EMAIL = :email or c.CREATED_BY =:email )";
				}					
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String sql = "select distinct c.course_id as courseId, c.course_code as courseCode,c.title as title, c.short_description as shortDescription, c.icon as icon , c.category as category , (select count(1) from section s where s.course_id = c.course_id) as numberSections, c.start_date startDate, c.end_date endDate, (select count(1) from trainee_status ts where ts.course_id = c.course_id and ts.final_assessment='PASS') as numberPassed,  (select count(1) from course_registration ts where ts.course_id = c.course_id) as numberFailed "
					+ " FROM Course c "
					+ join
					+ " where (:keyword is null or c.title like '%:keyword%') and (c.status = :status or :status='ALL') "  
					+ condition;
					

				Query queryCount = entityManager.createNativeQuery(Utils.getCountQuery(sql));
				queryCount.setParameter("keyword", coursesBean.getKeyword());
				queryCount.setParameter("status", coursesBean.getStatus());
				
				if(StringUtils.isNoneBlank(join) && StringUtils.isNoneBlank(condition)){
					queryCount.setParameter("email", userName);
				}
				
				BigInteger total = (BigInteger)	queryCount.getSingleResult();
			
				sql += " order by c.course_id desc";
				
				SQLQuery query = session.createSQLQuery(sql)
						.addScalar("courseId", LongType.INSTANCE)
						.addScalar("courseCode", StringType.INSTANCE)
						.addScalar("title", StringType.INSTANCE)
						.addScalar("shortDescription", StringType.INSTANCE)
						.addScalar("shortDescription", StringType.INSTANCE)
						.addScalar("icon", StringType.INSTANCE)
						.addScalar("category", StringType.INSTANCE)						
						.addScalar("numberSections", IntegerType.INSTANCE)
						.addScalar("startDate", DateType.INSTANCE)
						.addScalar("endDate", DateType.INSTANCE)
						.addScalar("numberPassed", IntegerType.INSTANCE)
						.addScalar("numberFailed", IntegerType.INSTANCE);;
				query.setMaxResults(coursesBean.getLimit()).setFirstResult(coursesBean.getPage()*coursesBean.getLimit());
				query.setParameter("keyword", coursesBean.getKeyword());
				query.setParameter("status", coursesBean.getStatus());

				if(StringUtils.isNoneBlank(join) && StringUtils.isNoneBlank(condition)){
					query.setParameter("email", userName);
				}
				
				List<Course> courseList =	query.setResultTransformer(Transformers.aliasToBean(Course.class)).list();
	
			coursesBean.setTotal(total.intValue());
			coursesBean.setListResult(courseList);
			return coursesBean;
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}finally{
			
		}
	}
	
	
	@Override
	public CoursesBean searchRegisteredCourses(CoursesBean coursesBean, long traineeId) {
		Session session = null;
		try {
			session = entityManager.unwrap(Session.class);
			
			
			String sql = "select c.course_id as courseId, c.course_code as courseCode,c.title as title, c.short_description as shortDescription, c.icon as icon, c.category as category "
					+ " FROM Course c, course_registration cr  "
					+ " where c.course_id = cr.course_id and cr.user_id = :userId and (:keyword is null or c.title like '%:keyword%') and c.status='1'";
			
			BigInteger total = (BigInteger) entityManager.createNativeQuery(Utils.getCountQuery(sql))
					.setParameter("keyword", coursesBean.getKeyword()).setParameter("userId", traineeId).getSingleResult();

			
				SQLQuery query = session.createSQLQuery(sql)
						.addScalar("courseId", LongType.INSTANCE)
						.addScalar("courseCode", StringType.INSTANCE)
						.addScalar("title", StringType.INSTANCE)
						.addScalar("shortDescription", StringType.INSTANCE)
						.addScalar("shortDescription", StringType.INSTANCE)
						.addScalar("icon", StringType.INSTANCE)
						.addScalar("category", StringType.INSTANCE);
				
				query.setMaxResults(coursesBean.getLimit()).setFirstResult(coursesBean.getPage()*coursesBean.getLimit());
				query.setParameter("keyword", coursesBean.getKeyword()).setParameter("userId", traineeId);

				List<Course> courseList =	query.setResultTransformer(Transformers.aliasToBean(Course.class)).list();
	
			coursesBean.setTotal(total.intValue());
			coursesBean.setListResult(courseList);
			return coursesBean;
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}finally{
			
		}
		
		
	}
	
	@Override
	public CoursesBean searchMyHistory(CoursesBean coursesBean, long traineeId) {
		Session session = null;
		try {
			session = entityManager.unwrap(Session.class);
			
			
			String sql = "select c.course_id as courseId, c.course_code as courseCode,c.title as title, c.short_description as shortDescription, c.icon as icon, c.category as category "
					+ " FROM Course c, course_registration cr, trainee_status ts "
					+ " where c.course_id = cr.course_id and cr.user_id = :userId and c.course_id = ts.course_id and ts.user_id = cr.user_id and (:keyword is null or c.title like '%:keyword%') and c.status='1'";
			
			BigInteger total = (BigInteger) entityManager.createNativeQuery(Utils.getCountQuery(sql))
					.setParameter("keyword", coursesBean.getKeyword()).setParameter("userId", traineeId).getSingleResult();

			
				SQLQuery query = session.createSQLQuery(sql)
						.addScalar("courseId", LongType.INSTANCE)
						.addScalar("courseCode", StringType.INSTANCE)
						.addScalar("title", StringType.INSTANCE)
						.addScalar("shortDescription", StringType.INSTANCE)
						.addScalar("shortDescription", StringType.INSTANCE)
						.addScalar("icon", StringType.INSTANCE)
						.addScalar("category", StringType.INSTANCE);
				query.setMaxResults(coursesBean.getLimit()).setFirstResult(coursesBean.getPage()*coursesBean.getLimit());
				query.setParameter("keyword", coursesBean.getKeyword()).setParameter("userId", traineeId);

				List<Course> courseList =	query.setResultTransformer(Transformers.aliasToBean(Course.class)).list();
	
			coursesBean.setTotal(total.intValue());
			coursesBean.setListResult(courseList);
			return coursesBean;
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}finally{
			
		}
	}
	
	@Override
	public CoursesBean searchOpenCourses(CoursesBean coursesBean) {
		Session session = null;
		try {
			session = entityManager.unwrap(Session.class);
			
			
			String sql = "select c.course_id as courseId, c.course_code as courseCode,c.title as title, c.short_description as shortDescription, c.icon as icon, c.category as category "
					+ " FROM Course c  "
					+ " where c.status='1' and (:keyword is null or c.title like '%:keyword%')";
			
			BigInteger total = (BigInteger) entityManager.createNativeQuery(Utils.getCountQuery(sql))
					.setParameter("keyword", coursesBean.getKeyword()).getSingleResult();

			
				SQLQuery query = session.createSQLQuery(sql)
						.addScalar("courseId", LongType.INSTANCE)
						.addScalar("courseCode", StringType.INSTANCE)
						.addScalar("title", StringType.INSTANCE)
						.addScalar("shortDescription", StringType.INSTANCE)
						.addScalar("shortDescription", StringType.INSTANCE)
						.addScalar("icon", StringType.INSTANCE)
						.addScalar("category", StringType.INSTANCE);
				query.setMaxResults(coursesBean.getLimit()).setFirstResult(coursesBean.getPage()*coursesBean.getLimit());
				query.setParameter("keyword", coursesBean.getKeyword());

				List<Course> courseList =	query.setResultTransformer(Transformers.aliasToBean(Course.class)).list();
	
			coursesBean.setTotal(total.intValue());
			coursesBean.setListResult(courseList);
			return coursesBean;
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}finally{
			
		}
	}


	@Override
	public TraineeBean searchTrainees(TraineeBean traineeBean) {
		Session session = null;
		try {
			session = entityManager.unwrap(Session.class);
			
			
			String sql = "select us.user_id userId, us.dob dob, us.full_name fullName, ts.center center, ts.final_comment finalComment, ts.final_Assessment finalAssessment "
					+ " FROM course_registration cr join users us on (cr.user_id = us.user_id) "
					+ " left join trainee_status ts on (ts.user_id = us.user_id and ts.course_id = cr.course_id)  "
					+ " where cr.course_id = :courseId  ";
			
			BigInteger total = (BigInteger) entityManager.createNativeQuery(Utils.getCountQuery(sql))
					.setParameter("courseId", traineeBean.getCourseId()).getSingleResult();

			
				SQLQuery query = session.createSQLQuery(sql)
						.addScalar("userId", LongType.INSTANCE)
						.addScalar("dob", DateType.INSTANCE)
						.addScalar("fullName", StringType.INSTANCE)
						.addScalar("center", StringType.INSTANCE)
						.addScalar("finalComment", StringType.INSTANCE)
						.addScalar("finalAssessment", StringType.INSTANCE);
						
				query.setMaxResults(traineeBean.getLimit()).setFirstResult(traineeBean.getPage()*traineeBean.getLimit());
				query.setParameter("courseId", traineeBean.getCourseId());

				List<Users> traineeList =	query.setResultTransformer(Transformers.aliasToBean(Users.class)).list();
	
			traineeBean.setTotal(total.intValue());
			traineeBean.setListResult(traineeList);
				
		} catch (Exception ex) {
			
		}
	
		return traineeBean;
	}
}
