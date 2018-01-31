package com.transformuk.hee.tis.reference.service.model;


import com.transformuk.hee.tis.reference.api.enums.Status;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TariffRate.
 */
@Entity
public class TariffRate implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "gradeId")
  private Long gradeId;

  private String gradeAbbreviation;

  private String tariffRate;

  private String tariffRateFringe;

  private String tariffRateLondon;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status")
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

  public TariffRate code(String code) {
    this.code = code;
    return this;
  }

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(Long gradeId) {
    this.gradeId = gradeId;
  }

  public TariffRate gradeId(Long gradeId) {
    this.gradeId = gradeId;
    return this;
  }

  public String getGradeAbbreviation() {
    return gradeAbbreviation;
  }

  public void setGradeAbbreviation(String gradeAbbreviation) {
    this.gradeAbbreviation = gradeAbbreviation;
  }

  public TariffRate gradeAbbreviation(String gradeAbbreviation) {
    this.gradeAbbreviation = gradeAbbreviation;
    return this;
  }

  public String getTariffRate() {
    return tariffRate;
  }

  public void setTariffRate(String tariffRate) {
    this.tariffRate = tariffRate;
  }

  public TariffRate tariffRate(String tariffRate) {
    this.tariffRate = tariffRate;
    return this;
  }

  public String getTariffRateFringe() {
    return tariffRateFringe;
  }

  public void setTariffRateFringe(String tariffRateFringe) {
    this.tariffRateFringe = tariffRateFringe;
  }

  public TariffRate tariffRateFringe(String tariffRateFringe) {
    this.tariffRateFringe = tariffRateFringe;
    return this;
  }

  public String getTariffRateLondon() {
    return tariffRateLondon;
  }

  public void setTariffRateLondon(String tariffRateLondon) {
    this.tariffRateLondon = tariffRateLondon;
  }

  public TariffRate tariffRateLondon(String tariffRateLondon) {
    this.tariffRateLondon = tariffRateLondon;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public TariffRate status(Status status) {
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
    TariffRate tariffRate = (TariffRate) o;
    if (tariffRate.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, tariffRate.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "TariffRate{" +
        "id=" + id +
        ", code='" + code + '\'' +
        ", gradeAbbreviation='" + gradeAbbreviation + '\'' +
        ", tariffRate='" + tariffRate + '\'' +
        ", tariffRateFringe='" + tariffRateFringe + '\'' +
        ", tariffRateLondon='" + tariffRateLondon + '\'' +
        ", status=" + status +
        '}';
  }
}
