package com.transformuk.hee.tis.reference.service.api.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.exception.CustomParameterizedException;
import com.transformuk.hee.tis.reference.service.model.FundingType;
import com.transformuk.hee.tis.reference.service.repository.FundingTypeRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FundingSubTypeValidatorTest {

  private static final Long FUNDING_TYPE_ID = 1L;

  @InjectMocks
  private FundingSubTypeValidator validator;

  @Mock
  private FundingTypeRepository repository;

  @Test
  void shouldPassValidationWhenParentFundingTypeExists() {
    FundingType fundingType = new FundingType();
    fundingType.setId(FUNDING_TYPE_ID);
    fundingType.setStatus(Status.CURRENT);

    when(repository.findById(FUNDING_TYPE_ID)).thenReturn(Optional.of(fundingType));

    FundingTypeDTO fundingTypeDto = new FundingTypeDTO();
    fundingTypeDto.setId(fundingType.getId());
    FundingSubTypeDto fundingSubTypeDto = new FundingSubTypeDto();
    fundingSubTypeDto.setFundingType(fundingTypeDto);

    assertDoesNotThrow(() -> validator.validate(fundingSubTypeDto));
  }

  @Test
  void shouldFailValidationWhenParentFundingTypeNotExists() {
    when(repository.findById(FUNDING_TYPE_ID)).thenReturn(Optional.empty());

    FundingTypeDTO fundingTypeDto = new FundingTypeDTO();
    fundingTypeDto.setId(FUNDING_TYPE_ID);
    FundingSubTypeDto fundingSubTypeDto = new FundingSubTypeDto();
    fundingSubTypeDto.setFundingType(fundingTypeDto);

    assertThrows(CustomParameterizedException.class, () -> validator.validate(fundingSubTypeDto));
  }
}
