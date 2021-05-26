package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the Gender entity.
 */
@Data
public class GenderDTO implements Serializable {

  private Long id;

  private UUID uuid;

  @NotNull
  private String code;

  @NotNull
  private String label;

  private Status status;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    GenderDTO genderDTO = (GenderDTO) o;

    return Objects.equals(id, genderDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
