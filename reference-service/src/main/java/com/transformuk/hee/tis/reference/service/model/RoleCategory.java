package com.transformuk.hee.tis.reference.service.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class RoleCategory implements Serializable {

  private static final long serialVersionUID = -3365258728986923380L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @OneToMany(mappedBy = "roleCategory", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Role> roles = new HashSet<>();

  private UUID uuid;

  public RoleCategory() {
  }

  public RoleCategory(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleCategory roleCategory = (RoleCategory) o;
    if (roleCategory.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, roleCategory.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "RoleCategory{" +
        "id=" + id +
         ", uuid='" + uuid.toString() + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
