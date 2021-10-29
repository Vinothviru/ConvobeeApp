package com.convobee.api.rest.request;

import lombok.Data;

@Data
public class GraphLineChartRequest {
	private String timeZone;
	private int month;
	private int year;
}
