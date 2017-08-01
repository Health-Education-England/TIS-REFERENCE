package com.transformuk.hee.tis.reference.service.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FundingIssue.
 */
@Entity
public class FundingIssue implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "code", nullable = false)
  private String code;

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

  public FundingIssue code(String code) {
    this.code = code;
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
    FundingIssue fundingIssue = (FundingIssue) o;
    if (fundingIssue.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, fundingIssue.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "FundingIssue{" +
        "id=" + id +
        ", code='" + code + "'" +
        '}';
  }
}
