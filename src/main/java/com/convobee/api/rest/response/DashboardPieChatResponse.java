package com.convobee.api.rest.response;

import lombok.Data;

@Data
public class DashboardPieChatResponse {

	private PieChartResponse.ConfidenceLevel confidenceLevel;
	private PieChartResponse.ProficiencyLevel proficiencyLevel;
	private PieChartResponse.ImpressionLevel impressionLevel;

}
