package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.service.domain.SexualOrientation;
import com.transformuk.hee.tis.reference.service.repository.SexualOrientationRepository;
import com.transformuk.hee.tis.reference.service.api.dto.SexualOrientationDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.SexualOrientationMapper;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SexualOrientation.
 */
@RestController
@RequestMapping("/api")
public class SexualOrientationResource {

	private static final String ENTITY_NAME = "sexualOrientation";
	private final Logger log = LoggerFactory.getLogger(SexualOrientationResource.class);
	private final SexualOrientationRepository sexualOrientationRepository;

	private final SexualOrientationMapper sexualOrientationMapper;

	public SexualOrientationResource(SexualOrientationRepository sexualOrientationRepository, SexualOrientationMapper sexualOrientationMapper) {
		this.sexualOrientationRepository = sexualOrientationRepository;
		this.sexualOrientationMapper = sexualOrientationMapper;
	}

	/**
	 * POST  /sexual-orientations : Create a new sexualOrientation.
	 *
	 * @param sexualOrientationDTO the sexualOrientationDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new sexualOrientationDTO, or with status 400 (Bad Request) if the sexualOrientation has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/sexual-orientations")
	@Timed
	public ResponseEntity<SexualOrientationDTO> createSexualOrientation(@Valid @RequestBody SexualOrientationDTO sexualOrientationDTO) throws URISyntaxException {
		log.debug("REST request to save SexualOrientation : {}", sexualOrientationDTO);
		if (sexualOrientationDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sexualOrientation cannot already have an ID")).body(null);
		}
		SexualOrientation sexualOrientation = sexualOrientationMapper.sexualOrientationDTOToSexualOrientation(sexualOrientationDTO);
		sexualOrientation = sexualOrientationRepository.save(sexualOrientation);
		SexualOrientationDTO result = sexualOrientationMapper.sexualOrientationToSexualOrientationDTO(sexualOrientation);
		return ResponseEntity.created(new URI("/api/sexual-orientations/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /sexual-orientations : Updates an existing sexualOrientation.
	 *
	 * @param sexualOrientationDTO the sexualOrientationDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated sexualOrientationDTO,
	 * or with status 400 (Bad Request) if the sexualOrientationDTO is not valid,
	 * or with status 500 (Internal Server Error) if the sexualOrientationDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/sexual-orientations")
	@Timed
	public ResponseEntity<SexualOrientationDTO> updateSexualOrientation(@Valid @RequestBody SexualOrientationDTO sexualOrientationDTO) throws URISyntaxException {
		log.debug("REST request to update SexualOrientation : {}", sexualOrientationDTO);
		if (sexualOrientationDTO.getId() == null) {
			return createSexualOrientation(sexualOrientationDTO);
		}
		SexualOrientation sexualOrientation = sexualOrientationMapper.sexualOrientationDTOToSexualOrientation(sexualOrientationDTO);
		sexualOrientation = sexualOrientationRepository.save(sexualOrientation);
		SexualOrientationDTO result = sexualOrientationMapper.sexualOrientationToSexualOrientationDTO(sexualOrientation);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sexualOrientationDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /sexual-orientations : get all the sexualOrientations.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of sexualOrientations in body
	 */
	@GetMapping("/sexual-orientations")
	@Timed
	public List<SexualOrientationDTO> getAllSexualOrientations() {
		log.debug("REST request to get all SexualOrientations");
		List<SexualOrientation> sexualOrientations = sexualOrientationRepository.findAll();
		return sexualOrientationMapper.sexualOrientationsToSexualOrientationDTOs(sexualOrientations);
	}

	/**
	 * GET  /sexual-orientations/:id : get the "id" sexualOrientation.
	 *
	 * @param id the id of the sexualOrientationDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the sexualOrientationDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/sexual-orientations/{id}")
	@Timed
	public ResponseEntity<SexualOrientationDTO> getSexualOrientation(@PathVariable Long id) {
		log.debug("REST request to get SexualOrientation : {}", id);
		SexualOrientation sexualOrientation = sexualOrientationRepository.findOne(id);
		SexualOrientationDTO sexualOrientationDTO = sexualOrientationMapper.sexualOrientationToSexualOrientationDTO(sexualOrientation);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sexualOrientationDTO));
	}

	/**
	 * DELETE  /sexual-orientations/:id : delete the "id" sexualOrientation.
	 *
	 * @param id the id of the sexualOrientationDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/sexual-orientations/{id}")
	@Timed
	public ResponseEntity<Void> deleteSexualOrientation(@PathVariable Long id) {
		log.debug("REST request to delete SexualOrientation : {}", id);
		sexualOrientationRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
