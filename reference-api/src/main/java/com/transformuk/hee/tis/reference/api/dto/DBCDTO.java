package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the DBC entity.
 */
@Data
@NoArgsConstructor
public class DBCDTO implements Serializable {

  private Long id;

  private UUID uuid;

  @NotNull
  private String dbc;

  @NotNull
  private String name;

  @NotNull
  private String abbr;

  private Status status;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DBCDTO dBCDTO = (DBCDTO) o;

    return Objects.equals(id, dBCDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
