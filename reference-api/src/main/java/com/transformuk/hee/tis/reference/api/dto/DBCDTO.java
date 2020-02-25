package com.transformuk.hee.tis.reference.api.dto;


import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the DBC entity.
 */
public class DBCDTO implements Serializable {

  private Long id;

  @NotNull
  private String dbc;

  @NotNull
  private String name;

  @NotNull
  private String abbr;

  private Status status;

  @NotNull
  private String entity;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getEntity() { return entity; }

  public void setEntity(String entity){ this.entity = entity; }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DBCDTO dBCDTO = (DBCDTO) o;

    if (!Objects.equals(id, dBCDTO.id)) {
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
    return "DBCDTO{" +
        "id=" + id +
        ", dbc='" + dbc + '\'' +
        ", name='" + name + '\'' +
        ", abbr='" + abbr + '\'' +
        ", status=" + status +
        '}';
  }
}
