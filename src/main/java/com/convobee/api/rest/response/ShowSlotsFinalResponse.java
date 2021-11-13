package com.convobee.api.rest.response;

import java.util.LinkedList;

import lombok.Data;

@Data
public class ShowSlotsFinalResponse {
	LinkedList<ShowSlotsResponse> showResponseFinal;
	private LinkedList<Integer> userBookedIds;
}
