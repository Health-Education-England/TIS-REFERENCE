package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.FundingIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the FundingIssue entity.
 */
@SuppressWarnings("unused")
public interface FundingIssueRepository extends JpaRepository<FundingIssue, Long>, JpaSpecificationExecutor {

}
