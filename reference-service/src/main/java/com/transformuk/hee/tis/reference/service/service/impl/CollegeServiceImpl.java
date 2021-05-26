package com.transformuk.hee.tis.reference.service.service.impl;

import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.in;

import com.transformuk.hee.tis.reference.service.model.College;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.repository.CollegeRepository;
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
public class CollegeServiceImpl {

  @Autowired
  private CollegeRepository collegeRepository;

  @Transactional(readOnly = true)
  public Page<College> advancedSearch(String searchString, List<ColumnFilter> columnFilters,
      Pageable pageable) {

    List<Specification<College>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specification.where(containsLike("abbreviation", searchString)).
          or(containsLike("name", searchString)));
    }
    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    Page<College> result;
    if (!specs.isEmpty()) {
      Specification<College> fullSpec = Specification.where(specs.get(0));
      //add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      result = collegeRepository.findAll(fullSpec, pageable);
    } else {
      result = collegeRepository.findAll(pageable);
    }

    return result;
  }
}
