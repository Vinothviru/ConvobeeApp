package com.convobee.api.rest.request;

import lombok.Data;

@Data
public class UsersRequest {

	private String username;
	private String nickname;
	private String password;
	private String mailid;
	private String role;
	private String country;
	private String city;
	private String educationlevel;
	private int proficiencylevel;
	private String signuptype;
	private int reportcount;
	private boolean isfeedback_given;
	
}