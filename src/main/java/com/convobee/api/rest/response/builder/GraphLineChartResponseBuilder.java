package com.convobee.api.rest.response.builder;

import java.util.LinkedList;

import org.springframework.stereotype.Service;

import com.convobee.api.rest.response.GraphLineChartResponse;

@Service
public class GraphLineChartResponseBuilder {
	public GraphLineChartResponse buildResponse(LinkedList<Double> confidenceLevel, LinkedList<Double> impressionLevel, LinkedList<Double> proficiencyLevel){
		GraphLineChartResponse graphLineChartResponse = GraphLineChartResponse.builder()
																		.confidenceDatalist(confidenceLevel)
																		.impressionDatalist(impressionLevel)
																		.proficiencyDatalist(proficiencyLevel)
																		.build();
													
		return graphLineChartResponse;
	}
}
