package com.transformuk.hee.tis.reference.service.model;

import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
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
 * An Assessment type.
 */
@Data
@Entity
public class AssessmentType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "uuid", nullable = false)
  private UUID uuid;

  @NotNull(groups = {Update.class,
      Create.class}, message = "code is needed for both create and update")
  private String code;

  @NotNull(groups = {Update.class,
      Create.class}, message = "label is needed for both create and update")
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
    AssessmentType assessmentType = (AssessmentType) o;
    if (assessmentType.code == null || code == null) {
      return false;
    }
    return Objects.equals(code, assessmentType.code);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(code);
  }
}
