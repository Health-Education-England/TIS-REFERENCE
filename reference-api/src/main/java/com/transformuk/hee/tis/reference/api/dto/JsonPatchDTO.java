package com.transformuk.hee.tis.reference.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the RebaseJson entity.
 */
@Data
public class JsonPatchDTO implements Serializable {

  private Long id;

  @NotNull
  private String tableDtoName;

  private String patchId;

  private String patch;

  private Date dateAdded;

  private Boolean enabled;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    JsonPatchDTO countryDTO = (JsonPatchDTO) o;

    return Objects.equals(id, countryDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
