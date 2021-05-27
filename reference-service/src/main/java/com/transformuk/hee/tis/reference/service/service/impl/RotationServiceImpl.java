package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.api.dto.RotationDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.model.Rotation;
import com.transformuk.hee.tis.reference.service.repository.RotationRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import com.transformuk.hee.tis.reference.service.service.RotationService;
import com.transformuk.hee.tis.reference.service.service.mapper.RotationMapper;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Rotation.
 */
@Service
@Transactional
public class RotationServiceImpl extends AbstractReferenceService<Rotation> implements
    RotationService {

  private final Logger log = LoggerFactory.getLogger(RotationServiceImpl.class);

  private final RotationRepository repository;

  private final RotationMapper mapper;

  public RotationServiceImpl(RotationRepository repository, RotationMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  /**
   * Save a rotation.
   *
   * @param rotationDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public RotationDTO save(RotationDTO rotationDTO) {
    log.debug("Request to save Rotation : {}", rotationDTO);
    Rotation rotation = mapper.toEntity(rotationDTO);
    rotation = repository.save(rotation);
    return mapper.toDto(rotation);
  }

  /**
   * Get all the rotations.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<RotationDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Rotations");
    return repository.findAll(pageable)
        .map(mapper::toDto);
  }

  /**
   * Get all the current rotations.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<RotationDTO> findAllCurrent(Pageable pageable) {
    log.debug("Request to get all Rotations");
    Rotation rotation = new Rotation().status(Status.CURRENT);
    return repository.findAll(Example.of(rotation), pageable)
        .map(mapper::toDto);
  }

  /**
   * Get the rotation labels
   *
   * @param values the lists of labels of the entity
   * @return list of existing labels
   */
  @Override
  @Transactional(readOnly = true)
  public List<String> findByLabelsIn(List<String> values) {
    log.debug("Request to get labels list");
    return repository.findByLabelsIn(values);
  }

  /**
   * Get one rotation by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public RotationDTO findOne(Long id) {
    log.debug("Request to get Rotation : {}", id);
    Rotation rotation = repository.findById(id).orElse(null);
    return mapper.toDto(rotation);
  }

  /**
   * Delete the  rotation by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Rotation : {}", id);
    repository.deleteById(id);
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<Rotation, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<Rotation> getSpecificationExecutor() {
    return repository;
  }
}
