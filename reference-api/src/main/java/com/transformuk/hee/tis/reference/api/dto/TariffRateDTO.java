package com.transformuk.hee.tis.reference.api.dto;


import com.transformuk.hee.tis.reference.api.enums.Status;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TariffRate entity.
 */
public class TariffRateDTO implements Serializable {

  private Long id;

  @NotNull
  private String code;

  private Long gradeId;

  private String gradeAbbreviation;

  private String tariffRate;

  private String tariffRateFringe;

  private String tariffRateLondon;

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

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(Long gradeId) {
    this.gradeId = gradeId;
  }

  public String getGradeAbbreviation() {
    return gradeAbbreviation;
  }

  public void setGradeAbbreviation(String gradeAbbreviation) {
    this.gradeAbbreviation = gradeAbbreviation;
  }

  public String getTariffRate() {
    return tariffRate;
  }

  public void setTariffRate(String tariffRate) {
    this.tariffRate = tariffRate;
  }

  public String getTariffRateFringe() {
    return tariffRateFringe;
  }

  public void setTariffRateFringe(String tariffRateFringe) {
    this.tariffRateFringe = tariffRateFringe;
  }

  public String getTariffRateLondon() {
    return tariffRateLondon;
  }

  public void setTariffRateLondon(String tariffRateLondon) {
    this.tariffRateLondon = tariffRateLondon;
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

    TariffRateDTO tariffRateDTO = (TariffRateDTO) o;

    if (!Objects.equals(id, tariffRateDTO.id)) {
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
    return "TariffRateDTO{" +
        "id=" + id +
        ", code='" + code + '\'' +
        ", gradeId=" + gradeId +
        ", gradeAbbreviation='" + gradeAbbreviation + '\'' +
        ", tariffRate='" + tariffRate + '\'' +
        ", tariffRateFringe='" + tariffRateFringe + '\'' +
        ", tariffRateLondon='" + tariffRateLondon + '\'' +
        ", status=" + status +
        '}';
  }
}
