package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.JsonPatchDTO;
import com.transformuk.hee.tis.reference.service.model.JsonPatch;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity JsonPatch and its DTO JsonPatchDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JsonPatchMapper {

  JsonPatchDTO jsonPatchToJsonPatchDTO(JsonPatch jsonPatch);

  List<JsonPatchDTO> jsonPatchesToJsonPatchDTOs(List<JsonPatch> jsonPatches);

  JsonPatch jsonPatchDTOToJsonPatch(JsonPatchDTO jsonPatchDTO);

  List<JsonPatch> jsonPatchDTOsToJsonPatches(List<JsonPatchDTO> jsonPatchDTOs);
}
