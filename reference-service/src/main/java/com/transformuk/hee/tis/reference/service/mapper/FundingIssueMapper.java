package com.transformuk.hee.tis.reference.service.mapper;

import com.transformuk.hee.tis.reference.domain.FundingIssue;
import com.transformuk.hee.tis.reference.service.dto.FundingIssueDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity FundingIssue and its DTO FundingIssueDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FundingIssueMapper {

	FundingIssueDTO fundingIssueToFundingIssueDTO(FundingIssue fundingIssue);

	List<FundingIssueDTO> fundingIssuesToFundingIssueDTOs(List<FundingIssue> fundingIssues);

	FundingIssue fundingIssueDTOToFundingIssue(FundingIssueDTO fundingIssueDTO);

	List<FundingIssue> fundingIssueDTOsToFundingIssues(List<FundingIssueDTO> fundingIssueDTOs);
}
