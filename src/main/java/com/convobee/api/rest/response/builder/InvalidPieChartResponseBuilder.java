package com.convobee.api.rest.response.builder;

import org.springframework.stereotype.Service;

import com.convobee.api.rest.response.InvalidPieChartResponse;

@Service
public class InvalidPieChartResponseBuilder {
	public InvalidPieChartResponse buildResponse(double proficiencyLevel, double confidenceLevel, double impressionLevel){
		InvalidPieChartResponse pieChartResponse = InvalidPieChartResponse.builder() 
										.proficiencyLevel(proficiencyLevel)
										.confidenceLevel(confidenceLevel)
										.impressionLevel(impressionLevel).build();
		return pieChartResponse;
	}
}
