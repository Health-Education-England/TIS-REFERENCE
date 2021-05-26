package com.transformuk.hee.tis.reference.service.service.impl;

import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.in;

import com.transformuk.hee.tis.reference.api.dto.LeavingReasonDto;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.LeavingReason;
import com.transformuk.hee.tis.reference.service.repository.LeavingReasonRepository;
import com.transformuk.hee.tis.reference.service.service.LeavingReasonService;
import com.transformuk.hee.tis.reference.service.service.mapper.LeavingReasonMapper;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * Implementation for {@link LeavingReasonService}.
 */
@Service
public class LeavingReasonServiceImpl implements LeavingReasonService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LeavingReasonService.class);

  private final LeavingReasonMapper mapper;
  private final LeavingReasonRepository repository;

  public LeavingReasonServiceImpl(LeavingReasonMapper mapper, LeavingReasonRepository repository) {
    this.mapper = mapper;
    this.repository = repository;
  }

  @Override
  public LeavingReasonDto save(LeavingReasonDto leavingReasonDto) {
    LOGGER.debug("Request to save leaving reason.");
    LeavingReason leavingReason = mapper.leavingReasonDtoToLeavingReason(leavingReasonDto);
    leavingReason = repository.save(leavingReason);
    return mapper.leavingReasonToLeavingReasonDto(leavingReason);
  }

  @Override
  public LeavingReasonDto find(Long id) {
    LOGGER.debug("Request to find leaving reason with id {}.", id);
    return mapper.leavingReasonToLeavingReasonDto(repository.findById(id).orElse(null));
  }

  @Override
  public List<LeavingReasonDto> findAll() {
    LOGGER.debug("Request to find all leaving reasons.");
    return mapper.leavingReasonsToLeavingReasonDtos(repository.findAll());
  }

  @Override
  public List<LeavingReasonDto> findAll(String searchString, List<ColumnFilter> columnFilters) {
    List<LeavingReason> result;

    List<Specification<LeavingReason>> specs = new ArrayList<>();
    // add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specification.where(containsLike("code", searchString))
          .or(containsLike("code", searchString))
          .or(containsLike("label", searchString)));
    }

    // add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    if (!specs.isEmpty()) {
      Specification<LeavingReason> fullSpec = Specification.where(specs.get(0));
      // add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      result = repository.findAll(fullSpec);
    } else {
      result = repository.findAll();
    }

    return mapper.leavingReasonsToLeavingReasonDtos(result);
  }

  @Override
  public void delete(Long id) {
    LOGGER.debug("Request to delete leaving reason with id {}.", id);
    repository.deleteById(id);
  }
}
