package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;

/**
 * A DTO for the LocalOfficeContactType entity.
 */
@Data
public class LocalOfficeContactTypeDto implements Serializable {

  @NotNull(groups = Update.class, message = "id must not be null during update")
  @Null(groups = Create.class, message = "id must be null during create")
  private UUID id;

  @NotNull(message = "Code is required", groups = {Update.class, Create.class})
  private String code;

  @NotNull(message = "Label is required", groups = {Update.class, Create.class})
  private String label;

  private Status status;
}
