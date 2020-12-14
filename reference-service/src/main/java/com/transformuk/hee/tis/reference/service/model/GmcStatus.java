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

/**
 * A GmcStatus.
 */
@Entity
public class GmcStatus implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "uuid")
  private UUID uuid;

  @NotNull
  @Column(name = "uuid", nullable = false)
  private UUID uuid;

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

  public UUID getUuid() { return uuid; }

  public void setUuid(UUID uuid) { this.uuid = uuid; }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public GmcStatus code(String code) {
    this.code = code;
    return this;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public GmcStatus label(String label) {
    this.label = label;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GmcStatus gmcStatus = (GmcStatus) o;
    if (gmcStatus.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, gmcStatus.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "GmcStatus{" +
        "id=" + id +
        ", uuid='" + uuid.toString() + '\'' +
        ", code='" + code + '\'' +
        ", label='" + label + '\'' +
        ", status=" + status +
        '}';
  }
}
