package com.transformuk.hee.tis.reference.service.service.impl;

import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.in;

import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.DBC;
import com.transformuk.hee.tis.reference.service.repository.DBCRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.DBCMapper;
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
public class DBCServiceImpl {

  @Autowired
  private DBCRepository dbcRepository;

  @Autowired
  private DBCMapper dbcMapper;


  @Transactional(readOnly = true)
  public Page<DBC> advancedSearch(String searchString, List<ColumnFilter> columnFilters,
      Pageable pageable) {

    List<Specification<DBC>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specification.where(containsLike("dbc", searchString)).
          or(containsLike("name", searchString))
          .or(containsLike("abbr", searchString)));
    }
    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    Page<DBC> result;
    if (!specs.isEmpty()) {
      Specification<DBC> fullSpec = Specification.where(specs.get(0));
      //add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      result = dbcRepository.findAll(fullSpec, pageable);
    } else {
      result = dbcRepository.findAll(pageable);
    }
    return result;
  }
}
