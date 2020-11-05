package com.transformuk.hee.tis.reference.service.service.impl;

import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.in;

import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.QualificationReference;
import com.transformuk.hee.tis.reference.service.repository.QualificationReferenceRepository;
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
public class QualificationReferenceServiceImpl {

  @Autowired
  private QualificationReferenceRepository qualificationReferenceRepository;

  @Transactional(readOnly = true)
  public Page<QualificationReference> advancedSearch(String searchString,
      List<ColumnFilter> columnFilters, Pageable pageable) {

    List<Specification<QualificationReference>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specification.where(containsLike("code", searchString)).
          or(containsLike("label", searchString)));
    }
    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    Page<QualificationReference> result;
    if (!specs.isEmpty()) {
      Specification<QualificationReference> fullSpec = Specification.where(specs.get(0));
      //add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      result = qualificationReferenceRepository.findAll(fullSpec, pageable);
    } else {
      result = qualificationReferenceRepository.findAll(pageable);
    }

    return result;
  }
}
