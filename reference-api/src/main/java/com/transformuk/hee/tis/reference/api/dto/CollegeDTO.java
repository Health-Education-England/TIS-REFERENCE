package com.transformuk.hee.tis.reference.api.dto;


import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the College entity.
 */
public class CollegeDTO implements Serializable {

  private Long id;

  @NotNull
  private UUID uuid;

  @NotNull
  private String abbreviation;

  private String name;

  private Status status;

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

    CollegeDTO collegeDTO = (CollegeDTO) o;

    if (!Objects.equals(id, collegeDTO.id)) {
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
    return "CollegeDTO{" +
        "id=" + id +
        ", uuid='" + uuid.toString() + '\'' +
        ", abbreviation='" + abbreviation + '\'' +
        ", name='" + name + '\'' +
        ", status=" + status +
        '}';
  }
}
