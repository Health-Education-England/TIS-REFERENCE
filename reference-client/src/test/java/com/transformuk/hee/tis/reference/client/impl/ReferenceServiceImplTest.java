package com.transformuk.hee.tis.reference.client.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.AssessmentTypeDto;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import com.transformuk.hee.tis.reference.api.dto.EthnicOriginDTO;
import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.api.dto.GdcStatusDTO;
import com.transformuk.hee.tis.reference.api.dto.GenderDTO;
import com.transformuk.hee.tis.reference.api.dto.GmcStatusDTO;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.LocalOfficeDTO;
import com.transformuk.hee.tis.reference.api.dto.MaritalStatusDTO;
import com.transformuk.hee.tis.reference.api.dto.NationalityDTO;
import com.transformuk.hee.tis.reference.api.dto.PermitToWorkDTO;
import com.transformuk.hee.tis.reference.api.dto.QualificationReferenceDTO;
import com.transformuk.hee.tis.reference.api.dto.QualificationTypeDTO;
import com.transformuk.hee.tis.reference.api.dto.ReligiousBeliefDTO;
import com.transformuk.hee.tis.reference.api.dto.RoleDTO;
import com.transformuk.hee.tis.reference.api.dto.SexualOrientationDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.api.dto.TitleDTO;
import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RunWith(BlockJUnit4ClassRunner.class)
public class ReferenceServiceImplTest {

  private static final double STANDARD_RATE_LIMIT = 50.0d;
  private static final double BULK_RATE_LIMIT = 5.0d;
  private static final String DBC = "1-DGBODY";
  private static final String REFERENCE_URL = "http://localhost:8088/reference";
  private static final String TRUST_CODE = "RJ7";
  private static final String SITE_CODE = "RJ706";
  private static final String UNKNOWN_CODE = "XXX";

  private final List<String> ids = Lists.newArrayList("SSL", "ADL");
  private final List<String> medicalSchoolValues = Lists
      .newArrayList("University of London", "United Medical & Dental School, London");
  private final List<String> countryValues = Lists.newArrayList("United Kingdom");

  @Mock
  private RestTemplate referenceRestTemplate;
  @InjectMocks
  private ReferenceServiceImpl referenceServiceImpl;

  @Before
  public void setUp() {
    referenceServiceImpl = new ReferenceServiceImpl(STANDARD_RATE_LIMIT, BULK_RATE_LIMIT);
    referenceServiceImpl.setServiceUrl(REFERENCE_URL);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void shouldGetDBCByCode() {
    // given
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
    given(referenceRestTemplate.getForEntity(eq(REFERENCE_URL + "/api/dbcs/code/" + DBC), any()))
        .willReturn(responseEntity);

    // when
    referenceServiceImpl.getDBCByCode(DBC);

    // then
    verify(referenceRestTemplate)
        .getForEntity(eq(REFERENCE_URL + "/api/dbcs/code/" + DBC), eq(DBCDTO.class));
  }

  @Test
  public void shouldFindSitesIn() {
    // given
    Set<String> codes = Sets.newHashSet("code1", "code2");
    List<SiteDTO> sites = new ArrayList<>();
    ResponseEntity<List<SiteDTO>> responseEntity = new ResponseEntity(sites, HttpStatus.OK);
    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), isNull(RequestEntity.class),
            any(ParameterizedTypeReference.class))).willReturn(responseEntity);

    // when
    List<SiteDTO> respList = referenceServiceImpl.findSitesIn(codes);

    // then
    verify(referenceRestTemplate).exchange(eq(REFERENCE_URL + "/api/sites/in/code2,code1"),
        eq(HttpMethod.GET), isNull(RequestEntity.class), any(ParameterizedTypeReference.class));
    assertEquals(sites, respList);
  }

  @Test
  public void shouldHandleNotFoundWhenFindSitesIdIn() {
    // given
    Set<Long> ids = Sets.newHashSet(1L, 2L);
    given(referenceRestTemplate
        .exchange(any(URI.class), eq(HttpMethod.GET), eq(null),
            any(ParameterizedTypeReference.class)))
        .willThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", null, null, null));

    // when
    List<SiteDTO> respList = referenceServiceImpl.findSitesIdIn(ids);

    // then
    verify(referenceRestTemplate).exchange(any(URI.class),
        eq(HttpMethod.GET), isNull(RequestEntity.class), any(ParameterizedTypeReference.class));
    assertEquals(0, respList.size());
  }

  @Test
  public void shouldHandleOtherExceptionWhenFindSitesIdIn() {
    // given
    Set<Long> ids = Sets.newHashSet(1L, 2L);
    given(referenceRestTemplate
        .exchange(any(URI.class), eq(HttpMethod.GET), eq(null),
            any(ParameterizedTypeReference.class)))
        .willThrow(new RuntimeException("Don't panic! this is an expected exception"));

    // when
    List<SiteDTO> respList = referenceServiceImpl.findSitesIdIn(ids);

    // then
    verify(referenceRestTemplate).exchange(any(URI.class),
        eq(HttpMethod.GET), isNull(RequestEntity.class), any(ParameterizedTypeReference.class));
    assertEquals(0, respList.size());
  }

  @Test
  public void shouldHandleNotFoundWhenFindGradesIdIn() {
    // given
    Set<Long> ids = Sets.newHashSet(1L, 2L);
    given(referenceRestTemplate
        .exchange(any(URI.class), eq(HttpMethod.GET), eq(null),
            any(ParameterizedTypeReference.class)))
        .willThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", null, null, null));

    // when
    List<GradeDTO> respList = referenceServiceImpl.findGradesIdIn(ids);

    // then
    verify(referenceRestTemplate).exchange(any(URI.class),
        eq(HttpMethod.GET), isNull(RequestEntity.class), any(ParameterizedTypeReference.class));
    assertEquals(0, respList.size());
  }

  @Test
  public void shouldHandleOtherExceptionWhenFindGradesIdIn() {
    // given
    Set<Long> ids = Sets.newHashSet(1L, 2L);
    given(referenceRestTemplate
        .exchange(any(URI.class), eq(HttpMethod.GET), eq(null),
            any(ParameterizedTypeReference.class)))
        .willThrow(new RuntimeException("Don't panic! this is an expected exception"));

    // when
    List<GradeDTO> respList = referenceServiceImpl.findGradesIdIn(ids);

    // then
    verify(referenceRestTemplate).exchange(any(URI.class),
        eq(HttpMethod.GET), isNull(RequestEntity.class), any(ParameterizedTypeReference.class));
    assertEquals(0, respList.size());
  }

  @Test
  public void shouldFindSiteByName() {
    // given
    String siteNameWithSpecialCharacters = "siteNameWithSpecialCharacters@!£&£$%@/\\";
    SiteDTO siteDTO = new SiteDTO();
    siteDTO.setSiteName(siteNameWithSpecialCharacters);
    List<SiteDTO> sites = Collections.singletonList(siteDTO);

    ResponseEntity<List<SiteDTO>> responseEntity = new ResponseEntity(sites, HttpStatus.OK);
    given(referenceRestTemplate.exchange(anyString(),
        any(HttpMethod.class), isNull(RequestEntity.class),
        Matchers.<ParameterizedTypeReference<java.util.List<com.transformuk.hee.tis.reference.api.dto.SiteDTO>>>any()))
        .willReturn(responseEntity);

    // when
    List<SiteDTO> respList = referenceServiceImpl.findSitesByName(siteNameWithSpecialCharacters);

    // then
    verify(referenceRestTemplate).exchange(eq(REFERENCE_URL
            + "/api/sites?columnFilters=%7B%22siteKnownAs%22%3A%5B%22siteNameWithSpecialCharacters%40%21%C2%A3%26%C2%A3%24%25%40%2F%5C%22%5D%2C%22status%22%3A%5B%22CURRENT%22%5D%7D"),
        eq(HttpMethod.GET), isNull(RequestEntity.class),
        Matchers.<ParameterizedTypeReference<java.util.List<com.transformuk.hee.tis.reference.api.dto.SiteDTO>>>any());
    assertEquals(sites, respList);
  }

  @Test
  public void shouldFindGradesIn() {
    // given
    Set<String> codes = Sets.newHashSet("code1", "code2");
    List<GradeDTO> grades = new ArrayList<>();
    ResponseEntity<List<GradeDTO>> responseEntity = new ResponseEntity(grades, HttpStatus.OK);
    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), isNull(RequestEntity.class),
            any(ParameterizedTypeReference.class))).willReturn(responseEntity);

    // when
    List<GradeDTO> respList = referenceServiceImpl.findGradesIn(codes);

    // then
    verify(referenceRestTemplate).exchange(eq(REFERENCE_URL + "/api/grades/in/code2,code1"),
        eq(HttpMethod.GET), isNull(RequestEntity.class), any(ParameterizedTypeReference.class));
    assertEquals(grades, respList);
  }

  @Test
  public void shouldFindGradeByName() {
    // given
    String gradeNameWithSpecialCharacters = "gradeNameWithSpecialCharacters@!£&£$%@/\\";
    GradeDTO gradeDTO = new GradeDTO();
    gradeDTO.setName(gradeNameWithSpecialCharacters);
    List<GradeDTO> grades = Collections.singletonList(gradeDTO);

    ResponseEntity<List<GradeDTO>> responseEntity = new ResponseEntity(grades, HttpStatus.OK);
    given(referenceRestTemplate.exchange(anyString(),
        any(HttpMethod.class), isNull(RequestEntity.class),
        Matchers.<ParameterizedTypeReference<java.util.List<com.transformuk.hee.tis.reference.api.dto.GradeDTO>>>any()))
        .willReturn(responseEntity);

    // when
    List<GradeDTO> respList = referenceServiceImpl.findGradesByName(gradeNameWithSpecialCharacters);

    // then
    verify(referenceRestTemplate).exchange(eq(REFERENCE_URL
            + "/api/grades?columnFilters=%7B%22name%22%3A%5B%22gradeNameWithSpecialCharacters%40%21%C2%A3%26%C2%A3%24%25%40%2F%5C%22%5D%2C%22status%22%3A%5B%22CURRENT%22%5D%7D"),
        eq(HttpMethod.GET), isNull(RequestEntity.class),
        Matchers.<ParameterizedTypeReference<java.util.List<com.transformuk.hee.tis.reference.api.dto.GradeDTO>>>any());
    assertEquals(grades, respList);
  }

  @Test
  public void shouldFindCurrentPlacementAndTrainingGrades() {
    // given
    List<GradeDTO> gradesCurrentPlacementAndTraining = Collections.singletonList(new GradeDTO());

    ResponseEntity<List<GradeDTO>> responseEntity = new ResponseEntity(gradesCurrentPlacementAndTraining, HttpStatus.OK);
    given(referenceRestTemplate.exchange(anyString(),
            any(HttpMethod.class), isNull(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<java.util.List<com.transformuk.hee.tis.reference.api.dto.GradeDTO>>>any()))
            .willReturn(responseEntity);

    // when
    List<GradeDTO> respList = referenceServiceImpl.findGradesCurrentPlacementAndTrainingGrades();

    // then
    verify(referenceRestTemplate).exchange(eq(REFERENCE_URL
                    + "/api/grades?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%2C+%22placementGrade%22%3A+%5B%22true%22%5D%2C+%22trainingGrade%22%3A+%5B%22true%22%5D%7D"),
            eq(HttpMethod.GET), isNull(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<java.util.List<com.transformuk.hee.tis.reference.api.dto.GradeDTO>>>any());
    assertEquals(gradesCurrentPlacementAndTraining, respList);
  }

  @Test
  public void shouldFindTrustByTrustKnownAs() {
    // given
    String trustNameWithSpecialCharacters = "trustNameWithSpecialCharacters@!£&£$%@/\\";
    TrustDTO trustDto = new TrustDTO();
    trustDto.setTrustKnownAs(trustNameWithSpecialCharacters);
    List<TrustDTO> trusts = Collections.singletonList(trustDto);

    ResponseEntity<List<TrustDTO>> responseEntity = new ResponseEntity(trusts, HttpStatus.OK);
    given(referenceRestTemplate.exchange(anyString(),
        any(HttpMethod.class), isNull(RequestEntity.class),
        Matchers.<ParameterizedTypeReference<java.util.List<TrustDTO>>>any()))
        .willReturn(responseEntity);

    // when
    List<TrustDTO> respList = referenceServiceImpl
        .findTrustByTrustKnownAs(trustNameWithSpecialCharacters);

    // then
    verify(referenceRestTemplate).exchange(eq(REFERENCE_URL
            + "/api/trusts?columnFilters=%7B%22trustKnownAs%22%3A%5B%22trustNameWithSpecialCharacters%40%21%C2%A3%26%C2%A3%24%25%40%2F%5C%22%5D%2C%22status%22%3A%5B%22CURRENT%22%5D%7D"),
        eq(HttpMethod.GET), isNull(RequestEntity.class),
        Matchers.<ParameterizedTypeReference<java.util.List<com.transformuk.hee.tis.reference.api.dto.SiteDTO>>>any());
    assertEquals(trusts, respList);
  }

  @Test
  public void shouldFindTrustById() {
    //given
    Long id = 10L;
    TrustDTO trustDTO = new TrustDTO();
    trustDTO.setId(id);

    ResponseEntity<TrustDTO> responseEntity = new ResponseEntity(trustDTO, HttpStatus.OK);
    given(referenceRestTemplate.getForEntity(anyString(), eq(TrustDTO.class)))
        .willReturn(responseEntity);

    //when
    TrustDTO response = referenceServiceImpl.findTrustById(id);

    //then
    verify(referenceRestTemplate).getForEntity(REFERENCE_URL + "/api/trusts/" + id,
        TrustDTO.class);
    assertEquals(trustDTO, response);
  }

  @Test(expected = RestClientException.class)
  public void shouldErrorWhenNotFound() {
    //given
    Long id = 10L;
    TrustDTO trustDTO = new TrustDTO();
    trustDTO.setId(id);

    given(referenceRestTemplate.getForEntity(anyString(), eq(TrustDTO.class)))
        .willThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    //when
    TrustDTO response = referenceServiceImpl.findTrustById(id);
  }

  @Test
  public void shouldFindLocalOfficesByName() {
    // given
    String localOfficeNameWithSpecialCharacters = "localOfficeNameWithSpecialCharacters@!£&£$%@/\\";
    LocalOfficeDTO localOfficeDTO = new LocalOfficeDTO();
    localOfficeDTO.setName(localOfficeNameWithSpecialCharacters);
    List<LocalOfficeDTO> localOffices = Collections.singletonList(localOfficeDTO);

    ResponseEntity<List<LocalOfficeDTO>> responseEntity = new ResponseEntity(localOffices,
        HttpStatus.OK);
    given(referenceRestTemplate.exchange(anyString(),
        any(HttpMethod.class), isNull(RequestEntity.class),
        Matchers.<ParameterizedTypeReference<java.util.List<LocalOfficeDTO>>>any()))
        .willReturn(responseEntity);

    // when
    List<LocalOfficeDTO> respList = referenceServiceImpl
        .findLocalOfficesByName(localOfficeNameWithSpecialCharacters);

    // then
    verify(referenceRestTemplate).exchange(eq(REFERENCE_URL
            + "/api/local-offices?columnFilters=%7B%22name%22%3A%5B%22localOfficeNameWithSpecialCharacters%40%21%C2%A3%26%C2%A3%24%25%40%2F%5C%22%5D%2C%22status%22%3A%5B%22CURRENT%22%5D%7D"),
        eq(HttpMethod.GET), isNull(RequestEntity.class),
        Matchers.<ParameterizedTypeReference<java.util.List<com.transformuk.hee.tis.reference.api.dto.SiteDTO>>>any());
    assertEquals(localOffices, respList);
  }

  @Test
  public void shouldGetGradeExists() {
    // given
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(ids);
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
    ParameterizedTypeReference<Map<String, Boolean>> responseType = getExistsReference();
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
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(ids);
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
    ParameterizedTypeReference<Map<String, Boolean>> responseType = getExistsReference();
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
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(ids);
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
    ParameterizedTypeReference<Map<String, Boolean>> responseType = getExistsReference();
    given(referenceRestTemplate.exchange(REFERENCE_URL + "/api/trusts/exists/",
        HttpMethod.POST, requestEntity, responseType)).
        willReturn(responseEntity);

    // when
    referenceServiceImpl.trustExists(ids);

    // then
    verify(referenceRestTemplate).exchange(REFERENCE_URL + "/api/trusts/exists/",
        HttpMethod.POST, requestEntity, responseType);
  }

  private ParameterizedTypeReference<Map<String, Boolean>> getExistsReference() {
    return new ParameterizedTypeReference<Map<String, Boolean>>() {
    };
  }

  private ParameterizedTypeReference<Map<String, Boolean>> getExistsStringReference() {
    return new ParameterizedTypeReference<Map<String, Boolean>>() {
    };
  }

  private ParameterizedTypeReference<Map<String, String>> getMatchesStringReference() {
    return new ParameterizedTypeReference<Map<String, String>>() {
    };
  }

  private ParameterizedTypeReference<HttpStatus> getCodeExistsReference() {
    return new ParameterizedTypeReference<HttpStatus>() {
    };
  }

  private ParameterizedTypeReference<Boolean> getValueExistsReference() {
    return new ParameterizedTypeReference<Boolean>() {
    };
  }

  private ParameterizedTypeReference<List<AssessmentTypeDto>> getAssessmentTypes() {
    return new ParameterizedTypeReference<List<AssessmentTypeDto>>() {
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
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/sites/trustmatch/" + TRUST_CODE, HttpMethod.POST,
            requestEntity, responseType);
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
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/sites/trustmatch/" + TRUST_CODE, HttpMethod.POST,
            requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyEthnicOriginExists() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(EthnicOriginDTO.class, "code123");

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/ethnic-origins/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckAnyEthnicOriginExistsWhenNotCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(EthnicOriginDTO.class, "code123", false);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/ethnic-origins/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckCurrentEthnicOriginExistsWhenCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(EthnicOriginDTO.class, "code123", true);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate).exchange(REFERENCE_URL
            + "/api/ethnic-origins/exists/?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyGdcStatusExists() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(GdcStatusDTO.class, "code123");

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/gdc-statuses/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckAnyGdcStatusExistsWhenNotCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(GdcStatusDTO.class, "code123", false);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/gdc-statuses/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckCurrentGdcStatusExistsWhenCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(GdcStatusDTO.class, "code123", true);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate).exchange(REFERENCE_URL
            + "/api/gdc-statuses/exists/?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyGenderExists() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(GenderDTO.class, "code123");

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/genders/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckAnyGenderExistsWhenNotCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(GenderDTO.class, "code123", false);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/genders/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckCurrentGenderExistsWhenCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(GenderDTO.class, "code123", true);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate).exchange(REFERENCE_URL
            + "/api/genders/exists/?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyGmcStatusExists() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(GmcStatusDTO.class, "code123");

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/gmc-statuses/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckAnyGmcStatusExistsWhenNotCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(GmcStatusDTO.class, "code123", false);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/gmc-statuses/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckCurrentGmcStatusExistsWhenCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(GmcStatusDTO.class, "code123", true);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate).exchange(REFERENCE_URL
            + "/api/gmc-statuses/exists/?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyMaritalStatusExists() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(MaritalStatusDTO.class, "code123");

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/marital-statuses/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckAnyMaritalStatusExistsWhenNotCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(MaritalStatusDTO.class, "code123", false);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/marital-statuses/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckCurrentMaritalStatusExistsWhenCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(MaritalStatusDTO.class, "code123", true);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate).exchange(REFERENCE_URL
            + "/api/marital-statuses/exists/?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyNationalityExists() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(NationalityDTO.class, "code123");

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/nationalities/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckAnyNationalityExistsWhenNotCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(NationalityDTO.class, "code123", false);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/nationalities/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckCurrentNationalityExistsWhenCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(NationalityDTO.class, "code123", true);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate).exchange(REFERENCE_URL
            + "/api/nationalities/exists/?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyPermitToWorkExists() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(PermitToWorkDTO.class, "code123");

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/permit-to-works/exists/", HttpMethod.POST,
            requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyPermitToWorkExistsWhenNotCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(PermitToWorkDTO.class, "code123", false);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/permit-to-works/exists/", HttpMethod.POST,
            requestEntity, responseType);
  }

  @Test
  public void shouldCheckCurrentPermitToWorkExistsWhenCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(PermitToWorkDTO.class, "code123", true);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate).exchange(REFERENCE_URL
            + "/api/permit-to-works/exists/?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyQualificationReferenceExists() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(QualificationReferenceDTO.class, "code123");

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/qualification-reference/exists/", HttpMethod.POST,
            requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyQualificationReferenceExistsWhenNotCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists =
        referenceServiceImpl.isValueExists(QualificationReferenceDTO.class, "code123", false);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/qualification-reference/exists/", HttpMethod.POST,
            requestEntity, responseType);
  }

  @Test
  public void shouldCheckCurrentQualificationReferenceExistsWhenCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists =
        referenceServiceImpl.isValueExists(QualificationReferenceDTO.class, "code123", true);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate).exchange(REFERENCE_URL
            + "/api/qualification-reference/exists/?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyQualificationTypeExists() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(QualificationTypeDTO.class, "code123");

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/qualification-types/exists/", HttpMethod.POST,
            requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyQualificationTypeExistsWhenNotCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists =
        referenceServiceImpl.isValueExists(QualificationTypeDTO.class, "code123", false);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/qualification-types/exists/", HttpMethod.POST,
            requestEntity, responseType);
  }

  @Test
  public void shouldCheckCurrentQualificationTypeExistsWhenCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists =
        referenceServiceImpl.isValueExists(QualificationTypeDTO.class, "code123", true);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate).exchange(REFERENCE_URL
            + "/api/qualification-types/exists/?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyReligiousBeliefExists() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(ReligiousBeliefDTO.class, "code123");

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/religious-beliefs/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckAnyReligiousBeliefExistsWhenNotCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(ReligiousBeliefDTO.class, "code123", false);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/religious-beliefs/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckCurrentReligiousBeliefExistsWhenCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(ReligiousBeliefDTO.class, "code123", true);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate).exchange(REFERENCE_URL
            + "/api/religious-beliefs/exists/?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnySexualOrientationExists() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(SexualOrientationDTO.class, "code123");

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/sexual-orientations/exists/", HttpMethod.POST,
            requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnySexualOrientationExistsWhenNotCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists =
        referenceServiceImpl.isValueExists(SexualOrientationDTO.class, "code123", false);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/sexual-orientations/exists/", HttpMethod.POST,
            requestEntity, responseType);
  }

  @Test
  public void shouldCheckCurrentSexualOrientationExistsWhenCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists =
        referenceServiceImpl.isValueExists(SexualOrientationDTO.class, "code123", true);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate).exchange(REFERENCE_URL
            + "/api/sexual-orientations/exists/?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyTitleExists() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(TitleDTO.class, "code123");

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/titles/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckAnyTitleExistsWhenNotCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(TitleDTO.class, "code123", false);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/titles/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckCurrentTitleExistsWhenCurrentOnly() {
    // Given.
    HttpEntity<String> requestEntity = new HttpEntity<>("code123");
    ParameterizedTypeReference<Boolean> responseType = getValueExistsReference();

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Boolean>>any()))
        .willReturn(ResponseEntity.ok(true));

    // When.
    Boolean exists = referenceServiceImpl.isValueExists(TitleDTO.class, "code123", true);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists, is(true));
    verify(referenceRestTemplate).exchange(REFERENCE_URL
            + "/api/titles/exists/?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyRolesExistsWhenNotCurrentOnly() {
    // Given.
    List<String> codes = Arrays.asList("code1", "code2");
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(codes);
    ParameterizedTypeReference<Map<String, Boolean>> responseType = getExistsStringReference();

    Map<String, Boolean> response = new HashMap<>();
    response.put("code1", true);
    response.put("code2", false);

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Map<String, Boolean>>>any()))
        .willReturn(ResponseEntity.ok(response));

    // When.
    Map<String, Boolean> exists = referenceServiceImpl.rolesExist(codes, false);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists.get("code1"), is(true));
    assertThat("Unexpected 'exists' result value.", exists.get("code2"), is(false));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/roles/exists/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckCurrentRolesExistsWhenCurrentOnly() {
    // Given.
    List<String> codes = Arrays.asList("code1", "code2");
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(codes);
    ParameterizedTypeReference<Map<String, Boolean>> responseType = getExistsStringReference();

    Map<String, Boolean> response = new HashMap<>();
    response.put("code1", true);
    response.put("code2", false);

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Map<String, Boolean>>>any()))
        .willReturn(ResponseEntity.ok(response));

    // When.
    Map<String, Boolean> exists = referenceServiceImpl.rolesExist(codes, true);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists.get("code1"), is(true));
    assertThat("Unexpected 'exists' result value.", exists.get("code2"), is(false));
    verify(referenceRestTemplate).exchange(
        REFERENCE_URL + "/api/roles/exists/?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyRolesMatchesWhenNotCurrentOnly() {
    // Given.
    List<String> codes = Arrays.asList("code1", "code2");
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(codes);
    ParameterizedTypeReference<Map<String, String>> responseType = getMatchesStringReference();

    Map<String, String> response = new HashMap<>();
    response.put("code1", "code1");
    response.put("code2", "");

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Map<String, String>>>any()))
        .willReturn(ResponseEntity.ok(response));

    // When.
    Map<String, String> matches = referenceServiceImpl.rolesMatch(codes, false);

    // Then.
    assertThat("Unexpected 'matches' result value.", matches.get("code1"), is("code1"));
    assertThat("Unexpected 'matches' result value.", matches.get("code2"), is(""));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/roles/matches/", HttpMethod.POST, requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckCurrentRolesMatchesWhenCurrentOnly() {
    // Given.
    List<String> codes = Arrays.asList("code1", "code2");
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(codes);
    ParameterizedTypeReference<Map<String, String>> responseType = getMatchesStringReference();

    Map<String, String> response = new HashMap<>();
    response.put("code1", "code1");
    response.put("code2", "");

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Map<String, String>>>any()))
        .willReturn(ResponseEntity.ok(response));

    // When.
    Map<String, String> matches = referenceServiceImpl.rolesMatch(codes, true);

    // Then.
    assertThat("Unexpected 'matches' result value.", matches.get("code1"), is("code1"));
    assertThat("Unexpected 'matches' result value.", matches.get("code2"), is(""));
    verify(referenceRestTemplate).exchange(
        REFERENCE_URL + "/api/roles/matches/?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldFindRolesIn() {
    // given
    String codes = "code1,code2";
    List<RoleDTO> roleDtos = new ArrayList<>();
    ResponseEntity<List<SiteDTO>> responseEntity = new ResponseEntity(roleDtos, HttpStatus.OK);
    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), isNull(RequestEntity.class),
            any(ParameterizedTypeReference.class))).willReturn(responseEntity);

    // when
    List<RoleDTO> respList = referenceServiceImpl.findRolesIn(codes);

    //then
    verify(referenceRestTemplate).exchange(eq(REFERENCE_URL + "/api/roles/codes?codes=" + codes),
        eq(HttpMethod.GET), isNull(RequestEntity.class), any(ParameterizedTypeReference.class));
    assertEquals(roleDtos, respList);
  }

  @Test
  public void shouldGetAllAssessmentTypes() {
    // Given.
    ParameterizedTypeReference<List<AssessmentTypeDto>> responseType = getAssessmentTypes();
    List<AssessmentTypeDto> assessmentTypeDtos = new ArrayList<>();
    AssessmentTypeDto assessmentType = new AssessmentTypeDto();
    assessmentType.setId(1L);
    assessmentType.setLabel("ARCP");
    assessmentType.setStatus(Status.CURRENT);
    assessmentTypeDtos.add(assessmentType);
    ResponseEntity<List<AssessmentTypeDto>> responseEntity = new ResponseEntity<>(
        assessmentTypeDtos, HttpStatus.OK);
    given(referenceRestTemplate.exchange(anyString(), any(HttpMethod.class),
        isNull(RequestEntity.class), any(ParameterizedTypeReference.class))).willReturn(
        responseEntity);

    // When.
    List<AssessmentTypeDto> respList = referenceServiceImpl.findAllAssessmentTypes();

    // Then.
    assertThat("Unexpected 'AssessmentType' result value.", respList.get(0).getLabel(), is("ARCP"));
    verify(referenceRestTemplate).exchange(REFERENCE_URL
            + "/api/assessment-types?size=3000&page=0&columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.GET, null, responseType);
  }

  @Test
  public void shouldCheckAnyProgrammeMembershipTypesExistWhenNotCurrentOnly() {
    // Given.
    List<String> codes = Arrays.asList("code1", "code2");
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(codes);
    ParameterizedTypeReference<Map<String, Boolean>> responseType = getExistsStringReference();

    Map<String, Boolean> response = new HashMap<>();
    response.put("code1", true);
    response.put("code2", false);

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Map<String, Boolean>>>any()))
        .willReturn(ResponseEntity.ok(response));

    // When.
    Map<String, Boolean> exists = referenceServiceImpl.programmeMembershipTypesExist(codes, false);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists.get("code1"), is(true));
    assertThat("Unexpected 'exists' result value.", exists.get("code2"), is(false));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/programme-membership-types/exist", HttpMethod.POST,
            requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckCurrentProgrammeMembershipTypesExistWhenCurrentOnly() {
    // Given.
    List<String> codes = Arrays.asList("code1", "code2");
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(codes);
    ParameterizedTypeReference<Map<String, Boolean>> responseType = getExistsStringReference();

    Map<String, Boolean> response = new HashMap<>();
    response.put("code1", true);
    response.put("code2", false);

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Map<String, Boolean>>>any()))
        .willReturn(ResponseEntity.ok(response));

    // When.
    Map<String, Boolean> exists = referenceServiceImpl.programmeMembershipTypesExist(codes, true);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists.get("code1"), is(true));
    assertThat("Unexpected 'exists' result value.", exists.get("code2"), is(false));
    verify(referenceRestTemplate).exchange(
        REFERENCE_URL
            + "/api/programme-membership-types/exist?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldCheckAnyLeavingReasonsExistWhenNotCurrentOnly() {
    // Given.
    List<String> codes = Arrays.asList("code1", "code2");
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(codes);
    ParameterizedTypeReference<Map<String, String>> responseType = getMatchesStringReference();

    Map<String, String> response = new HashMap<>();
    response.put("code1", "CODE1");
    response.put("code2", "");

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Map<String, String>>>any()))
        .willReturn(ResponseEntity.ok(response));

    // When.
    Map<String, String> exists = referenceServiceImpl.leavingReasonsMatch(codes, false);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists.get("code1"), is("CODE1"));
    assertThat("Unexpected 'exists' result value.", exists.get("code2"), is(""));
    verify(referenceRestTemplate)
        .exchange(REFERENCE_URL + "/api/leaving-reasons/match", HttpMethod.POST,
            requestEntity,
            responseType);
  }

  @Test
  public void shouldCheckCurrentLeavingReasonsExistWhenCurrentOnly() {
    // Given.
    List<String> codes = Arrays.asList("code1", "code2");
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(codes);
    ParameterizedTypeReference<Map<String, String>> responseType = getMatchesStringReference();

    Map<String, String> response = new HashMap<>();
    response.put("code1", "CODE1");
    response.put("code2", "CODE2");

    given(referenceRestTemplate
        .exchange(anyString(), any(HttpMethod.class), any(RequestEntity.class),
            Matchers.<ParameterizedTypeReference<Map<String, String>>>any()))
        .willReturn(ResponseEntity.ok(response));

    // When.
    Map<String, String> exists = referenceServiceImpl.leavingReasonsMatch(codes, true);

    // Then.
    assertThat("Unexpected 'exists' result value.", exists.get("code1"), is("CODE1"));
    assertThat("Unexpected 'exists' result value.", exists.get("code2"), is("CODE2"));
    verify(referenceRestTemplate).exchange(
        REFERENCE_URL
            + "/api/leaving-reasons/match?columnFilters=%7B%22status%22%3A%5B%22CURRENT%22%5D%7D",
        HttpMethod.POST, requestEntity, responseType);
  }

  @Test
  public void shouldGetCurrentFundingSubTypesForFundingType() {
    // given
    FundingTypeDTO fundingTypeDto = new FundingTypeDTO();
    fundingTypeDto.setId(1L);
    FundingSubTypeDto fundingSubTypeDto = new FundingSubTypeDto();
    fundingSubTypeDto.setCode("code");
    fundingSubTypeDto.setId(UUID.randomUUID());
    fundingSubTypeDto.setLabel("label");
    fundingSubTypeDto.setStatus(Status.CURRENT);

    List<FundingSubTypeDto> expectedFundingSubTypeDtos = Arrays.asList(fundingSubTypeDto);
    ResponseEntity<List<FundingSubTypeDto>> responseEntity = new ResponseEntity(
        expectedFundingSubTypeDtos, HttpStatus.OK);
    given(referenceRestTemplate
        .exchange(anyString(), eq(HttpMethod.GET), isNull(RequestEntity.class),
            any(ParameterizedTypeReference.class))).willReturn(responseEntity);

    // When.
    List<FundingSubTypeDto> fundingSubTypeDtos =
        referenceServiceImpl.findCurrentFundingSubTypesForFundingTypeId(1L);

    //then
    verify(referenceRestTemplate).exchange(eq(REFERENCE_URL +
            "/api/funding-types/1/funding-sub-types"), eq(HttpMethod.GET),
        isNull(RequestEntity.class), any(ParameterizedTypeReference.class));
    assertEquals(expectedFundingSubTypeDtos, fundingSubTypeDtos);
  }

  @Test
  public void shouldFindFundingSubTypesByLabel() {
    // given
    String label = "label";
    FundingSubTypeDto fundingSubType = new FundingSubTypeDto();
    fundingSubType.setLabel(label);
    List<FundingSubTypeDto> fundingSubTypes = Arrays.asList(fundingSubType);

    ResponseEntity<List<FundingSubTypeDto>> responseEntity = new ResponseEntity(fundingSubTypes,
        HttpStatus.OK);
    given(referenceRestTemplate.exchange(anyString(),
        any(HttpMethod.class), isNull(RequestEntity.class),
        Matchers.<ParameterizedTypeReference<List<FundingSubTypeDto>>>any()))
        .willReturn(responseEntity);

    // when
    Set<String> labelSet = new HashSet<>();
    labelSet.add(label);
    List<FundingSubTypeDto> respList = referenceServiceImpl
        .findCurrentFundingSubTypesByLabels(labelSet);

    // then
    verify(referenceRestTemplate).exchange(eq(REFERENCE_URL +
            "/api/funding-sub-types?columnFilters="
            + "%7B%22label%22%3A%5B%22label%22%5D%2C%22status%22%3A%5B%22CURRENT%22%5D%7D"),
        eq(HttpMethod.GET), isNull(RequestEntity.class),
        Matchers.<ParameterizedTypeReference<java.util.List<com.transformuk.hee.tis.reference.api.dto.SiteDTO>>>any());
    assertEquals(fundingSubTypes, respList);
  }
}
