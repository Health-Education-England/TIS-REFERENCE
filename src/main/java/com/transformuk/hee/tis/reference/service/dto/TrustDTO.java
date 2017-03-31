package com.transformuk.hee.tis.reference.service.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Trust entity.
 */
public class TrustDTO implements Serializable {

	private Long id;

	@NotNull
	private String code;

	private String status;

	private String trustKnownAs;

	private String trustName;

	private String trustNumber;

	private String address;

	private String postCode;

	private Long localOfficeId;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTrustKnownAs() {
		return trustKnownAs;
	}

	public void setTrustKnownAs(String trustKnownAs) {
		this.trustKnownAs = trustKnownAs;
	}

	public String getTrustName() {
		return trustName;
	}

	public void setTrustName(String trustName) {
		this.trustName = trustName;
	}

	public String getTrustNumber() {
		return trustNumber;
	}

	public void setTrustNumber(String trustNumber) {
		this.trustNumber = trustNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public Long getLocalOfficeId() {
		return localOfficeId;
	}

	public void setLocalOfficeId(Long localOfficeId) {
		this.localOfficeId = localOfficeId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		TrustDTO trustDTO = (TrustDTO) o;

		if (!Objects.equals(id, trustDTO.id)) {
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
		return "TrustDTO{" +
				"id=" + id +
				", code='" + code + "'" +
				", status='" + status + "'" +
				", trustKnownAs='" + trustKnownAs + "'" +
				", trustName='" + trustName + "'" +
				", trustNumber='" + trustNumber + "'" +
				", address='" + address + "'" +
				", postCode='" + postCode + "'" +
				'}';
	}
}
