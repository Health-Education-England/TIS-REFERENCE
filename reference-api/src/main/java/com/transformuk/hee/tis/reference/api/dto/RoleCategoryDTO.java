package com.transformuk.hee.tis.reference.api.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleCategoryDTO implements Serializable {

  private static final long serialVersionUID = -8918107034703932684L;

  private Long id;
  @NotNull
  private String name;

  private String uuid;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RoleCategoryDTO roleDTO = (RoleCategoryDTO) o;

    if (!Objects.equals(id, roleDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "RoleCategoryDTO{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
