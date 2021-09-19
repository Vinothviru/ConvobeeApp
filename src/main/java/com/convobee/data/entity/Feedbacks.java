package com.convobee.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.sun.istack.NotNull;

import lombok.Data;

@Entity
@Data
public class Feedbacks {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int feedback_id;
	@OneToOne
	@JoinColumn(name = "meeting_id")
	@NotNull
	private Meetings meetings;
	@ManyToOne
	@JoinColumn(name = "provider_user_id")
	@NotNull
	private Users provider_user_id;
	@ManyToOne
	@JoinColumn(name = "receiver_user_id")
	@NotNull
	private Users receiver_user_id;
	@NotNull
	private float impression_level;
	@NotNull
	private float confidence_level;
	@NotNull
	private float proficiency_level;
	@Column(length = 255)
	@NotNull
	private String appreciate_feedback;
	@Column(length = 255)
	@NotNull
	private String advise_feedback; 
	@NotNull
	private Timestamp created_at;
}
