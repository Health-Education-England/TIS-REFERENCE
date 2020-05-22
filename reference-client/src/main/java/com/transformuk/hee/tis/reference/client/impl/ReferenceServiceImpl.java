package com.transformuk.hee.tis.reference.client.impl;

import com.google.common.collect.Maps;
import com.transformuk.hee.tis.client.impl.AbstractClientService;
import com.transformuk.hee.tis.reference.api.dto.CollegeDTO;
import com.transformuk.hee.tis.reference.api.dto.CountryDTO;
import com.transformuk.hee.tis.reference.api.dto.CurriculumSubTypeDTO;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import com.transformuk.hee.tis.reference.api.dto.EthnicOriginDTO;
import com.transformuk.hee.tis.reference.api.dto.FundingIssueDTO;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.api.dto.GdcStatusDTO;
import com.transformuk.hee.tis.reference.api.dto.GenderDTO;
import com.transformuk.hee.tis.reference.api.dto.GmcStatusDTO;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.InactiveReasonDTO;
import com.transformuk.hee.tis.reference.api.dto.JsonPatchDTO;
import com.transformuk.hee.tis.reference.api.dto.LeavingDestinationDTO;
import com.transformuk.hee.tis.reference.api.dto.LimitedListResponse;
import com.transformuk.hee.tis.reference.api.dto.LocalOfficeDTO;
import com.transformuk.hee.tis.reference.api.dto.MaritalStatusDTO;
import com.transformuk.hee.tis.reference.api.dto.MedicalSchoolDTO;
import com.transformuk.hee.tis.reference.api.dto.NationalityDTO;
import com.transformuk.hee.tis.reference.api.dto.PlacementTypeDTO;
import com.transformuk.hee.tis.reference.api.dto.ProgrammeMembershipTypeDTO;
import com.transformuk.hee.tis.reference.api.dto.QualificationReferenceDTO;
import com.transformuk.hee.tis.reference.api.dto.QualificationTypeDTO;
import com.transformuk.hee.tis.reference.api.dto.RecordTypeDTO;
import com.transformuk.hee.tis.reference.api.dto.ReligiousBeliefDTO;
import com.transformuk.hee.tis.reference.api.dto.RoleDTO;
import com.transformuk.hee.tis.reference.api.dto.SettledDTO;
import com.transformuk.hee.tis.reference.api.dto.SexualOrientationDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.api.dto.StatusDTO;
import com.transformuk.hee.tis.reference.api.dto.TariffRateDTO;
import com.transformuk.hee.tis.reference.api.dto.TitleDTO;
import com.transformuk.hee.tis.reference.api.dto.TrainingNumberTypeDTO;
import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
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

/**
 * The default implementation of the reference service client. Provides method for which we use to
 * communicate with the tis reference service
 */
@Service
public class ReferenceServiceImpl extends AbstractClientService implements ReferenceService {

  private static final Logger LOG = LoggerFactory.getLogger(ReferenceServiceImpl.class);
  private static final Map<Class, ParameterizedTypeReference> classToParamTypeRefMap;
  private static final String FIND_FUNDING_TYPES_ENDPOINT = "/api/funding-types?columnFilters=";
  private static final String FIND_GRADES_BY_NAME_ENDPOINT = "/api/grades?columnFilters=";
  private static final String FIND_GRADES_IN_ENDPOINT = "/api/grades/in/";
  private static final String FIND_GRADES_ID_IN_ENDPOINT = "/api/grades/ids/in";
  private static final String FIND_SITES_BY_NAME_ENDPOINT = "/api/sites?columnFilters=";
  private static final String FIND_SITES_IN_ENDPOINT = "/api/sites/in/";
  private static final String FIND_SITES_ID_IN_ENDPOINT = "/api/sites/ids/in";
  private static final String FIND_ALL_LOCAL_OFFICE_ENDPOINT = "/api/local-offices";
  private static final String FIND_TRUSTS_ENDPOINT = "/api/trusts?columnFilters=";
  private static final String FIND_LOCALOFFICES_BY_NAME_ENDPOINT =
      "/api/local-offices?columnFilters=";
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
  private static final String SEXUAL_ORIENTATION_MAPPINGS_ENDPOINT =
      "/api/sexual-orientations/exists/";
  private static final String RELIGIOUS_BELIF_MAPPINGS_ENDPOINT = "/api/religious-beliefs/exists/";
  private static final String QUALIFICATION_MAPPINGS_ENDPOINT =
      "/api/qualification-reference/exists/";
  private static final String QUALIFICATION_TYPE_MAPPINGS_ENDPOINT =
      "/api/qualification-types/exists/";
  private static final String ROLES_BY_ROLE_CATEGORY = "/api/roles/categories/";

  private static String gradesJsonQuerystringURLEncoded;
  private static String labelJsonQuerystringURLEncoded;
  private static String localOfficesJsonQuerystringURLEncoded;
  private static String sitesKnownAsJsonQuerystringURLEncoded;
  private static String trustKnownAsJsonQuerystringURLEncoded;
  private static String statusCurrentUrlEncoded;

  static {
    try {
      gradesJsonQuerystringURLEncoded =
          new URLCodec().encode("{\"name\":[\"PARAMETER_NAME\"],\"status\":[\"CURRENT\"]}");
      labelJsonQuerystringURLEncoded =
          new URLCodec().encode("{\"label\":[\"PARAMETER_LABEL\"],\"status\":[\"CURRENT\"]}");
      localOfficesJsonQuerystringURLEncoded = new URLCodec()
          .encode("{\"name\":[\"PARAMETER_LOCALOFFICENAME\"],\"status\":[\"CURRENT\"]}");
      sitesKnownAsJsonQuerystringURLEncoded =
          new URLCodec().encode("{\"siteKnownAs\":[\"PARAMETER_NAME\"],\"status\":[\"CURRENT\"]}");
      trustKnownAsJsonQuerystringURLEncoded = new URLCodec()
          .encode("{\"trustKnownAs\":[\"PARAMETER_TRUSTKNOWNAS\"],\"status\":[\"CURRENT\"]}");
      statusCurrentUrlEncoded = new URLCodec().encode("{\"status\":[\"CURRENT\"]}");
    } catch (EncoderException e) {
      LOG.error(e.getLocalizedMessage(), e);
    }
  }

  static {
    classToParamTypeRefMap = Maps.newHashMap();
    classToParamTypeRefMap
        .put(CollegeDTO.class, new ParameterizedTypeReference<List<CollegeDTO>>() {
        });
    classToParamTypeRefMap
        .put(CountryDTO.class, new ParameterizedTypeReference<List<CountryDTO>>() {
        });
    classToParamTypeRefMap.put(CurriculumSubTypeDTO.class,
        new ParameterizedTypeReference<List<CurriculumSubTypeDTO>>() {
        });
    classToParamTypeRefMap.put(DBCDTO.class, new ParameterizedTypeReference<List<DBCDTO>>() {
    });
    classToParamTypeRefMap
        .put(EthnicOriginDTO.class, new ParameterizedTypeReference<List<EthnicOriginDTO>>() {
        });
    classToParamTypeRefMap
        .put(FundingIssueDTO.class, new ParameterizedTypeReference<List<FundingIssueDTO>>() {
        });
    classToParamTypeRefMap
        .put(FundingTypeDTO.class, new ParameterizedTypeReference<List<FundingTypeDTO>>() {
        });
    classToParamTypeRefMap
        .put(GdcStatusDTO.class, new ParameterizedTypeReference<List<GdcStatusDTO>>() {
        });
    classToParamTypeRefMap.put(GenderDTO.class, new ParameterizedTypeReference<List<GenderDTO>>() {
    });
    classToParamTypeRefMap
        .put(GmcStatusDTO.class, new ParameterizedTypeReference<List<GmcStatusDTO>>() {
        });
    classToParamTypeRefMap.put(GradeDTO.class, new ParameterizedTypeReference<List<GradeDTO>>() {
    });
    classToParamTypeRefMap
        .put(InactiveReasonDTO.class, new ParameterizedTypeReference<List<InactiveReasonDTO>>() {
        });
    classToParamTypeRefMap.put(LeavingDestinationDTO.class,
        new ParameterizedTypeReference<List<LeavingDestinationDTO>>() {
        });
    classToParamTypeRefMap
        .put(LocalOfficeDTO.class, new ParameterizedTypeReference<List<LocalOfficeDTO>>() {
        });
    classToParamTypeRefMap
        .put(MaritalStatusDTO.class, new ParameterizedTypeReference<List<MaritalStatusDTO>>() {
        });
    classToParamTypeRefMap
        .put(MedicalSchoolDTO.class, new ParameterizedTypeReference<List<MedicalSchoolDTO>>() {
        });
    classToParamTypeRefMap
        .put(NationalityDTO.class, new ParameterizedTypeReference<List<NationalityDTO>>() {
        });
    classToParamTypeRefMap
        .put(PlacementTypeDTO.class, new ParameterizedTypeReference<List<PlacementTypeDTO>>() {
        });
    classToParamTypeRefMap.put(ProgrammeMembershipTypeDTO.class,
        new ParameterizedTypeReference<List<ProgrammeMembershipTypeDTO>>() {
        });
    classToParamTypeRefMap
        .put(RecordTypeDTO.class, new ParameterizedTypeReference<List<RecordTypeDTO>>() {
        });
    classToParamTypeRefMap
        .put(ReligiousBeliefDTO.class, new ParameterizedTypeReference<List<ReligiousBeliefDTO>>() {
        });
    classToParamTypeRefMap.put(RoleDTO.class, new ParameterizedTypeReference<List<RoleDTO>>() {
    });
    classToParamTypeRefMap
        .put(SettledDTO.class, new ParameterizedTypeReference<List<SettledDTO>>() {
        });
    classToParamTypeRefMap.put(SexualOrientationDTO.class,
        new ParameterizedTypeReference<List<SexualOrientationDTO>>() {
        });
    classToParamTypeRefMap.put(SiteDTO.class, new ParameterizedTypeReference<List<SiteDTO>>() {
    });
    classToParamTypeRefMap.put(StatusDTO.class, new ParameterizedTypeReference<List<StatusDTO>>() {
    });
    classToParamTypeRefMap
        .put(TariffRateDTO.class, new ParameterizedTypeReference<List<TariffRateDTO>>() {
        });
    classToParamTypeRefMap.put(TitleDTO.class, new ParameterizedTypeReference<List<TitleDTO>>() {
    });
    classToParamTypeRefMap.put(TrainingNumberTypeDTO.class,
        new ParameterizedTypeReference<List<TrainingNumberTypeDTO>>() {
        });
    classToParamTypeRefMap.put(TrustDTO.class, new ParameterizedTypeReference<List<TrustDTO>>() {
    });
  }

  @Autowired
  private RestTemplate referenceRestTemplate;

  @Value("${reference.service.url}")
  private String serviceUrl;

  public ReferenceServiceImpl(
      @Value("${reference.client.rate.limit}") double standardRequestsPerSecondLimit,
      @Value("${reference.client.bulk.rate.limit}") double bulkRequestsPerSecondLimit) {
    super(standardRequestsPerSecondLimit, bulkRequestsPerSecondLimit);
  }

  @Override
  public List<JsonPatchDTO> getJsonPathByTableDtoNameOrderByDateAddedAsc(String endpointUrl,
      Class objectDTO) {
    ParameterizedTypeReference<List<JsonPatchDTO>> typeReference = getJsonPatchDtoReference();
    ResponseEntity<List<JsonPatchDTO>> response = referenceRestTemplate
        .exchange(serviceUrl + endpointUrl + objectDTO.getSimpleName(),
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
    ResponseEntity<Map<Long, Boolean>> responseEntity = referenceRestTemplate
        .exchange(url, HttpMethod.POST, requestEntity,
            responseType);
    return responseEntity.getBody();
  }

  private Map<String, Boolean> exists(String url, List<String> ids) {
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(ids);
    ParameterizedTypeReference<Map<String, Boolean>> responseType = getExistsReference();
    ResponseEntity<Map<String, Boolean>> responseEntity = referenceRestTemplate
        .exchange(url, HttpMethod.POST, requestEntity,
            responseType);
    return responseEntity.getBody();
  }

  private Map<Long, Boolean> idExists(String url, List<Long> ids) {
    HttpEntity<List<Long>> requestEntity = new HttpEntity<>(ids);
    ParameterizedTypeReference<Map<Long, Boolean>> responseType = getExistsLongReference();
    ResponseEntity<Map<Long, Boolean>> responseEntity = referenceRestTemplate
        .exchange(url, HttpMethod.POST, requestEntity,
            responseType);
    return responseEntity.getBody();
  }

  private Map<String, Boolean> valuesExists(String url, List<String> values) {
    HttpEntity<List<String>> requestEntity = new HttpEntity<>(values);
    ParameterizedTypeReference<Map<String, Boolean>> responseType = getExistsStringReference();
    ResponseEntity<Map<String, Boolean>> responseEntity = referenceRestTemplate
        .exchange(url, HttpMethod.POST, requestEntity,
            responseType);
    return responseEntity.getBody();
  }

  private HttpStatus codeExists(String url, String code) {
    HttpEntity<String> requestEntity = new HttpEntity<>(code);
    ParameterizedTypeReference<HttpStatus> responseType = getCodeExistsReference();
    LOG.info("Trying codeExists with URL: {}", url);
    ResponseEntity<HttpStatus> responseEntity = referenceRestTemplate
        .exchange(url, HttpMethod.POST, requestEntity,
            responseType);
    return responseEntity.getStatusCode();
  }

  private Boolean codeExistsDto(String url, String code) {
    HttpEntity<String> requestEntity = new HttpEntity<>(code);
    ParameterizedTypeReference<Boolean> responseType = getCodeExistsDto();
    LOG.info("Trying codeExists with URL: {}", url);
    ResponseEntity<Boolean> responseEntity = referenceRestTemplate
        .exchange(url, HttpMethod.POST, requestEntity,
            responseType);
    return responseEntity.getBody();
  }

  private HttpStatus siteTrustMatchExists(String url, String siteCode) {
    HttpEntity<String> requestEntity = new HttpEntity<>(siteCode);
    ParameterizedTypeReference<HttpStatus> responseType = getSiteTrustMatchReference();
    ResponseEntity<HttpStatus> responseEntity = referenceRestTemplate
        .exchange(url, HttpMethod.POST, requestEntity,
            responseType);
    return responseEntity.getStatusCode();
  }

  @Cacheable("sites")
  @Override
  public List<SiteDTO> findSitesByName(String siteName) {
    LOG.debug("calling getSitesByName with {}", siteName);

    return referenceRestTemplate
        .exchange(serviceUrl + FIND_SITES_BY_NAME_ENDPOINT
                + sitesKnownAsJsonQuerystringURLEncoded
                .replace("PARAMETER_NAME", urlEncode(siteName)), HttpMethod.GET, null,
            new ParameterizedTypeReference<List<SiteDTO>>() {
            })
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
              new ParameterizedTypeReference<List<SiteDTO>>() {
              });
      return responseEntity.getBody();
    } catch (Exception e) {
      LOG.error(
          "Exception during find sites id in for ids [{}], returning empty list. Here's the error message {}",
          joinedIds, e.getMessage());
      return Collections.emptyList();
    }
  }

  @Cacheable("grades")
  @Override
  public List<GradeDTO> findGradesByName(String gradeName) {
    LOG.debug("calling getGradesByName with {}", gradeName);
    return referenceRestTemplate
        .exchange(serviceUrl + FIND_GRADES_BY_NAME_ENDPOINT + gradesJsonQuerystringURLEncoded
                .replace("PARAMETER_NAME", urlEncode(gradeName)), HttpMethod.GET, null,
            new ParameterizedTypeReference<List<GradeDTO>>() {
            })
        .getBody();
  }

  @Override
  public List<LocalOfficeDTO> findAllLocalOffice() {
    LOG.debug("calling getGradesByName with {}");
    return referenceRestTemplate
        .exchange(serviceUrl + FIND_ALL_LOCAL_OFFICE_ENDPOINT, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<LocalOfficeDTO>>() {
            })
        .getBody();
  }

  @Override
  public Collection<RoleDTO> getRolesByCategory(final Long categoryId) {
    LOG.debug("calling getRolesByCategory with {}", categoryId);

    return referenceRestTemplate
        .exchange(serviceUrl + ROLES_BY_ROLE_CATEGORY + String.valueOf(categoryId), HttpMethod.GET,
            null, new ParameterizedTypeReference<List<RoleDTO>>() {
            })
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
  public List<FundingTypeDTO> findCurrentFundingTypesByLabelIn(Set<String> labels) {
    String joinedLabels = StringUtils.join(labels, "\",\"");
    String url = serviceUrl + FIND_FUNDING_TYPES_ENDPOINT
        + labelJsonQuerystringURLEncoded.replace("PARAMETER_LABEL", urlEncode(joinedLabels));
    ResponseEntity<List<FundingTypeDTO>> responseEntity = referenceRestTemplate
        .exchange(url, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<FundingTypeDTO>>() {
            });
    return responseEntity.getBody();
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
          exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET, null,
              new ParameterizedTypeReference<List<GradeDTO>>() {
              });
      return responseEntity.getBody();
    } catch (Exception e) {
      LOG.error(
          "Exception during find grade id in for ids [{}], returning empty list. Here's the error message {}",
          joinedIds, e.getMessage());
      return Collections.emptyList();
    }
  }

  @Override
  public List<TrustDTO> findTrustByTrustKnownAs(String trustKnownAs) {
    LOG.debug("calling findTrustByTrustKnownAs with {}", trustKnownAs);
    String url = serviceUrl + FIND_TRUSTS_ENDPOINT
        + trustKnownAsJsonQuerystringURLEncoded
        .replace("PARAMETER_TRUSTKNOWNAS", urlEncode(trustKnownAs));
    return referenceRestTemplate
        .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<TrustDTO>>() {
        })
        .getBody();
  }

  @Override
  public List<TrustDTO> findCurrentTrustsByTrustKnownAsIn(Set<String> allTrustKnownAs) {
    String joinedTrustKnownAs = StringUtils.join(allTrustKnownAs, "\",\"");
    return findTrustByTrustKnownAs(joinedTrustKnownAs);
  }

  @Cacheable("localOffices")
  @Override
  public List<LocalOfficeDTO> findLocalOfficesByName(String owner) {
    LOG.debug("calling getLocalOfficesByName with {}", owner);
    return referenceRestTemplate
        .exchange(
            serviceUrl + FIND_LOCALOFFICES_BY_NAME_ENDPOINT + localOfficesJsonQuerystringURLEncoded
                .replace("PARAMETER_LOCALOFFICENAME", urlEncode(owner)), HttpMethod.GET, null,
            new ParameterizedTypeReference<List<LocalOfficeDTO>>() {
            })
        .getBody();
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
    return isValueExists(dtoClass, value, false);
  }

  @Override
  public Boolean isValueExists(Class dtoClass, String value, boolean currentOnly) {
    String url = serviceUrl;
    if (dtoClass.equals(TitleDTO.class)) {
      url += TITLE_MAPPINGS_ENDPOINT;
    } else if (dtoClass.equals(GmcStatusDTO.class)) {
      url += GMC_STATUS_MAPPINGS_ENDPOINT;
    } else if (dtoClass.equals(GdcStatusDTO.class)) {
      url += GDC_STATUS_MAPPINGS_ENDPOINT;
    } else if (dtoClass.equals(GenderDTO.class)) {
      url += GENDER_MAPPINGS_ENDPOINT;
    } else if (dtoClass.equals(NationalityDTO.class)) {
      url += NATIONALITY_STATUS_MAPPINGS_ENDPOINT;
    } else if (dtoClass.equals(EthnicOriginDTO.class)) {
      url += ETHINIC_ORIGIN_MAPPINGS_ENDPOINT;
    } else if (dtoClass.equals(MaritalStatusDTO.class)) {
      url += MARITAL_STATUS_MAPPINGS_ENDPOINT;
    } else if (dtoClass.equals(SexualOrientationDTO.class)) {
      url += SEXUAL_ORIENTATION_MAPPINGS_ENDPOINT;
    } else if (dtoClass.equals(ReligiousBeliefDTO.class)) {
      url += RELIGIOUS_BELIF_MAPPINGS_ENDPOINT;
    } else if (dtoClass.equals(QualificationReferenceDTO.class)) {
      url += QUALIFICATION_MAPPINGS_ENDPOINT;
    } else if (dtoClass.equals(QualificationTypeDTO.class)) {
      url += QUALIFICATION_TYPE_MAPPINGS_ENDPOINT;
    }

    if (currentOnly) {
      url += "?columnFilters=" + statusCurrentUrlEncoded;
    }

    return codeExistsDto(url, value);
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
    ResponseEntity<Set<DBCDTO>> responseEntity = referenceRestTemplate
        .exchange(serviceUrl + "/api/dbcs?size=100&page=0", HttpMethod.GET, null, getDBCDto());
    return responseEntity.getBody();
  }
}