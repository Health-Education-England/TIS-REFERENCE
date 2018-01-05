package com.transformuk.hee.tis.reference.service.service.impl;

import com.google.common.base.Preconditions;
import com.transformuk.hee.tis.reference.api.dto.CurriculumSubTypeDTO;
import com.transformuk.hee.tis.reference.service.model.CurriculumSubType;
import com.transformuk.hee.tis.reference.service.repository.CurriculumSubTypeRepository;
import com.transformuk.hee.tis.reference.service.service.CurriculumSubTypeService;
import com.transformuk.hee.tis.reference.service.service.mapper.CurriculumSubTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.containsLike;

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

  @Override
  public Page<CurriculumSubTypeDTO> advancedSearch(String searchQuery, Pageable pageable) {
    Preconditions.checkNotNull(searchQuery);

    List<Specification<CurriculumSubType>> specs = new ArrayList<>();
    //add the text search criteria
    specs.add(Specifications.where(containsLike("code", searchQuery)).
        or(containsLike("label", searchQuery)));

    Page<CurriculumSubType> result;

    Specifications<CurriculumSubType> fullSpec = Specifications.where(specs.get(0));
    //add the rest of the specs that made it in
    for (int i = 1; i < specs.size(); i++) {
      fullSpec = fullSpec.and(specs.get(i));
    }
    result = curriculumSubTypeRepository.findAll(fullSpec, pageable);


    return result.map(cst -> curriculumSubTypeMapper.curriculumSubTypeToCurriculumSubTypeDTO(cst));
  }
}
