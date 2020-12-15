package com.transformuk.hee.tis.reference.service.model;


import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

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
  @Column(name = "uuid", nullable = false)
  private UUID uuid;

  @Column(name = "abbreviation")
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

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status")
  private Status status;

  @Column(name = "intrepidId")
  private String intrepidId;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Grade id(Long id) {
    this.id = id;
    return this;
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

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Grade status(Status status) {
    this.status = status;
    return this;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public Grade intrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
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
    if (grade.abbreviation == null || abbreviation == null) {
      return false;
    }
    return Objects.equals(abbreviation, grade.abbreviation);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(abbreviation);
  }

  @Override
  public String toString() {
    return "Grade{" +
        "id=" + id +
        ", abbreviation='" + abbreviation + '\'' +
        ", name='" + name + '\'' +
        ", label='" + label + '\'' +
        ", trainingGrade=" + trainingGrade +
        ", postGrade=" + postGrade +
        ", placementGrade=" + placementGrade +
        ", status=" + status +
        ", intrepidId='" + intrepidId + '\'' +
        '}';
  }
}
