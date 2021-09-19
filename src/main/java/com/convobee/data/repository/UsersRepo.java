package com.convobee.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.convobee.data.entity.Users;

public interface UsersRepo extends JpaRepository<Users, Integer>{

}
