package com.convobee.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.convobee.data.entity.FeedbacksToUs;

public interface FeedbacksToUsRepo extends JpaRepository<FeedbacksToUs, Integer>{
	@Query(value = "select * from feedbacks_to_us where meeting_id=:meetingId",nativeQuery = true)
	List<FeedbacksToUs> findByMeetingid(int meetingId);
}
