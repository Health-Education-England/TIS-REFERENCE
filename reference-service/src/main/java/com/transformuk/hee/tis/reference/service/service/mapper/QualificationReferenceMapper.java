package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.QualificationReferenceDTO;
import com.transformuk.hee.tis.reference.service.model.QualificationReference;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity QualificationReference and its DTO QualificationReferenceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QualificationReferenceMapper {

  QualificationReferenceDTO qualificationReferenceToQualificationReferenceDTO(
      QualificationReference qualificationReference);

  List<QualificationReferenceDTO> qualificationReferencesToQualificationReferenceDTOs(
      List<QualificationReference> qualificationReferences);

  QualificationReference qualificationReferenceDTOToQualificationReference(
      QualificationReferenceDTO qualificationReferenceDTO);

  List<QualificationReference> qualificationReferenceDTOsToQualificationReferences(
      List<QualificationReferenceDTO> qualificationReferenceDTOs);
}
