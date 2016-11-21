package vn.wse.lms.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import vn.wse.lms.entity.Role;
import vn.wse.lms.entity.UserRole;
import vn.wse.lms.entity.Users;
import vn.wse.lms.repository.UserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository{
	
	@PersistenceContext(unitName="punit")
	EntityManager entityManager;
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getRoles(String email) {
		List<String> results = new ArrayList<String>();
		
		String sql = " SELECT role.role_id FROM user_roles role "
				+ " JOIN users usr ON role.user_id = usr.user_id "
				+ " where usr.email= :email  ";
									
		Query query = entityManager.createNativeQuery(sql).setParameter("email", email);
		
		results = query.getResultList();		
		return results;
	}
	
	public Users findByEmail(String email) {
		try {
			return entityManager.createNamedQuery(Users.FIND_BY_EMAIL, Users.class)
					.setParameter("email", email)
					.getSingleResult();
		} catch (PersistenceException e) {
			return null;
		}
	}
	
	@Override
	public void add(Users user)
	{
		entityManager.persist(user);
		if(user.getUserId() != null) {
			UserRole userRole = new UserRole();
			if(user.getUserType() == 1) { // Trainee
				Role role = new Role();
				role.setRoleId("TRAINEE");			
				userRole.setRole(role);	
			}else if(user.getUserType() == 0) {
				Role role = new Role();
				role.setRoleId("TRAINER");			
				userRole.setRole(role);	
			}else if(user.getUserType() == 9999) {
				Role role = new Role();
				role.setRoleId("ADMIN");			 
				userRole.setRole(role);	
			}
			userRole.setUsers(user);
			entityManager.persist(userRole);
		}
	}

	@Override
	public void edit(Users user)
	{
		entityManager.merge(user);
	}
	
	@Override 
	public void delete(Long userId)
	{
		String sql = "UPDATE users SET status='Inactive' WHERE USER_ID=:userId";
		entityManager.createNativeQuery(sql).setParameter("userId", userId).executeUpdate();
	}
	
	@Override 
	public void updateListUserRole(Users user){
		String deleteSql = "DELETE FROM user_roles where USER_ID =:userId";
		entityManager.createNativeQuery(deleteSql).setParameter("userId", user.getUserId()).executeUpdate();
		
		for (Role role  : user.getRolesNeedSave()) {
			UserRole userRole = new UserRole();
			userRole.setRole(role);
			userRole.setUsers(user);
			entityManager.persist(userRole);
		}
		
	}
	
	@Override
	public List<Users> getAll()
	{
		String sql = "select u from Users u where  status='Active' and u.fullName is not null and u.fullName <> '' and u.email is not null and u.email <> ''";
		return entityManager.createQuery(sql).getResultList();
	}
	
	@Override
	public List<Users> getAllTrainee()
	{
		String sql = "select u from Users u where  status='Active' and u.fullName is not null and u.fullName <> '' and u.email is not null and u.email <> '' and exists (select ur from UserRole ur where ur.users.userId=u.userId and ur.role.roleId='TRAINEE')";
		return entityManager.createQuery(sql).getResultList();
	}
	
	@Override
	public List<Users> getAllTrainer()
	{
		String sql = "select u from Users u where  status='Active' and u.fullName is not null and u.fullName <> '' and u.email is not null and u.email <> '' and exists (select ur from UserRole ur where ur.users.userId=u.userId and ur.role.roleId='TRAINER')";
		return entityManager.createQuery(sql).getResultList();
	}

	@Override
	public Users getById(Long id) {
		try {
			return entityManager.createNamedQuery(Users.FIND_BY_ID, Users.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (PersistenceException e) {
			return null;
		}
	}
	
	@Override
	public List<UserRole> getAllUserRole()
	{
		String sql = "SELECT u FROM UserRole u";
		return entityManager.createQuery(sql).getResultList();
	}
	
	@Override
	public List<String> getAllRole()
	{
		List<String> results = new ArrayList<String>();
		String sql = "SELECT r.roleId FROM Role r";
		Query query =  entityManager.createQuery(sql);
		results = query.getResultList();		
		return results;
	}
	
}
