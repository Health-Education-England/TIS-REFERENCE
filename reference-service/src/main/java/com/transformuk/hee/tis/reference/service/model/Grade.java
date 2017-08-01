package com.transformuk.hee.tis.reference.service.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Grade.
 */
@Entity
public class Grade implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "abbreviation", nullable = false)
  private String abbreviation;

  @Column(name = "name")
  private String name;

  @Column(name = "label")
  private String label;

  @NotNull
  @Column(name = "trainingGrade", nullable = false)
  private Boolean trainingGrade;

  @NotNull
  @Column(name = "postGrade", nullable = false)
  private Boolean postGrade;

  @NotNull
  @Column(name = "placementGrade", nullable = false)
  private Boolean placementGrade;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  public Grade abbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Grade name(String name) {
    this.name = name;
    return this;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Grade label(String label) {
    this.label = label;
    return this;
  }

  public Boolean isTrainingGrade() {
    return trainingGrade;
  }

  public Grade trainingGrade(Boolean trainingGrade) {
    this.trainingGrade = trainingGrade;
    return this;
  }

  public void setTrainingGrade(Boolean trainingGrade) {
    this.trainingGrade = trainingGrade;
  }

  public Boolean isPostGrade() {
    return postGrade;
  }

  public Grade postGrade(Boolean postGrade) {
    this.postGrade = postGrade;
    return this;
  }

  public void setPostGrade(Boolean postGrade) {
    this.postGrade = postGrade;
  }

  public Boolean isPlacementGrade() {
    return placementGrade;
  }

  public Grade placementGrade(Boolean placementGrade) {
    this.placementGrade = placementGrade;
    return this;
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
    Grade grade = (Grade) o;
    if (grade.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, grade.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Grade{" +
        "id=" + id +
        ", abbreviation='" + abbreviation + "'" +
        ", name='" + name + "'" +
        ", label='" + label + "'" +
        ", trainingGrade='" + trainingGrade + "'" +
        ", postGrade='" + postGrade + "'" +
        ", placementGrade='" + placementGrade + "'" +
        '}';
  }
}
