package com.transformuk.hee.tis.reference.service.service.impl;

import com.google.common.base.Preconditions;
import com.transformuk.hee.tis.reference.api.dto.CurriculumSubTypeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.Country;
import com.transformuk.hee.tis.reference.service.model.CurriculumSubType;
import com.transformuk.hee.tis.reference.service.repository.CurriculumSubTypeRepository;
import com.transformuk.hee.tis.reference.service.service.CurriculumSubTypeService;
import com.transformuk.hee.tis.reference.service.service.mapper.CurriculumSubTypeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.isEqual;

@Service
public class CurriculumSubTypeServiceImpl implements CurriculumSubTypeService {

  @Autowired
  private CurriculumSubTypeRepository curriculumSubTypeRepository;

  @Autowired
  private CurriculumSubTypeMapper curriculumSubTypeMapper;

  @Override
  public Page<CurriculumSubTypeDTO> findAll(Pageable pageable) {
    Page<CurriculumSubType> all = curriculumSubTypeRepository.findAll(pageable);
    return all.map(cst -> curriculumSubTypeMapper.curriculumSubTypeToCurriculumSubTypeDTO(cst));
  }


  @Transactional(readOnly = true)
  public Page<CurriculumSubTypeDTO> advancedSearch(String searchString, List<ColumnFilter> columnFilters, Pageable pageable) {

    List<Specification<CurriculumSubType>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specifications.where(containsLike("code", searchString)).
          or(containsLike("label", searchString)));
    }
    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    Page<CurriculumSubType> result;
    if (!specs.isEmpty()) {
      Specifications<CurriculumSubType> fullSpec = Specifications.where(specs.get(0));
      //add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      result = curriculumSubTypeRepository.findAll(fullSpec, pageable);
    } else {
      result = curriculumSubTypeRepository.findAll(pageable);
    }
    return result.map(curriculumSubTypeMapper::curriculumSubTypeToCurriculumSubTypeDTO);
  }
}
