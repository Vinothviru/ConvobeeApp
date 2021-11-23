package com.convobee.api.rest.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShareProfileResponse {
	private String sharableLink;
	private String userName;
	private String city;
	private String country;
	private GraphLineChartResponse graphLineChartResponse;
	private DashboardPieChatResponse dashboardPieChatResponse;
}
