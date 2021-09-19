package com.convobee.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.sun.istack.NotNull;

import lombok.Data;

@Entity
@Data
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int user_id;
	@Column(length = 20)
	@NotNull
	private String user_name;
	@Column(length = 10)
	@NotNull
	private String nick_name;
	@Column(length = 15)
	@NotNull
	private String password;
	@Column(length = 30)
	@NotNull
	private String mail_id;
	@Column(length = 20)
	@NotNull
	private String country;
	@Column(length = 20)
	@NotNull
	private String city;
	@Column(length = 5)
	@NotNull
	private String education_level;
	@NotNull
	private int proficiency_level;
	@Column(length = 15)
	@NotNull
	private String signup_type;
	@NotNull
	private int report_count;
	private boolean is_feedback_given;
	@NotNull
	private Timestamp created_at;
	@NotNull
	private Timestamp modified_at;
	
}
