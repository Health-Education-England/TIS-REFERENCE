package com.transformuk.hee.tis.reference.client.impl;

import com.google.common.collect.Maps;
import com.transformuk.hee.tis.reference.api.dto.CurriculumSubTypeDTO;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
public class ReferenceServiceImpl implements ReferenceService {

	private static final String BULK_CREATE_UPDATE_CURRICULUM_SUB_TYPES = "api/bulk-curriculum-sub-types";
	private static final String BULK_CREATE_UPDATE_GRADES = "api/bulk-grades";
	private static final String BULK_CREATE_UPDATE_SITES = "api/bulk-sites";
	private static final String BULK_CREATE_UPDATE_TRUSTS = "api/bulk-trusts";

	private RestTemplate referenceRestTemplate;

	@Value("${reference.service.url}")
	private String serviceUrl;

	@Autowired
	public ReferenceServiceImpl(@Qualifier("referenceRestTemplate") RestTemplate referenceRestTemplate) {
		this.referenceRestTemplate = referenceRestTemplate;
	}

	/**
	 * Bulk create new CurriculumSubTypes Entities
	 *
	 * @param curriculumSubTypeDTOs A list of curriculum sub types to be created
	 * @return A list of Curriculum sub types that have been created with their associated ids
	 */
	@Override
	public List<CurriculumSubTypeDTO> bulkCreateCurriculumSubType(List<CurriculumSubTypeDTO> curriculumSubTypeDTOs) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<List<CurriculumSubTypeDTO>> httpEntity = new HttpEntity<>(curriculumSubTypeDTOs, headers);

		ParameterizedTypeReference<List<CurriculumSubTypeDTO>> typeReference = getCurriculumSubTypeReference();

		ResponseEntity<List<CurriculumSubTypeDTO>> response = referenceRestTemplate.exchange(
				serviceUrl + BULK_CREATE_UPDATE_CURRICULUM_SUB_TYPES, HttpMethod.POST, httpEntity, typeReference);
		return response.getBody();
	}

	/**
	 * Bulk update CurriculumSubType entities
	 *
	 * @param curriculumSubTypeDTOs list of the curriculum sub types to be updated
	 * @return the Curriculum sub types that were updated
	 */
	@Override
	public List<CurriculumSubTypeDTO> bulkUpdateCurriculumSubType(List<CurriculumSubTypeDTO> curriculumSubTypeDTOs) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<List<CurriculumSubTypeDTO>> httpEntity = new HttpEntity<>(curriculumSubTypeDTOs, headers);

		ParameterizedTypeReference<List<CurriculumSubTypeDTO>> typeReference = getCurriculumSubTypeReference();

		ResponseEntity<List<CurriculumSubTypeDTO>> response = referenceRestTemplate.exchange(
				serviceUrl + BULK_CREATE_UPDATE_CURRICULUM_SUB_TYPES, HttpMethod.PUT, httpEntity, typeReference);
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
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<List<GradeDTO>> httpEntity = new HttpEntity<>(gradeDTOs, headers);

		ParameterizedTypeReference<List<GradeDTO>> typeReference = getGradeReference();

		ResponseEntity<List<GradeDTO>> response = referenceRestTemplate.exchange(serviceUrl + BULK_CREATE_UPDATE_GRADES,
				HttpMethod.POST, httpEntity, typeReference);
		return response.getBody();
	}

	/**
	 * Bulk update Grade entities
	 *
	 * @param gradeDTOs A list of grade entities to update
	 * @return A list of GradeDTO's that have been updatad
	 */
	@Override
	public List<GradeDTO> bulkUpdateGrade(List<GradeDTO> gradeDTOs) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<List<GradeDTO>> httpEntity = new HttpEntity<>(gradeDTOs, headers);

		ParameterizedTypeReference<List<GradeDTO>> typeReference = getGradeReference();

		ResponseEntity<List<GradeDTO>> response = referenceRestTemplate.exchange(serviceUrl + BULK_CREATE_UPDATE_GRADES,
				HttpMethod.PUT, httpEntity, typeReference);
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
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<List<SiteDTO>> httpEntity = new HttpEntity<>(siteDTOs, headers);

		ParameterizedTypeReference<List<SiteDTO>> typeReference = getSiteReference();

		ResponseEntity<List<SiteDTO>> response = referenceRestTemplate.exchange(serviceUrl + BULK_CREATE_UPDATE_SITES,
				HttpMethod.POST, httpEntity, typeReference);
		return response.getBody();
	}

	/**
	 * Bulk update Site entities
	 *
	 * @param siteDTOs A list of site entities to update
	 * @return A list of SiteDTO's that have been updated
	 */
	@Override
	public List<SiteDTO> bulkUpdateSite(List<SiteDTO> siteDTOs) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<List<SiteDTO>> httpEntity = new HttpEntity<>(siteDTOs, headers);

		ParameterizedTypeReference<List<SiteDTO>> typeReference = getSiteReference();

		ResponseEntity<List<SiteDTO>> response = referenceRestTemplate.exchange(serviceUrl + BULK_CREATE_UPDATE_SITES,
				HttpMethod.PUT, httpEntity, typeReference);
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
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<List<TrustDTO>> httpEntity = new HttpEntity<>(trustDTOs, headers);

		ParameterizedTypeReference<List<TrustDTO>> typeReference = getTrustReference();

		ResponseEntity<List<TrustDTO>> response = referenceRestTemplate.exchange(serviceUrl + BULK_CREATE_UPDATE_TRUSTS,
				HttpMethod.POST, httpEntity, typeReference);
		return response.getBody();
	}

	/**
	 * Bulk update Trust entities
	 *
	 * @param trustDTOs A list of Trust entities to update
	 * @return A list of TrustDTO's that have been updated
	 */
	@Override
	public List<TrustDTO> bulkUpdateTrust(List<TrustDTO> trustDTOs) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<List<TrustDTO>> httpEntity = new HttpEntity<>(trustDTOs, headers);

		ParameterizedTypeReference<List<TrustDTO>> typeReference = getTrustReference();

		ResponseEntity<List<TrustDTO>> response = referenceRestTemplate.exchange(serviceUrl + BULK_CREATE_UPDATE_SITES,
				HttpMethod.PUT, httpEntity, typeReference);
		return response.getBody();
	}

	private ParameterizedTypeReference<List<CurriculumSubTypeDTO>> getCurriculumSubTypeReference() {
		return new ParameterizedTypeReference<List<CurriculumSubTypeDTO>>(){};
	}

	private ParameterizedTypeReference<List<GradeDTO>> getGradeReference() {
		return new ParameterizedTypeReference<List<GradeDTO>>(){};
	}

	private ParameterizedTypeReference<List<SiteDTO>> getSiteReference() {
		return new ParameterizedTypeReference<List<SiteDTO>>(){};
	}

	private ParameterizedTypeReference<List<TrustDTO>> getTrustReference() {
		return new ParameterizedTypeReference<List<TrustDTO>>(){};
	}
}
