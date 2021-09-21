package com.convobee.api.rest.request;

import lombok.Data;

@Data
public class AuthenticationRequest {
	private String mailid;
	private String password;
}
