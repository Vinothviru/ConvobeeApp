package com.convobee.api.rest.response.builder;

import org.springframework.stereotype.Service;

import com.convobee.api.rest.response.DashboardPieChatResponse;
import com.convobee.api.rest.response.GraphLineChartResponse;
import com.convobee.api.rest.response.ShareProfileResponse;

@Service
public class ShareProfileResponseBuilder {
	public  ShareProfileResponse buildResponseForProfileSharing(String sharableURL) {
		ShareProfileResponse shareProfileLink = ShareProfileResponse.builder()
														.sharableLink(sharableURL).build();
		return shareProfileLink;
	}
	
	public  ShareProfileResponse buildResponseForSharedProfile(String userName, String city, String country, 
			GraphLineChartResponse graphLineChartResponse, DashboardPieChatResponse dashboardPieChatResponse) {
		ShareProfileResponse shareProfileResponse = ShareProfileResponse.builder()
																	.userName(userName)
																	.city(city)
																	.country(country)
																	.graphLineChartResponse(graphLineChartResponse)
																	.dashboardPieChatResponse(dashboardPieChatResponse)
																	.build();
		
			return shareProfileResponse;											
	}
}
