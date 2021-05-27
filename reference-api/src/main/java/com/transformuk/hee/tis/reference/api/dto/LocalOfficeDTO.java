package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the LocalOffice entity.
 */
@Data
public class LocalOfficeDTO implements Serializable {

  private Long id;

  private UUID uuid;

  @NotNull
  private String abbreviation;

  @NotNull
  private String name;

  @NotNull
  private String postAbbreviation;

  private Status status;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    LocalOfficeDTO localOfficeDTO = (LocalOfficeDTO) o;

    return Objects.equals(id, localOfficeDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
