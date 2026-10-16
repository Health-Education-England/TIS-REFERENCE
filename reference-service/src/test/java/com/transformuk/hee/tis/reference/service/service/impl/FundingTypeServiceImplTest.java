package com.transformuk.hee.tis.reference.service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.service.model.FundingType;
import com.transformuk.hee.tis.reference.service.repository.FundingTypeRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FundingTypeServiceImplTest {

  @Mock
  private FundingTypeRepository repository;

  @InjectMocks
  private FundingTypeServiceImpl service;

  @Test
  void shouldFindFundingTypesByIds() {
    // given
    List<Long> ids = Arrays.asList(1L, 2L);

    FundingType fundingType1 = new FundingType();
    fundingType1.setId(1L);
    FundingType fundingType2 = new FundingType();
    fundingType2.setId(2L);
    List<FundingType> expected = Arrays.asList(fundingType1, fundingType2);

    when(repository.findAllById(ids)).thenReturn(expected);

    // when
    List<FundingType> result = service.findByIds(ids);

    // then
    assertEquals(expected, result);
    verify(repository).findAllById(ids);
  }
}
