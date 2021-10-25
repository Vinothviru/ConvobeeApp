package com.convobee.api.rest.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvalidPieChartResponse {
	private double proficiencyLevel;
	private double confidenceLevel;
	private double impressionLevel;
}
