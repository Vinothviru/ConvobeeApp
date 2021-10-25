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
	
	
	@Query(value = "select impression_level as il,count(impression_level) from feedbacks where receiver_user_id=:userId group by il",nativeQuery = true)
	LinkedList<Object[]> findImpressionLevelByReceiveruser(int userId);
	
	@Query(value = "select confidence_level as cl,count(confidence_level) from feedbacks where receiver_user_id=:userId group by cl",nativeQuery = true)
	LinkedList<Object[]> findConfidenceLevelByReceiveruser(int userId);
	
	@Query(value = "select proficiency_level as pl,count(proficiency_level) from feedbacks where receiver_user_id=:userId group by pl",nativeQuery = true)
	LinkedList<Object[]> findProficiencyLevelByReceiveruser(int userId);
	
	@Query(value = "select count(*) from feedbacks where receiver_user_id=:userId",nativeQuery = true)
	Integer findTotalRowsByReceiveruserid(int userId);
	
	/* This has become a unused query :joy */
	@Query(value = "select avg(proficiency_level),avg(confidence_level),avg(impression_level) from feedbacks where receiver_user_id=:userId",nativeQuery = true)
	LinkedList<Object[]> findAllByReceiveruser(int userId);
}
