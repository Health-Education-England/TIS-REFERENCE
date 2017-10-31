package com.transformuk.hee.tis.reference.client.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;

@RunWith(BlockJUnit4ClassRunner.class)
public class ReferenceServiceImplTest {

  private static final double STANDARD_RATE_LIMIT = 50.0d;
  private static final double BULK_RATE_LIMIT = 5.0d;
  private static final String DBC = "1-DGBODY";
  private static final String REFERENCE_URL = "http://localhost:8088/reference";
  private static final String TRUST_CODE = "RJ7";
  private static final String SITE_CODE = "RJ706";
  private static final String UNKNOWN_CODE = "XXX";

  private List<Long> ids = Lists.newArrayList(10L, 20L);
  private List<String> medicalSchoolValues = Lists.newArrayList("University of London", "United Medical & Dental School, London");
  private List<String> countryValues = Lists.newArrayList("United Kingdom");

  @Mock
  private RestTemplate referenceRestTemplate;
  @InjectMocks
  private ReferenceServiceImpl referenceServiceImpl;

  @Before
  public void setUp() throws Exception {
    referenceServiceImpl = new ReferenceServiceImpl(STANDARD_RATE_LIMIT, BULK_RATE_LIMIT);
    referenceServiceImpl.setServiceUrl(REFERENCE_URL);
    MockitoAnnotations.initMocks(this);
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
  public void shouldFindSitesIn() {
    // given
    Set<String> codes = Sets.newHashSet("code1", "code2");
    List<SiteDTO> sites = new ArrayList<>();
    ResponseEntity<List<SiteDTO>> responseEntity = new ResponseEntity(sites, HttpStatus.OK);
    given(referenceRestTemplate.exchange(anyString(), any(HttpMethod.class), isNull(RequestEntity.class),
        any(ParameterizedTypeReference.class))).willReturn(responseEntity);

    // when
    List<SiteDTO> respList = referenceServiceImpl.findSitesIn(codes);

    // then
    verify(referenceRestTemplate).exchange(eq(REFERENCE_URL + "/api/sites/in/code2,code1"),
        eq(HttpMethod.GET), isNull(RequestEntity.class), any(ParameterizedTypeReference.class));
    assertEquals(sites, respList);
  }

  @Test
  public void shouldFindGradesIn() {
    // given
    Set<String> codes = Sets.newHashSet("code1", "code2");
    List<GradeDTO> grades = new ArrayList<>();
    ResponseEntity<List<GradeDTO>> responseEntity = new ResponseEntity(grades, HttpStatus.OK);
    given(referenceRestTemplate.exchange(anyString(), any(HttpMethod.class), isNull(RequestEntity.class),
        any(ParameterizedTypeReference.class))).willReturn(responseEntity);

    // when
    List<GradeDTO> respList = referenceServiceImpl.findGradesIn(codes);

    // then
    verify(referenceRestTemplate).exchange(eq(REFERENCE_URL + "/api/grades/in/code2,code1"),
        eq(HttpMethod.GET), isNull(RequestEntity.class), any(ParameterizedTypeReference.class));
    assertEquals(grades, respList);
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
  public void shouldGetMedicalSchoolExists() {
    // given
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(medicalSchoolValues);
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
    ParameterizedTypeReference<Map<String, Boolean>> responseType = getExistsStringReference();
    given(referenceRestTemplate.exchange(REFERENCE_URL + "/api/medical-schools/exists/",
        HttpMethod.POST, requestEntity, responseType)).
        willReturn(responseEntity);

    // when
    referenceServiceImpl.medicalSchoolsExists(medicalSchoolValues);

    // then
    verify(referenceRestTemplate).exchange(REFERENCE_URL + "/api/medical-schools/exists/",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldGetCountriesExists() {
    // given
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(countryValues);
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
    ParameterizedTypeReference<Map<String, Boolean>> responseType = getExistsStringReference();
    given(referenceRestTemplate.exchange(REFERENCE_URL + "/api/countries/exists/",
        HttpMethod.POST, requestEntity, responseType)).
        willReturn(responseEntity);

    // when
    referenceServiceImpl.countryExists(countryValues);

    // then
    verify(referenceRestTemplate).exchange(REFERENCE_URL + "/api/countries/exists/",
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

  private ParameterizedTypeReference<Map<String, Boolean>> getExistsStringReference() {
    return new ParameterizedTypeReference<Map<String, Boolean>>() {
    };
  }

  private ParameterizedTypeReference<HttpStatus> getCodeExistsReference() {
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