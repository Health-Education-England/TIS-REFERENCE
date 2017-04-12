package com.transformuk.hee.tis.reference.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.domain.Title;
import com.transformuk.hee.tis.reference.repository.TitleRepository;
import com.transformuk.hee.tis.reference.api.dto.TitleDTO;
import com.transformuk.hee.tis.reference.service.mapper.TitleMapper;
import com.transformuk.hee.tis.reference.web.rest.util.HeaderUtil;
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
 * REST controller for managing Title.
 */
@RestController
@RequestMapping("/api")
public class TitleResource {

	private static final String ENTITY_NAME = "title";
	private final Logger log = LoggerFactory.getLogger(TitleResource.class);
	private final TitleRepository titleRepository;

	private final TitleMapper titleMapper;

	public TitleResource(TitleRepository titleRepository, TitleMapper titleMapper) {
		this.titleRepository = titleRepository;
		this.titleMapper = titleMapper;
	}

	/**
	 * POST  /titles : Create a new title.
	 *
	 * @param titleDTO the titleDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new titleDTO, or with status 400 (Bad Request) if the title has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/titles")
	@Timed
	public ResponseEntity<TitleDTO> createTitle(@Valid @RequestBody TitleDTO titleDTO) throws URISyntaxException {
		log.debug("REST request to save Title : {}", titleDTO);
		if (titleDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new title cannot already have an ID")).body(null);
		}
		Title title = titleMapper.titleDTOToTitle(titleDTO);
		title = titleRepository.save(title);
		TitleDTO result = titleMapper.titleToTitleDTO(title);
		return ResponseEntity.created(new URI("/api/titles/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /titles : Updates an existing title.
	 *
	 * @param titleDTO the titleDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated titleDTO,
	 * or with status 400 (Bad Request) if the titleDTO is not valid,
	 * or with status 500 (Internal Server Error) if the titleDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/titles")
	@Timed
	public ResponseEntity<TitleDTO> updateTitle(@Valid @RequestBody TitleDTO titleDTO) throws URISyntaxException {
		log.debug("REST request to update Title : {}", titleDTO);
		if (titleDTO.getId() == null) {
			return createTitle(titleDTO);
		}
		Title title = titleMapper.titleDTOToTitle(titleDTO);
		title = titleRepository.save(title);
		TitleDTO result = titleMapper.titleToTitleDTO(title);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, titleDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /titles : get all the titles.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of titles in body
	 */
	@GetMapping("/titles")
	@Timed
	public List<TitleDTO> getAllTitles() {
		log.debug("REST request to get all Titles");
		List<Title> titles = titleRepository.findAll();
		return titleMapper.titlesToTitleDTOs(titles);
	}

	/**
	 * GET  /titles/:id : get the "id" title.
	 *
	 * @param id the id of the titleDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the titleDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/titles/{id}")
	@Timed
	public ResponseEntity<TitleDTO> getTitle(@PathVariable Long id) {
		log.debug("REST request to get Title : {}", id);
		Title title = titleRepository.findOne(id);
		TitleDTO titleDTO = titleMapper.titleToTitleDTO(title);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(titleDTO));
	}

	/**
	 * DELETE  /titles/:id : delete the "id" title.
	 *
	 * @param id the id of the titleDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/titles/{id}")
	@Timed
	public ResponseEntity<Void> deleteTitle(@PathVariable Long id) {
		log.debug("REST request to delete Title : {}", id);
		titleRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
