package com.convobee.api.rest.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersResponse {
	private String userName;
	private String nickName;
	private String password;
	private String mailId;
	private String country;
	private String city;
	private String educationLevel;
	private LocalDateTime createdAt;
	private List<String> interests;
}
