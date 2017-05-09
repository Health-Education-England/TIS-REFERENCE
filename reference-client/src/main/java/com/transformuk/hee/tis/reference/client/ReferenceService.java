package com.transformuk.hee.tis.reference.client;

import com.transformuk.hee.tis.reference.api.dto.CurriculumSubTypeDTO;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.api.dto.TrustDTO;

import java.util.List;

public interface ReferenceService {

	List<CurriculumSubTypeDTO> bulkCreateCurriculumSubType(List<CurriculumSubTypeDTO> curriculumSubTypeDTOs);
	List<CurriculumSubTypeDTO> bulkUpdateCurriculumSubType(List<CurriculumSubTypeDTO> curriculumSubTypeDTOs);

	List<GradeDTO> bulkCreateGrade(List<GradeDTO> gradeDTOs);
	List<GradeDTO> bulkUpdateGrade(List<GradeDTO> gradeDTOs);

	List<SiteDTO> bulkCreateSite(List<SiteDTO> siteDTOs);
	List<SiteDTO> bulkUpdateSite(List<SiteDTO> siteDTOs);


	List<TrustDTO> bulkCreateTrust(List<TrustDTO> trustDTOs);
	List<TrustDTO> bulkUpdateTrust(List<TrustDTO> trustDTOs);


}
