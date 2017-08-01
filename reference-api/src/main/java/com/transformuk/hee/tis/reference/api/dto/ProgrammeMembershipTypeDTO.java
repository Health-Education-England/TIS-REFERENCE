package com.transformuk.hee.tis.reference.api.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ProgrammeMembershipType entity.
 */
public class ProgrammeMembershipTypeDTO implements Serializable {

  private Long id;

  @NotNull
  private String code;

  @NotNull
  private String label;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ProgrammeMembershipTypeDTO programmeMembershipTypeDTO = (ProgrammeMembershipTypeDTO) o;

    if (!Objects.equals(id, programmeMembershipTypeDTO.id)) {
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
    return "ProgrammeMembershipTypeDTO{" +
        "id=" + id +
        ", code='" + code + "'" +
        ", label='" + label + "'" +
        '}';
  }
}
