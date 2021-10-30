package com.convobee.api.rest.request;

import java.time.Month;

import lombok.Data;

@Data
public class GraphLineChartRequest {
	private String timeZone;
	private Month month;
	private int year;
}
