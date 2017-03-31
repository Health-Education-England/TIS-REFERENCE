package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.FundingIssue;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the FundingIssue entity.
 */
@SuppressWarnings("unused")
public interface FundingIssueRepository extends JpaRepository<FundingIssue, Long> {

}
