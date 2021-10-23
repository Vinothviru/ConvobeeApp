package com.convobee.service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.convobee.api.rest.request.FeedbacksRequest;
import com.convobee.api.rest.request.FeedbacksToUsRequest;
import com.convobee.api.rest.response.FeedbackHistoryResponse;
import com.convobee.data.entity.Feedbacks;
import com.convobee.data.entity.FeedbacksToUs;
import com.convobee.data.entity.Users;
import com.convobee.data.mapper.FeedbacksMapper;
import com.convobee.data.mapper.FeedbacksToUsMapper;
import com.convobee.data.repository.FeedbacksRepo;
import com.convobee.data.repository.FeedbacksToUsRepo;
import com.convobee.data.repository.UsersRepo;
import com.convobee.utils.UserUtil;

@Service
public class FeedbacksService {

	@Autowired
	FeedbacksMapper feedbacksMapper;
	
	@Autowired
	FeedbacksToUsMapper feedbacksToUsMapper;
	
	@Autowired
	FeedbacksRepo feedbacksRepo;
	
	@Autowired
	FeedbacksToUsRepo feedbacksToUsRepo;
	
	@Autowired
	UsersRepo usersRepo;
	
	@Autowired
	UserUtil userUtil;
	
	public void submitFeedback(HttpServletRequest request, FeedbacksRequest feedbacksRequest) {
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		Feedbacks feedback = feedbacksMapper.mapFeedbacks(feedbacksRequest, loggedinUserId);
		feedbacksRepo.save(feedback);
		/* After providing feedback, corresponding user's is_feedback_given status column will be changed to true */
		Optional<Users> user = usersRepo.findById(loggedinUserId);
		user.get().setIsfeedbackgiven(true);
		usersRepo.save(user.get());
	}
	
	public void submitFeedbackToUs(HttpServletRequest request, FeedbacksToUsRequest feedbacksToUsRequest) {
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		FeedbacksToUs feedbacksToUs = feedbacksToUsMapper.mapFeedbacksToUs(feedbacksToUsRequest, loggedinUserId);
		feedbacksToUsRepo.save(feedbacksToUs);
	}
	
	/* No need of user validation here because no param is passed from request exclusively. It is handled using JWT itself */
	public LinkedList<FeedbackHistoryResponse> getFeedbackHistory(HttpServletRequest request) throws Exception {
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		LinkedList<FeedbackHistoryResponse> feedbackHistoryResponse = new LinkedList<FeedbackHistoryResponse>();
		LinkedList<String> listOfSlotTime = feedbacksRepo.findSlotTimeByUserId(loggedinUserId);
		LinkedList<Object[]> nickNameAndfeedbackIdList = feedbacksRepo.findNickNamesAndfeedbackIdByUserId(loggedinUserId);
		int size = nickNameAndfeedbackIdList.size();
		for(int i = 0; i<size ;i++) {
			FeedbackHistoryResponse feedbackHistory = new FeedbackHistoryResponse();
			feedbackHistory.setSlotDateTime(listOfSlotTime.get(i).replace('T', ' '));
			feedbackHistory.setPeerNickName(nickNameAndfeedbackIdList.get(i)[0].toString());
			feedbackHistory.setFeedbackId(Integer.valueOf(nickNameAndfeedbackIdList.get(i)[1].toString()));
			feedbackHistoryResponse.add(feedbackHistory);
		}
		return feedbackHistoryResponse;
	}
}
