package com.convobee.api.rest.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuthResponse {

	private String username;
	private String mailid;
	
}
