package com.transformuk.hee.tis.reference.service.service;

import com.transformuk.hee.tis.reference.api.dto.CurriculumSubTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CurriculumSubTypeService {

  Page<CurriculumSubTypeDTO> findAll(Pageable pageable);

  Page<CurriculumSubTypeDTO> findAllCurrent(Pageable pageable);

  Page<CurriculumSubTypeDTO> advancedSearch(boolean currentStatus, String searchQuery, Pageable pageable);
}
