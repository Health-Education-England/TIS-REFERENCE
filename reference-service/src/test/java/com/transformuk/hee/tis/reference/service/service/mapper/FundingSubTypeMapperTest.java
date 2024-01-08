package com.transformuk.hee.tis.reference.service.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.model.FundingSubType;
import com.transformuk.hee.tis.reference.service.model.FundingType;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class FundingSubTypeMapperTest {

  private static final String FUNDING_SUB_TYPE_LABEL = "label";
  private static final String FUNDING_SUB_TYPE_CODE = "code";
  private static final UUID FUNDING_SUB_TYPE_UUID = UUID.randomUUID();
  private static final Long FUNDING_TYPE_ID = 1L;

  FundingSubTypeMapper mapper;

  @BeforeEach
  public void setUp() {
    mapper = new FundingSubTypeMapperImpl();
    ReflectionTestUtils.setField(mapper, "fundingTypeMapper", new FundingTypeMapperImpl());
  }

  @Test
  public void fundingSubTypeToDto() {
    FundingType fundingType = new FundingType();
    fundingType.setId(FUNDING_TYPE_ID);
    fundingType.setStatus(Status.CURRENT);
    FundingSubType fundingSubType = new FundingSubType();
    fundingSubType.setFundingType(fundingType);
    fundingSubType.setLabel(FUNDING_SUB_TYPE_LABEL);
    fundingSubType.setCode(FUNDING_SUB_TYPE_CODE);
    fundingSubType.setUuid(FUNDING_SUB_TYPE_UUID);
    fundingSubType.setStatus(Status.CURRENT);

    FundingSubTypeDto fundingSubTypeDto = mapper.toDto(fundingSubType);

    assertEquals(FUNDING_SUB_TYPE_LABEL, fundingSubTypeDto.getLabel());
    assertEquals(FUNDING_SUB_TYPE_CODE, fundingSubTypeDto.getCode());
    assertEquals(FUNDING_SUB_TYPE_UUID, fundingSubTypeDto.getUuid());
    assertEquals(Status.CURRENT, fundingSubTypeDto.getStatus());
    assertEquals(FUNDING_TYPE_ID, fundingSubTypeDto.getFundingTypeDto().getId());
  }

  @Test
  public void fundingSubTypeDtoToEndity() {
    FundingTypeDTO fundingTypeDto = new FundingTypeDTO();
    fundingTypeDto.setId(FUNDING_TYPE_ID);
    fundingTypeDto.setStatus(Status.CURRENT);
    FundingSubTypeDto fundingSubTypeDto = new FundingSubTypeDto();
    fundingSubTypeDto.setFundingTypeDto(fundingTypeDto);
    fundingSubTypeDto.setLabel(FUNDING_SUB_TYPE_LABEL);
    fundingSubTypeDto.setCode(FUNDING_SUB_TYPE_CODE);
    fundingSubTypeDto.setUuid(FUNDING_SUB_TYPE_UUID);
    fundingSubTypeDto.setStatus(Status.CURRENT);

    FundingSubType fundingSubType = mapper.toEntity(fundingSubTypeDto);

    assertEquals(FUNDING_SUB_TYPE_LABEL, fundingSubType.getLabel());
    assertEquals(FUNDING_SUB_TYPE_CODE, fundingSubType.getCode());
    assertEquals(FUNDING_SUB_TYPE_UUID, fundingSubType.getUuid());
    assertEquals(Status.CURRENT, fundingSubType.getStatus());
    assertEquals(FUNDING_TYPE_ID, fundingSubType.getFundingType().getId());
  }
}
