package com.convobee.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

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
	@Column(columnDefinition = "boolean default false")
	private boolean art;
	@Column(columnDefinition = "boolean default false")
	private boolean blogging;
	@Column(columnDefinition = "boolean default false")
	private boolean acting;
	@Column(columnDefinition = "boolean default false")
	private boolean sports;
	@Column(columnDefinition = "boolean default false")
	private boolean gaming;
	@Column(columnDefinition = "boolean default false")
	private boolean traveling;
	@Column(name = "pet_care", columnDefinition = "boolean default false")
	private boolean petcare;
	@Column(columnDefinition = "boolean default false")
	private boolean music;
	@Column(columnDefinition = "boolean default false")
	private boolean cooking;
	@Column(name = "reading_books", columnDefinition = "boolean default false")
	private boolean readingbooks;
	@Column(columnDefinition = "boolean default false")
	private boolean dance;
	@Column(columnDefinition = "boolean default false")
	private boolean technology;
	@NotNull
	private Timestamp createdat;
	@NotNull
	private Timestamp modifiedat;
	
}
