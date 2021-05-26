package com.transformuk.hee.tis.reference.service.service.impl;

import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.in;

import com.transformuk.hee.tis.reference.service.model.AssessmentType;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.repository.AssessmentTypeRepository;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssessmentTypeServiceImpl {

  @Autowired
  private AssessmentTypeRepository assessmentTypeRepository;

  @Transactional(readOnly = true)
  public Page<AssessmentType> advancedSearch(String searchString, List<ColumnFilter> columnFilters,
      Pageable pageable) {

    List<Specification<AssessmentType>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specification.where(containsLike("label", searchString)).
          or(containsLike("code", searchString)));
    }
    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    Page<AssessmentType> result;
    if (!specs.isEmpty()) {
      Specification<AssessmentType> fullSpec = Specification.where(specs.get(0));
      //add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      result = assessmentTypeRepository.findAll(fullSpec, pageable);
    } else {
      result = assessmentTypeRepository.findAll(pageable);
    }

    return result;
  }
}
