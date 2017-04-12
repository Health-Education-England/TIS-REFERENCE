package com.transformuk.hee.tis.reference.service.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the EthnicOrigin entity.
 */
public class EthnicOriginDTO implements Serializable {

	private Long id;

	@NotNull
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		EthnicOriginDTO ethnicOriginDTO = (EthnicOriginDTO) o;

		if (!Objects.equals(id, ethnicOriginDTO.id)) {
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
		return "EthnicOriginDTO{" +
				"id=" + id +
				", code='" + code + "'" +
				'}';
	}
}
