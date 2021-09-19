package com.convobee.data.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class InterestsDTO {

	private int interest_id;
	private UsersDTO user;
	private boolean art;
	private boolean blogging;
	private boolean acting;
	private boolean sports;
	private boolean gaming;
	private boolean traveling;
	private boolean pet_care;
	private boolean music;
	private boolean cooking;
	private boolean reading_books;
	private boolean dance;
	private boolean technology;
	private Timestamp created_at;
	private Timestamp modified_at;
	
}
