package com.convobee.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.convobee.data.entity.Feedbacks;

public interface FeedbacksRepo extends JpaRepository<Feedbacks, Integer>{

}
