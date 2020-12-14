package com.transformuk.hee.tis.reference.api.dto;


import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the Nationality entity.
 */
public class NationalityDTO implements Serializable {

  private Long id;

  private UUID uuid;

  @NotNull
  private String countryNumber;

  @NotNull
  private String nationality;

  private Status status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UUID getUuid() { return uuid; }

  public void setUuid(UUID uuid) { this.uuid = uuid; }

  public String getCountryNumber() {
    return countryNumber;
  }

  public void setCountryNumber(String countryNumber) {
    this.countryNumber = countryNumber;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
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

    NationalityDTO nationalityDTO = (NationalityDTO) o;

    if (!Objects.equals(id, nationalityDTO.id)) {
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
    return "NationalityDTO{" +
        "id=" + id +
        ", uuid='" + uuid.toString() + '\'' +
        ", countryNumber='" + countryNumber + '\'' +
        ", nationality='" + nationality + '\'' +
        ", status=" + status +
        '}';
  }
}
