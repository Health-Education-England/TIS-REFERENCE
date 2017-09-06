package com.transformuk.hee.tis.reference.client.impl;

import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import com.transformuk.hee.tis.reference.api.dto.LimitedListResponse;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
//import junit.framework.Assert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class
)
public class ReferenceServiceImplTest {

  private static final String DBC = "1-DGBODY";
  private static final String REFERENCE_URL = "http://localhost:8088/reference";
  private static final String TRUST_CODE = "RJ7";
  private static final String SITE_CODE = "RJ706";
  private static final String OTHER_SITE_CODE = "RJ720";
  private static final String UNKNOWN_CODE = "XXX";

  private static final SiteDTO TEST_SITE_DTO_1 = new SiteDTO();
  private static final SiteDTO TEST_SITE_DTO_2 = new SiteDTO();


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

  @Test
  public void shouldGetHttpStatusOkGetTrustByCode() {
    // given
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
    given(referenceRestTemplate.getForEntity(eq(REFERENCE_URL + "/api/trusts/code/" + TRUST_CODE),any())).willReturn(responseEntity);

    // when
    referenceServiceImpl.getTrustByCodeHttpStatus(TRUST_CODE);

    // then
    verify(referenceRestTemplate).getForEntity(eq(REFERENCE_URL + "/api/trusts/code/" + TRUST_CODE), eq(TrustDTO.class));
  }

  @Test
  public void shouldGetHttpStatusOkGetSiteByCode() {
    // given
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
    given(referenceRestTemplate.getForEntity(eq(REFERENCE_URL + "/api/sites/code/" + SITE_CODE),any())).willReturn(responseEntity);

    // when
    referenceServiceImpl.getSiteByCodeHttpStatus(SITE_CODE);

    // then
    verify(referenceRestTemplate).getForEntity(eq(REFERENCE_URL + "/api/sites/code/" + SITE_CODE), eq(SiteDTO.class));
  }

  @Test
  public void shouldGetHttpStatusNotFoundGetTrustByCode() {
    // given
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
    given(referenceRestTemplate.getForEntity(eq(REFERENCE_URL + "/api/trusts/code/" + UNKNOWN_CODE),any())).willReturn(responseEntity);

    // when
    referenceServiceImpl.getTrustByCodeHttpStatus(UNKNOWN_CODE);

    // then
    verify(referenceRestTemplate).getForEntity(eq(REFERENCE_URL + "/api/trusts/code/" + UNKNOWN_CODE), eq(TrustDTO.class));
  }

  @Test
  public void shouldGetHttpStatusNotFoundGetSiteByCode() {
    // given
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
    given(referenceRestTemplate.getForEntity(eq(REFERENCE_URL + "/api/sites/code/" + UNKNOWN_CODE),any())).willReturn(responseEntity);

    // when
    referenceServiceImpl.getSiteByCodeHttpStatus(UNKNOWN_CODE);

    // then
    verify(referenceRestTemplate).getForEntity(eq(REFERENCE_URL + "/api/sites/code/" + UNKNOWN_CODE), eq(SiteDTO.class));
  }

  @Test
  public void shouldGetSitesByTrustCode() {
    // given
    TEST_SITE_DTO_1.setSiteCode(SITE_CODE);
    TEST_SITE_DTO_1.setId(6344L);
    TEST_SITE_DTO_1.setTrustCode("RJ7");
    TEST_SITE_DTO_1.setSiteName("Bolingbroke Hospital");
    TEST_SITE_DTO_1.setAddress("Bolingbroke Grove London Greater London");
    TEST_SITE_DTO_1.setPostCode("SW11 6HN");

    TEST_SITE_DTO_2.setSiteCode(OTHER_SITE_CODE);
    TEST_SITE_DTO_2.setId(6346L);
    TEST_SITE_DTO_2.setTrustCode("RJ7");
    TEST_SITE_DTO_2.setSiteName("St Georges At Kingston Hospital");
    TEST_SITE_DTO_2.setAddress("Galsworthy Road Kingston Upon Thames Surrey");
    TEST_SITE_DTO_2.setPostCode("KT2 7QB");

    List<SiteDTO> siteDTOS = new ArrayList<>();
    siteDTOS.add(TEST_SITE_DTO_1);
    siteDTOS.add(TEST_SITE_DTO_2);
    LimitedListResponse<SiteDTO> dtos = new LimitedListResponse<>();
    dtos.setList(siteDTOS);
    ResponseEntity<LimitedListResponse<SiteDTO>> siteDtoResponse = new ResponseEntity<LimitedListResponse<SiteDTO>>(dtos,HttpStatus.OK);
    ParameterizedTypeReference<LimitedListResponse<SiteDTO>> responseType = new ParameterizedTypeReference<LimitedListResponse<SiteDTO>>(){

    };
    given(referenceRestTemplate.exchange(REFERENCE_URL + "/api/sites/search-by-trust/" + TRUST_CODE, HttpMethod.GET,
        null,responseType)).willReturn(siteDtoResponse);

    // when
    LimitedListResponse<SiteDTO> response = referenceServiceImpl.getSitesByTrustCode(TRUST_CODE);

    // then
    Assert.assertEquals(siteDtoResponse.getBody(), response);

  }
}