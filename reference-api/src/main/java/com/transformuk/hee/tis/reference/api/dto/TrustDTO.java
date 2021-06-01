package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the Trust entity.
 */
@Data
public class TrustDTO implements Serializable {

  private Long id;

  private String code;

  private String localOffice;

  private Status status;

  private String trustKnownAs;

  private String trustName;

  private String trustNumber;

  private String address;

  private String postCode;

  private OrganizationTypeDto organizationType;

  private String intrepidId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TrustDTO trustDTO = (TrustDTO) o;

    return Objects.equals(code, trustDTO.code);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(code);
  }
}
