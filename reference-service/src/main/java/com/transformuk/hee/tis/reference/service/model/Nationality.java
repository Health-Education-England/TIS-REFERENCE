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
 * A Nationality.
 */
@Data
@Entity
public class Nationality implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "uuid")
  private UUID uuid;

  @NotNull
  @Column(name = "countryNumber", nullable = false)
  private String countryNumber;

  @NotNull
  @Column(name = "nationality", nullable = false)
  private String nationality;

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
    Nationality nationality = (Nationality) o;
    if (nationality.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, nationality.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
