package com.transformuk.hee.tis.reference.service.service;

import com.transformuk.hee.tis.reference.api.dto.LeavingReasonDto;
import java.util.List;

/**
 * Service interface for managing {@code LeavingReason}.
 */
public interface LeavingReasonService {

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
