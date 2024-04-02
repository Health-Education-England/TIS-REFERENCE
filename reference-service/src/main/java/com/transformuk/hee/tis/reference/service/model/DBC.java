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
 * A DBC (Designated Body Code).
 */
@Data
@Entity
public class DBC implements Serializable {

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
  @Column(name = "type", nullable = false)
  private String type;

  @NotNull
  @Column(name = "internal", nullable = false)
  private boolean internal;

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
}
