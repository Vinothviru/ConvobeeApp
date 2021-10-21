package com.convobee.data.mapper;

import java.sql.Timestamp;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.convobee.api.rest.request.FeedbacksRequest;
import com.convobee.data.entity.Feedbacks;
import com.convobee.data.entity.Meetings;
import com.convobee.data.entity.Users;
import com.convobee.data.repository.MeetingsRepo;
import com.convobee.utils.UserUtil;

@Service
public class FeedbacksMapper {
	
	@Autowired
	MeetingsRepo meetingsRepo;
	
	@Autowired
	UserUtil userUtil;
	
	public Feedbacks mapFeedbacks(HttpServletRequest request, FeedbacksRequest feedbacksRequest, int currentUserId) {
		Feedbacks feedback = new Feedbacks();
		int meetingId = feedbacksRequest.getMeetingId();
		Optional<Meetings> meeting = meetingsRepo.findById(meetingId);
		int loggedinUserId = currentUserId;
		int providerUserId = 0;
		int receiverUserId = 0;
		int userAId = meeting.get().getUseraid().getUserid();
		int userBId = meeting.get().getUserbid().getUserid();
		if(loggedinUserId == userAId)
		{
			providerUserId = userAId;
			receiverUserId = userBId;
		}
		else if(loggedinUserId == userBId)
		{
			providerUserId = userBId;
			receiverUserId = userAId;
		}
		else
		{
			System.out.println("Inoruthan data va access pandriya da body soda");
			return null;
		}
		
		Users providedUser = new Users();
		providedUser.setUserid(providerUserId);
		Users receiverUser = new Users();
		receiverUser.setUserid(receiverUserId);
		feedback.setMeetings(meeting.get());
		feedback.setProvideruser(providedUser);
		feedback.setReceiveruser(receiverUser);
		feedback.setImpressionlevel(feedbacksRequest.getImpressionLevel());
		feedback.setConfidencelevel(feedbacksRequest.getConfidenceLevel());
		feedback.setProficiencylevel(feedbacksRequest.getProficiencyLevel());
		feedback.setAppreciatefeedback(feedbacksRequest.getAppreciateFeedback());
		feedback.setAdvisefeedback(feedbacksRequest.getAdviseFeedback());
		feedback.setCreatedat(Timestamp.valueOf("1970-01-01 00:00:01"));
		return feedback;
	}
}
