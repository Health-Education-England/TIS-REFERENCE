package com.transformuk.hee.tis.reference.service.service;

import com.transformuk.hee.tis.reference.api.dto.CurriculumSubTypeDTO;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CurriculumSubTypeService {

  Page<CurriculumSubTypeDTO> findAll(Pageable pageable);

  Page<CurriculumSubTypeDTO> advancedSearch(String searchString, List<ColumnFilter> columnFilters, Pageable pageable);
}
