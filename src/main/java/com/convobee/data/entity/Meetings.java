package com.convobee.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Meetings {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "meeting_id")
	private int meetingid;
	@ManyToOne
	@JoinColumn(name = "slot_id")
	@NotNull
	private Slots slots;
	@ManyToOne
	@JoinColumn(name = "user_a_id")
	@NotNull
	private Users useraid;
	@ManyToOne
	@JoinColumn(name = "user_b_id")
	@NotNull
	private Users userbid;
	@Column(name = "meeting_url", length = 255)
	@NotNull
	private String meetingurl;
	@Column(name = "meeting_status")
	@NotNull
	private String meetingstatus;
	@Column(name  = "started_at")
	@NotNull
	private Timestamp startedat;
	@Column(name  = "ended_at")
	private Timestamp endedat;
	
}
