package com.convobee.api.rest.request;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class UsersRequest {

	private int user_id;
	private String user_name;
	private String nick_name;
	private String password;
	private String mail_id;
	private String country;
	private String city;
	private String education_level;
	private int proficiency_level;
	private String signup_type;
	private int report_count;
	private boolean is_feedback_given;
	private Timestamp created_at;
	private Timestamp modified_at;
	
}