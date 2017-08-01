package com.transformuk.hee.tis.reference.api.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Country entity.
 */
public class CountryDTO implements Serializable {

  private Long id;

  @NotNull
  private String countryNumber;

  @NotNull
  private String nationality;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CountryDTO countryDTO = (CountryDTO) o;

    if (!Objects.equals(id, countryDTO.id)) {
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
    return "CountryDTO{" +
        "id=" + id +
        ", countryNumber='" + countryNumber + "'" +
        ", nationality='" + nationality + "'" +
        '}';
  }
}
