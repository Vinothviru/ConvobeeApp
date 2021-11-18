package com.convobee.api.rest.request;

import lombok.Data;
@Data
public class BookedSlotsRequest {

	private int bookedslotid;
	private int newslotid;
	private String timeZone;
}
