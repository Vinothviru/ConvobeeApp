package com.convobee.api.rest.response;

import lombok.Data;

@Data
public class AuthenticationResponse {
	private final String jwt;
}
