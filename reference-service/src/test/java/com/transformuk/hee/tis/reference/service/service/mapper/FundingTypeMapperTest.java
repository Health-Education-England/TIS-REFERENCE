package com.transformuk.hee.tis.reference.service.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.service.model.FundingType;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FundingTypeMapperTest {

  private static final Long ID_1 = 1L;
  private static final UUID UUID_1 = UUID.randomUUID();
  private static final String CODE_1 = "CODE1";
  private static final String LABEL_1 = "LABEL1";

  private static final Long ID_2 = 2L;
  private static final UUID UUID_2 = UUID.randomUUID();
  private static final String CODE_2 = "CODE2";
  private static final String LABEL_2 = "LABEL2";

  private FundingTypeMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new FundingTypeMapperImpl();
  }

  @Test
  void testFundingTypeToFundingTypeDTO() {
    FundingType entity = new FundingType();
    entity.setId(ID_1);
    entity.setUuid(UUID_1);
    entity.setCode(CODE_1);
    entity.setLabel(LABEL_1);

    FundingTypeDTO dto = mapper.fundingTypeToFundingTypeDTO(entity);

    assertNotNull(dto);
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getUuid(), dto.getUuid());
    assertEquals(entity.getCode(), dto.getCode());
    assertEquals(entity.getLabel(), dto.getLabel());
  }

  @Test
  void testFundingTypesToFundingTypeDTOs() {
    FundingType entity1 = new FundingType();
    entity1.setId(ID_1);
    entity1.setUuid(UUID_1);
    entity1.setCode(CODE_1);
    entity1.setLabel(LABEL_1);

    FundingType entity2 = new FundingType();
    entity2.setId(ID_2);
    entity2.setUuid(UUID_2);
    entity2.setCode(CODE_2);
    entity2.setLabel(LABEL_2);

    List<FundingType> entities = List.of(entity1, entity2);
    List<FundingTypeDTO> dtos = mapper.fundingTypesToFundingTypeDTOs(entities);

    assertEquals(2, dtos.size());
    FundingTypeDTO dto1 = dtos.get(0);
    FundingTypeDTO dto2 = dtos.get(1);

    assertEquals(ID_1, dto1.getId());
    assertEquals(UUID_1, dto1.getUuid());
    assertEquals(CODE_1, dto1.getCode());
    assertEquals(LABEL_1, dto1.getLabel());
    assertEquals(ID_2, dto2.getId());
    assertEquals(UUID_2, dto2.getUuid());
    assertEquals(CODE_2, dto2.getCode());
    assertEquals(LABEL_2, dto2.getLabel());
  }

  @Test
  void testFundingTypeDTOToFundingType() {
    FundingTypeDTO dto = new FundingTypeDTO();
    dto.setId(ID_1);
    dto.setUuid(UUID_1);
    dto.setCode(CODE_1);
    dto.setLabel(LABEL_1);

    FundingType entity = mapper.fundingTypeDTOToFundingType(dto);

    assertNotNull(entity);
    assertEquals(dto.getId(), entity.getId());
    assertEquals(dto.getUuid(), entity.getUuid());
    assertEquals(dto.getCode(), entity.getCode());
    assertEquals(dto.getLabel(), entity.getLabel());
  }

  @Test
  void testFundingTypeDTOsToFundingTypes() {
    FundingTypeDTO dto1 = new FundingTypeDTO();
    dto1.setId(ID_1);
    dto1.setUuid(UUID_1);
    dto1.setCode(CODE_1);
    dto1.setLabel(LABEL_1);

    FundingTypeDTO dto2 = new FundingTypeDTO();
    dto2.setId(ID_2);
    dto2.setUuid(UUID_2);
    dto2.setCode(CODE_2);
    dto2.setLabel(LABEL_2);

    List<FundingTypeDTO> dtos = List.of(dto1, dto2);
    List<FundingType> entities = mapper.fundingTypeDTOsToFundingTypes(dtos);

    assertEquals(2, entities.size());
    FundingType entity1 = entities.get(0);
    FundingType entity2 = entities.get(1);

    assertEquals(ID_1, entity1.getId());
    assertEquals(UUID_1, entity1.getUuid());
    assertEquals(CODE_1, entity1.getCode());
    assertEquals(LABEL_1, entity1.getLabel());
    assertEquals(ID_2, entity2.getId());
    assertEquals(UUID_2, entity2.getUuid());
    assertEquals(CODE_2, entity2.getCode());
    assertEquals(LABEL_2, entity2.getLabel());
  }

  @Test
  void testUpdateFundingTypeFromDto() {
    FundingType entity = new FundingType();
    entity.setId(ID_1);
    entity.setUuid(UUID_1);
    entity.setCode(CODE_1);
    entity.setLabel(LABEL_1);

    FundingTypeDTO dto = new FundingTypeDTO();
    dto.setCode(CODE_2);
    dto.setLabel(LABEL_2);
    dto.setId(ID_1);
    dto.setUuid(UUID_1);

    mapper.updateFundingTypeFromDto(dto, entity);

    assertEquals(CODE_2, entity.getCode());
    assertEquals(LABEL_2, entity.getLabel());
  }
}
