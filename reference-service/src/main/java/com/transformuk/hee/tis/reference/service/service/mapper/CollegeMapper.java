package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.CollegeDTO;
import com.transformuk.hee.tis.reference.service.model.College;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity College and its DTO CollegeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CollegeMapper {

  CollegeDTO collegeToCollegeDTO(College college);

  List<CollegeDTO> collegesToCollegeDTOs(List<College> colleges);

  College collegeDTOToCollege(CollegeDTO collegeDTO);

  List<College> collegeDTOsToColleges(List<CollegeDTO> collegeDTOs);
}
