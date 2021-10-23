package com.convobee.api.rest.v1;

import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.FeedbacksRequest;
import com.convobee.api.rest.request.FeedbacksToUsRequest;
import com.convobee.api.rest.response.FeedbackHistoryResponse;
import com.convobee.service.FeedbacksService;

@RestController
public class FeedbackAPI {

	@Autowired
	FeedbacksService feedbacksService;

	@RequestMapping(value = "/submitfeedback", method = RequestMethod.POST)
	public String submitFeedback(HttpServletRequest request, @RequestBody FeedbacksRequest feedbacksRequest) throws Exception{
		feedbacksService.submitFeedback(request, feedbacksRequest);
		return "OK";
	}
	
	@RequestMapping(value = "/submitfeedbacktous", method = RequestMethod.POST)
	public String submitFeedbackToUs(HttpServletRequest request, @RequestBody FeedbacksToUsRequest feedbacksToUsRequest) throws Exception{
		feedbacksService.submitFeedbackToUs(request, feedbacksToUsRequest);
		return "OK";
	}
	
	@RequestMapping(value = "/getfeedbackhistory", method = RequestMethod.GET)
	public LinkedList<FeedbackHistoryResponse> getFeedbackHistory(HttpServletRequest request) throws Exception{
		return feedbacksService.getFeedbackHistory(request);
		
	}
}
