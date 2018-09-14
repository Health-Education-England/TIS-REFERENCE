package com.transformuk.hee.tis.reference.client.impl;

import com.google.common.collect.Maps;
import com.transformuk.hee.tis.client.impl.AbstractClientService;
import com.transformuk.hee.tis.reference.api.dto.*;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The default implementation of the reference service client. Provides method for which we use to communicate with
 * the tis reference service
 */
@Service
public class ReferenceServiceImpl extends AbstractClientService implements ReferenceService {

  private static final Logger LOG = LoggerFactory.getLogger(ReferenceServiceImpl.class);
  private static final Map<Class, ParameterizedTypeReference> classToParamTypeRefMap;
  private static final String FIND_GRADES_BY_NAME_ENDPOINT = "/api/grades?columnFilters=";
  private static final String FIND_GRADES_IN_ENDPOINT = "/api/grades/in/";
  private static final String FIND_GRADES_ID_IN_ENDPOINT = "/api/grades/ids/in";
  private static final String FIND_SITES_BY_NAME_ENDPOINT = "/api/sites?columnFilters=";
  private static final String FIND_SITES_IN_ENDPOINT = "/api/sites/in/";
  private static final String FIND_SITES_ID_IN_ENDPOINT = "/api/sites/ids/in";
  private static final String FIND_ALL_LOCAL_OFFICE_ENDPOINT = "/api/local-offices";
  private static final String DBCS_MAPPINGS_ENDPOINT = "/api/dbcs/code/";
  private static final String TRUSTS_MAPPINGS_CODE_ENDPOINT = "/api/trusts/codeexists/";
  private static final String SITES_MAPPINGS_CODE_ENDPOINT = "/api/sites/codeexists/";
  private static final String SITE_TRUST_MATCH_ENDPOINT = "/api/sites/trustmatch/";
  private static final String GRADES_MAPPINGS_ENDPOINT = "/api/grades/exists/";
  private static final String GRADES_IDS_MAPPINGS_ENDPOINT = "/api/grades/ids/exists/";
  private static final String SITES_MAPPINGS_ENDPOINT = "/api/sites/exists/";
  private static final String SITES_IDS_MAPPINGS_ENDPOINT = "/api/sites/ids/exists/";
  private static final String TRUSTS_MAPPINGS_ENDPOINT = "/api/trusts/exists/";
  private static final String TRUSTS_IDS_MAPPINGS_ENDPOINT = "/api/trusts/ids/exists/";
  private static final String MEDICAL_SCHOOLS_MAPPINGS_ENDPOINT = "/api/medical-schools/exists/";
  private static final String COUNTRIES_MAPPINGS_ENDPOINT = "/api/countries/exists/";
  private static final String ROTATIONS_MAPPINGS_ENDPOINT = "/api/rotations/exists/";
  private static final String PLACEMENT_TYPES_MAPPINGS_ENDPOINT = "/api/placement-types/exists/";
  private static final String TITLE_MAPPINGS_ENDPOINT = "/api/titles/exists/";
  private static final String GMC_STATUS_MAPPINGS_ENDPOINT = "/api/gmc-statuses/exists/";
  private static final String GDC_STATUS_MAPPINGS_ENDPOINT = "/api/gdc-statuses/exists/";
  private static final String GENDER_MAPPINGS_ENDPOINT = "/api/genders/exists/";
  private static final String NATIONALITY_STATUS_MAPPINGS_ENDPOINT = "/api/nationalities/exists/";
  private static final String ETHINIC_ORIGIN_MAPPINGS_ENDPOINT = "/api/ethnic-origins/exists/";
  private static final String MARITAL_STATUS_MAPPINGS_ENDPOINT = "/api/marital-statuses/exists/";
  private static final String SEXUAL_ORIENTATION_MAPPINGS_ENDPOINT = "/api/sexual-orientations/exists/";
  private static final String RELIGIOUS_BELIF_MAPPINGS_ENDPOINT = "/api/religious-beliefs/exists/";
  private static final String QUALIFICATION_MAPPINGS_ENDPOINT = "/api/qualification-reference/exists/";
  private static final String QUALIFICATION_TYPE_MAPPINGS_ENDPOINT = "/api/qualification-types/exists/";
  private static final String ROLES_BY_ROLE_CATEGORY = "/api/roles/categories/";


  private static String sitesJsonQuerystringURLEncoded;
  private static String gradesJsonQuerystringURLEncoded;

  static {
    try {
      sitesJsonQuerystringURLEncoded = new org.apache.commons.codec.net.URLCodec().encode("{\"siteKnownAs\":[\"PARAMETER_NAME\"],\"status\":[\"CURRENT\"]}");
      gradesJsonQuerystringURLEncoded = new org.apache.commons.codec.net.URLCodec().encode("{\"name\":[\"PARAMETER_NAME\"],\"status\":[\"CURRENT\"]}");
    } catch (EncoderException e) {
      e.printStackTrace();
    }
  }

  static {
    classToParamTypeRefMap = Maps.newHashMap();
    classToParamTypeRefMap.put(CollegeDTO.class, new ParameterizedTypeReference<List<CollegeDTO>>() {
    });
    classToParamTypeRefMap.put(CountryDTO.class, new ParameterizedTypeReference<List<CountryDTO>>() {
    });
    classToParamTypeRefMap.put(CurriculumSubTypeDTO.class, new ParameterizedTypeReference<List<CurriculumSubTypeDTO>>() {
    });
    classToParamTypeRefMap.put(DBCDTO.class, new ParameterizedTypeReference<List<DBCDTO>>() {
    });
    classToParamTypeRefMap.put(EthnicOriginDTO.class, new ParameterizedTypeReference<List<EthnicOriginDTO>>() {
    });
    classToParamTypeRefMap.put(FundingIssueDTO.class, new ParameterizedTypeReference<List<FundingIssueDTO>>() {
    });
    classToParamTypeRefMap.put(FundingTypeDTO.class, new ParameterizedTypeReference<List<FundingTypeDTO>>() {
    });
    classToParamTypeRefMap.put(GdcStatusDTO.class, new ParameterizedTypeReference<List<GdcStatusDTO>>() {
    });
    classToParamTypeRefMap.put(GenderDTO.class, new ParameterizedTypeReference<List<GenderDTO>>() {
    });
    classToParamTypeRefMap.put(GmcStatusDTO.class, new ParameterizedTypeReference<List<GmcStatusDTO>>() {
    });
    classToParamTypeRefMap.put(GradeDTO.class, new ParameterizedTypeReference<List<GradeDTO>>() {
    });
    classToParamTypeRefMap.put(InactiveReasonDTO.class, new ParameterizedTypeReference<List<InactiveReasonDTO>>() {
    });
    classToParamTypeRefMap.put(LeavingDestinationDTO.class, new ParameterizedTypeReference<List<LeavingDestinationDTO>>() {
    });
    classToParamTypeRefMap.put(LocalOfficeDTO.class, new ParameterizedTypeReference<List<LocalOfficeDTO>>() {
    });
    classToParamTypeRefMap.put(MaritalStatusDTO.class, new ParameterizedTypeReference<List<MaritalStatusDTO>>() {
    });
    classToParamTypeRefMap.put(MedicalSchoolDTO.class, new ParameterizedTypeReference<List<MedicalSchoolDTO>>() {
    });
    classToParamTypeRefMap.put(NationalityDTO.class, new ParameterizedTypeReference<List<NationalityDTO>>() {
    });
    classToParamTypeRefMap.put(PlacementTypeDTO.class, new ParameterizedTypeReference<List<PlacementTypeDTO>>() {
    });
    classToParamTypeRefMap.put(ProgrammeMembershipTypeDTO.class, new ParameterizedTypeReference<List<ProgrammeMembershipTypeDTO>>() {
    });
    classToParamTypeRefMap.put(RecordTypeDTO.class, new ParameterizedTypeReference<List<RecordTypeDTO>>() {
    });
    classToParamTypeRefMap.put(ReligiousBeliefDTO.class, new ParameterizedTypeReference<List<ReligiousBeliefDTO>>() {
    });
    classToParamTypeRefMap.put(RoleDTO.class, new ParameterizedTypeReference<List<RoleDTO>>() {
    });
    classToParamTypeRefMap.put(SettledDTO.class, new ParameterizedTypeReference<List<SettledDTO>>() {
    });
    classToParamTypeRefMap.put(SexualOrientationDTO.class, new ParameterizedTypeReference<List<SexualOrientationDTO>>() {
    });
    classToParamTypeRefMap.put(SiteDTO.class, new ParameterizedTypeReference<List<SiteDTO>>() {
    });
    classToParamTypeRefMap.put(StatusDTO.class, new ParameterizedTypeReference<List<StatusDTO>>() {
    });
    classToParamTypeRefMap.put(TariffRateDTO.class, new ParameterizedTypeReference<List<TariffRateDTO>>() {
    });
    classToParamTypeRefMap.put(TitleDTO.class, new ParameterizedTypeReference<List<TitleDTO>>() {
    });
    classToParamTypeRefMap.put(TrainingNumberTypeDTO.class, new ParameterizedTypeReference<List<TrainingNumberTypeDTO>>() {
    });
    classToParamTypeRefMap.put(TrustDTO.class, new ParameterizedTypeReference<List<TrustDTO>>() {
    });
  }

  @Autowired
  private RestTemplate referenceRestTemplate;

  @Value("${reference.service.url}")
  private String serviceUrl;

  public ReferenceServiceImpl(@Value("${reference.client.rate.limit}") double standardRequestsPerSecondLimit,
                              @Value("${reference.client.bulk.rate.limit}") double bulkRequestsPerSecondLimit) {
    super(standardRequestsPerSecondLimit, bulkRequestsPerSecondLimit);
  }

  @Override
  public List<JsonPatchDTO> getJsonPathByTableDtoNameOrderByDateAddedAsc(String endpointUrl, Class objectDTO) {
    ParameterizedTypeReference<List<JsonPatchDTO>> typeReference = getJsonPatchDtoReference();
    ResponseEntity<List<JsonPatchDTO>> response = referenceRestTemplate.exchange(serviceUrl + endpointUrl + objectDTO.getSimpleName(),
        HttpMethod.GET, null, typeReference);
    return response.getBody();
  }

  private ParameterizedTypeReference<List<JsonPatchDTO>> getJsonPatchDtoReference() {
    return new ParameterizedTypeReference<List<JsonPatchDTO>>() {
    };
  }

  private <DTO> ParameterizedTypeReference<List<DTO>> getDTOReference() {
    return new ParameterizedTypeReference<List<DTO>>() {
    };
  }

  private ParameterizedTypeReference<Map<Long, Boolean>> getExistsWithLongReference() {
    return new ParameterizedTypeReference<Map<Long, Boolean>>() {
    };
  }

  private ParameterizedTypeReference<Map<String, Boolean>> getExistsReference() {
    return new ParameterizedTypeReference<Map<String, Boolean>>() {
    };
  }

  private ParameterizedTypeReference<Map<Long, Boolean>> getExistsLongReference() {
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

  private ParameterizedTypeReference<Boolean> getCodeExistsDto() {
    return new ParameterizedTypeReference<Boolean>() {
    };
  }

  private ParameterizedTypeReference<Set<DBCDTO>> getDBCDto() {
    return new ParameterizedTypeReference<Set<DBCDTO>>() {
    };
  }

  private ParameterizedTypeReference<HttpStatus> getSiteTrustMatchReference() {
    return new ParameterizedTypeReference<HttpStatus>() {
    };
  }

  private Map<Long, Boolean> existsWithLong(String url, List<Long> ids) {
    HttpEntity<List<Long>> requestEntity = new HttpEntity<>(ids);
    ParameterizedTypeReference<Map<Long, Boolean>> responseType = getExistsWithLongReference();
    ResponseEntity<Map<Long, Boolean>> responseEntity = referenceRestTemplate.exchange(url, HttpMethod.POST, requestEntity,
        responseType);
    return responseEntity.getBody();
  }

  private Map<String, Boolean> exists(String url, List<String> ids) {
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(ids);
    ParameterizedTypeReference<Map<String, Boolean>> responseType = getExistsReference();
    ResponseEntity<Map<String, Boolean>> responseEntity = referenceRestTemplate.exchange(url, HttpMethod.POST, requestEntity,
        responseType);
    return responseEntity.getBody();
  }

  private Map<Long, Boolean> idExists(String url, List<Long> ids) {
    HttpEntity<List<Long>> requestEntity = new HttpEntity<>(ids);
    ParameterizedTypeReference<Map<Long, Boolean>> responseType = getExistsLongReference();
    ResponseEntity<Map<Long, Boolean>> responseEntity = referenceRestTemplate.exchange(url, HttpMethod.POST, requestEntity,
        responseType);
    return responseEntity.getBody();
  }

  private Map<String, Boolean> valuesExists(String url, List<String> values) {
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(values);
    ParameterizedTypeReference<Map<String, Boolean>> responseType = getExistsStringReference();
    ResponseEntity<Map<String, Boolean>> responseEntity = referenceRestTemplate.exchange(url, HttpMethod.POST, requestEntity,
        responseType);
    return responseEntity.getBody();
  }

  private HttpStatus codeExists(String url, String code) {
    HttpEntity<String> requestEntity = new HttpEntity<>(code);
    ParameterizedTypeReference<HttpStatus> responseType = getCodeExistsReference();
    LOG.info("Trying codeExists with URL: {}", url);
    ResponseEntity<HttpStatus> responseEntity = referenceRestTemplate.exchange(url, HttpMethod.POST, requestEntity,
        responseType);
    return responseEntity.getStatusCode();
  }

  private Boolean codeExistsDto(String url, String code) {
    HttpEntity<String> requestEntity = new HttpEntity<>(code);
    ParameterizedTypeReference<Boolean> responseType = getCodeExistsDto();
    LOG.info("Trying codeExists with URL: {}", url);
    ResponseEntity<Boolean> responseEntity = referenceRestTemplate.exchange(url, HttpMethod.POST, requestEntity,
            responseType);
    return responseEntity.getBody();
  }

  private HttpStatus siteTrustMatchExists(String url, String siteCode) {
    HttpEntity<String> requestEntity = new HttpEntity<>(siteCode);
    ParameterizedTypeReference<HttpStatus> responseType = getSiteTrustMatchReference();
    ResponseEntity<HttpStatus> responseEntity = referenceRestTemplate.exchange(url, HttpMethod.POST, requestEntity,
        responseType);
    return responseEntity.getStatusCode();
  }

  @Cacheable("sites")
  @Override
  public List<SiteDTO> findSitesByName(String siteName) {
    LOG.debug("calling getSitesByName with {}", siteName);

    return referenceRestTemplate
        .exchange(serviceUrl + FIND_SITES_BY_NAME_ENDPOINT + sitesJsonQuerystringURLEncoded.replace("PARAMETER_NAME", urlEncode(siteName)), HttpMethod.GET, null, new ParameterizedTypeReference<List<SiteDTO>>() {})
        .getBody();
  }

  @Override
  public List<SiteDTO> findSitesIn(Set<String> codes) {
    String url = serviceUrl + FIND_SITES_IN_ENDPOINT + String.join(",", codes);
    ResponseEntity<List<SiteDTO>> responseEntity = referenceRestTemplate.
        exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<SiteDTO>>() {
        });
    return responseEntity.getBody();
  }

  @Override
  public List<SiteDTO> findSitesIdIn(Set<Long> ids) {
    String url = serviceUrl + FIND_SITES_ID_IN_ENDPOINT;
    String joinedIds = StringUtils.join(ids, ",");
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("ids", joinedIds);
    try {
      ResponseEntity<List<SiteDTO>> responseEntity = referenceRestTemplate.
          exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET, null,
              new ParameterizedTypeReference<List<SiteDTO>>() {});
      return responseEntity.getBody();
    } catch(Exception e) {
      LOG.error("Exception during find sites id in for ids [{}], returning empty list. Here's the error message {}",
          joinedIds, e.getMessage());
      return Collections.EMPTY_LIST;
    }
  }

  @Cacheable("grades")
  @Override
  public List<GradeDTO> findGradesByName(String gradeName) {
    LOG.debug("calling getGradesByName with {}", gradeName);
    return referenceRestTemplate
        .exchange(serviceUrl + FIND_GRADES_BY_NAME_ENDPOINT + gradesJsonQuerystringURLEncoded.replace("PARAMETER_NAME", urlEncode(gradeName)), HttpMethod.GET, null, new ParameterizedTypeReference<List<GradeDTO>>() {})
        .getBody();
  }

  @Override
  public List<LocalOfficeDTO> findAllLocalOffice() {
    LOG.debug("calling getGradesByName with {}");
    return referenceRestTemplate
        .exchange(serviceUrl + FIND_ALL_LOCAL_OFFICE_ENDPOINT, HttpMethod.GET, null, new ParameterizedTypeReference<List<LocalOfficeDTO>>() {})
        .getBody();
  }

  @Override
  public Collection<RoleDTO> getRolesByCategory(final Long categoryId) {
    LOG.debug("calling getRolesByCategory with {}", categoryId);

    return referenceRestTemplate
            .exchange(serviceUrl + ROLES_BY_ROLE_CATEGORY + String.valueOf(categoryId), HttpMethod.GET, null, new ParameterizedTypeReference<List<RoleDTO>>() {})
            .getBody();
  }


  private String urlEncode(String name) {
    try {
      return URLEncoder.encode(name, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("UTF-8 is unknown");
    }
  }

  @Override
  public List<GradeDTO> findGradesIn(Set<String> codes) {
    String url = serviceUrl + FIND_GRADES_IN_ENDPOINT + String.join(",", codes);
    ResponseEntity<List<GradeDTO>> responseEntity = referenceRestTemplate.
        exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<GradeDTO>>() {
        });
    return responseEntity.getBody();
  }

  @Override
  public List<GradeDTO> findGradesIdIn(Set<Long> ids) {
    String url = serviceUrl + FIND_GRADES_ID_IN_ENDPOINT;
    String joinedIds = StringUtils.join(ids, ",");
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("ids", joinedIds);
    try {
      ResponseEntity<List<GradeDTO>> responseEntity = referenceRestTemplate.
          exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<GradeDTO>>() {
          });
      return responseEntity.getBody();
    }catch (Exception e) {
      LOG.error("Exception during find grade id in for ids [{}], returning empty list. Here's the error message {}",
          joinedIds, e.getMessage());
      return Collections.EMPTY_LIST;
    }
  }

  @Override
  public ResponseEntity<DBCDTO> getDBCByCode(String code) {
    String url = serviceUrl + DBCS_MAPPINGS_ENDPOINT + code;
    ResponseEntity<DBCDTO> responseEntity = referenceRestTemplate.getForEntity(url, DBCDTO.class);
    return responseEntity;
  }

  @Override
  public Map<String, Boolean> gradeExists(List<String> ids) {
    String url = serviceUrl + GRADES_MAPPINGS_ENDPOINT;
    return exists(url, ids);
  }

  @Override
  public Map<Long, Boolean> gradeIdsExists(List<Long> ids) {
    String url = serviceUrl + GRADES_IDS_MAPPINGS_ENDPOINT;
    return idExists(url, ids);
  }

  @Override
  public Map<String, Boolean> siteExists(List<String> ids) {
    String url = serviceUrl + SITES_MAPPINGS_ENDPOINT;
    return exists(url, ids);
  }

  @Override
  public Map<Long, Boolean> siteIdExists(List<Long> ids) {
    String url = serviceUrl + SITES_IDS_MAPPINGS_ENDPOINT;
    return idExists(url, ids);
  }

  private ParameterizedTypeReference<LimitedListResponse<SiteDTO>> getSiteDtoReference() {
    return new ParameterizedTypeReference<LimitedListResponse<SiteDTO>>() {
    };
  }

  @Override
  public Map<String, Boolean> trustExists(List<String> ids) {
    String url = serviceUrl + TRUSTS_MAPPINGS_ENDPOINT;
    return exists(url, ids);
  }

  @Override
  public Map<Long, Boolean> trustIdsExists(List<Long> ids) {
    String url = serviceUrl + TRUSTS_IDS_MAPPINGS_ENDPOINT;
    return idExists(url, ids);
  }

  @Override
  public HttpStatus trustCodeExists(String code) {
    String url = serviceUrl + TRUSTS_MAPPINGS_CODE_ENDPOINT;
    return codeExists(url, code);
  }

  @Override
  public HttpStatus siteCodeExists(String code) {
    String url = serviceUrl + SITES_MAPPINGS_CODE_ENDPOINT;
    return codeExists(url, code);
  }

  @Override
  public Map<String, Boolean> medicalSchoolsExists(List<String> values) {
    String url = serviceUrl + MEDICAL_SCHOOLS_MAPPINGS_ENDPOINT;
    return valuesExists(url, values);
  }

  @Override
  public Map<String, Boolean> countryExists(List<String> values) {
    String url = serviceUrl + COUNTRIES_MAPPINGS_ENDPOINT;
    return valuesExists(url, values);
  }

  @Override
  public Map<String, Boolean> rotationExists(List<String> values) {
    String url = serviceUrl + ROTATIONS_MAPPINGS_ENDPOINT;
    return valuesExists(url, values);
  }

  @Override
  public Map<String, Boolean> placementTypeExists(List<String> values) {
    String url = serviceUrl + PLACEMENT_TYPES_MAPPINGS_ENDPOINT;
    return exists(url, values);
  }

  @Override
  public Boolean isValueExists(Class dtoClass, String value) {
    String url = serviceUrl;
    if(dtoClass.equals(TitleDTO.class)){
      url = url + TITLE_MAPPINGS_ENDPOINT;
    } else if(dtoClass.equals(GmcStatusDTO.class)){
      url = url + GMC_STATUS_MAPPINGS_ENDPOINT;
    } else if(dtoClass.equals(GdcStatusDTO.class)){
      url = url + GDC_STATUS_MAPPINGS_ENDPOINT;
    } else if(dtoClass.equals(GenderDTO.class)){
      url = url + GENDER_MAPPINGS_ENDPOINT;
    } else if(dtoClass.equals(NationalityDTO.class)){
      url = url + NATIONALITY_STATUS_MAPPINGS_ENDPOINT;
    } else if(dtoClass.equals(EthnicOriginDTO.class)){
      url= url + ETHINIC_ORIGIN_MAPPINGS_ENDPOINT;
    } else if(dtoClass.equals(MaritalStatusDTO.class)){
      url= url + MARITAL_STATUS_MAPPINGS_ENDPOINT;
    } else if(dtoClass.equals(SexualOrientationDTO.class)){
      url= url + SEXUAL_ORIENTATION_MAPPINGS_ENDPOINT;
    } else if(dtoClass.equals(ReligiousBeliefDTO.class)){
      url= url + RELIGIOUS_BELIF_MAPPINGS_ENDPOINT;
    } else if(dtoClass.equals(QualificationReferenceDTO.class)){
      url= url + QUALIFICATION_MAPPINGS_ENDPOINT;
    } else if(dtoClass.equals(QualificationTypeDTO.class)){
      url= url + QUALIFICATION_TYPE_MAPPINGS_ENDPOINT;
    }
    return codeExistsDto(url,value);
  }

  @Override
  public HttpStatus siteTrustMatch(String siteCode, String trustCode) {
    String url = serviceUrl + SITE_TRUST_MATCH_ENDPOINT + trustCode;
    return siteTrustMatchExists(url, siteCode);
  }

  @Override
  public RestTemplate getRestTemplate() {
    return this.referenceRestTemplate;
  }

  @Override
  public String getServiceUrl() {
    return this.serviceUrl;
  }

  public void setServiceUrl(String serviceUrl) {
    this.serviceUrl = serviceUrl;
  }

  @Override
  public Map<Class, ParameterizedTypeReference> getClassToParamTypeRefMap() {
    return classToParamTypeRefMap;
  }

  @Override
  public Set<DBCDTO> getAllDBCs() {
    ResponseEntity<Set<DBCDTO>> responseEntity = referenceRestTemplate.exchange(serviceUrl + "/api/dbcs?size=100&page=0", HttpMethod.GET, null, getDBCDto());
    return responseEntity.getBody();
  }
}