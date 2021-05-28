package com.transformuk.hee.tis.reference.service.service;

import com.transformuk.hee.tis.reference.api.dto.LeavingReasonDto;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
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
   * Find the LeavingReason with the given ID.
   *
   * @param id The ID of the LeavingReason to find.
   * @return The LeavingReason as a LeavingReasonDto if found, else an empty LeavingReasonDto.
   */
  LeavingReasonDto find(Long id);

  /**
   * Find all LeavingReasons.
   *
   * @return A List of all found LeavingReasons as LeavingReasonDtos.
   */
  List<LeavingReasonDto> findAll();

  /**
   * Find all LeavingReasons which match the column filters.
   *
   * @param searchQuery   The string to filter Leaving Reasons by.
   * @param columnFilters The columns filters to apply.
   * @return A List of all found LeavingReasons as LeavingReasonDtos.
   */
  List<LeavingReasonDto> findAll(String searchQuery, List<ColumnFilter> columnFilters);

  /**
   * Delete the LeavingReason with the given ID.
   *
   * @param id The ID of the LeavingReason to delete.
   */
  void delete(Long id);
}
