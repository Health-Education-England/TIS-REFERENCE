package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.service.model.RecordType;
import com.transformuk.hee.tis.reference.service.repository.RecordTypeRepository;
import com.transformuk.hee.tis.reference.api.dto.RecordTypeDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.RecordTypeMapper;
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
 * REST controller for managing RecordType.
 */
@RestController
@RequestMapping("/api")
public class RecordTypeResource {

	private static final String ENTITY_NAME = "recordType";
	private final Logger log = LoggerFactory.getLogger(RecordTypeResource.class);
	private final RecordTypeRepository recordTypeRepository;

	private final RecordTypeMapper recordTypeMapper;

	public RecordTypeResource(RecordTypeRepository recordTypeRepository, RecordTypeMapper recordTypeMapper) {
		this.recordTypeRepository = recordTypeRepository;
		this.recordTypeMapper = recordTypeMapper;
	}

	/**
	 * POST  /record-types : Create a new recordType.
	 *
	 * @param recordTypeDTO the recordTypeDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new recordTypeDTO, or with status 400 (Bad Request) if the recordType has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/record-types")
	@Timed
	public ResponseEntity<RecordTypeDTO> createRecordType(@Valid @RequestBody RecordTypeDTO recordTypeDTO) throws URISyntaxException {
		log.debug("REST request to save RecordType : {}", recordTypeDTO);
		if (recordTypeDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new recordType cannot already have an ID")).body(null);
		}
		RecordType recordType = recordTypeMapper.recordTypeDTOToRecordType(recordTypeDTO);
		recordType = recordTypeRepository.save(recordType);
		RecordTypeDTO result = recordTypeMapper.recordTypeToRecordTypeDTO(recordType);
		return ResponseEntity.created(new URI("/api/record-types/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /record-types : Updates an existing recordType.
	 *
	 * @param recordTypeDTO the recordTypeDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated recordTypeDTO,
	 * or with status 400 (Bad Request) if the recordTypeDTO is not valid,
	 * or with status 500 (Internal Server Error) if the recordTypeDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/record-types")
	@Timed
	public ResponseEntity<RecordTypeDTO> updateRecordType(@Valid @RequestBody RecordTypeDTO recordTypeDTO) throws URISyntaxException {
		log.debug("REST request to update RecordType : {}", recordTypeDTO);
		if (recordTypeDTO.getId() == null) {
			return createRecordType(recordTypeDTO);
		}
		RecordType recordType = recordTypeMapper.recordTypeDTOToRecordType(recordTypeDTO);
		recordType = recordTypeRepository.save(recordType);
		RecordTypeDTO result = recordTypeMapper.recordTypeToRecordTypeDTO(recordType);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recordTypeDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /record-types : get all the recordTypes.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of recordTypes in body
	 */
	@GetMapping("/record-types")
	@Timed
	public List<RecordTypeDTO> getAllRecordTypes() {
		log.debug("REST request to get all RecordTypes");
		List<RecordType> recordTypes = recordTypeRepository.findAll();
		return recordTypeMapper.recordTypesToRecordTypeDTOs(recordTypes);
	}

	/**
	 * GET  /record-types/:id : get the "id" recordType.
	 *
	 * @param id the id of the recordTypeDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the recordTypeDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/record-types/{id}")
	@Timed
	public ResponseEntity<RecordTypeDTO> getRecordType(@PathVariable Long id) {
		log.debug("REST request to get RecordType : {}", id);
		RecordType recordType = recordTypeRepository.findOne(id);
		RecordTypeDTO recordTypeDTO = recordTypeMapper.recordTypeToRecordTypeDTO(recordType);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(recordTypeDTO));
	}

	/**
	 * DELETE  /record-types/:id : delete the "id" recordType.
	 *
	 * @param id the id of the recordTypeDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/record-types/{id}")
	@Timed
	public ResponseEntity<Void> deleteRecordType(@PathVariable Long id) {
		log.debug("REST request to delete RecordType : {}", id);
		recordTypeRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
