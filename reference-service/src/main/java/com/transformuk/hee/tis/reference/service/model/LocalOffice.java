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
 * A LocalOffice.
 */
@Entity
public class LocalOffice implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "dbc", nullable = false)
  private String dbc;

  @NotNull
  @Column(name = "abbreviation", nullable = false)
  private String abbreviation;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "entityId", nullable = false)
  private Long entityId;

  @NotNull
  @Column(name = "postAbbreviation", nullable = false)
  private String postAbbreviation;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status")
  private Status status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDBC() {
    return dbc;
  }

  public void setDBC(String dbc) {
    this.dbc = dbc;
  }

  public LocalOffice dbc(String dbc) {
    this.dbc = dbc;
    return this;
  }

  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  public LocalOffice abbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalOffice name(String name) {
    this.name = name;
    return this;
  }

  public Long getEntityId() {
    return entityId;
  }

  public void setEntityId(Long entityId) {
    this.entityId = entityId;
  }

  public LocalOffice entityId(Long entityId) {
    this.entityId = entityId;
    return this;
  }

  public String getPostAbbreviation() {
    return postAbbreviation;
  }

  public void setPostAbbreviation(String postAbbreviation) {
    this.postAbbreviation = postAbbreviation;
  }

  public LocalOffice postAbbreviation(String postAbbreviation) {
    this.postAbbreviation = postAbbreviation;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public LocalOffice status(Status status) {
    this.status = status;
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
    LocalOffice localOffice = (LocalOffice) o;
    if (localOffice.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, localOffice.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "LocalOffice{" +
        "id=" + id +
        ", dbc='" + dbc + '\'' +
        ", abbreviation='" + abbreviation + '\'' +
        ", name='" + name + '\'' +
        ", entityId='" + entityId + '\'' +
        ", postAbbreviation='" + postAbbreviation + '\'' +
        ", status=" + status +
        '}';
  }
}
