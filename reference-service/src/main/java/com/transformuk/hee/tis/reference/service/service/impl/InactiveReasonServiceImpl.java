package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.InactiveReason;
import com.transformuk.hee.tis.reference.service.repository.InactiveReasonRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.in;

@Service
public class InactiveReasonServiceImpl {

  @Autowired
  private InactiveReasonRepository inactiveReasonRepository;

  @Transactional(readOnly = true)
  public Page<InactiveReason> advancedSearch(String searchString, List<ColumnFilter> columnFilters, Pageable pageable) {

    List<Specification<InactiveReason>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specifications.where(containsLike("code", searchString)).
          or(containsLike("label", searchString)));
    }
    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    Page<InactiveReason> result;
    if (!specs.isEmpty()) {
      Specifications<InactiveReason> fullSpec = Specifications.where(specs.get(0));
      //add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      result = inactiveReasonRepository.findAll(fullSpec, pageable);
    } else {
      result = inactiveReasonRepository.findAll(pageable);
    }

    return result;
  }
}
