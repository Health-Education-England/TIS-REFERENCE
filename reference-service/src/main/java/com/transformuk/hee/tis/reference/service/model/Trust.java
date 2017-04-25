package com.transformuk.hee.tis.reference.service.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Trust.
 */
@Entity
@Table(name = "Trust")
public class Trust implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "code", nullable = false)
	private String code;

	private String localOffice;

	private String status;

	private String trustKnownAs;

	private String trustName;

	private String trustNumber;

	private String address;

	private String postCode;

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

	public Trust code(String code) {
		this.code = code;
		return this;
	}

	public String getLocalOffice() {
		return localOffice;
	}

	public void setLocalOffice(String localOffice) {
		this.localOffice = localOffice;
	}

	public Trust localOffice(String localOffice) {
		this.localOffice = localOffice;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Trust status(String status) {
		this.status = status;
		return this;
	}

	public String getTrustKnownAs() {
		return trustKnownAs;
	}

	public void setTrustKnownAs(String trustKnownAs) {
		this.trustKnownAs = trustKnownAs;
	}

	public Trust trustKnownAs(String trustKnownAs) {
		this.trustKnownAs = trustKnownAs;
		return this;
	}

	public String getTrustName() {
		return trustName;
	}

	public void setTrustName(String trustName) {
		this.trustName = trustName;
	}

	public Trust trustName(String trustName) {
		this.trustName = trustName;
		return this;
	}

	public String getTrustNumber() {
		return trustNumber;
	}

	public void setTrustNumber(String trustNumber) {
		this.trustNumber = trustNumber;
	}

	public Trust trustNumber(String trustNumber) {
		this.trustNumber = trustNumber;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Trust address(String address) {
		this.address = address;
		return this;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public Trust postCode(String postCode) {
		this.postCode = postCode;
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
		Trust trust = (Trust) o;
		if (trust.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, trust.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Trust{" +
				"id=" + id +
				", code='" + code + "'" +
				", localOffice='" + localOffice + "'" +
				", status='" + status + "'" +
				", trustKnownAs='" + trustKnownAs + "'" +
				", trustName='" + trustName + "'" +
				", trustNumber='" + trustNumber + "'" +
				", address='" + address + "'" +
				", postCode='" + postCode + "'" +
				'}';
	}
}