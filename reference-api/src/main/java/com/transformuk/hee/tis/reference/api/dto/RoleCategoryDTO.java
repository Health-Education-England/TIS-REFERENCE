package com.transformuk.hee.tis.reference.api.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class RoleCategoryDTO implements Serializable {
    private static final long serialVersionUID = -8918107034703932684L;

    private Long id;
    @NotNull
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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

        RoleCategoryDTO roleDTO = (RoleCategoryDTO) o;

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
        return "RoleCategoryDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
