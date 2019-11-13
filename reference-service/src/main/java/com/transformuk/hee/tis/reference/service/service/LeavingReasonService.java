package com.transformuk.hee.tis.reference.service.service;

import com.transformuk.hee.tis.reference.api.dto.LeavingReasonDto;
import java.util.List;

/**
 * Service interface for managing {@code LeavingReason}.
 */
public interface LeavingReasonService {

  List<LeavingReasonDto> findAll();
}
