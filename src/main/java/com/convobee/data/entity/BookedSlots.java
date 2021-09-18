package com.convobee.data.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class BookedSlots {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int booked_slot_id;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users users;
	@ManyToOne
	@JoinColumn(name = "slot_id")
	private Slots slots;
	private Timestamp created_at;
	private Timestamp modified_at;
	
}
