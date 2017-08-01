package com.transformuk.hee.tis.reference.client.impl;

import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class
)
public class ReferenceServiceImplTest {

  private static final String DBC = "1-DGBODY";
  private static final String REFERENCE_URL = "http://localhost:8088/reference";


  @Mock
  private RestTemplate referenceRestTemplate;
  @InjectMocks
  private ReferenceServiceImpl referenceServiceImpl;

  @Before
  public void setUp() throws Exception {
    referenceServiceImpl.setServiceUrl(REFERENCE_URL);
  }


  @Test
  public void shouldGetDBCByCode() {
    // given
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
    given(referenceRestTemplate.getForEntity(eq(REFERENCE_URL + "/api/dbcs/code/" + DBC), any())).willReturn(responseEntity);

    // when
    referenceServiceImpl.getDBCByCode(DBC);

    // then
    verify(referenceRestTemplate).getForEntity(eq(REFERENCE_URL + "/api/dbcs/code/" + DBC), eq(DBCDTO.class));
  }

}