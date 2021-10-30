package com.convobee.api.rest.response;

import lombok.Builder;
import lombok.Data;

@Data
public class PieChartResponse {
	@Builder
	public static class ConfidenceLevel{
		public double oneStar;
		public double twoStar;
		public double threeStar;
		public double fourStar;
		public double fiveStar;
	}
	@Builder
	public static class ProficiencyLevel{
		public double oneStar;
		public double twoStar;
		public double threeStar;
		public double fourStar;
		public double fiveStar;
	}
	@Builder
	public static class ImpressionLevel{
		public double oneStar;
		public double twoStar;
		public double threeStar;
		public double fourStar;
		public double fiveStar;
	}
}
