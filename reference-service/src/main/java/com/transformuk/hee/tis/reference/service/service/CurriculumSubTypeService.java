package com.transformuk.hee.tis.reference.service.service;

import com.transformuk.hee.tis.reference.api.dto.CurriculumSubTypeDTO;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CurriculumSubTypeService {

  Page<CurriculumSubTypeDTO> findAll(Pageable pageable);

  Page<CurriculumSubTypeDTO> advancedSearch(String searchString, List<ColumnFilter> columnFilters,
      Pageable pageable);
}
