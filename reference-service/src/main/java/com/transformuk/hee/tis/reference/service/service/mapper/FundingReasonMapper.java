package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.FundingReasonDto;
import com.transformuk.hee.tis.reference.service.model.FundingReason;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity FundingReason and its DTO FundingReasonDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FundingReasonMapper {

  FundingReasonDto toDto(FundingReason fundingReason);

  FundingReason toEntity(FundingReasonDto fundingReasonDto);
}
