package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.TitleDTO;
import com.transformuk.hee.tis.reference.service.model.Title;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Title and its DTO TitleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TitleMapper {

  TitleDTO titleToTitleDTO(Title title);

  List<TitleDTO> titlesToTitleDTOs(List<Title> titles);

  Title titleDTOToTitle(TitleDTO titleDTO);

  List<Title> titleDTOsToTitles(List<TitleDTO> titleDTOs);
}
