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
public class Feedbacks {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "feedback_id")
	private int feedbackid;
	@OneToOne
	@JoinColumn(name = "meeting_id")
	@NotNull
	private Meetings meetings;
	@ManyToOne
	@JoinColumn(name = "provider_user_id")
	@NotNull
	private Users provideruser;
	@ManyToOne
	@JoinColumn(name = "receiver_user_id")
	@NotNull
	private Users receiveruser;
	@Column(name = "impression_level")
	@NotNull
	private float impressionlevel;
	@Column(name = "confidence_level")
	@NotNull
	private float confidencelevel;
	@Column(name = "proficiency_level")
	@NotNull
	private float proficiencylevel;
	@Column(name = "appreciate_feedback", length = 255)
	@NotNull
	private String appreciatefeedback;
	@Column(name = "advise_feedback", length = 255)
	@NotNull
	private String advisefeedback; 
	@Column(name = "created_at")
	@NotNull
	private Timestamp createdat;
}
