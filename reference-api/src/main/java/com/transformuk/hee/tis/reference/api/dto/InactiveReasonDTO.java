package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the InactiveReason entity.
 */
@Data
public class InactiveReasonDTO implements Serializable {

  private Long id;

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

    InactiveReasonDTO inactiveReasonDTO = (InactiveReasonDTO) o;

    return Objects.equals(id, inactiveReasonDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
