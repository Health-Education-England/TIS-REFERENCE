package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the LeavingReason entity.
 */
@Data
public class LeavingReasonDto implements Serializable {

  private Long id;

  @NotNull
  private String code;

  @NotNull
  private String label;

  private Status status;
}
