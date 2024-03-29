package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;

/**
 * A DTO for the Grade entity.
 */
@Data
public class GradeDTO implements Serializable {

  @NotNull(groups = Update.class, message = "id must not be null during update")
  @Null(groups = Create.class, message = "id must be null during create")
  private Long id;

  private UUID uuid;

  private String abbreviation;

  private String name;

  private String label;

  private boolean trainingGrade;

  private boolean postGrade;

  private boolean placementGrade;

  private Status status;

  private String intrepidId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    GradeDTO gradeDTO = (GradeDTO) o;

    return Objects.equals(abbreviation, gradeDTO.abbreviation);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(abbreviation);
  }
}
