package com.transformuk.hee.tis.reference.service.service.impl;

import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.in;

import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.Grade;
import com.transformuk.hee.tis.reference.service.repository.GradeRepository;
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
public class GradeServiceImpl {

  @Autowired
  private GradeRepository gradeRepository;

  @Transactional(readOnly = true)
  public Page<Grade> advancedSearch(String searchString, List<ColumnFilter> columnFilters,
      Pageable pageable) {

    List<Specification<Grade>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specification.where(containsLike("name", searchString)).
          or(containsLike("abbreviation", searchString)).
          or(containsLike("label", searchString))
      );
    }
    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    Page<Grade> result;
    if (!specs.isEmpty()) {
      Specification<Grade> fullSpec = Specification.where(specs.get(0));
      //add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      result = gradeRepository.findAll(fullSpec, pageable);
    } else {
      result = gradeRepository.findAll(pageable);
    }

    return result;
  }
}
