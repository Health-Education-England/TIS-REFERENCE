package com.transformuk.hee.tis.reference.api.dto;


import com.transformuk.hee.tis.reference.api.enums.Status;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the EthnicOrigin entity.
 */
public class EthnicOriginDTO implements Serializable {

  private Long id;

  @NotNull
  private String code;

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

    EthnicOriginDTO ethnicOriginDTO = (EthnicOriginDTO) o;

    if (!Objects.equals(id, ethnicOriginDTO.id)) {
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
    return "EthnicOriginDTO{" +
        "id=" + id +
        ", code='" + code + '\'' +
        ", status=" + status +
        '}';
  }
}
