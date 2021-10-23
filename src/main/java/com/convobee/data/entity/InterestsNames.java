package com.convobee.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class InterestsNames {
	@Id
	@Column(name = "interest_names")
	private String interestnames;
}
