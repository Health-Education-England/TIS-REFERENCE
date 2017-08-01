package com.transformuk.hee.tis.reference.client.impl;

import com.google.common.collect.Maps;
import com.transformuk.hee.tis.client.impl.AbstractClientService;
import com.transformuk.hee.tis.reference.api.dto.AssessmentTypeDTO;
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
import com.transformuk.hee.tis.reference.api.dto.LocalOfficeDTO;
import com.transformuk.hee.tis.reference.api.dto.MaritalStatusDTO;
import com.transformuk.hee.tis.reference.api.dto.MedicalSchoolDTO;
import com.transformuk.hee.tis.reference.api.dto.NationalityDTO;
import com.transformuk.hee.tis.reference.api.dto.PlacementTypeDTO;
import com.transformuk.hee.tis.reference.api.dto.ProgrammeMembershipTypeDTO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
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
    classToParamTypeRefMap.put(AssessmentTypeDTO.class, new ParameterizedTypeReference<List<AssessmentTypeDTO>>() {
    });
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
  public <DTO> List getAllDto(String endpointUrl, String pageSizeUrl, Class<DTO> dtoClass) throws IOException {
    final ParameterizedTypeReference parameterizedTypeReference = classToParamTypeRefMap.get(dtoClass);
    ResponseEntity<List> response = referenceRestTemplate.exchange(
        serviceUrl + endpointUrl + pageSizeUrl, HttpMethod.GET, null, parameterizedTypeReference);
    return response.getBody();
  }

  @Override
  public <DTO> DTO createDto(DTO objectDTO, String endpointUrl, Class<DTO> dtoClass) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<DTO> httpEntity = new HttpEntity<>(objectDTO, headers);

    ResponseEntity<DTO> response = referenceRestTemplate.exchange(
        serviceUrl + endpointUrl, HttpMethod.POST, httpEntity, dtoClass);
    return response.getBody();
  }

  @Override
  public <DTO> DTO updateDto(DTO objectDTO, String endpointUrl, Class<DTO> dtoClass) {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<DTO> httpEntity = new HttpEntity<>(objectDTO, headers);

    ResponseEntity<DTO> response = referenceRestTemplate.exchange(
        serviceUrl + endpointUrl, HttpMethod.PUT, httpEntity, dtoClass);
    return response.getBody();
  }

  @Override
  public <DTO> List<DTO> bulkCreateDto(List<DTO> objectDTOs, String endpointUrl, Class<DTO> dtoClass) {
    if (CollectionUtils.isEmpty(objectDTOs)) {
      LOG.info(COLLECTION_VALIDATION_MESSAGE);
      return Collections.EMPTY_LIST;
    } else {
      HttpHeaders headers = new HttpHeaders();

      HttpEntity<List<DTO>> httpEntity = new HttpEntity<>(objectDTOs, headers);
      ParameterizedTypeReference<List<DTO>> typeReference = getDTOReference();

      ResponseEntity<List<DTO>> response = referenceRestTemplate.exchange(serviceUrl + endpointUrl,
          HttpMethod.POST, httpEntity, typeReference);
      return response.getBody();
    }
  }

  @Override
  public <DTO> List<DTO> bulkUpdateDto(List<DTO> objectDTOs, String endpointUrl, Class<DTO> dtoClass) {
    if (CollectionUtils.isEmpty(objectDTOs)) {
      LOG.info(COLLECTION_VALIDATION_MESSAGE);
      return Collections.EMPTY_LIST;
    } else {
      HttpHeaders headers = new HttpHeaders();
      HttpEntity<List<DTO>> httpEntity = new HttpEntity<>(objectDTOs, headers);

      ParameterizedTypeReference<List<DTO>> typeReference = getDTOReference();

      ResponseEntity<List<DTO>> response = referenceRestTemplate.exchange(serviceUrl + endpointUrl,
          HttpMethod.PUT, httpEntity, typeReference);
      return response.getBody();
    }
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
