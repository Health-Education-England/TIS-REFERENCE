package com.transformuk.hee.tis.reference.service.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Site entity.
 */
public class SiteDTO implements Serializable {

	private Long id;

	@NotNull
	private String siteCode;

	private String siteName;

	private String address;

	private String postCode;

	private String siteKnownAs;

	private String siteNumber;

	private String organisationalUnit;

	private Long localOfficeId;

	private Long trustCodeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
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

	public String getSiteKnownAs() {
		return siteKnownAs;
	}

	public void setSiteKnownAs(String siteKnownAs) {
		this.siteKnownAs = siteKnownAs;
	}

	public String getSiteNumber() {
		return siteNumber;
	}

	public void setSiteNumber(String siteNumber) {
		this.siteNumber = siteNumber;
	}

	public String getOrganisationalUnit() {
		return organisationalUnit;
	}

	public void setOrganisationalUnit(String organisationalUnit) {
		this.organisationalUnit = organisationalUnit;
	}

	public Long getLocalOfficeId() {
		return localOfficeId;
	}

	public void setLocalOfficeId(Long localOfficeId) {
		this.localOfficeId = localOfficeId;
	}

	public Long getTrustCodeId() {
		return trustCodeId;
	}

	public void setTrustCodeId(Long trustId) {
		this.trustCodeId = trustId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SiteDTO siteDTO = (SiteDTO) o;

		if (!Objects.equals(id, siteDTO.id)) {
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
		return "SiteDTO{" +
				"id=" + id +
				", siteCode='" + siteCode + "'" +
				", siteName='" + siteName + "'" +
				", address='" + address + "'" +
				", postCode='" + postCode + "'" +
				", siteKnownAs='" + siteKnownAs + "'" +
				", siteNumber='" + siteNumber + "'" +
				", organisationalUnit='" + organisationalUnit + "'" +
				'}';
	}
}
