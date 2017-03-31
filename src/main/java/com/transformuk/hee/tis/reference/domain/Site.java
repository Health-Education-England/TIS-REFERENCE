package com.transformuk.hee.tis.reference.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Site.
 */
@Entity
public class Site implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "siteCode", nullable = false)
	private String siteCode;

	@Column(name = "siteName")
	private String siteName;

	@Column(name = "address")
	private String address;

	@Column(name = "postCode")
	private String postCode;

	@Column(name = "siteKnownAs")
	private String siteKnownAs;

	@Column(name = "siteNumber")
	private String siteNumber;

	@Column(name = "organisationalUnit")
	private String organisationalUnit;

	@ManyToOne
	@JoinColumn(name = "localOfficeId")
	private LocalOffice localOffice;

	@ManyToOne
	@JoinColumn(name = "trustCodeId")
	private Trust trustCode;

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

	public Site siteCode(String siteCode) {
		this.siteCode = siteCode;
		return this;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Site siteName(String siteName) {
		this.siteName = siteName;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Site address(String address) {
		this.address = address;
		return this;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public Site postCode(String postCode) {
		this.postCode = postCode;
		return this;
	}

	public String getSiteKnownAs() {
		return siteKnownAs;
	}

	public void setSiteKnownAs(String siteKnownAs) {
		this.siteKnownAs = siteKnownAs;
	}

	public Site siteKnownAs(String siteKnownAs) {
		this.siteKnownAs = siteKnownAs;
		return this;
	}

	public String getSiteNumber() {
		return siteNumber;
	}

	public void setSiteNumber(String siteNumber) {
		this.siteNumber = siteNumber;
	}

	public Site siteNumber(String siteNumber) {
		this.siteNumber = siteNumber;
		return this;
	}

	public String getOrganisationalUnit() {
		return organisationalUnit;
	}

	public void setOrganisationalUnit(String organisationalUnit) {
		this.organisationalUnit = organisationalUnit;
	}

	public Site organisationalUnit(String organisationalUnit) {
		this.organisationalUnit = organisationalUnit;
		return this;
	}

	public LocalOffice getLocalOffice() {
		return localOffice;
	}

	public void setLocalOffice(LocalOffice localOffice) {
		this.localOffice = localOffice;
	}

	public Site localOffice(LocalOffice localOffice) {
		this.localOffice = localOffice;
		return this;
	}

	public Trust getTrustCode() {
		return trustCode;
	}

	public void setTrustCode(Trust trust) {
		this.trustCode = trust;
	}

	public Site trustCode(Trust trust) {
		this.trustCode = trust;
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
				", siteName='" + siteName + "'" +
				", address='" + address + "'" +
				", postCode='" + postCode + "'" +
				", siteKnownAs='" + siteKnownAs + "'" +
				", siteNumber='" + siteNumber + "'" +
				", organisationalUnit='" + organisationalUnit + "'" +
				'}';
	}
}
