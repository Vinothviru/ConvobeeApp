package com.convobee.service;

import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.convobee.api.rest.request.GraphLineChartRequest;
import com.convobee.api.rest.response.DashboardPieChatResponse;
import com.convobee.api.rest.response.DashboardResponse;
import com.convobee.api.rest.response.FeedbackHistoryResponse;
import com.convobee.api.rest.response.GraphLineChartResponse;
import com.convobee.api.rest.response.SessionResponse;
import com.convobee.api.rest.response.builder.DashboardResponseBuilder;
import com.convobee.utils.UserUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class DashboardService {
	
	@Autowired
	BookedSlotsService bookedSlotsService;
	
	@Autowired
	FeedbacksService feedbacksService;
	
	@Autowired
	UserUtil userUtil;
	
	@Autowired
	DashboardResponseBuilder dashboardResponseBuilder;
	
	public DashboardResponse getDashboardDetails(HttpServletRequest request, GraphLineChartRequest graphLineChartRequest) throws Exception {
		LinkedList<SessionResponse> upcommingSessionResponse = bookedSlotsService.getUpcomingSessions(request);
		LinkedList<FeedbackHistoryResponse> feedbackHistoryResponse = feedbacksService.getFeedbackHistory(request);
		GraphLineChartResponse graphLineChartResponse = feedbacksService.getGraphLineChart(request, graphLineChartRequest);
		DashboardPieChatResponse dashboardPieChatResponse = feedbacksService.getPieChart(request);
		Integer userId = userUtil.getLoggedInUserId(request);
		DashboardResponse dashboardResponse = dashboardResponseBuilder.buildResponse(upcommingSessionResponse, feedbackHistoryResponse, graphLineChartResponse, dashboardPieChatResponse, userId);
		return dashboardResponse;
	}
}
