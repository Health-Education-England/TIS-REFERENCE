package com.transformuk.hee.tis.reference.api.dto;


import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.api.enums.Status;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Site entity.
 */
public class SiteDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id cannot be null during update")
  @Null(groups = Create.class, message = "Id must be null in order to create")
  private Long id;

  private String siteCode;

  private String localOffice;

  private String trustCode;

  private Long trustId;

  private String siteName;

  private String address;

  private String postCode;

  private String siteKnownAs;

  private String siteNumber;

  private String organisationalUnit;

  private Status status;

  private String intrepidId;

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

  public String getLocalOffice() {
    return localOffice;
  }

  public void setLocalOffice(String localOffice) {
    this.localOffice = localOffice;
  }

  public String getTrustCode() {
    return trustCode;
  }

  public void setTrustCode(String trustCode) {
    this.trustCode = trustCode;
  }

  public Long getTrustId() {
    return trustId;
  }

  public void setTrustId(Long trustId) {
    this.trustId = trustId;
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

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
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

    if (!Objects.equals(siteCode, siteDTO.siteCode)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(siteCode);
  }

  @Override
  public String toString() {
    return "SiteDTO{" +
        "id=" + id +
        ", siteCode='" + siteCode + '\'' +
        ", localOffice='" + localOffice + '\'' +
        ", trustCode='" + trustCode + '\'' +
        ", trustId=" + trustId +
        ", siteName='" + siteName + '\'' +
        ", address='" + address + '\'' +
        ", postCode='" + postCode + '\'' +
        ", siteKnownAs='" + siteKnownAs + '\'' +
        ", siteNumber='" + siteNumber + '\'' +
        ", organisationalUnit='" + organisationalUnit + '\'' +
        ", status=" + status +
        ", intrepidId='" + intrepidId + '\'' +
        '}';
  }
}
