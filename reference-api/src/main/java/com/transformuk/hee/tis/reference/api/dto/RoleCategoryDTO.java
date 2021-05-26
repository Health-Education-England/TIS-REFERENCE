package com.transformuk.hee.tis.reference.api.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the RoleCategory entity.
 */
@Data
public class RoleCategoryDTO implements Serializable {

  private static final long serialVersionUID = -8918107034703932684L;

  private Long id;
  @NotNull
  private String name;

  private UUID uuid;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RoleCategoryDTO roleDTO = (RoleCategoryDTO) o;

    return Objects.equals(id, roleDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
