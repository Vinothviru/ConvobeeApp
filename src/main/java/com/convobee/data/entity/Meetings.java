package com.convobee.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.sun.istack.NotNull;

import lombok.Data;

@Entity
@Data
public class Meetings {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int meeting_id;
	@ManyToOne
	@JoinColumn(name = "slot_id")
	@NotNull
	private Slots slots;
	@ManyToOne
	@JoinColumn(name = "user_a_id")
	@NotNull
	private Users user_a_id;
	@ManyToOne
	@JoinColumn(name = "user_b_id")
	@NotNull
	private Users user_b_id;
	@Column(length = 255)
	@NotNull
	private String meeting_url;
	@NotNull
	private boolean meeting_status;
	@NotNull
	private Timestamp started_at;
	private Timestamp ended_at;
	
}
