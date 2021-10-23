package com.convobee.data.repository;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.convobee.data.entity.Feedbacks;

public interface FeedbacksRepo extends JpaRepository<Feedbacks, Integer>{
	
	@Query(value = "select * from feedbacks where meeting_id=:meetingId",nativeQuery = true)
	List<Feedbacks> findByMeetingid(int meetingId);
	
	@Query(value = "select slot_time from slots where slot_id in(select meetings.slot_id as sid from meetings inner join feedbacks on meetings.meeting_id = feedbacks.meeting_id and feedbacks.receiver_user_id=:userid) order by slot_time desc limit 50",nativeQuery = true)
	LinkedList<String> findSlotTimeByUserId(int userid);
	
	@Query(value = "select users.nick_name, feedbacks.feedback_id as fid from feedbacks inner join users on users.user_id=feedbacks.provider_user_id and feedbacks.receiver_user_id=:userid order by fid desc limit 50",nativeQuery = true)
	LinkedList<Object[]> findNickNamesAndfeedbackIdByUserId(int userid);
}
