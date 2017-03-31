package com.transformuk.hee.tis.reference.service.mapper;

import com.transformuk.hee.tis.reference.domain.Title;
import com.transformuk.hee.tis.reference.service.dto.TitleDTO;
import org.mapstruct.Mapper;

import java.util.List;

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
