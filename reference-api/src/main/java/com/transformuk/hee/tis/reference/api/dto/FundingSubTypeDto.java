package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the FundingSubType entity.
 */
@Data
public class FundingSubTypeDto implements Serializable {

  private UUID uuid;

  @NotNull
  private String code;

  @NotNull
  private String label;

  private Status status;

  private FundingTypeDTO fundingTypeDto;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FundingSubTypeDto fundingSubTypeDto = (FundingSubTypeDto) o;

    return Objects.equals(uuid, fundingSubTypeDto.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(uuid);
  }
}
