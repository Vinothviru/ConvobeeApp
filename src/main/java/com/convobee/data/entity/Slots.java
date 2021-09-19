package com.convobee.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.sun.istack.NotNull;

import lombok.Data;

@Entity
@Data
public class Slots {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int slot_id;
	@NotNull
	private Timestamp slot_time;
	@Column(length = 255)
	@NotNull
	private String slot_url;
	@NotNull
	private Timestamp created_at;
	
}
