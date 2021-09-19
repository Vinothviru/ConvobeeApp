package com.convobee.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.convobee.data.entity.Meetings;

public interface MeetingsRepo extends JpaRepository<Meetings, Integer>{

}
