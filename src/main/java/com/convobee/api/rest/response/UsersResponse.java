package com.convobee.api.rest.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersResponse {
	private String username;
	private String nickname;
	private String password;
	private String mailid;
	private String country;
	private String city;
	private String educationlevel;
	private LocalDateTime createdat;
	private String role;
	private String signuptype;
	private List<String> interests;
}
