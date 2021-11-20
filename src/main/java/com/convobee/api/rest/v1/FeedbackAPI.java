package com.convobee.api.rest.v1;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.FeedbacksHistoryRequest;
import com.convobee.api.rest.request.FeedbacksRequest;
import com.convobee.api.rest.request.FeedbacksToUsRequest;
import com.convobee.api.rest.request.GraphLineChartRequest;
import com.convobee.api.rest.request.ViewFeedbackRequest;
import com.convobee.api.rest.response.BaseResponse;
import com.convobee.service.FeedbacksService;

@RestController
@CrossOrigin
public class FeedbackAPI {

	@Autowired
	FeedbacksService feedbacksService;

	@RequestMapping(value = "/submitfeedback", method = RequestMethod.POST)
	public ResponseEntity submitFeedback(HttpServletRequest request, @RequestBody FeedbacksRequest feedbacksRequest) throws Exception{
		return  new BaseResponse().getResponse( ()-> feedbacksService.submitFeedback(request, feedbacksRequest));
	}
	
	@RequestMapping(value = "/submitfeedbacktous", method = RequestMethod.POST)
	public ResponseEntity submitFeedbackToUs(HttpServletRequest request, @RequestBody FeedbacksToUsRequest feedbacksToUsRequest) throws Exception{
		return  new BaseResponse().getResponse( ()-> feedbacksService.submitFeedbackToUs(request, feedbacksToUsRequest));
	}
	
	@RequestMapping(value = "/getfeedbackhistory", method = RequestMethod.GET)
	public ResponseEntity getFeedbackHistory(HttpServletRequest request, @ModelAttribute FeedbacksHistoryRequest feedbacksHistoryRequest) throws Exception{
		return  new BaseResponse().getResponse( ()-> feedbacksService.getFeedbackHistory(request, feedbacksHistoryRequest.getTimeZone()));
	}
	
	@RequestMapping(value = "/getfeedbackhistoryforconsecutiverequests", method = RequestMethod.GET)
	public ResponseEntity getFeedbackHistoryForConsecutiveRequests(HttpServletRequest request, @ModelAttribute FeedbacksHistoryRequest feedbacksHistoryRequest) throws Exception{
		return  new BaseResponse().getResponse( ()-> feedbacksService.getFeedbackHistoryForConsecutiveRequests(request, feedbacksHistoryRequest));
	}
	
	@RequestMapping(value = "/getfeedbackhistoryforcustomrange", method = RequestMethod.GET)
	public ResponseEntity getFeedbackHistoryForCustomRange(HttpServletRequest request, @ModelAttribute FeedbacksHistoryRequest feedbacksHistoryRequest) throws Exception{
		return  new BaseResponse().getResponse( ()-> feedbacksService.getFeedbackHistoryForCustomRange(request, feedbacksHistoryRequest));
	}
	
	@RequestMapping(value = "/viewfeedback", method = RequestMethod.GET)
	public ResponseEntity viewFeedback(HttpServletRequest request, @ModelAttribute ViewFeedbackRequest viewFeedbackRequest) throws Exception{
		return  new BaseResponse().getResponse( ()-> feedbacksService.viewFeedback(request, viewFeedbackRequest));
	}
	
	@RequestMapping(value = "/getpiechart", method = RequestMethod.GET)
	public ResponseEntity getPieChart(HttpServletRequest request) throws Exception{
		return  new BaseResponse().getResponse( ()-> feedbacksService.getPieChart(request));
	}
	
	@RequestMapping(value = "/getgraphlinechart", method = RequestMethod.GET)
	public ResponseEntity getGraphLineChart(HttpServletRequest request, @ModelAttribute GraphLineChartRequest graphLineChartRequest) throws Exception {
		return  new BaseResponse().getResponse( ()-> feedbacksService.getGraphLineChart(request, graphLineChartRequest));
	}
	
	@RequestMapping(value = "/getgraphlinechartforyear", method = RequestMethod.GET)
	public ResponseEntity getGraphLineChartForYear(HttpServletRequest request, GraphLineChartRequest graphLineChartRequest) throws Exception {
		return  new BaseResponse().getResponse( ()-> feedbacksService.getGraphLineChartForYear(request, graphLineChartRequest));
	}
	
	
	@RequestMapping(value = "/getpiechartinvalid", method = RequestMethod.GET)
	public ResponseEntity getPieChartInvalid(HttpServletRequest request) throws Exception{
		return  new BaseResponse().getResponse( ()-> feedbacksService.getPieChartInvalid(request));
	}
}
