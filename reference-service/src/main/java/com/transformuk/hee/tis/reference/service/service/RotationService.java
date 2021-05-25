package com.transformuk.hee.tis.reference.service.service;

import com.transformuk.hee.tis.reference.api.dto.RotationDTO;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.Rotation;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Rotation.
 */
public interface RotationService {

  /**
   * Save a rotation.
   *
   * @param rotationDTO the entity to save
   * @return the persisted entity
   */
  RotationDTO save(RotationDTO rotationDTO);

  /**
   * Get all the rotations.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<RotationDTO> findAllDtos(Pageable pageable);

  /**
   * Get all the current rotations.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<RotationDTO> findAllCurrent(Pageable pageable);

  /**
   * Get the "id" rotation.
   *
   * @param values the lists of labels of the entity
   * @return list of existing labels
   */
  List<String> findByLabelsIn(List<String> values);

  /**
   * Get the "id" rotation.
   *
   * @param id the id of the entity
   * @return the entity
   */
  RotationDTO findOne(Long id);

  /**
   * Delete the "id" rotation.
   *
   * @param id the id of the entity
   */
  void delete(Long id);

  Page<Rotation> advancedSearch(String searchString, List<ColumnFilter> columnFilters,
      Pageable pageable);

}
