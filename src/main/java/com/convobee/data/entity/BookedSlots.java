package com.convobee.data.entity;

import java.sql.Timestamp;

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
public class BookedSlots {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int booked_slot_id;
	@ManyToOne
	@JoinColumn(name = "user_id")
	@NotNull
	private Users users;
	@ManyToOne
	@JoinColumn(name = "slot_id")
	@NotNull
	private Slots slots;
	@NotNull
	private Timestamp created_at;
	@NotNull
	private Timestamp modified_at;
	
}
