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
 * A Nationality.
 */
@Entity
public class Nationality implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "uuid")
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

  public UUID getUuid() { return uuid; }

  public void setUuid(UUID uuid) { this.uuid = uuid; }

  public String getCountryNumber() {
    return countryNumber;
  }

  public void setCountryNumber(String countryNumber) {
    this.countryNumber = countryNumber;
  }

  public Nationality countryNumber(String countryNumber) {
    this.countryNumber = countryNumber;
    return this;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  public Nationality nationality(String nationality) {
    this.nationality = nationality;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Nationality status(Status status) {
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
    Nationality nationality = (Nationality) o;
    if (nationality.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, nationality.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Nationality{" +
        "id=" + id +
        ", countryNumber='" + countryNumber + '\'' +
        ", nationality='" + nationality + '\'' +
        ", status=" + status +
        '}';
  }
}
