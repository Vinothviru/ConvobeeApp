package com.convobee.data.mapper;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.convobee.api.rest.request.FeedbacksToUsRequest;
import com.convobee.constants.Constants;
import com.convobee.data.entity.FeedbacksToUs;
import com.convobee.data.entity.Meetings;
import com.convobee.data.entity.Users;
import com.convobee.data.repository.FeedbacksToUsRepo;
import com.convobee.data.repository.MeetingsRepo;
import com.convobee.data.repository.UsersRepo;
import com.convobee.exception.UserValidationException;
import com.convobee.utils.DateTimeUtil;

@Transactional(rollbackFor = Exception.class)
@Service
public class FeedbacksToUsMapper {
	@Autowired
	MeetingsRepo meetingsRepo;
	
	@Autowired
	FeedbacksToUsRepo feedbacksToUsRepo;
	
	@Autowired
	UsersRepo usersRepo;
	
	public FeedbacksToUs mapFeedbacksToUs(FeedbacksToUsRequest feedbacksRequestToUs, int currentUserId) throws Exception {
		int meetingId = feedbacksRequestToUs.getMeetingId();
		
		/* Checking whether the user is already provided the feedback for particular meeting and if yes, not allowing to do the same */
		
		List<FeedbacksToUs> fb = feedbacksToUsRepo.findByMeetingid(meetingId);
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
		
		FeedbacksToUs feedbacksToUs = new FeedbacksToUs();
		Optional<Meetings> meeting = meetingsRepo.findById(meetingId);
		int loggedinUserId = currentUserId;
		int providerUserId = 0;
		int reporteeUserId = 0;
		int userAId = meeting.get().getUseraid().getUserid();
		int userBId = meeting.get().getUserbid().getUserid();
		if(loggedinUserId == userAId)
		{
			providerUserId = userAId;
			reporteeUserId = userBId;
		}
		else if(loggedinUserId == userBId)
		{
			providerUserId = userBId;
			reporteeUserId = userAId;
		}
		else
		{
			throw new UserValidationException(Constants.USER_TRYING_TO_ACCESS_IRRELEVANT_DATA);
			//System.out.println("Inoruthan data va access pandriya da body soda");
			//return null;
		}
		Users providerUser = new Users();
		providerUser.setUserid(providerUserId);
		
		feedbacksToUs.setMeetings(meeting.get());
		feedbacksToUs.setProvideruser(providerUser);
		boolean isReportUser = feedbacksRequestToUs.isIsreport_user();
		if(isReportUser) {
			
			/* Checking whether the user's report count exceeds 10 and if it is yes, then banning him else Increasing of report count */
			Users user = usersRepo.getById(reporteeUserId);
			int reportCount = user.getReportcount();
			reportCount+=1;
			if(reportCount > 10)
			{
				user.setIsuserbanned(true);
				usersRepo.save(user);//Banning user here
				System.out.println("Ada peyula povaan :joy: 10 times report vangirukiye. You are banned buddy!!");
			}
			else {
				user.setReportcount(reportCount);
				usersRepo.save(user);
			}

			/* Banning or Increasing of report count ended here */
			
			feedbacksToUs.setReportuser(true);
			Users reporteeUser = new Users();
			reporteeUser.setUserid(reporteeUserId);
			feedbacksToUs.setReporteeuser(reporteeUser);
			feedbacksToUs.setReporttype(feedbacksRequestToUs.getReportType());
			feedbacksToUs.setReportdescription(feedbacksRequestToUs.getReportDescription());
		}
		else {
			feedbacksToUs.setReportuser(false);
			feedbacksToUs.setReporteeuser(null);
			feedbacksToUs.setReporttype(null);
			feedbacksToUs.setReportdescription(null);
		}
		feedbacksToUs.setFeedbacktous(feedbacksRequestToUs.getFeedbackToProduct());
		feedbacksToUs.setCreatedat(DateTimeUtil.getCurrentUTCTime());
		return feedbacksToUs;
	}
}
