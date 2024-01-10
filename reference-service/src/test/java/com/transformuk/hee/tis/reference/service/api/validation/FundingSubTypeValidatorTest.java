package com.transformuk.hee.tis.reference.service.api.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.CustomParameterizedException;
import com.transformuk.hee.tis.reference.service.model.FundingType;
import com.transformuk.hee.tis.reference.service.repository.FundingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class FundingSubTypeValidatorTest {

  private FundingSubTypeValidator validator;

  @Autowired
  private FundingTypeRepository repository;

  @BeforeEach
  void setUp() {
    validator = new FundingSubTypeValidator(repository);
  }

  @Test
  void shouldPassValidationWhenParentFundingTypeExists() {
    FundingType fundingType = new FundingType();
    fundingType.setCode("fundingType");
    fundingType.setLabel("fundingType");
    fundingType.setStatus(Status.CURRENT);
    fundingType = repository.save(fundingType);

    FundingTypeDTO fundingTypeDto = new FundingTypeDTO();
    fundingTypeDto.setId(fundingType.getId());
    FundingSubTypeDto fundingSubTypeDto = new FundingSubTypeDto();
    fundingSubTypeDto.setFundingType(fundingTypeDto);

    assertDoesNotThrow(() -> validator.validate(fundingSubTypeDto));
  }

  @Test
  void shouldFailValidationWhenParentFundingTypeNotExists() {
    FundingTypeDTO fundingTypeDto = new FundingTypeDTO();
    fundingTypeDto.setId(999L);
    FundingSubTypeDto fundingSubTypeDto = new FundingSubTypeDto();
    fundingSubTypeDto.setFundingType(fundingTypeDto);

    assertThrows(CustomParameterizedException.class, () -> validator.validate(fundingSubTypeDto));
  }
}
