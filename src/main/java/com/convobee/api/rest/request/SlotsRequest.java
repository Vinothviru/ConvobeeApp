package com.convobee.api.rest.request;

import lombok.Data;

@Data
public class SlotsRequest {
	//private String fromdate;
	//private String fromtime;
	//private String todate;
	//private String totime;
	private Integer slotId;
	private Integer numberOfDays;
	private String timeZone;
}
