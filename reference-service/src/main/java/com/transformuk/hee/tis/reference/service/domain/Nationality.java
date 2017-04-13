package com.transformuk.hee.tis.reference.service.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Nationality.
 */
@Entity
public class Nationality implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "countryNumber", nullable = false)
	private String countryNumber;

	@NotNull
	@Column(name = "nationality", nullable = false)
	private String nationality;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountryNumber() {
		return countryNumber;
	}

	public void setCountryNumber(String countryNumber) {
		this.countryNumber = countryNumber;
	}

	public Nationality countryNumber(String countryNumber) {
		this.countryNumber = countryNumber;
		return this;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Nationality nationality(String nationality) {
		this.nationality = nationality;
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
		Nationality nationality = (Nationality) o;
		if (nationality.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, nationality.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Nationality{" +
				"id=" + id +
				", countryNumber='" + countryNumber + "'" +
				", nationality='" + nationality + "'" +
				'}';
	}
}
