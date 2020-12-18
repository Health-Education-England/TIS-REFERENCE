package com.transformuk.hee.tis.reference.api.dto;


import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;

public class RoleDTO implements Serializable {

  private static final long serialVersionUID = -8162014454856728253L;

  private Long id;

  private UUID uuid;

  @NotNull
  private String code;
  @NotNull
  private String label;
  private Status status;
  private RoleCategoryDTO roleCategory;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public RoleCategoryDTO getRoleCategory() {
    return roleCategory;
  }

  public void setRoleCategory(RoleCategoryDTO roleCategory) {
    this.roleCategory = roleCategory;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RoleDTO roleDTO = (RoleDTO) o;

    if (!Objects.equals(id, roleDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "RoleDTO{" +
        "id=" + id +
        ", uuid='" + uuid.toString() + '\'' +
        ", code='" + code + '\'' +
        ", label='" + label + '\'' +
        ", status=" + status +
        ", category=" + roleCategory +
        '}';
  }
}
