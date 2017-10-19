package com.transformuk.hee.tis.reference.api.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Grade entity.
 */
public class GradeDTO implements Serializable {

  @NotNull
  private String abbreviation;

  private String name;

  private String label;

  @NotNull
  private Boolean trainingGrade;

  @NotNull
  private Boolean postGrade;

  @NotNull
  private Boolean placementGrade;

  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Boolean getTrainingGrade() {
    return trainingGrade;
  }

  public void setTrainingGrade(Boolean trainingGrade) {
    this.trainingGrade = trainingGrade;
  }

  public Boolean getPostGrade() {
    return postGrade;
  }

  public void setPostGrade(Boolean postGrade) {
    this.postGrade = postGrade;
  }

  public Boolean getPlacementGrade() {
    return placementGrade;
  }

  public void setPlacementGrade(Boolean placementGrade) {
    this.placementGrade = placementGrade;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    GradeDTO gradeDTO = (GradeDTO) o;

    if (!Objects.equals(abbreviation, gradeDTO.abbreviation)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(abbreviation);
  }

  @Override
  public String toString() {
    return "GradeDTO{" +
        "abbreviation='" + abbreviation + "'" +
        ", name='" + name + "'" +
        ", label='" + label + "'" +
        ", trainingGrade='" + trainingGrade + "'" +
        ", postGrade='" + postGrade + "'" +
        ", placementGrade='" + placementGrade + "'" +
        '}';
  }
}
