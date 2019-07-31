package com.transformuk.hee.tis.reference.api.dto;


import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * A DTO for the Rotation entity.
 */
public class RotationDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a rotation")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new rotation")
  private Long id;

  @NotNull(message = "Code is required", groups = {Update.class, Create.class})
  private String code;

  @NotNull(message = "Label is required", groups = {Update.class, Create.class})
  private String label;

  @NotNull(message = "LocalOffice is required", groups = {Update.class, Create.class})
  private String localOffice;

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

  public String getLocalOffice() {
    return localOffice;
  }

  public void setLocalOffice(String localOffice) {
    this.localOffice = localOffice;
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

    RotationDTO rotationDTO = (RotationDTO) o;
    if (rotationDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), rotationDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "RotationDTO{" +
        "id=" + id +
        ", code='" + code + '\'' +
        ", label='" + label + '\'' +
        ", localOffice='" + localOffice + '\'' +
        ", status=" + status +
        '}';
  }
}
