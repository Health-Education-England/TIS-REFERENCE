package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssessmentTypeDto implements Serializable {

  private Long id;

  @NotNull
  private UUID uuid;

  @NotNull(groups = {Update.class,
      Create.class}, message = "code is needed for both create and update")
  private String code;

  @NotNull(groups = {Update.class,
      Create.class}, message = "label is needed for both create and update")
  private String label;

  private Status status;
}
