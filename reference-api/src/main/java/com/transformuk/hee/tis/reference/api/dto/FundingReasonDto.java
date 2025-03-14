package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
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

}
