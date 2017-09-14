package com.transformuk.hee.tis.reference.client.impl;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

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
  private static final String UNKNOWN_CODE = "XXX";

  private List<Long> ids = Lists.newArrayList(10L, 20L);

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
  public void shouldGetGradeExists() {
    // given
    HttpEntity<List<Long>> requestEntity = new HttpEntity<>(ids);
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
    ParameterizedTypeReference<Map<Long, Boolean>> responseType = getExistsReference();
    given(referenceRestTemplate.exchange(REFERENCE_URL + "/api/grades/exists/",
        HttpMethod.POST, requestEntity, responseType)).
        willReturn(responseEntity);

    // when
    referenceServiceImpl.gradeExists(ids);

    // then
    verify(referenceRestTemplate).exchange(REFERENCE_URL + "/api/grades/exists/",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldGetSiteExists() {
    // given
    HttpEntity<List<Long>> requestEntity = new HttpEntity<>(ids);
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
    ParameterizedTypeReference<Map<Long, Boolean>> responseType = getExistsReference();
    given(referenceRestTemplate.exchange(REFERENCE_URL + "/api/sites/exists/",
        HttpMethod.POST, requestEntity, responseType)).
        willReturn(responseEntity);

    // when
    referenceServiceImpl.siteExists(ids);

    // then
    verify(referenceRestTemplate).exchange(REFERENCE_URL + "/api/sites/exists/",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldGetTrustExists() {
    // given
    HttpEntity<List<Long>> requestEntity = new HttpEntity<>(ids);
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
    ParameterizedTypeReference<Map<Long, Boolean>> responseType = getExistsReference();
    given(referenceRestTemplate.exchange(REFERENCE_URL + "/api/trusts/exists/",
        HttpMethod.POST, requestEntity, responseType)).
        willReturn(responseEntity);

    // when
    referenceServiceImpl.trustExists(ids);

    // then
    verify(referenceRestTemplate).exchange(REFERENCE_URL + "/api/trusts/exists/",
        HttpMethod.POST, requestEntity, responseType);
  }

  private ParameterizedTypeReference<Map<Long, Boolean>> getExistsReference() {
    return new ParameterizedTypeReference<Map<Long, Boolean>>() {
    };
  }

  private ParameterizedTypeReference<HttpStatus>  getCodeExistsReference() {
    return new ParameterizedTypeReference<HttpStatus>() {
    };
  }

  @Test
  public void shouldGetSiteCodeExists() {
    // given
    HttpEntity<String> requestEntity = new HttpEntity<>(SITE_CODE);
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.FOUND);
    ParameterizedTypeReference<HttpStatus> responseType = getCodeExistsReference();
    given(referenceRestTemplate.exchange(REFERENCE_URL + "/api/sites/codeexists/",
        HttpMethod.POST, requestEntity, responseType)).
        willReturn(responseEntity);

    // when
    referenceServiceImpl.siteCodeExists(SITE_CODE);

    // then
    verify(referenceRestTemplate).exchange(REFERENCE_URL + "/api/sites/codeexists/",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldGetSiteCodeDoesNotExist() {
    // given
    HttpEntity<String> requestEntity = new HttpEntity<>(UNKNOWN_CODE);
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
    ParameterizedTypeReference<HttpStatus> responseType = getCodeExistsReference();
    given(referenceRestTemplate.exchange(REFERENCE_URL + "/api/sites/codeexists/",
        HttpMethod.POST, requestEntity, responseType)).
        willReturn(responseEntity);

    // when
    referenceServiceImpl.siteCodeExists(UNKNOWN_CODE);

    // then
    verify(referenceRestTemplate).exchange(REFERENCE_URL + "/api/sites/codeexists/",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldGetTrustCodeExists() {
    // given
    HttpEntity<String> requestEntity = new HttpEntity<>(TRUST_CODE);
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.FOUND);
    ParameterizedTypeReference<HttpStatus> responseType = getCodeExistsReference();
    given(referenceRestTemplate.exchange(REFERENCE_URL + "/api/trusts/codeexists/",
        HttpMethod.POST, requestEntity, responseType)).
        willReturn(responseEntity);

    // when
    referenceServiceImpl.trustCodeExists(TRUST_CODE);

    // then
    verify(referenceRestTemplate).exchange(REFERENCE_URL + "/api/trusts/codeexists/",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldGetTrustCodeDoesNotExist() {
    // given
    HttpEntity<String> requestEntity = new HttpEntity<>(UNKNOWN_CODE);
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
    ParameterizedTypeReference<HttpStatus> responseType = getCodeExistsReference();
    given(referenceRestTemplate.exchange(REFERENCE_URL + "/api/trusts/codeexists/",
        HttpMethod.POST, requestEntity, responseType)).
        willReturn(responseEntity);

    // when
    referenceServiceImpl.trustCodeExists(UNKNOWN_CODE);

    // then
    verify(referenceRestTemplate).exchange(REFERENCE_URL + "/api/trusts/codeexists/",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldGetSiteTrustMatch() {
    // given
    HttpEntity<String> requestEntity = new HttpEntity<>(SITE_CODE);
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.FOUND);
    ParameterizedTypeReference<HttpStatus> responseType = getCodeExistsReference();
    given(referenceRestTemplate.exchange(REFERENCE_URL + "/api/sites/trustmatch/" + TRUST_CODE,
        HttpMethod.POST, requestEntity, responseType)).willReturn(responseEntity);

    // when
    referenceServiceImpl.siteTrustMatch(SITE_CODE, TRUST_CODE);

    // then
    verify(referenceRestTemplate).exchange(REFERENCE_URL + "/api/sites/trustmatch/" + TRUST_CODE, HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldNotGetSiteTrustMatchIfThereIsNoSiteTrustRelationship() {
    // given
    HttpEntity<String> requestEntity = new HttpEntity<>(UNKNOWN_CODE);
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
    ParameterizedTypeReference<HttpStatus> responseType = getCodeExistsReference();
    given(referenceRestTemplate.exchange(REFERENCE_URL + "/api/sites/trustmatch/" + TRUST_CODE,
        HttpMethod.POST, requestEntity, responseType)).willReturn(responseEntity);

    // when
    referenceServiceImpl.siteTrustMatch(UNKNOWN_CODE, TRUST_CODE);

    // then
    verify(referenceRestTemplate).exchange(REFERENCE_URL + "/api/sites/trustmatch/" + TRUST_CODE, HttpMethod.POST, requestEntity, responseType);
  }

}