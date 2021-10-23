package com.convobee.data.repository;

import java.util.LinkedList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.convobee.data.entity.Interests;

public interface InterestsRepo extends JpaRepository<Interests, Integer>{
	//List<Interests> findInterestByUser(Users user);
	
	@Query(value = "select interest_names from interests where user_id=:userid",nativeQuery = true)
	LinkedList<String> findInterestByUser(int userid);
}
