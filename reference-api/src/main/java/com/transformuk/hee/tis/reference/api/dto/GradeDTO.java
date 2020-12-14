package com.transformuk.hee.tis.reference.api.dto;


import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * A DTO for the Grade entity.
 */
public class GradeDTO implements Serializable {

  @NotNull(groups = Update.class, message = "id must not be null during update")
  @Null(groups = Create.class, message = "id must be null during create")
  private Long id;

  @NotNull
  private UUID uuid;

  private String abbreviation;

  private String name;

  private String label;

  private boolean trainingGrade;

  private boolean postGrade;

  private boolean placementGrade;

  private Status status;

  private String intrepidId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UUID getUuid() { return uuid; }

  public void setUuid(UUID uuid) { this.uuid = uuid; }

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

  public void setPlacementGrade(Boolean placementGrade) {
    this.placementGrade = placementGrade;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
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
        "id=" + id +
        ", uuid='" + uuid.toString() + '\'' +
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
