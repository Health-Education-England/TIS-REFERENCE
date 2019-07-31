package com.transformuk.hee.tis.reference.service.model;


import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * An Assessment type.
 */
@Entity
public class AssessmentType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @NotNull(groups = {Update.class,
      Create.class}, message = "code is needed for both create and update")
  private String code;

  @NotNull(groups = {Update.class,
      Create.class}, message = "label is needed for both create and update")
  private String label;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status")
  private Status status;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public AssessmentType name(String label) {
    this.label = label;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public AssessmentType status(Status status) {
    this.status = status;
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
    AssessmentType assessmentType = (AssessmentType) o;
    if (assessmentType.code == null || code == null) {
      return false;
    }
    return Objects.equals(code, assessmentType.code);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(code);
  }

  @Override
  public String toString() {
    return "AssessmentType{" +
        "code='" + code + '\'' +
        ", label='" + label + '\'' +
        ", status=" + status +
        '}';
  }
}
