package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.service.model.Gender;
import com.transformuk.hee.tis.reference.service.repository.GenderRepository;
import com.transformuk.hee.tis.reference.api.dto.GenderDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.GenderMapper;
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
 * REST controller for managing Gender.
 */
@RestController
@RequestMapping("/api")
public class GenderResource {

	private static final String ENTITY_NAME = "gender";
	private final Logger log = LoggerFactory.getLogger(GenderResource.class);
	private final GenderRepository genderRepository;

	private final GenderMapper genderMapper;

	public GenderResource(GenderRepository genderRepository, GenderMapper genderMapper) {
		this.genderRepository = genderRepository;
		this.genderMapper = genderMapper;
	}

	/**
	 * POST  /genders : Create a new gender.
	 *
	 * @param genderDTO the genderDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new genderDTO, or with status 400 (Bad Request) if the gender has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/genders")
	@Timed
	public ResponseEntity<GenderDTO> createGender(@Valid @RequestBody GenderDTO genderDTO) throws URISyntaxException {
		log.debug("REST request to save Gender : {}", genderDTO);
		if (genderDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new gender cannot already have an ID")).body(null);
		}
		Gender gender = genderMapper.genderDTOToGender(genderDTO);
		gender = genderRepository.save(gender);
		GenderDTO result = genderMapper.genderToGenderDTO(gender);
		return ResponseEntity.created(new URI("/api/genders/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /genders : Updates an existing gender.
	 *
	 * @param genderDTO the genderDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated genderDTO,
	 * or with status 400 (Bad Request) if the genderDTO is not valid,
	 * or with status 500 (Internal Server Error) if the genderDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/genders")
	@Timed
	public ResponseEntity<GenderDTO> updateGender(@Valid @RequestBody GenderDTO genderDTO) throws URISyntaxException {
		log.debug("REST request to update Gender : {}", genderDTO);
		if (genderDTO.getId() == null) {
			return createGender(genderDTO);
		}
		Gender gender = genderMapper.genderDTOToGender(genderDTO);
		gender = genderRepository.save(gender);
		GenderDTO result = genderMapper.genderToGenderDTO(gender);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, genderDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /genders : get all the genders.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of genders in body
	 */
	@GetMapping("/genders")
	@Timed
	public List<GenderDTO> getAllGenders() {
		log.debug("REST request to get all Genders");
		List<Gender> genders = genderRepository.findAll();
		return genderMapper.gendersToGenderDTOs(genders);
	}

	/**
	 * GET  /genders/:id : get the "id" gender.
	 *
	 * @param id the id of the genderDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the genderDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/genders/{id}")
	@Timed
	public ResponseEntity<GenderDTO> getGender(@PathVariable Long id) {
		log.debug("REST request to get Gender : {}", id);
		Gender gender = genderRepository.findOne(id);
		GenderDTO genderDTO = genderMapper.genderToGenderDTO(gender);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(genderDTO));
	}

	/**
	 * DELETE  /genders/:id : delete the "id" gender.
	 *
	 * @param id the id of the genderDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/genders/{id}")
	@Timed
	public ResponseEntity<Void> deleteGender(@PathVariable Long id) {
		log.debug("REST request to delete Gender : {}", id);
		genderRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
