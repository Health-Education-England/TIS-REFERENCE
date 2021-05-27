package com.transformuk.hee.tis.reference.service.model;

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
 * A Grade.
 */
@Data
@Entity
public class Grade implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "uuid")
  private UUID uuid;

  @Column(name = "abbreviation")
  private String abbreviation;

  @Column(name = "name")
  private String name;

  @Column(name = "label")
  private String label;

  @NotNull
  @Column(name = "trainingGrade", nullable = false)
  private Boolean trainingGrade;

  @NotNull
  @Column(name = "postGrade", nullable = false)
  private Boolean postGrade;

  @NotNull
  @Column(name = "placementGrade", nullable = false)
  private Boolean placementGrade;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status")
  private Status status;

  @Column(name = "intrepidId")
  private String intrepidId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Grade grade = (Grade) o;
    if (grade.abbreviation == null || abbreviation == null) {
      return false;
    }
    return Objects.equals(abbreviation, grade.abbreviation);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(abbreviation);
  }
}
