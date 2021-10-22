package com.convobee.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.transaction.annotation.Transactional;

import lombok.Data;

@Entity
@Data
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private int userid;
	@NotNull
	@Column(name = "user_name", length = 20)
	private String username;
	@Column(length = 20)
	@NotNull
	private String role;
	@Column(name = "nick_name", length = 10)
	@NotNull
	private String nickname;
	@Column(length = 15)
	@NotNull
	private String password;
	@Column(name = "mail_id", length = 30, unique = true)
	@NotNull
	private String mailid;
	@Column(length = 20)
	@NotNull
	private String country;
	@Column(length = 20)
	@NotNull
	private String city;
	@Column(name = "education_level", length = 5)
	@NotNull
	private String educationlevel;
	@Column(name = "proficiency_level")
	@NotNull
	private int proficiencylevel;
	@Column(name = "signup_type", length = 15)
	@NotNull
	private String signuptype;
	@Column(name = "report_count")
	@NotNull
	private int reportcount;
	@Column(name = "is_feedback_given")
	@NotNull
	private boolean isfeedbackgiven;
	@Column(name = "is_user_banned")
	@NotNull
	private boolean isuserbanned;
	@Column(name = "created_at")
	@NotNull
	private Timestamp createdat;
	@Column(name = "modified_at")
	@NotNull
	private Timestamp modifiedat;
	
}
