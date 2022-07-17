package com.convobee.data.repository;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.convobee.data.entity.Feedbacks;

public interface FeedbacksRepo extends JpaRepository<Feedbacks, Integer>{
	
	@Query(value = "select * from feedbacks where meeting_id=:meetingId",nativeQuery = true)
	List<Feedbacks> findByMeetingid(int meetingId);
	
	@Query(value = "select feedbacks.feedback_id, slots.slot_time, users.nick_name from slots inner join meetings on meetings.slot_id = slots.slot_id inner join feedbacks on meetings.meeting_id = feedbacks.meeting_id and feedbacks.receiver_user_id=:userid inner join users on users.user_id=feedbacks.provider_user_id and feedbacks.receiver_user_id=:userid order by feedbacks.created_at desc limit 50",nativeQuery = true)
	LinkedList<Object[]> findFeedbackHistorybyUserId(int userid);
	
	@Query(value = "select feedbacks.feedback_id, slots.slot_time, users.nick_name from slots inner join meetings on meetings.slot_id = slots.slot_id inner join feedbacks on meetings.meeting_id = feedbacks.meeting_id and feedbacks.receiver_user_id=:userid and feedbacks.feedback_id<:feedbackid inner join users on users.user_id=feedbacks.provider_user_id and feedbacks.receiver_user_id=:userid order by feedbacks.created_at desc limit 50",nativeQuery = true)
	LinkedList<Object[]> findFeedbackHistoryForConsecutiveRequestByUserIdAndFeedbackId(int userid, int feedbackid);
	
	@Query(value = "select feedbacks.feedback_id, slots.slot_time, users.nick_name from slots inner join meetings on slots.slot_id=meetings.slot_id inner join feedbacks on meetings.meeting_id = feedbacks.meeting_id and feedbacks.receiver_user_id=:userid inner join users on users.user_id=feedbacks.receiver_user_id and slots.slot_time between :startdatetime and :enddatetime order by slots.slot_time asc;",nativeQuery = true)
	LinkedList<Object[]> findFeedbackHistorybyUserIdAndCustomDateTimeRange(int userid, String startdatetime, String enddatetime);
	
	@Query(value = "select impression_level as il,count(impression_level) from feedbacks where receiver_user_id=:userId group by il",nativeQuery = true)
	LinkedList<Object[]> findImpressionLevelByReceiveruser(int userId);
	
	@Query(value = "select confidence_level as cl,count(confidence_level) from feedbacks where receiver_user_id=:userId group by cl",nativeQuery = true)
	LinkedList<Object[]> findConfidenceLevelByReceiveruser(int userId);
	
	@Query(value = "select proficiency_level as pl,count(proficiency_level) from feedbacks where receiver_user_id=:userId group by pl",nativeQuery = true)
	LinkedList<Object[]> findProficiencyLevelByReceiveruser(int userId);
	
	@Query(value = "select count(*) from feedbacks where receiver_user_id=:userId",nativeQuery = true)
	Integer findTotalRowsByReceiveruserid(int userId);
	
	//@Query(value = "select slot_time from slots where slot_id in(select meetings.slot_id as sid from meetings inner join feedbacks on meetings.meeting_id = feedbacks.meeting_id and feedbacks.receiver_user_id=:userId) and slot_time between :startTime and :endTime",nativeQuery = true)
	@Query(value = "select slot_time from slots where slot_id in(select meetings.slot_id from meetings inner join feedbacks on meetings.meeting_id = feedbacks.meeting_id and feedbacks.receiver_user_id=:userId) and slot_time between :startTime and :endTime order by slot_time",nativeQuery = true)
	LinkedList<Timestamp> findSlotTimeByUserIdAndDateTime(int userId, String startTime, String endTime);
	
	@Query(value = "select confidence_level as confidence, impression_level as impression, proficiency_level as proficiency from feedbacks where meeting_id in (select meeting_id from meetings cm inner join slots as cs on cs.slot_id=cm.slot_id and (user_a_id=:userId or user_b_id=:userId) and slot_time between :startTime and :endTime order by slot_time) and receiver_user_id=:userId group by confidence,impression,proficiency",nativeQuery = true)
	LinkedList<Object[]> findSkillFactorsByUserIdAndDateTime(int userId, String startTime, String endTime);
	
	@Query(value = "select confidence_level, impression_level, proficiency_level from feedbacks where receiver_user_id=:userId",nativeQuery = true)
	LinkedList<Object[]> findOverAllSkillFactors(int userId);
	
	/* This has become a unused query :joy */
	@Query(value = "select avg(proficiency_level),avg(confidence_level),avg(impression_level) from feedbacks where receiver_user_id=:userId",nativeQuery = true)
	LinkedList<Object[]> findAllByReceiveruser(int userId);
}
