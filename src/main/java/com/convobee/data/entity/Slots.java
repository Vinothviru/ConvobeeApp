package com.convobee.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Slots {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "slot_id")
	private int slotid;
	@Column(name = "slot_time")
	@NotNull
	private Timestamp slottime;
	@Column(name = "slot_url", length = 255)
	@NotNull
	private String sloturl;
	@Column(name = "created_at") 
	@NotNull
	private Timestamp createdat;
	
}
