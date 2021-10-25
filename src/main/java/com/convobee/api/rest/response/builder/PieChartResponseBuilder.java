package com.convobee.api.rest.response.builder;

import org.springframework.stereotype.Service;

import com.convobee.api.rest.response.PieChartResponse;

@Service
public class PieChartResponseBuilder {
	public PieChartResponse.ConfidenceLevel buildConfidenceResponse(double oneStar, double twoStar, double threeStar, double fourStar, double fiveStar){
		PieChartResponse.ConfidenceLevel pieChartResponse = PieChartResponse.ConfidenceLevel.builder()
														.oneStar(oneStar)
														.twoStar(twoStar)
														.threeStar(threeStar)
														.fourStar(fourStar)
														.fiveStar(fiveStar).build();
		return pieChartResponse;
	}
	
	public PieChartResponse.ProficiencyLevel buildProficiencyResponse(double oneStar, double twoStar, double threeStar, double fourStar, double fiveStar){
		PieChartResponse.ProficiencyLevel pieChartResponse = PieChartResponse.ProficiencyLevel.builder()
														.oneStar(oneStar)
														.twoStar(twoStar)
														.threeStar(threeStar)
														.fourStar(fourStar)
														.fiveStar(fiveStar).build();
		return pieChartResponse;
	}
	
	public PieChartResponse.ImpressionLevel buildImpressionResponse(double oneStar, double twoStar, double threeStar, double fourStar, double fiveStar){
		PieChartResponse.ImpressionLevel pieChartResponse = PieChartResponse.ImpressionLevel.builder()
														.oneStar(oneStar)
														.twoStar(twoStar)
														.threeStar(threeStar)
														.fourStar(fourStar)
														.fiveStar(fiveStar).build();
		return pieChartResponse;
	}
}
