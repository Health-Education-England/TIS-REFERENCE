package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;

/**
 * A DTO for the LocalOfficeContact entity.
 */
@Data
public class LocalOfficeContactDto {

  @NotNull(groups = Update.class, message = "id must not be null during update")
  @Null(groups = Create.class, message = "id must be null during create")
  private UUID id;

  @NotNull(message = "Local Office is required", groups = {Update.class, Create.class})
  private LocalOfficeDTO localOffice;

  @NotNull(message = "Contact Type is required", groups = {Update.class, Create.class})
  private LocalOfficeContactTypeDto contactType;

  private String contact;
}
