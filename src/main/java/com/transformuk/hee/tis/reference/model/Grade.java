package com.transformuk.hee.tis.reference.model;

/**
 * A trainee doctor's current education grade
 *
 */
public class Grade {

	private String label;
	private String abbreviation;
	private boolean trainingGrade;
	private boolean postGrade;
	private boolean placementGrad;

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

	public boolean isPlacementGrad() {
		return placementGrad;
	}

	public void setPlacementGrad(boolean placementGrad) {
		this.placementGrad = placementGrad;
	}
}
