package com.transformuk.hee.tis.reference.service.model;

import com.transformuk.hee.tis.reference.api.enums.Status;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 * A Site.
 */
@Entity
@Table(name = "Site")
@Data
public class Site implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String siteCode;

  private LocalDate startDate;

  private LocalDate endDate;

  private String localOffice;

  private String trustCode;

  private Long trustId;

  private String siteName;

  private String address;

  private String postCode;

  private String siteKnownAs;

  private String siteNumber;

  private String organisationalUnit;

  @ManyToOne
  @JoinColumn(name = "organizationTypeId", referencedColumnName = "id")
  private OrganizationType organizationType;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status")
  private Status status;

  private String intrepidId;

  public Site startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  public Site endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  public Site siteCode(String siteCode) {
    this.siteCode = siteCode;
    return this;
  }

  public Site localOffice(String localOffice) {
    this.localOffice = localOffice;
    return this;
  }

  public Site trustCode(String trustCode) {
    this.trustCode = trustCode;
    return this;
  }

  public Site trustId(Long trustId) {
    this.trustId = trustId;
    return this;
  }

  public Site siteName(String siteName) {
    this.siteName = siteName;
    return this;
  }

  public Site address(String address) {
    this.address = address;
    return this;
  }

  public Site postCode(String postCode) {
    this.postCode = postCode;
    return this;
  }

  public Site siteKnownAs(String siteKnownAs) {
    this.siteKnownAs = siteKnownAs;
    return this;
  }

  public Site siteNumber(String siteNumber) {
    this.siteNumber = siteNumber;
    return this;
  }

  public Site organisationalUnit(String organisationalUnit) {
    this.organisationalUnit = organisationalUnit;
    return this;
  }

  public Site intrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
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
}
