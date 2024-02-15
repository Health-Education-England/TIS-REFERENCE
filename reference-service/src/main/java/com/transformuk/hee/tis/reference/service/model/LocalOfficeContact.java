package com.transformuk.hee.tis.reference.service.model;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * A local office contact.
 */
@Data
@Entity
public class LocalOfficeContact implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID id;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "localOfficeId", referencedColumnName = "uuid", nullable = false)
  private LocalOffice localOffice;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "contactTypeId", referencedColumnName = "id", nullable = false)
  private LocalOfficeContactType contactType;

  private String contact;
}
