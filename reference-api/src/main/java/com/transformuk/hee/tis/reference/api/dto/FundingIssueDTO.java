package com.transformuk.hee.tis.reference.api.dto;

import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the FundingIssue entity.
 */
@Data
public class FundingIssueDTO implements Serializable {

  private Long id;

  @NotNull
  private String code;

  private Status status;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FundingIssueDTO fundingIssueDTO = (FundingIssueDTO) o;

    return Objects.equals(id, fundingIssueDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
