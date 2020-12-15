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
 * A Country.
 */
@Entity
public class Country implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "uuid", nullable = false)
  private UUID uuid;

  @NotNull
  @Column(name = "countryNumber", nullable = false)
  private String countryNumber;

  @NotNull
  @Column(name = "nationality", nullable = false)
  private String nationality;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status")
  private Status status;

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

  public Country countryNumber(String countryNumber) {
    this.countryNumber = countryNumber;
    return this;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  public Country nationality(String nationality) {
    this.nationality = nationality;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Country status(Status status) {
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
    Country country = (Country) o;
    if (country.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, country.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Country{" +
        "id=" + id +
        ", countryNumber='" + countryNumber + '\'' +
        ", nationality='" + nationality + '\'' +
        ", status=" + status +
        '}';
  }
}
