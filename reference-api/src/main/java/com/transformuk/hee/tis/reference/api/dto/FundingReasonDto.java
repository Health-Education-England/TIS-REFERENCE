package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the FundingReason entity.
 */
@Data
public class FundingReasonDto implements Serializable {

  private UUID id;

  @NotNull
  private String reason;

  private Status status;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FundingReasonDto fundingReasonDto = (FundingReasonDto) o;

    return Objects.equals(id, fundingReasonDto.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
