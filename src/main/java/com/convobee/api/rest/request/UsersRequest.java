package com.convobee.api.rest.request;

import java.util.List;

import lombok.Data;

@Data
public class UsersRequest {

	private int userid;
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
	private List<String> interests;
	private String token;
	private String bvfhdjsk;
	private List<String> deleted_interests;
	//Having createdat as String and not as timestamp because to avoid the UTC and local time discrepancy
	private String createdat;
}