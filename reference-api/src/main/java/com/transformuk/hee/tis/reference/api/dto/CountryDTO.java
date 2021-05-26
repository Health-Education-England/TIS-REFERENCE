package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the Country entity.
 */
@Data
public class CountryDTO implements Serializable {

  private Long id;

  private UUID uuid;

  @NotNull
  private String countryNumber;

  @NotNull
  private String nationality;

  private Status status;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CountryDTO countryDTO = (CountryDTO) o;

    return Objects.equals(id, countryDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
