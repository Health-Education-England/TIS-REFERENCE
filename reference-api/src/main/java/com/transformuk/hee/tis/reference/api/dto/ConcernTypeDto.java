package com.transformuk.hee.tis.reference.api.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConcernTypeDto {

  private Long id;

  @NotNull
  private String code;

  @NotNull
  private String label;
}
