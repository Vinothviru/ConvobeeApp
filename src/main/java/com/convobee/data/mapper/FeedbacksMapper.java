package com.convobee.data.mapper;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.convobee.api.rest.request.FeedbacksRequest;
import com.convobee.constants.Constants;
import com.convobee.data.entity.Feedbacks;
import com.convobee.data.entity.Meetings;
import com.convobee.data.entity.Users;
import com.convobee.data.repository.FeedbacksRepo;
import com.convobee.data.repository.MeetingsRepo;
import com.convobee.exception.UserValidationException;
import com.convobee.utils.DateTimeUtil;

@Transactional(rollbackFor = Exception.class)
@Service
public class FeedbacksMapper {
	
	@Autowired
	MeetingsRepo meetingsRepo;
	
	@Autowired
	FeedbacksRepo feedbacksRepo;
	
	public Feedbacks mapFeedbacks(FeedbacksRequest feedbacksRequest, int currentUserId) throws Exception{
		int meetingId = feedbacksRequest.getMeetingId();
		
		/* Checking whether the user is already provided the feedback for particular meeting and if yes, not allowing to do the same */
		
		List<Feedbacks> fb = feedbacksRepo.findByMeetingid(meetingId);
		if(!fb.isEmpty()) {
			if(fb.get(0).getProvideruser().getUserid() == currentUserId)
			{
				throw new Exception(Constants.ALREADY_FEEDBACK_PROVIDED);
				//System.out.println("Already kuduthutiye da dei :joy:");
				//return null;
			}
			else if(fb.size()>1) {
				throw new Exception(Constants.EITHER_PROVIDED_OR_TRYING_TO_ACCESS_IRRELEVANT_DATA);
				//System.out.println("Epdiyo kuduthurpa ilena vera edho oru user indha data va access pana try panran");
				//return null;
			}
		}

		/* Actual mapping starts here */
		
		Feedbacks feedback = new Feedbacks();
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
		/* This else condition passes only when a user trying to provide feedback to another meeting id in which he/she is not mapped :joy: */
		else
		{
			throw new UserValidationException(Constants.USER_TRYING_TO_ACCESS_IRRELEVANT_DATA);
			//System.out.println("Inoruthan data va access pandriya da body soda");
			//return null;
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
		feedback.setCreatedat(DateTimeUtil.getCurrentUTCTime());
		return feedback;
	}
}
