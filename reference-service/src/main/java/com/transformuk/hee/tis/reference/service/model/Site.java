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
