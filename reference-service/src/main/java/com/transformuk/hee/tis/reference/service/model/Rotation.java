package com.transformuk.hee.tis.reference.service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Rotation.
 */
@Entity
public class Rotation implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code")
  private String code;

  @Column(name = "label")
  private String label;

  @Column(name = "localOffice")
  private String localOffice;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public Rotation code(String code) {
    this.code = code;
    return this;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getLabel() {
    return label;
  }

  public Rotation label(String label) {
    this.label = label;
    return this;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getLocalOffice() {
    return localOffice;
  }

  public Rotation localOffice(String localOffice) {
    this.localOffice = localOffice;
    return this;
  }

  public void setLocalOffice(String localOffice) {
    this.localOffice = localOffice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Rotation rotation = (Rotation) o;
    if (rotation.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), rotation.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "Rotation{" +
        "id=" + getId() +
        ", code='" + getCode() + "'" +
        ", label='" + getLabel() + "'" +
        ", localOffice='" + getLocalOffice() + "'" +
        "}";
  }
}
