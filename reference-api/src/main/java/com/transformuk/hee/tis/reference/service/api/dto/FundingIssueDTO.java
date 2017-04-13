package com.transformuk.hee.tis.reference.service.api.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the FundingIssue entity.
 */
public class FundingIssueDTO implements Serializable {

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

		FundingIssueDTO fundingIssueDTO = (FundingIssueDTO) o;

		if (!Objects.equals(id, fundingIssueDTO.id)) {
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
		return "FundingIssueDTO{" +
				"id=" + id +
				", code='" + code + "'" +
				'}';
	}
}
