package com.transformuk.hee.tis.reference.service.model;

import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * The representation of the OrganizationType entity.
 */
@Data
@Entity
public class OrganizationType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private UUID uuid;

  @NotNull
  private String code;

  @NotNull
  private String label;

  @NotNull
  private Status status;
}
