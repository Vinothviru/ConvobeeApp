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
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class FeedbacksToUs {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "feedback_to_us_id")
	private int feedbacktousid;
	@OneToOne
	@JoinColumn(name = "meeting_id")
	@NotNull
	private Meetings meetings; 
	@ManyToOne
	@JoinColumn(name = "provider_user_id")
	@NotNull
	private Users provideruser; 
	@Column(name = "report_user")
	private boolean reportuser;
	@ManyToOne
	@JoinColumn(name = "reportee_user_id")
	@NotNull
	private Users reporteeuser; 
	@Column(name = "report_type", length = 20)
	private String reporttype;
	@Column(name = "report_description", length = 255)
	private String reportdescription;
	@Column(name = "feedback_to_us", length = 255)
	@NotNull
	private String feedbacktous;
	@Column(name = "created_at")
	@NotNull
	private Timestamp createdat;
	
}
