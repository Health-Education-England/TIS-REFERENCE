package com.transformuk.hee.tis.reference.client;

import com.transformuk.hee.tis.reference.api.dto.CurriculumSubTypeDTO;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import org.springframework.http.ResponseEntity;

public interface ReferenceService {

	//ENDPOINT URL's
	String BULK_CREATE_UPDATE_CURRICULUM_SUB_TYPES = "/api/bulk-curriculum-sub-types";
	String CREATE_UPDATE_GRADES = "/api/grades";
	String BULK_CREATE_UPDATE_GRADES = "/api/bulk-grades";
	String CREATE_UPDATE_SITES = "/api/sites";
	String BULK_CREATE_UPDATE_SITES = "/api/bulk-sites";
	String BULK_CREATE_UPDATE_TRUSTS = "/api/bulk-trusts";
	String CREATE_UPDATE_TRUSTS = "/api/trusts";


	CurriculumSubTypeDTO createCurriculumSubType(CurriculumSubTypeDTO curriculumSubTypeDTO);

	CurriculumSubTypeDTO updateCurriculumSubType(CurriculumSubTypeDTO curriculumSubTypeDTO);

	List<CurriculumSubTypeDTO> bulkCreateCurriculumSubType(List<CurriculumSubTypeDTO> curriculumSubTypeDTOs);

	List<CurriculumSubTypeDTO> bulkUpdateCurriculumSubType(List<CurriculumSubTypeDTO> curriculumSubTypeDTOs);

	GradeDTO createGrade(GradeDTO gradeDTO);

	GradeDTO updateGrade(GradeDTO gradeDTO);

	List<GradeDTO> bulkCreateGrade(List<GradeDTO> gradeDTOs);

	List<GradeDTO> bulkUpdateGrade(List<GradeDTO> gradeDTOs);

	SiteDTO createSite(SiteDTO siteDTO);

	SiteDTO updateSite(SiteDTO siteDTO);

	List<SiteDTO> bulkCreateSite(List<SiteDTO> siteDTOs);

	List<SiteDTO> bulkUpdateSite(List<SiteDTO> siteDTOs);


	TrustDTO createTrust(TrustDTO trustDTO);

	TrustDTO updateTrust(TrustDTO trustDTO);

	List<TrustDTO> bulkCreateTrust(List<TrustDTO> trustDTOs);

	List<TrustDTO> bulkUpdateTrust(List<TrustDTO> trustDTOs);


	<DTO> DTO createDto(DTO objectDTO, String endpointUrl, Class<DTO> dtoClass);

	<DTO> DTO updateDto(DTO objectDTO, String endpointUrl, Class<DTO> dtoClass);

	<DTO> List<DTO> bulkCreateDtos(List<DTO> objectDTOs, String endpointUrl);

	<DTO> List<DTO> bulkUpdateDtos(List<DTO> objectDTOs, String endpointUrl);
	ResponseEntity<DBCDTO> getDBCByCode(String code);
}
