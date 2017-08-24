package com.transformuk.hee.tis.reference.client.impl;

import com.google.common.collect.Maps;
import com.transformuk.hee.tis.client.impl.AbstractClientService;
import com.transformuk.hee.tis.reference.api.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * The default implementation of the reference service client. Provides method for which we use to communicate with
 * the tis reference service
 */
@Service
public class ReferenceServiceImpl extends AbstractClientService {

  private static final Logger LOG = LoggerFactory.getLogger(ReferenceServiceImpl.class);
  private static final String COLLECTION_VALIDATION_MESSAGE = "Collection provided is empty, will not make call";
  private static final Map<Class, ParameterizedTypeReference> classToParamTypeRefMap;
  private static final String DBCS_MAPPINGS_ENDPOINT = "/api/dbcs/code/";

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

  private RestTemplate referenceRestTemplate;

  @Value("${reference.service.url}")
  private String serviceUrl;

  @Autowired
  public ReferenceServiceImpl(@Qualifier("referenceRestTemplate") RestTemplate referenceRestTemplate) {
    this.referenceRestTemplate = referenceRestTemplate;
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

  public ResponseEntity<DBCDTO> getDBCByCode(String code) {
    String url = serviceUrl + DBCS_MAPPINGS_ENDPOINT + code;
    ResponseEntity<DBCDTO> responseEntity = referenceRestTemplate.getForEntity(url, DBCDTO.class);
    return responseEntity;
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
}
