package com.convobee.api.rest.response;

import java.util.LinkedList;

import lombok.Data;

@Data
public class ShowSlotsResponse {
	private String date;
	private LinkedList<SlotTimeResponse> slotTimeResponseList;
}
