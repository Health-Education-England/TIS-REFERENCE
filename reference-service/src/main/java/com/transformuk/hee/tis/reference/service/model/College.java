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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * A College.
 */
@Data
@Entity
public class College implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "uuid")
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
          name = "UUID",
          strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID uuid;

  @NotNull
  @Column(name = "abbreviation", nullable = false)
  private String abbreviation;

  private String name;

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
    College college = (College) o;
    if (college.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, college.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
