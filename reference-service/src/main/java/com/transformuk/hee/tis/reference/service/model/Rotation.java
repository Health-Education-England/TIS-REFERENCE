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
import lombok.Data;

/**
 * A Rotation.
 */
@Data
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

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status")
  private Status status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Rotation code(String code) {
    this.code = code;
    return this;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Rotation label(String label) {
    this.label = label;
    return this;
  }

  public String getLocalOffice() {
    return localOffice;
  }

  public void setLocalOffice(String localOffice) {
    this.localOffice = localOffice;
  }

  public Rotation localOffice(String localOffice) {
    this.localOffice = localOffice;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Rotation status(Status status) {
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
}
