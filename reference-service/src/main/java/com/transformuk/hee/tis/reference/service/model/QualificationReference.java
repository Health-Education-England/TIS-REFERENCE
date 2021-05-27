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
import lombok.Data;

/**
 * A QualificationReference.
 */
@Data
@Entity
public class QualificationReference implements Serializable {

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QualificationReference maritalStatus = (QualificationReference) o;
    if (maritalStatus.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, maritalStatus.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
