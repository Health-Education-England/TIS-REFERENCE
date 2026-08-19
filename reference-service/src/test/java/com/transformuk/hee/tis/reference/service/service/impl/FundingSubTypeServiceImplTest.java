package com.transformuk.hee.tis.reference.service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.transformuk.hee.tis.reference.service.model.FundingSubType;
import com.transformuk.hee.tis.reference.service.repository.FundingSubTypeRepository;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FundingSubTypeServiceImplTest {

  @Mock
  private FundingSubTypeRepository repository;

  @InjectMocks
  private FundingSubTypeServiceImpl service;

  @Test
  void shouldFindFundingSubTypesByIds() {
    // given
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    List<UUID> ids = Arrays.asList(id1, id2);

    FundingSubType fundingSubType1 = new FundingSubType();
    fundingSubType1.setId(id1);
    FundingSubType fundingSubType2 = new FundingSubType();
    fundingSubType2.setId(id2);
    List<FundingSubType> expected = Arrays.asList(fundingSubType1, fundingSubType2);

    given(repository.findByIdIn(ids)).willReturn(expected);

    // when
    List<FundingSubType> result = service.findByIds(ids);

    // then
    assertEquals(expected, result);
    verify(repository).findByIdIn(ids);
  }
}
