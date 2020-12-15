package com.transformuk.hee.tis.reference.api.dto;


import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the CurriculumSubType entity.
 */
public class CurriculumSubTypeDTO implements Serializable {

  private Long id;

  @NotNull
  private UUID uuid;

  @NotNull
  private String code;

  @NotNull
  private String label;

  private Status status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CurriculumSubTypeDTO curriculumSubTypeDTO = (CurriculumSubTypeDTO) o;

    if (!Objects.equals(id, curriculumSubTypeDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "CurriculumSubTypeDTO{" +
        "id=" + id +
        ", code='" + code + '\'' +
        ", label='" + label + '\'' +
        ", status=" + status +
        '}';
  }
}
