package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.LeavingReasonDto;
import com.transformuk.hee.tis.reference.service.model.LeavingReason;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * A mapper for the {@link LeavingReason} entity and {@link LeavingReasonDto}.
 */
@Mapper(componentModel = "spring")
public interface LeavingReasonMapper {

  LeavingReasonDto leavingReasonToLeavingReasonDto(LeavingReason leavingReason);

  List<LeavingReasonDto> leavingReasonsToLeavingReasonDtos(List<LeavingReason> leavingReasons);

  LeavingReason leavingReasonDtoToLeavingReason(LeavingReasonDto leavingReasonDto);

  List<LeavingReason> leavingReasonDtosToLeavingReasons(List<LeavingReasonDto> leavingReasonDtos);
}
