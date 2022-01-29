package com.convobee.api.rest.response;

import lombok.Data;

@Data
public class SessionResponse {
	private Integer slotId;
	private Integer bookedSlotId;
	private String SlotTime;
}
