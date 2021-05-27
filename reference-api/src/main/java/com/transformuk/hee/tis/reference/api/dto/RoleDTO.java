package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the Role entity.
 */
@Data
public class RoleDTO implements Serializable {

  private static final long serialVersionUID = -8162014454856728253L;

  private Long id;

  private UUID uuid;

  @NotNull
  private String code;
  @NotNull
  private String label;
  private Status status;
  private RoleCategoryDTO roleCategory;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RoleDTO roleDTO = (RoleDTO) o;

    return Objects.equals(id, roleDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
