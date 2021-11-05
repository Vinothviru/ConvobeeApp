package com.convobee.data.repository;

import java.util.LinkedList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.convobee.data.entity.Users;

public interface UsersRepo extends JpaRepository<Users, Integer>{

	Optional<Users> findByMailid(String mailid);
	Users findByUserid(int userid);
	
	@Query(value = "SELECT user_name,nick_name,password,mail_id,country,city,education_level,created_at FROM users where user_id=:userId",nativeQuery = true)
	LinkedList<Object[]> findUserDetailsByUserid(int userId);
	
	@Query(value = "SELECT interest_names FROM interests where user_id=:userId",nativeQuery = true)
	LinkedList<String> findInterestsByUserid(int userId);
	
}
