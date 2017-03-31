package com.transformuk.hee.tis.reference.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SexualOrientation.
 */
@Entity
public class SexualOrientation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "code", nullable = false)
	private String code;

	@NotNull
	@Column(name = "label", nullable = false)
	private String label;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public SexualOrientation code(String code) {
		this.code = code;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public SexualOrientation label(String label) {
		this.label = label;
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
		SexualOrientation sexualOrientation = (SexualOrientation) o;
		if (sexualOrientation.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, sexualOrientation.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "SexualOrientation{" +
				"id=" + id +
				", code='" + code + "'" +
				", label='" + label + "'" +
				'}';
	}
}
