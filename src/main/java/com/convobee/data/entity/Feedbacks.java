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

import lombok.Data;

@Entity
@Data
public class Feedbacks {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int feedback_id;
	@OneToOne
	@JoinColumn(name = "meeting_id")
	private Meetings meetings;
	@ManyToOne
	@JoinColumn(name = "provider_user_id")
	private Users provider_user_id;
	@ManyToOne
	@JoinColumn(name = "receiver_user_id")
	private Users receiver_user_id;
	private float impression_level;
	private float confidence_level;
	private float proficiency_level;
	@Column(length = 255)
	private String appreciate_feedback;
	@Column(length = 255)
	private String advise_feedback; 
	private Timestamp created_at;
}
