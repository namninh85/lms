package vn.wse.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.wse.lms.entity.Users;

public interface TraineeRepository extends JpaRepository<Users, Long>  {

}
