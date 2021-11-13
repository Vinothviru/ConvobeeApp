package com.convobee.api.rest.response.builder;

import java.util.LinkedList;

import org.springframework.stereotype.Service;

import com.convobee.api.rest.response.DashboardPieChatResponse;
import com.convobee.api.rest.response.DashboardResponse;
import com.convobee.api.rest.response.FeedbackHistoryResponse;
import com.convobee.api.rest.response.GraphLineChartResponse;
import com.convobee.api.rest.response.SessionResponse;

@Service
public class DashboardResponseBuilder {
	public DashboardResponse buildResponse(LinkedList<SessionResponse> upcommingSessionResponse,
	LinkedList<FeedbackHistoryResponse> feedbackHistoryResponse,
	GraphLineChartResponse graphLineChartResponse,
	DashboardPieChatResponse dashboardPieChatResponse,
	Integer userId){
		DashboardResponse dashboardResponse = DashboardResponse.builder()
																.upcommingSessionResponse(upcommingSessionResponse)
																.feedbackHistoryResponse(feedbackHistoryResponse)
																.graphLineChartResponse(graphLineChartResponse)
																.dashboardPieChatResponse(dashboardPieChatResponse)
																.userId(userId)
																.build();
													
		return dashboardResponse;
	}
}
