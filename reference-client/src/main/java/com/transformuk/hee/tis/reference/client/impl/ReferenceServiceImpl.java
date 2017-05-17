package com.transformuk.hee.tis.reference.client.impl;

import com.transformuk.hee.tis.reference.api.dto.CurriculumSubTypeDTO;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
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

import java.util.Collections;
import java.util.List;

/**
 * The default implementation of the reference service client. Provides method for which we use to communicate with
 * the tis reference service
 */
@Service
public class ReferenceServiceImpl implements ReferenceService {

	private static final Logger LOG = LoggerFactory.getLogger(ReferenceServiceImpl.class);
	private static final String BULK_CREATE_UPDATE_CURRICULUM_SUB_TYPES = "/api/bulk-curriculum-sub-types";
	private static final String CREATE_UPDATE_GRADES = "/api/grades";
	private static final String BULK_CREATE_UPDATE_GRADES = "/api/bulk-grades";
	private static final String CREATE_UPDATE_SITES = "/api/sites";
	private static final String BULK_CREATE_UPDATE_SITES = "/api/bulk-sites";
	private static final String BULK_CREATE_UPDATE_TRUSTS = "/api/bulk-trusts";
	private static final String CREATE_UPDATE_TRUSTS = "/api/trusts";
	private static final String COLLECTION_VALIDATION_MESSAGE = "Collection provided is empty, will not make call";

	private RestTemplate referenceRestTemplate;

	@Value("${reference.service.url}")
	private String serviceUrl;

	@Autowired
	public ReferenceServiceImpl(@Qualifier("referenceRestTemplate") RestTemplate referenceRestTemplate) {
		this.referenceRestTemplate = referenceRestTemplate;
	}

	@Override
	public CurriculumSubTypeDTO createCurriculumSubType(CurriculumSubTypeDTO curriculumSubTypeDTO) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<CurriculumSubTypeDTO> httpEntity = new HttpEntity<>(curriculumSubTypeDTO, headers);

		ResponseEntity<CurriculumSubTypeDTO> response = referenceRestTemplate.exchange(
				serviceUrl + BULK_CREATE_UPDATE_CURRICULUM_SUB_TYPES, HttpMethod.POST, httpEntity, CurriculumSubTypeDTO.class);
		return response.getBody();
	}

	@Override
	public CurriculumSubTypeDTO updateCurriculumSubType(CurriculumSubTypeDTO curriculumSubTypeDTO) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<CurriculumSubTypeDTO> httpEntity = new HttpEntity<>(curriculumSubTypeDTO, headers);

		ResponseEntity<CurriculumSubTypeDTO> response = referenceRestTemplate.exchange(
				serviceUrl + BULK_CREATE_UPDATE_CURRICULUM_SUB_TYPES, HttpMethod.PUT, httpEntity, CurriculumSubTypeDTO.class);
		return response.getBody();
	}


	/**
	 * Bulk create new CurriculumSubTypes Entities
	 *
	 * @param curriculumSubTypeDTOs A list of curriculum sub types to be created
	 * @return A list of Curriculum sub types that have been created with their associated ids
	 */
	@Override
	public List<CurriculumSubTypeDTO> bulkCreateCurriculumSubType(List<CurriculumSubTypeDTO> curriculumSubTypeDTOs) {
		if (CollectionUtils.isEmpty(curriculumSubTypeDTOs)) {
			LOG.info(COLLECTION_VALIDATION_MESSAGE);
			return Collections.EMPTY_LIST;
		} else {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<List<CurriculumSubTypeDTO>> httpEntity = new HttpEntity<>(curriculumSubTypeDTOs, headers);

			ParameterizedTypeReference<List<CurriculumSubTypeDTO>> typeReference = getCurriculumSubTypeReference();

			ResponseEntity<List<CurriculumSubTypeDTO>> response = referenceRestTemplate.exchange(
					serviceUrl + BULK_CREATE_UPDATE_CURRICULUM_SUB_TYPES, HttpMethod.POST, httpEntity, typeReference);
			return response.getBody();
		}
	}

	/**
	 * Bulk update CurriculumSubType entities
	 *
	 * @param curriculumSubTypeDTOs list of the curriculum sub types to be updated
	 * @return the Curriculum sub types that were updated
	 */
	@Override
	public List<CurriculumSubTypeDTO> bulkUpdateCurriculumSubType(List<CurriculumSubTypeDTO> curriculumSubTypeDTOs) {
		if (CollectionUtils.isEmpty(curriculumSubTypeDTOs)) {
			LOG.info(COLLECTION_VALIDATION_MESSAGE);
			return Collections.EMPTY_LIST;
		} else {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<List<CurriculumSubTypeDTO>> httpEntity = new HttpEntity<>(curriculumSubTypeDTOs, headers);

			ParameterizedTypeReference<List<CurriculumSubTypeDTO>> typeReference = getCurriculumSubTypeReference();

			ResponseEntity<List<CurriculumSubTypeDTO>> response = referenceRestTemplate.exchange(
					serviceUrl + BULK_CREATE_UPDATE_CURRICULUM_SUB_TYPES, HttpMethod.PUT, httpEntity, typeReference);
			return response.getBody();
		}
	}

	@Override
	public GradeDTO createGrade(GradeDTO gradeDTO) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<GradeDTO> httpEntity = new HttpEntity<>(gradeDTO, headers);

		ResponseEntity<GradeDTO> response = referenceRestTemplate.exchange(
				serviceUrl + CREATE_UPDATE_GRADES, HttpMethod.POST, httpEntity, GradeDTO.class);
		return response.getBody();
	}

	@Override
	public GradeDTO updateGrade(GradeDTO gradeDTO) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<GradeDTO> httpEntity = new HttpEntity<>(gradeDTO, headers);

		ResponseEntity<GradeDTO> response = referenceRestTemplate.exchange(
				serviceUrl + CREATE_UPDATE_GRADES, HttpMethod.PUT, httpEntity, GradeDTO.class);
		return response.getBody();
	}

	/**
	 * Bulk create Grade entities
	 *
	 * @param gradeDTOs A list of GradeDTO to be created
	 * @return A list of GradeDTO that have been created with their id's
	 */
	@Override
	public List<GradeDTO> bulkCreateGrade(List<GradeDTO> gradeDTOs) {
		if (CollectionUtils.isEmpty(gradeDTOs)) {
			LOG.info(COLLECTION_VALIDATION_MESSAGE);
			return Collections.EMPTY_LIST;
		} else {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<List<GradeDTO>> httpEntity = new HttpEntity<>(gradeDTOs, headers);

			ParameterizedTypeReference<List<GradeDTO>> typeReference = getGradeReference();

			ResponseEntity<List<GradeDTO>> response = referenceRestTemplate.exchange(serviceUrl + BULK_CREATE_UPDATE_GRADES,
					HttpMethod.POST, httpEntity, typeReference);
			return response.getBody();
		}
	}

	/**
	 * Bulk update Grade entities
	 *
	 * @param gradeDTOs A list of grade entities to update
	 * @return A list of GradeDTO's that have been updatad
	 */
	@Override
	public List<GradeDTO> bulkUpdateGrade(List<GradeDTO> gradeDTOs) {
		if (CollectionUtils.isEmpty(gradeDTOs)) {
			LOG.info(COLLECTION_VALIDATION_MESSAGE);
			return Collections.EMPTY_LIST;
		} else {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<List<GradeDTO>> httpEntity = new HttpEntity<>(gradeDTOs, headers);

			ParameterizedTypeReference<List<GradeDTO>> typeReference = getGradeReference();

			ResponseEntity<List<GradeDTO>> response = referenceRestTemplate.exchange(serviceUrl + BULK_CREATE_UPDATE_GRADES,
					HttpMethod.PUT, httpEntity, typeReference);
			return response.getBody();
		}
	}


	@Override
	public SiteDTO createSite(SiteDTO siteDTO) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<SiteDTO> httpEntity = new HttpEntity<>(siteDTO, headers);

		ResponseEntity<SiteDTO> response = referenceRestTemplate.exchange(
				serviceUrl + CREATE_UPDATE_SITES, HttpMethod.POST, httpEntity, SiteDTO.class);
		return response.getBody();
	}

	@Override
	public SiteDTO updateSite(SiteDTO siteDTO) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<SiteDTO> httpEntity = new HttpEntity<>(siteDTO, headers);

		ResponseEntity<SiteDTO> response = referenceRestTemplate.exchange(
				serviceUrl + CREATE_UPDATE_SITES, HttpMethod.PUT, httpEntity, SiteDTO.class);
		return response.getBody();
	}

	/**
	 * Bulk create Site entities
	 *
	 * @param siteDTOs A list of Site entities to create
	 * @return A list of SiteDTO's that have been created with associated id's
	 */
	@Override
	public List<SiteDTO> bulkCreateSite(List<SiteDTO> siteDTOs) {
		if (CollectionUtils.isEmpty(siteDTOs)) {
			LOG.info(COLLECTION_VALIDATION_MESSAGE);
			return Collections.EMPTY_LIST;
		} else {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<List<SiteDTO>> httpEntity = new HttpEntity<>(siteDTOs, headers);

			ParameterizedTypeReference<List<SiteDTO>> typeReference = getSiteReference();

			ResponseEntity<List<SiteDTO>> response = referenceRestTemplate.exchange(serviceUrl + BULK_CREATE_UPDATE_SITES,
					HttpMethod.POST, httpEntity, typeReference);
			return response.getBody();
		}
	}

	/**
	 * Bulk update Site entities
	 *
	 * @param siteDTOs A list of site entities to update
	 * @return A list of SiteDTO's that have been updated
	 */
	@Override
	public List<SiteDTO> bulkUpdateSite(List<SiteDTO> siteDTOs) {
		if (CollectionUtils.isEmpty(siteDTOs)) {
			LOG.info(COLLECTION_VALIDATION_MESSAGE);
			return Collections.EMPTY_LIST;
		} else {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<List<SiteDTO>> httpEntity = new HttpEntity<>(siteDTOs, headers);

			ParameterizedTypeReference<List<SiteDTO>> typeReference = getSiteReference();

			ResponseEntity<List<SiteDTO>> response = referenceRestTemplate.exchange(serviceUrl + BULK_CREATE_UPDATE_SITES,
					HttpMethod.PUT, httpEntity, typeReference);
			return response.getBody();
		}
	}

	@Override
	public TrustDTO createTrust(TrustDTO trustDTO) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<TrustDTO> httpEntity = new HttpEntity<>(trustDTO, headers);

		ResponseEntity<TrustDTO> response = referenceRestTemplate.exchange(
				serviceUrl + CREATE_UPDATE_TRUSTS, HttpMethod.POST, httpEntity, TrustDTO.class);
		return response.getBody();
	}

	@Override
	public TrustDTO updateTrust(TrustDTO trustDTO) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<TrustDTO> httpEntity = new HttpEntity<>(trustDTO, headers);

		ResponseEntity<TrustDTO> response = referenceRestTemplate.exchange(
				serviceUrl + CREATE_UPDATE_TRUSTS, HttpMethod.PUT, httpEntity, TrustDTO.class);
		return response.getBody();
	}

	/**
	 * Bulk create Trust entities
	 *
	 * @param trustDTOs A list of Trust entities to create
	 * @return A list of TrustDTO's that have been created with their associated id's
	 */
	@Override
	public List<TrustDTO> bulkCreateTrust(List<TrustDTO> trustDTOs) {
		if (CollectionUtils.isEmpty(trustDTOs)) {
			LOG.info(COLLECTION_VALIDATION_MESSAGE);
			return Collections.EMPTY_LIST;
		} else {
			HttpHeaders headers = new HttpHeaders();

			HttpEntity<List<TrustDTO>> httpEntity = new HttpEntity<>(trustDTOs, headers);
			ParameterizedTypeReference<List<TrustDTO>> typeReference = getTrustReference();

			ResponseEntity<List<TrustDTO>> response = referenceRestTemplate.exchange(serviceUrl + BULK_CREATE_UPDATE_TRUSTS,
					HttpMethod.POST, httpEntity, typeReference);
			return response.getBody();
		}
	}

	/**
	 * Bulk update Trust entities
	 *
	 * @param trustDTOs A list of Trust entities to update
	 * @return A list of TrustDTO's that have been updated
	 */
	@Override
	public List<TrustDTO> bulkUpdateTrust(List<TrustDTO> trustDTOs) {
		if (CollectionUtils.isEmpty(trustDTOs)) {
			LOG.info(COLLECTION_VALIDATION_MESSAGE);
			return Collections.EMPTY_LIST;
		} else {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<List<TrustDTO>> httpEntity = new HttpEntity<>(trustDTOs, headers);

			ParameterizedTypeReference<List<TrustDTO>> typeReference = getTrustReference();

			ResponseEntity<List<TrustDTO>> response = referenceRestTemplate.exchange(serviceUrl + BULK_CREATE_UPDATE_TRUSTS,
					HttpMethod.PUT, httpEntity, typeReference);
			return response.getBody();
		}
	}

	private ParameterizedTypeReference<List<CurriculumSubTypeDTO>> getCurriculumSubTypeReference() {
		return new ParameterizedTypeReference<List<CurriculumSubTypeDTO>>() {
		};
	}

	private ParameterizedTypeReference<List<GradeDTO>> getGradeReference() {
		return new ParameterizedTypeReference<List<GradeDTO>>() {
		};
	}

	private ParameterizedTypeReference<List<SiteDTO>> getSiteReference() {
		return new ParameterizedTypeReference<List<SiteDTO>>() {
		};
	}

	private ParameterizedTypeReference<List<TrustDTO>> getTrustReference() {
		return new ParameterizedTypeReference<List<TrustDTO>>() {
		};
	}
}
