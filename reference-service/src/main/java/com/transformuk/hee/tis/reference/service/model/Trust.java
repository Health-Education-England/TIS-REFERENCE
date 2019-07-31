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
 * A Trust.
 */
@Entity
@Table(name = "Trust")
public class Trust implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code")
  private String code;

  private String localOffice;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status")
  private Status status;

  private String trustKnownAs;

  private String trustName;

  private String trustNumber;

  private String address;

  private String postCode;

  private String intrepidId;

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

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Trust status(Status status) {
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

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public Trust intrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
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
        ", code='" + code + '\'' +
        ", localOffice='" + localOffice + '\'' +
        ", status=" + status +
        ", trustKnownAs='" + trustKnownAs + '\'' +
        ", trustName='" + trustName + '\'' +
        ", trustNumber='" + trustNumber + '\'' +
        ", address='" + address + '\'' +
        ", postCode='" + postCode + '\'' +
        ", intrepidId='" + intrepidId + '\'' +
        '}';
  }
}
