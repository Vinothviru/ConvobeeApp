package com.convobee.api.rest.response;

import java.sql.Timestamp;

public interface BookedSlotsResponse {

	Integer getBookedSlotId();
	Timestamp getSlotTime();
}

//public interface IdsOnly {
//	  Integer getId();
//	  String getOtherId();
//	}