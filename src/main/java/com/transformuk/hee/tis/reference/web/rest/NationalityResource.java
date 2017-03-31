package com.transformuk.hee.tis.reference.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.domain.Nationality;
import com.transformuk.hee.tis.reference.repository.NationalityRepository;
import com.transformuk.hee.tis.reference.service.dto.NationalityDTO;
import com.transformuk.hee.tis.reference.service.mapper.NationalityMapper;
import com.transformuk.hee.tis.reference.web.rest.util.HeaderUtil;
import com.transformuk.hee.tis.reference.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Nationality.
 */
@RestController
@RequestMapping("/api")
public class NationalityResource {

	private static final String ENTITY_NAME = "nationality";
	private final Logger log = LoggerFactory.getLogger(NationalityResource.class);
	private final NationalityRepository nationalityRepository;

	private final NationalityMapper nationalityMapper;

	public NationalityResource(NationalityRepository nationalityRepository, NationalityMapper nationalityMapper) {
		this.nationalityRepository = nationalityRepository;
		this.nationalityMapper = nationalityMapper;
	}

	/**
	 * POST  /nationalities : Create a new nationality.
	 *
	 * @param nationalityDTO the nationalityDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new nationalityDTO, or with status 400 (Bad Request) if the nationality has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/nationalities")
	@Timed
	public ResponseEntity<NationalityDTO> createNationality(@Valid @RequestBody NationalityDTO nationalityDTO) throws URISyntaxException {
		log.debug("REST request to save Nationality : {}", nationalityDTO);
		if (nationalityDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new nationality cannot already have an ID")).body(null);
		}
		Nationality nationality = nationalityMapper.nationalityDTOToNationality(nationalityDTO);
		nationality = nationalityRepository.save(nationality);
		NationalityDTO result = nationalityMapper.nationalityToNationalityDTO(nationality);
		return ResponseEntity.created(new URI("/api/nationalities/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /nationalities : Updates an existing nationality.
	 *
	 * @param nationalityDTO the nationalityDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated nationalityDTO,
	 * or with status 400 (Bad Request) if the nationalityDTO is not valid,
	 * or with status 500 (Internal Server Error) if the nationalityDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/nationalities")
	@Timed
	public ResponseEntity<NationalityDTO> updateNationality(@Valid @RequestBody NationalityDTO nationalityDTO) throws URISyntaxException {
		log.debug("REST request to update Nationality : {}", nationalityDTO);
		if (nationalityDTO.getId() == null) {
			return createNationality(nationalityDTO);
		}
		Nationality nationality = nationalityMapper.nationalityDTOToNationality(nationalityDTO);
		nationality = nationalityRepository.save(nationality);
		NationalityDTO result = nationalityMapper.nationalityToNationalityDTO(nationality);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, nationalityDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /nationalities : get all the nationalities.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of nationalities in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/nationalities")
	@Timed
	public ResponseEntity<List<NationalityDTO>> getAllNationalities(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Nationalities");
		Page<Nationality> page = nationalityRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/nationalities");
		return new ResponseEntity<>(nationalityMapper.nationalitiesToNationalityDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /nationalities/:id : get the "id" nationality.
	 *
	 * @param id the id of the nationalityDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the nationalityDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/nationalities/{id}")
	@Timed
	public ResponseEntity<NationalityDTO> getNationality(@PathVariable Long id) {
		log.debug("REST request to get Nationality : {}", id);
		Nationality nationality = nationalityRepository.findOne(id);
		NationalityDTO nationalityDTO = nationalityMapper.nationalityToNationalityDTO(nationality);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(nationalityDTO));
	}

	/**
	 * DELETE  /nationalities/:id : delete the "id" nationality.
	 *
	 * @param id the id of the nationalityDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/nationalities/{id}")
	@Timed
	public ResponseEntity<Void> deleteNationality(@PathVariable Long id) {
		log.debug("REST request to delete Nationality : {}", id);
		nationalityRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
