package com.transformuk.hee.tis.reference.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A trainee doctor's current education grade
 *
 */
@ApiModel(description = "Grade data")
@Entity
@Table(name = "Grade")
public class Grade {
	private String label;
	@Id
	private String abbreviation;
	private boolean trainingGrade;
	private boolean postGrade;
	private boolean placementGrade;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public boolean isTrainingGrade() {
		return trainingGrade;
	}

	public void setTrainingGrade(boolean trainingGrade) {
		this.trainingGrade = trainingGrade;
	}

	public boolean isPostGrade() {
		return postGrade;
	}

	public void setPostGrade(boolean postGrade) {
		this.postGrade = postGrade;
	}

	public boolean isPlacementGrade() {
		return placementGrade;
	}

	public void setPlacementGrade(boolean placementGrade) {
		this.placementGrade = placementGrade;
	}
}
