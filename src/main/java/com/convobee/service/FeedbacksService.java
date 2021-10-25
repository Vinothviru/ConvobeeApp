package com.convobee.service;

import java.util.LinkedList;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.convobee.api.rest.request.FeedbacksRequest;
import com.convobee.api.rest.request.FeedbacksToUsRequest;
import com.convobee.api.rest.request.ViewFeedbackRequest;
import com.convobee.api.rest.response.DashboardPieChatResponse;
import com.convobee.api.rest.response.FeedbackHistoryResponse;
import com.convobee.api.rest.response.InvalidPieChartResponse;
import com.convobee.api.rest.response.ViewFeedbackResponse;
import com.convobee.api.rest.response.builder.InvalidPieChartResponseBuilder;
import com.convobee.api.rest.response.builder.PieChartResponseBuilder;
import com.convobee.api.rest.response.builder.ViewFeedbackResponseBuilder;
import com.convobee.data.entity.Feedbacks;
import com.convobee.data.entity.FeedbacksToUs;
import com.convobee.data.entity.Users;
import com.convobee.data.mapper.FeedbacksMapper;
import com.convobee.data.mapper.FeedbacksToUsMapper;
import com.convobee.data.repository.FeedbacksRepo;
import com.convobee.data.repository.FeedbacksToUsRepo;
import com.convobee.data.repository.UsersRepo;
import com.convobee.utils.CommonUtil;
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
	
	@Autowired
	ViewFeedbackResponseBuilder viewFeedbackResponseBuilder;
	
	@Autowired
	PieChartResponseBuilder pieChartResponseBuilder;
	
	@Autowired
	InvalidPieChartResponseBuilder invalidPieChartResponseBuilder;
	
	int count = 0;
	
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
	
	public ViewFeedbackResponse viewFeedback(HttpServletRequest request, ViewFeedbackRequest viewFeedbackRequest) throws Exception {
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		Feedbacks feedback = feedbacksRepo.findById(viewFeedbackRequest.getFeedbackId()).get();
		int feedbackReceiverUserId = feedback.getReceiveruser().getUserid();
		/* Checking whether the logged in user accessing his data or not */
		if(loggedinUserId == feedbackReceiverUserId)
		{
			ViewFeedbackResponse viewFeedbackResponse = viewFeedbackResponseBuilder.buildResponse(feedback.getProficiencylevel(), feedback.getConfidencelevel(), 
										feedback.getImpressionlevel(), feedback.getAppreciatefeedback(), feedback.getAdvisefeedback());
			return viewFeedbackResponse;
		}
		else
		{
			System.out.println("Inoruthan data va access pandriya da body soda");
			return null;
		}
	}
	
	/* No need of user validation here because no param is passed from request exclusively. It is handled using JWT itself */
	public DashboardPieChatResponse getPieChart(HttpServletRequest request) throws Exception {
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		int rowCount = feedbacksRepo.findTotalRowsByReceiveruserid(loggedinUserId);
		
		DashboardPieChatResponse pieChartResponseList = new DashboardPieChatResponse(); 
		
		LinkedList<Object[]> confidenceLevel = feedbacksRepo.findConfidenceLevelByReceiveruser(loggedinUserId);
		pieChartResponseList = processPieChartValues(confidenceLevel, rowCount, pieChartResponseList);
		
		LinkedList<Object[]> proficiencyLevel = feedbacksRepo.findProficiencyLevelByReceiveruser(loggedinUserId);
		pieChartResponseList = processPieChartValues(proficiencyLevel, rowCount, pieChartResponseList);
		
		LinkedList<Object[]> impressionLevel = feedbacksRepo.findImpressionLevelByReceiveruser(loggedinUserId);
		pieChartResponseList = processPieChartValues(impressionLevel, rowCount, pieChartResponseList);
		
		return pieChartResponseList;
	}
	
	public DashboardPieChatResponse  processPieChartValues(LinkedList<Object[]> result, int rowCount, DashboardPieChatResponse pieChartResponseList) throws Exception {
		double oneStar = 0, twoStar = 0, threeStar = 0, fourStar = 0, fiveStar = 0;
		for(int i = 0; i<result.size(); i++) {
			if(result.get(i)[0] != null) {
				if(Double.valueOf(result.get(i)[0].toString())==1.0) {
					oneStar = CommonUtil.calculatePercentage(Double.valueOf(result.get(i)[1].toString()),rowCount);
				}
				else if(Double.valueOf(result.get(i)[0].toString())==2.0) {
					twoStar = CommonUtil.calculatePercentage(Double.valueOf(result.get(i)[1].toString()),rowCount);
				}
				else if(Double.valueOf(result.get(i)[0].toString())==3.0) {
					threeStar = CommonUtil.calculatePercentage(Double.valueOf(result.get(i)[1].toString()),rowCount);
				}
				else if(Double.valueOf(result.get(i)[0].toString())==4.0) {
					fourStar = CommonUtil.calculatePercentage(Double.valueOf(result.get(i)[1].toString()),rowCount);
				}
				else if(Double.valueOf(result.get(i)[0].toString())==5.0) {
					fiveStar = CommonUtil.calculatePercentage(Double.valueOf(result.get(i)[1].toString()),rowCount);
				}
			}
		}
		count++;
		if(count==1) {
			pieChartResponseList.setConfidenceLevel(pieChartResponseBuilder.buildConfidenceResponse(oneStar, twoStar, threeStar, fourStar, fiveStar));
		}
			
		else if(count==2)
			pieChartResponseList.setProficiencyLevel(pieChartResponseBuilder.buildProficiencyResponse(oneStar, twoStar, threeStar, fourStar, fiveStar));
		else if(count==3) {
			count=0;
			pieChartResponseList.setImpressionLevel(pieChartResponseBuilder.buildImpressionResponse(oneStar, twoStar, threeStar, fourStar, fiveStar));
		}
		return pieChartResponseList;
	}
	
	/* No need of user validation here because no param is passed from request exclusively. It is handled using JWT itself */
	/* Unfortunately this is not our required API, We can use this if needed */
	public InvalidPieChartResponse getPieChartInvalid(HttpServletRequest request) throws Exception {
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		
		LinkedList<Object[]> result = feedbacksRepo.findAllByReceiveruser(loggedinUserId);
		double proficiencyLevel = 0, confidenceLevel = 0, impressionLevel = 0; 
		for(int i = 0; i<result.size() ;i++) {
			if(result.get(i)[0] != null) {
				proficiencyLevel = Double.valueOf(result.get(i)[0].toString());
				confidenceLevel = Double.valueOf(result.get(i)[1].toString());
				impressionLevel = Double.valueOf(result.get(i)[2].toString());
			}
		}
		double percentageProficiencyLevel = (proficiencyLevel/5)*100;
		double percentageconfidenceLevel = (confidenceLevel/5)*100;
		double percentageimpressionLevel = (impressionLevel/5)*100;
		InvalidPieChartResponse pieChartResponse = invalidPieChartResponseBuilder.buildResponse(percentageProficiencyLevel, percentageconfidenceLevel, percentageimpressionLevel);
		return pieChartResponse;
	}
}
