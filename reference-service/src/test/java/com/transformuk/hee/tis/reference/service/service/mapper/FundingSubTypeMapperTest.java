package com.transformuk.hee.tis.reference.service.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.model.FundingSubType;
import com.transformuk.hee.tis.reference.service.model.FundingType;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class FundingSubTypeMapperTest {

  private static final String FUNDING_SUB_TYPE_LABEL_1 = "label1";
  private static final String FUNDING_SUB_TYPE_CODE_1 = "code";
  private static final UUID FUNDING_SUB_TYPE_UUID_1 = UUID.randomUUID();
  private static final String FUNDING_SUB_TYPE_LABEL_2 = "label2";
  private static final String FUNDING_SUB_TYPE_CODE_2 = "code2";
  private static final UUID FUNDING_SUB_TYPE_UUID_2 = UUID.randomUUID();
  private static final Long FUNDING_TYPE_ID = 1L;

  FundingSubTypeMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new FundingSubTypeMapperImpl();
    ReflectionTestUtils.setField(mapper, "fundingTypeMapper", new FundingTypeMapperImpl());
  }

  @Test
  void fundingSubTypeToDto() {
    FundingType fundingType = new FundingType();
    fundingType.setId(FUNDING_TYPE_ID);
    fundingType.setStatus(Status.CURRENT);
    FundingSubType fundingSubType = new FundingSubType();
    fundingSubType.setFundingType(fundingType);
    fundingSubType.setLabel(FUNDING_SUB_TYPE_LABEL_1);
    fundingSubType.setCode(FUNDING_SUB_TYPE_CODE_1);
    fundingSubType.setId(FUNDING_SUB_TYPE_UUID_1);
    fundingSubType.setStatus(Status.CURRENT);

    FundingSubTypeDto fundingSubTypeDto = mapper.toDto(fundingSubType);

    assertEquals(FUNDING_SUB_TYPE_LABEL_1, fundingSubTypeDto.getLabel());
    assertEquals(FUNDING_SUB_TYPE_CODE_1, fundingSubTypeDto.getCode());
    assertEquals(FUNDING_SUB_TYPE_UUID_1, fundingSubTypeDto.getId());
    assertEquals(Status.CURRENT, fundingSubTypeDto.getStatus());
    assertEquals(FUNDING_TYPE_ID, fundingSubTypeDto.getFundingType().getId());
  }

  @Test
  void fundingSubTypeDtoToEntity() {
    FundingTypeDTO fundingTypeDto = new FundingTypeDTO();
    fundingTypeDto.setId(FUNDING_TYPE_ID);
    fundingTypeDto.setStatus(Status.CURRENT);
    FundingSubTypeDto fundingSubTypeDto = new FundingSubTypeDto();
    fundingSubTypeDto.setFundingType(fundingTypeDto);
    fundingSubTypeDto.setLabel(FUNDING_SUB_TYPE_LABEL_1);
    fundingSubTypeDto.setCode(FUNDING_SUB_TYPE_CODE_1);
    fundingSubTypeDto.setId(FUNDING_SUB_TYPE_UUID_1);
    fundingSubTypeDto.setStatus(Status.CURRENT);

    FundingSubType fundingSubType = mapper.toEntity(fundingSubTypeDto);

    assertEquals(FUNDING_SUB_TYPE_LABEL_1, fundingSubType.getLabel());
    assertEquals(FUNDING_SUB_TYPE_CODE_1, fundingSubType.getCode());
    assertEquals(FUNDING_SUB_TYPE_UUID_1, fundingSubType.getId());
    assertEquals(Status.CURRENT, fundingSubType.getStatus());
    assertEquals(FUNDING_TYPE_ID, fundingSubType.getFundingType().getId());
  }

  @Test
  void fundingSubTypeToDtos() {
    FundingType fundingType = new FundingType();
    fundingType.setId(FUNDING_TYPE_ID);
    fundingType.setStatus(Status.CURRENT);

    FundingSubType fundingSubType1 = new FundingSubType();
    fundingSubType1.setFundingType(fundingType);
    fundingSubType1.setLabel(FUNDING_SUB_TYPE_LABEL_1);
    fundingSubType1.setCode(FUNDING_SUB_TYPE_CODE_1);
    fundingSubType1.setId(FUNDING_SUB_TYPE_UUID_1);
    fundingSubType1.setStatus(Status.CURRENT);

    FundingSubType fundingSubType2 = new FundingSubType();
    fundingSubType2.setFundingType(fundingType);
    fundingSubType2.setLabel(FUNDING_SUB_TYPE_LABEL_2);
    fundingSubType2.setCode(FUNDING_SUB_TYPE_CODE_2);
    fundingSubType2.setId(FUNDING_SUB_TYPE_UUID_2);
    fundingSubType2.setStatus(Status.CURRENT);

    List<FundingSubTypeDto> fundingSubTypeDtos = mapper.toDtos(
        Arrays.asList(fundingSubType1, fundingSubType2));

    assertEquals(2, fundingSubTypeDtos.size());
    assertTrue(fundingSubTypeDtos.stream().anyMatch(e -> e.getId().equals(FUNDING_SUB_TYPE_UUID_1)));
    assertTrue(fundingSubTypeDtos.stream().anyMatch(e -> e.getId().equals(FUNDING_SUB_TYPE_UUID_2)));
    assertTrue(fundingSubTypeDtos.stream().anyMatch(e -> e.getLabel().equals(FUNDING_SUB_TYPE_LABEL_1)));
    assertTrue(fundingSubTypeDtos.stream().anyMatch(e -> e.getLabel().equals(FUNDING_SUB_TYPE_LABEL_2)));
    assertTrue(fundingSubTypeDtos.stream().anyMatch(e -> e.getCode().equals(FUNDING_SUB_TYPE_CODE_1)));
    assertTrue(fundingSubTypeDtos.stream().anyMatch(e -> e.getCode().equals(FUNDING_SUB_TYPE_CODE_2)));
  }
}
