package com.transformuk.hee.tis.reference.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Site.
 */
@Entity
@Table(name = "Site")
public class Site implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String siteCode;

	private String localOffice;

	private String trustCode;

	private String siteName;

	private String address;

	private String postCode;

	private String siteKnownAs;

	private String siteNumber;

	private String organisationalUnit;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public Site siteCode(String siteCode) {
		this.siteCode = siteCode;
		return this;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getLocalOffice() {
		return localOffice;
	}

	public Site localOffice(String localOffice) {
		this.localOffice = localOffice;
		return this;
	}

	public void setLocalOffice(String localOffice) {
		this.localOffice = localOffice;
	}

	public String getTrustCode() {
		return trustCode;
	}

	public Site trustCode(String trustCode) {
		this.trustCode = trustCode;
		return this;
	}

	public void setTrustCode(String trustCode) {
		this.trustCode = trustCode;
	}

	public String getSiteName() {
		return siteName;
	}

	public Site siteName(String siteName) {
		this.siteName = siteName;
		return this;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getAddress() {
		return address;
	}

	public Site address(String address) {
		this.address = address;
		return this;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostCode() {
		return postCode;
	}

	public Site postCode(String postCode) {
		this.postCode = postCode;
		return this;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getSiteKnownAs() {
		return siteKnownAs;
	}

	public Site siteKnownAs(String siteKnownAs) {
		this.siteKnownAs = siteKnownAs;
		return this;
	}

	public void setSiteKnownAs(String siteKnownAs) {
		this.siteKnownAs = siteKnownAs;
	}

	public String getSiteNumber() {
		return siteNumber;
	}

	public Site siteNumber(String siteNumber) {
		this.siteNumber = siteNumber;
		return this;
	}

	public void setSiteNumber(String siteNumber) {
		this.siteNumber = siteNumber;
	}

	public String getOrganisationalUnit() {
		return organisationalUnit;
	}

	public Site organisationalUnit(String organisationalUnit) {
		this.organisationalUnit = organisationalUnit;
		return this;
	}

	public void setOrganisationalUnit(String organisationalUnit) {
		this.organisationalUnit = organisationalUnit;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Site site = (Site) o;
		if (site.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, site.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Site{" +
				"id=" + id +
				", siteCode='" + siteCode + "'" +
				", localOffice='" + localOffice + "'" +
				", trustCode='" + trustCode + "'" +
				", siteName='" + siteName + "'" +
				", address='" + address + "'" +
				", postCode='" + postCode + "'" +
				", siteKnownAs='" + siteKnownAs + "'" +
				", siteNumber='" + siteNumber + "'" +
				", organisationalUnit='" + organisationalUnit + "'" +
				'}';
	}
}
