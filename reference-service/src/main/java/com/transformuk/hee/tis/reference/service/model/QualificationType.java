package com.transformuk.hee.tis.reference.service.model;


import com.transformuk.hee.tis.reference.api.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A QualificationType.
 */
@Entity
public class QualificationType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "code", nullable = false)
  private String code;

  @NotNull
  @Column(name = "label", nullable = false)
  private String label;

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

  public QualificationType code(String code) {
    this.code = code;
    return this;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public QualificationType label(String label) {
    this.label = label;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public QualificationType status(Status status) {
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
    QualificationType qualificationType = (QualificationType) o;
    if (qualificationType.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, qualificationType.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "QualificationType{" +
        "id=" + id +
        ", code='" + code + '\'' +
        ", label='" + label + '\'' +
        ", status=" + status +
        '}';
  }
}
