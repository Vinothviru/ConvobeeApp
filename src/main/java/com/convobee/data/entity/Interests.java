package com.convobee.data.entity;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Interests {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "interests_id")
	private long interestsid;
	@ManyToOne
	@JoinColumn(name = "user_id")
	@NotNull
	private Users user;
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "interest_names")
	private InterestsNames interest;
	@NotNull
	private Timestamp createdat;
	@NotNull
	private Timestamp modifiedat;
	
}
