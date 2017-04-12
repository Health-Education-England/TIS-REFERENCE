package com.transformuk.hee.tis.reference.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EthnicOrigin.
 */
@Entity
public class EthnicOrigin implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "code", nullable = false)
	private String code;

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

	public EthnicOrigin code(String code) {
		this.code = code;
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
		EthnicOrigin ethnicOrigin = (EthnicOrigin) o;
		if (ethnicOrigin.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, ethnicOrigin.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "EthnicOrigin{" +
				"id=" + id +
				", code='" + code + "'" +
				'}';
	}
}
