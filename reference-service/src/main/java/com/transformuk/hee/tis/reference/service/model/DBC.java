package com.transformuk.hee.tis.reference.service.model;


import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * A DBC (Designated Body Code).
 */
@Entity
public class DBC implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "dbc", nullable = false)
  private String dbc;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "abbr", nullable = false)
  private String abbr;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status")
  private Status status;

  @NotNull
  @Column(name = "entity", nullable = false)
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

  public DBC dbc(String dbc) {
    this.dbc = dbc;
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DBC name(String name) {
    this.name = name;
    return this;
  }

  public String getAbbr() {
    return abbr;
  }

  public void setAbbr(String abbr) {
    this.abbr = abbr;
  }

  public DBC abbr(String abbr) {
    this.abbr = abbr;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public DBC status(Status status) {
    this.status = status;
    return this;
  }

  public String getEntity() { return entity; }

  public void setEntity(String entity){ this.entity = entity; }

  public DBC entity(String entity){
    this.entity = entity;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DBC dBC = (DBC) o;
    if (dBC.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, dBC.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "DBC{" +
        "id=" + id +
        ", dbc='" + dbc + '\'' +
        ", name='" + name + '\'' +
        ", abbr='" + abbr + '\'' +
        ", status=" + status +
        '}';
  }
}
