package com.transformuk.hee.tis.reference.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.model.FundingSubType;
import com.transformuk.hee.tis.reference.service.model.FundingType;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Application.class)
class FundingSubTypeRepositoryTest {

  private static final Long FUNDING_TYPE_ID = 1L;
  private static final String CODE_1 = "AAAAAAAAAA";
  private static final String LABEL_1 = "AAAAAAAAAA";
  private static final String CODE_2 = "BBBBBBBBBB";
  private static final String LABEL_2 = "BBBBBBBBBB";

  @Autowired
  private FundingSubTypeRepository fundingSubTypeRepository;

  @Test
  @Transactional
  void findByIdInReturnsMatchingFundingSubTypes() {
    FundingSubType firstFundingSubType = buildFundingSubType(CODE_1, LABEL_1);
    FundingSubType secondFundingSubType = buildFundingSubType(CODE_2, LABEL_2);

    firstFundingSubType = fundingSubTypeRepository.saveAndFlush(firstFundingSubType);
    secondFundingSubType = fundingSubTypeRepository.saveAndFlush(secondFundingSubType);

    List<FundingSubType> results = fundingSubTypeRepository.findByIdIn(
        Arrays.asList(firstFundingSubType.getId(), secondFundingSubType.getId()));

    assertThat(results).hasSize(2);
    assertThat(results).extracting(FundingSubType::getId)
        .containsExactlyInAnyOrder(firstFundingSubType.getId(), secondFundingSubType.getId());
  }

  @Test
  @Transactional
  void findByIdInIgnoresUnknownIds() {
    FundingSubType fundingSubType = buildFundingSubType(CODE_1, LABEL_1);
    fundingSubType = fundingSubTypeRepository.saveAndFlush(fundingSubType);

    List<FundingSubType> results = fundingSubTypeRepository.findByIdIn(
        Arrays.asList(fundingSubType.getId(), UUID.randomUUID()));

    assertThat(results).hasSize(1);
    assertThat(results.get(0).getId()).isEqualTo(fundingSubType.getId());
  }

  private FundingSubType buildFundingSubType(String code, String label) {
    FundingType fundingType = new FundingType();
    fundingType.setId(FUNDING_TYPE_ID);

    FundingSubType fundingSubType = new FundingSubType();
    fundingSubType.setCode(code);
    fundingSubType.setLabel(label);
    fundingSubType.setStatus(Status.CURRENT);
    fundingSubType.setFundingType(fundingType);
    return fundingSubType;
  }
}
