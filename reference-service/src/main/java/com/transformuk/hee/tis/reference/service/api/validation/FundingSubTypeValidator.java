package com.transformuk.hee.tis.reference.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.service.exception.CustomParameterizedException;
import com.transformuk.hee.tis.reference.service.exception.ErrorConstants;
import com.transformuk.hee.tis.reference.service.model.FundingType;
import com.transformuk.hee.tis.reference.service.repository.FundingTypeRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FundingSubTypeValidator {

  private static final String PARENT_FUNDING_TYPE_NOT_FOUND = "Funding type id (%s) not found.";

  private final FundingTypeRepository fundingTypeRepository;

  public FundingSubTypeValidator(FundingTypeRepository fundingTypeRepository) {
    this.fundingTypeRepository = fundingTypeRepository;
  }

  public void validate(FundingSubTypeDto fundingSubTypeDto) {
    log.debug("Validating fundingSubType: {}", fundingSubTypeDto);

    Long fundingTypeId = fundingSubTypeDto.getFundingTypeDto().getId();
    Optional<FundingType> optionalFundingType = fundingTypeRepository.findById(fundingTypeId);
    if (!optionalFundingType.isPresent()) {
      String error = String.format(PARENT_FUNDING_TYPE_NOT_FOUND, fundingTypeId);
      log.warn(error);
      throw new CustomParameterizedException(error, ErrorConstants.ERR_VALIDATION);
    }
  }
}
