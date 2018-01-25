package com.transformuk.hee.tis.reference.api.dto;


import com.transformuk.hee.tis.reference.api.enums.Status;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the LocalOffice entity.
 */
public class LocalOfficeDTO implements Serializable {

  private Long id;

  @NotNull
  private String abbreviation;

  @NotNull
  private String name;

  private Status status;

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

    LocalOfficeDTO localOfficeDTO = (LocalOfficeDTO) o;

    if (!Objects.equals(id, localOfficeDTO.id)) {
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
    return "LocalOfficeDTO{" +
        "id=" + id +
        ", abbreviation='" + abbreviation + '\'' +
        ", name='" + name + '\'' +
        ", status=" + status +
        '}';
  }
}
