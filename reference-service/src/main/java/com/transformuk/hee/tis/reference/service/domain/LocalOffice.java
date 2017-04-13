package com.transformuk.hee.tis.reference.service.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A LocalOffice.
 */
@Entity
public class LocalOffice implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "abbreviation", nullable = false)
	private String abbreviation;

	@NotNull
	@Column(name = "name", nullable = false)
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public LocalOffice abbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalOffice name(String name) {
		this.name = name;
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
		LocalOffice localOffice = (LocalOffice) o;
		if (localOffice.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, localOffice.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "LocalOffice{" +
				"id=" + id +
				", abbreviation='" + abbreviation + "'" +
				", name='" + name + "'" +
				'}';
	}
}
