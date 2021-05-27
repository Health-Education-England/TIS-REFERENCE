package com.transformuk.hee.tis.reference.service.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

/**
 * The representation of the JsonPatch entity.
 */
@Data
@Entity
@DynamicInsert
public class JsonPatch implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull
  private String tableDtoName;

  private String patchId;

  private String patch;

  @Temporal(TemporalType.TIMESTAMP)
  private Date dateAdded;

  private Boolean enabled;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JsonPatch jsonPatch = (JsonPatch) o;
    if (jsonPatch.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, jsonPatch.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
