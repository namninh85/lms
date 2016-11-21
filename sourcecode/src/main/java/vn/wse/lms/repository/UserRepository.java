package vn.wse.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.wse.lms.entity.Course;
import vn.wse.lms.entity.Role;
import vn.wse.lms.entity.UserRole;
import vn.wse.lms.entity.Users;



public interface UserRepository  {
	
	List<String> getRoles(String userName);
	
	Users findByEmail(String email);
	
	public void add(Users users);
	
	public void edit(Users users);
	
	public void delete(Long userId);
	
	public List<Users> getAllTrainee();
	
	public List<Users> getAllTrainer();

	Users getById(Long userId);
	
	public List<UserRole> getAllUserRole();
	
	public List<String> getAllRole();

	public void updateListUserRole(Users user);

	List<Users> getAll();

}
