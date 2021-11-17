package com.convobee.api.rest.response.builder;

import java.util.LinkedList;

import org.springframework.stereotype.Service;

import com.convobee.api.rest.response.PieChartResponse;

@Service
public class PieChartResponseBuilder {
	public PieChartResponse.ConfidenceLevel buildConfidenceResponse(LinkedList<Double> skill){
		PieChartResponse.ConfidenceLevel pieChartResponse = PieChartResponse.ConfidenceLevel.builder()
														.confidence(skill).build();
		return pieChartResponse;
	}
	
	public PieChartResponse.ProficiencyLevel buildProficiencyResponse(LinkedList<Double> skill){
		PieChartResponse.ProficiencyLevel pieChartResponse = PieChartResponse.ProficiencyLevel.builder()
														.proficiency(skill).build();
		return pieChartResponse;
	}
	
	public PieChartResponse.ImpressionLevel buildImpressionResponse(LinkedList<Double> skill){
		PieChartResponse.ImpressionLevel pieChartResponse = PieChartResponse.ImpressionLevel.builder()
														.impression(skill).build();
		return pieChartResponse;
	}
}
