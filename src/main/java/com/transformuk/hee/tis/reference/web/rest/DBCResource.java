package com.transformuk.hee.tis.reference.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.domain.DBC;
import com.transformuk.hee.tis.reference.repository.DBCRepository;
import com.transformuk.hee.tis.reference.service.dto.DBCDTO;
import com.transformuk.hee.tis.reference.service.mapper.DBCMapper;
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
 * REST controller for managing DBC.
 */
@RestController
@RequestMapping("/api")
public class DBCResource {

	private static final String ENTITY_NAME = "dBC";
	private final Logger log = LoggerFactory.getLogger(DBCResource.class);
	private final DBCRepository dBCRepository;

	private final DBCMapper dBCMapper;

	public DBCResource(DBCRepository dBCRepository, DBCMapper dBCMapper) {
		this.dBCRepository = dBCRepository;
		this.dBCMapper = dBCMapper;
	}

	/**
	 * POST  /d-bcs : Create a new dBC.
	 *
	 * @param dBCDTO the dBCDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new dBCDTO, or with status 400 (Bad Request) if the dBC has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/d-bcs")
	@Timed
	public ResponseEntity<DBCDTO> createDBC(@Valid @RequestBody DBCDTO dBCDTO) throws URISyntaxException {
		log.debug("REST request to save DBC : {}", dBCDTO);
		if (dBCDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new dBC cannot already have an ID")).body(null);
		}
		DBC dBC = dBCMapper.dBCDTOToDBC(dBCDTO);
		dBC = dBCRepository.save(dBC);
		DBCDTO result = dBCMapper.dBCToDBCDTO(dBC);
		return ResponseEntity.created(new URI("/api/d-bcs/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /d-bcs : Updates an existing dBC.
	 *
	 * @param dBCDTO the dBCDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated dBCDTO,
	 * or with status 400 (Bad Request) if the dBCDTO is not valid,
	 * or with status 500 (Internal Server Error) if the dBCDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/d-bcs")
	@Timed
	public ResponseEntity<DBCDTO> updateDBC(@Valid @RequestBody DBCDTO dBCDTO) throws URISyntaxException {
		log.debug("REST request to update DBC : {}", dBCDTO);
		if (dBCDTO.getId() == null) {
			return createDBC(dBCDTO);
		}
		DBC dBC = dBCMapper.dBCDTOToDBC(dBCDTO);
		dBC = dBCRepository.save(dBC);
		DBCDTO result = dBCMapper.dBCToDBCDTO(dBC);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dBCDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /d-bcs : get all the dBCS.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of dBCS in body
	 */
	@GetMapping("/d-bcs")
	@Timed
	public List<DBCDTO> getAllDBCS() {
		log.debug("REST request to get all DBCS");
		List<DBC> dBCS = dBCRepository.findAll();
		return dBCMapper.dBCSToDBCDTOs(dBCS);
	}

	/**
	 * GET  /d-bcs/:id : get the "id" dBC.
	 *
	 * @param id the id of the dBCDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the dBCDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/d-bcs/{id}")
	@Timed
	public ResponseEntity<DBCDTO> getDBC(@PathVariable Long id) {
		log.debug("REST request to get DBC : {}", id);
		DBC dBC = dBCRepository.findOne(id);
		DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(dBC);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dBCDTO));
	}

	/**
	 * DELETE  /d-bcs/:id : delete the "id" dBC.
	 *
	 * @param id the id of the dBCDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/d-bcs/{id}")
	@Timed
	public ResponseEntity<Void> deleteDBC(@PathVariable Long id) {
		log.debug("REST request to delete DBC : {}", id);
		dBCRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
