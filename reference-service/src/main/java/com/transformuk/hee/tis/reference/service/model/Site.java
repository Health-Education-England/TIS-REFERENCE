package com.transformuk.hee.tis.reference.service.model;


import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status")
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

  public Site siteCode(String siteCode) {
    this.siteCode = siteCode;
    return this;
  }

  public String getLocalOffice() {
    return localOffice;
  }

  public void setLocalOffice(String localOffice) {
    this.localOffice = localOffice;
  }

  public Site localOffice(String localOffice) {
    this.localOffice = localOffice;
    return this;
  }

  public String getTrustCode() {
    return trustCode;
  }

  public void setTrustCode(String trustCode) {
    this.trustCode = trustCode;
  }

  public Site trustCode(String trustCode) {
    this.trustCode = trustCode;
    return this;
  }

  public Long getTrustId() {
    return trustId;
  }

  public void setTrustId(Long trustId) {
    this.trustId = trustId;
  }

  public Site trustId(Long trustId) {
    this.trustId = trustId;
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

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public Site intrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Site status(Status status) {
    this.status = status;
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
    if (site.siteCode == null || siteCode == null) {
      return false;
    }
    return Objects.equals(siteCode, site.siteCode);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(siteCode);
  }

  @Override
  public String toString() {
    return "Site{" +
        "id=" + id +
        ", siteCode='" + siteCode + '\'' +
        ", localOffice='" + localOffice + '\'' +
        ", trustCode='" + trustCode + '\'' +
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
