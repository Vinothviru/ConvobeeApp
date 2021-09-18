package com.convobee.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int user_id;
	@Column(length = 20)
	private String user_name;
	@Column(length = 10)
	private String nick_name;
	@Column(length = 15)
	private String password;
	@Column(length = 30)
	private String mail_id;
	@Column(length = 20)
	private String country;
	@Column(length = 20)
	private String city;
	@Column(length = 5)
	private String education_level;
	private int proficiency_level;
	@Column(length = 15)
	private String signup_type;
	private int report_count;
	private boolean is_feedback_given;
	private Timestamp created_at;
	private Timestamp modified_at;
	
}
