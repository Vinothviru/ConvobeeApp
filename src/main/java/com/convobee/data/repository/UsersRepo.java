package com.convobee.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.convobee.data.entity.Users;

public interface UsersRepo extends JpaRepository<Users, Integer>{

	Optional<Users> findByMailid(String mailid);
	Users findByUserid(int userid);
}
