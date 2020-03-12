package com.transformuk.hee.tis.reference.api.dto;


import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;

/**
 * A DTO for the Site entity.
 */
@Data
public class SiteDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id cannot be null during update")
  @Null(groups = Create.class, message = "Id must be null in order to create")
  private Long id;

  private String siteCode;

  private LocalDate startDate;

  private LocalDate endDate;

  private String localOffice;

  private String trustCode;

  private Long trustId;

  private String siteName;

  private String address;

  private String postCode;

  private String siteKnownAs;

  private String siteNumber;

  private String organisationalUnit;

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

    SiteDTO siteDTO = (SiteDTO) o;

    return Objects.equals(siteCode, siteDTO.siteCode);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(siteCode);
  }
}
