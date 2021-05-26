package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the TrainingNumberType entity.
 */
@Data
public class TrainingNumberTypeDTO implements Serializable {

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

    TrainingNumberTypeDTO trainingNumberTypeDTO = (TrainingNumberTypeDTO) o;

    return Objects.equals(id, trainingNumberTypeDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
