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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Role implements Serializable {

  private static final long serialVersionUID = -2957449831898187124L;

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

  @ManyToOne
  @JoinColumn(name = "categoryId")
  private RoleCategory roleCategory;

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

  public Role code(String code) {
    this.code = code;
    return this;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Role label(String label) {
    this.label = label;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Role status(Status status) {
    this.status = status;
    return this;
  }

  public RoleCategory getRoleCategory() {
    return roleCategory;
  }

  public void setRoleCategory(RoleCategory roleCategory) {
    this.roleCategory = roleCategory;
  }

  public Role roleCategory(RoleCategory roleCategory) {
    this.roleCategory = roleCategory;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Role role = (Role) o;
    if (role.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, role.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Role{" +
        "id=" + id +
        ", uuid='" + uuid.toString() + '\'' +
        ", code='" + code + '\'' +
        ", label='" + label + '\'' +
        ", status=" + status +
        ", roleCategory=" + roleCategory +
        '}';
  }
}
