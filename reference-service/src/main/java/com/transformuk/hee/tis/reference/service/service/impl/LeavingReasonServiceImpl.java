package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.api.dto.LeavingReasonDto;
import com.transformuk.hee.tis.reference.service.model.LeavingReason;
import com.transformuk.hee.tis.reference.service.repository.LeavingReasonRepository;
import com.transformuk.hee.tis.reference.service.service.LeavingReasonService;
import com.transformuk.hee.tis.reference.service.service.mapper.LeavingReasonMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    return mapper.leavingReasonToLeavingReasonDto(repository.findOne(id));
  }

  @Override
  public List<LeavingReasonDto> findAll() {
    LOGGER.debug("Request to find all leaving reasons.");
    return mapper.leavingReasonsToLeavingReasonDtos(repository.findAll());
  }

  @Override
  public void delete(Long id) {
    LOGGER.debug("Request to delete leaving reason with id {}.", id);
    repository.delete(id);
  }
}