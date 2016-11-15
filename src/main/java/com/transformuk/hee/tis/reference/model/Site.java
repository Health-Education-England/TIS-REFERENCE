package com.transformuk.hee.tis.reference.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * An nhs site, part of a {@link Trust}
 */
@ApiModel(description = "Site data")
@Entity
@Table(name = "Site")
public class Site {
	@Id
	private String siteCode;
	private String trustCode;
	private String siteName;
	private String address;
	private String postCode;

	public Site() {
	}

	public Site(String siteCode, String trustCode, String siteName, String address, String postCode) {
		this.siteCode = siteCode;
		this.trustCode = trustCode;
		this.siteName = siteName;
		this.address = address;
		this.postCode = postCode;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getTrustCode() {
		return trustCode;
	}

	public void setTrustCode(String trustCode) {
		this.trustCode = trustCode;
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
}
