package com.transformuk.hee.tis.reference.api.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConnectionSourceDto {

  private Long id;

  @NotNull
  private String name;
}
