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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public String getDbc() {
    return dbc;
  }

  public void setDbc(String dbc) {
    this.dbc = dbc;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAbbr() {
    return abbr;
  }

  public void setAbbr(String abbr) {
    this.abbr = abbr;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

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
