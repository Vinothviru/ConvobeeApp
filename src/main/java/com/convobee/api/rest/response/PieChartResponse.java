package com.convobee.api.rest.response;

import java.util.LinkedList;

import lombok.Builder;
import lombok.Data;

@Data
public class PieChartResponse {
	@Builder
	public static class ConfidenceLevel{
		public LinkedList<Double> confidence;
	}
	@Builder
	public static class ProficiencyLevel{
		public LinkedList<Double> proficiency;
	}
	@Builder
	public static class ImpressionLevel{
		public LinkedList<Double> impression;
	}
}
