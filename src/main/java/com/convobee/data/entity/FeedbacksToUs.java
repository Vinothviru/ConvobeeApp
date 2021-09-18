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
public class FeedbacksToUs {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int feedback_to_us_id;
	@OneToOne
	@JoinColumn(name = "meeting_id")
	private Meetings meetings; 
	@ManyToOne
	@JoinColumn(name = "provider_user_id")
	private Users provider_user_id; 
	private boolean report_user;
	@ManyToOne
	@JoinColumn(name = "reportee_user_id")
	private Users reportee_user_id; 
	@Column(length = 20)
	private String report_type;
	@Column(length = 255)
	private String report_description;
	@Column(length = 255)
	private String feedback_to_us;
	private Timestamp created_at;
	
}
