package com.convobee.data.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.sun.istack.NotNull;

import lombok.Data;

@Entity
@Data
public class Interests {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int interest_id;
	@OneToOne
	@JoinColumn(name = "user_id")
	@NotNull
	private Users user;
	private boolean art;
	private boolean blogging;
	private boolean acting;
	private boolean sports;
	private boolean gaming;
	private boolean traveling;
	private boolean pet_care;
	private boolean music;
	private boolean cooking;
	private boolean reading_books;
	private boolean dance;
	private boolean technology;
	@NotNull
	private Timestamp created_at;
	@NotNull
	private Timestamp modified_at;
	
}
