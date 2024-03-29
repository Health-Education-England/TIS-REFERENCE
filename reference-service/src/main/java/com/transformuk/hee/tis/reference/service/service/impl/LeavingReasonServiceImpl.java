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
import org.apache.commons.lang.StringUtils;
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
  public List<LeavingReasonDto> findAll(String searchQuery, List<ColumnFilter> columnFilters) {
    List<LeavingReason> result;

    List<Specification<LeavingReason>> specs = new ArrayList<>();
    // add the text search criteria
    if (StringUtils.isNotEmpty(searchQuery)) {
      specs.add(Specification.where(containsLike("code", searchQuery))
          .or(containsLike("code", searchQuery))
          .or(containsLike("label", searchQuery)));
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

  /**
   * Find LeavingReasons by codes, filtered by columnFilters (mainly status).
   *
   * @param codes         the codes to find.
   * @param columnFilters The columns filters to apply.
   * @return a list of found LeavingReasons as LeavingReasonDtos.
   */
  @Override
  public List<LeavingReasonDto> findCodes(List<String> codes, List<ColumnFilter> columnFilters) {
    Specification<LeavingReason> specs = Specification.where(in("code", new ArrayList<>(codes)));

    for (ColumnFilter columnFilter : columnFilters) {
      specs = specs.and(in(columnFilter.getName(), columnFilter.getValues()));
    }

    List<LeavingReason> leavingReasons = repository.findAll(specs);
    return mapper.leavingReasonsToLeavingReasonDtos(leavingReasons);
  }

  @Override
  public void delete(Long id) {
    LOGGER.debug("Request to delete leaving reason with id {}.", id);
    repository.deleteById(id);
  }
}
