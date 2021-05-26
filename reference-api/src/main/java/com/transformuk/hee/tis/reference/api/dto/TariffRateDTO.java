package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.enums.Status;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the TariffRate entity.
 */
@Data
public class TariffRateDTO implements Serializable {

  private Long id;

  @NotNull
  private String code;

  @ApiModelProperty("The grade id that this tariff is linked to")
  private Long gradeId;

  private String gradeAbbreviation;

  private String tariffRate;

  private String tariffRateFringe;

  private String tariffRateLondon;

  private Status status;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TariffRateDTO tariffRateDTO = (TariffRateDTO) o;

    return Objects.equals(id, tariffRateDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
