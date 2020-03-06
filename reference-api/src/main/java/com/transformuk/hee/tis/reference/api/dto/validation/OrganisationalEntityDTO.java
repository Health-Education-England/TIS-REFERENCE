package com.transformuk.hee.tis.reference.api.dto.validation;

import com.transformuk.hee.tis.reference.api.dto.LocalOfficeDTO;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the OrganisationalEntity entity. (HEE, NI)
 */
public class OrganisationalEntityDTO implements Serializable {

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

        OrganisationalEntityDTO organisationalEntityDTO = (OrganisationalEntityDTO) o;

        if (!Objects.equals(id, organisationalEntityDTO.id)) {
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
        return "OrganisationalEntityDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
