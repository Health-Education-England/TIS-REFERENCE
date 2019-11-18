package com.transformuk.hee.tis.reference.service.service;

import com.transformuk.hee.tis.reference.api.dto.LeavingReasonDto;
import java.util.List;

/**
 * Service interface for managing {@code LeavingReason}.
 */
public interface LeavingReasonService {

  /**
   * Save the given LeavingReasonDto.
   *
   * @param leavingReasonDto The LeavingReasonDto to save.
   * @return The saved LeavingReasonDto.
   */
  LeavingReasonDto save(LeavingReasonDto leavingReasonDto);

  /**
   * Find all LeavingReasons.
   *
   * @return A List of all found LeavingReasons as LeavingReasonDtos.
   */
  List<LeavingReasonDto> findAll();

  /**
   * Delete the LeavingReason with the given ID.
   *
   * @param id The ID of the LeavingReason to delete.
   */
  void delete(Long id);
}
