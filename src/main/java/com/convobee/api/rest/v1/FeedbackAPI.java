package com.convobee.api.rest.v1;

import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.FeedbacksRequest;
import com.convobee.api.rest.request.FeedbacksToUsRequest;
import com.convobee.api.rest.request.GraphLineChartRequest;
import com.convobee.api.rest.request.ViewFeedbackRequest;
import com.convobee.api.rest.response.DashboardPieChatResponse;
import com.convobee.api.rest.response.FeedbackHistoryResponse;
import com.convobee.api.rest.response.GraphLineChartResponse;
import com.convobee.api.rest.response.InvalidPieChartResponse;
import com.convobee.api.rest.response.ViewFeedbackResponse;
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
	
	@RequestMapping(value = "/viewfeedback", method = RequestMethod.GET)
	public ViewFeedbackResponse viewFeedback(HttpServletRequest request, @ModelAttribute ViewFeedbackRequest viewFeedbackRequest) throws Exception{
		return feedbacksService.viewFeedback(request, viewFeedbackRequest);
	}
	
	@RequestMapping(value = "/getpiechart", method = RequestMethod.GET)
	public DashboardPieChatResponse getPieChart(HttpServletRequest request) throws Exception{
		return feedbacksService.getPieChart(request);
	}
	
	@RequestMapping(value = "/getgraphlinechart", method = RequestMethod.GET)
	public GraphLineChartResponse getGraphLineChart(HttpServletRequest request, @ModelAttribute GraphLineChartRequest graphLineChartRequest) throws Exception {
		return feedbacksService.getGraphLineChart(request, graphLineChartRequest);
	}
	
	@RequestMapping(value = "/getgraphlinechartforyear", method = RequestMethod.GET)
	public GraphLineChartResponse getGraphLineChartForYear(HttpServletRequest request, GraphLineChartRequest graphLineChartRequest) throws Exception {
		return feedbacksService.getGraphLineChartForYear(request, graphLineChartRequest);
	}
	
	
	@RequestMapping(value = "/getpiechartinvalid", method = RequestMethod.GET)
	public InvalidPieChartResponse getPieChartInvalid(HttpServletRequest request) throws Exception{
		return feedbacksService.getPieChartInvalid(request);
	}
}
