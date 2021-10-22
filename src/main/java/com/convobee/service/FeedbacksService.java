package com.convobee.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.convobee.api.rest.request.FeedbacksRequest;
import com.convobee.api.rest.request.FeedbacksToUsRequest;
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
		int currentUserId = userUtil.getLoggedInUserId(request);
		Feedbacks feedback = feedbacksMapper.mapFeedbacks(feedbacksRequest, currentUserId);
		feedbacksRepo.save(feedback);
		/* After providing feedback, corresponding user's is_feedback_given status column will be changed to true */
		Optional<Users> user = usersRepo.findById(currentUserId);
		user.get().setIsfeedbackgiven(true);
		usersRepo.save(user.get());
	}
	
	public void submitFeedbackToUs(HttpServletRequest request, FeedbacksToUsRequest feedbacksToUsRequest) {
		int currentUserId = userUtil.getLoggedInUserId(request);
		FeedbacksToUs feedbacksToUs = feedbacksToUsMapper.mapFeedbacksToUs(feedbacksToUsRequest, currentUserId);
		feedbacksToUsRepo.save(feedbacksToUs);
	}
}
