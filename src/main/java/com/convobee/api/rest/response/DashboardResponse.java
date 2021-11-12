package com.convobee.api.rest.response;

import java.util.LinkedList;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {
	private LinkedList<SessionResponse> upcommingSessionResponse;
	private LinkedList<FeedbackHistoryResponse> feedbackHistoryResponse;
	private GraphLineChartResponse graphLineChartResponse;
	private DashboardPieChatResponse dashboardPieChatResponse;
	private Integer userId;
}
