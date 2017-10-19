package com.transformuk.hee.tis.reference.api.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Trust entity.
 */
public class TrustDTO implements Serializable {

  @NotNull
  private String code;

  private String localOffice;

  private String status;

  private String trustKnownAs;

  private String trustName;

  private String trustNumber;

  private String address;

  private String postCode;

  private String intrepidId;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getLocalOffice() {
    return localOffice;
  }

  public void setLocalOffice(String localOffice) {
    this.localOffice = localOffice;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTrustKnownAs() {
    return trustKnownAs;
  }

  public void setTrustKnownAs(String trustKnownAs) {
    this.trustKnownAs = trustKnownAs;
  }

  public String getTrustName() {
    return trustName;
  }

  public void setTrustName(String trustName) {
    this.trustName = trustName;
  }

  public String getTrustNumber() {
    return trustNumber;
  }

  public void setTrustNumber(String trustNumber) {
    this.trustNumber = trustNumber;
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

    TrustDTO trustDTO = (TrustDTO) o;

    if (!Objects.equals(code, trustDTO.code)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(code);
  }

  @Override
  public String toString() {
    return "TrustDTO{" +
        "code='" + code + "'" +
        ", localOffice='" + localOffice + "'" +
        ", status='" + status + "'" +
        ", trustKnownAs='" + trustKnownAs + "'" +
        ", trustName='" + trustName + "'" +
        ", trustNumber='" + trustNumber + "'" +
        ", address='" + address + "'" +
        ", postCode='" + postCode + "'" +
        ", intrepidId='" + intrepidId + "'" +
        '}';
  }
}
