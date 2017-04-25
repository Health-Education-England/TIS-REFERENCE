package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.service.model.FundingType;
import com.transformuk.hee.tis.reference.service.repository.FundingTypeRepository;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.FundingTypeMapper;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FundingType.
 */
@RestController
@RequestMapping("/api")
public class FundingTypeResource {

	private static final String ENTITY_NAME = "fundingType";
	private final Logger log = LoggerFactory.getLogger(FundingTypeResource.class);
	private final FundingTypeRepository fundingTypeRepository;

	private final FundingTypeMapper fundingTypeMapper;

	public FundingTypeResource(FundingTypeRepository fundingTypeRepository, FundingTypeMapper fundingTypeMapper) {
		this.fundingTypeRepository = fundingTypeRepository;
		this.fundingTypeMapper = fundingTypeMapper;
	}

	/**
	 * POST  /funding-types : Create a new fundingType.
	 *
	 * @param fundingTypeDTO the fundingTypeDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new fundingTypeDTO, or with status 400 (Bad Request) if the fundingType has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/funding-types")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<FundingTypeDTO> createFundingType(@Valid @RequestBody FundingTypeDTO fundingTypeDTO) throws URISyntaxException {
		log.debug("REST request to save FundingType : {}", fundingTypeDTO);
		if (fundingTypeDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new fundingType cannot already have an ID")).body(null);
		}
		FundingType fundingType = fundingTypeMapper.fundingTypeDTOToFundingType(fundingTypeDTO);
		fundingType = fundingTypeRepository.save(fundingType);
		FundingTypeDTO result = fundingTypeMapper.fundingTypeToFundingTypeDTO(fundingType);
		return ResponseEntity.created(new URI("/api/funding-types/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /funding-types : Updates an existing fundingType.
	 *
	 * @param fundingTypeDTO the fundingTypeDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated fundingTypeDTO,
	 * or with status 400 (Bad Request) if the fundingTypeDTO is not valid,
	 * or with status 500 (Internal Server Error) if the fundingTypeDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/funding-types")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<FundingTypeDTO> updateFundingType(@Valid @RequestBody FundingTypeDTO fundingTypeDTO) throws URISyntaxException {
		log.debug("REST request to update FundingType : {}", fundingTypeDTO);
		if (fundingTypeDTO.getId() == null) {
			return createFundingType(fundingTypeDTO);
		}
		FundingType fundingType = fundingTypeMapper.fundingTypeDTOToFundingType(fundingTypeDTO);
		fundingType = fundingTypeRepository.save(fundingType);
		FundingTypeDTO result = fundingTypeMapper.fundingTypeToFundingTypeDTO(fundingType);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fundingTypeDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /funding-types : get all the fundingTypes.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of fundingTypes in body
	 */
	@GetMapping("/funding-types")
	@Timed
	public List<FundingTypeDTO> getAllFundingTypes() {
		log.debug("REST request to get all FundingTypes");
		List<FundingType> fundingTypes = fundingTypeRepository.findAll();
		return fundingTypeMapper.fundingTypesToFundingTypeDTOs(fundingTypes);
	}

	/**
	 * GET  /funding-types/:id : get the "id" fundingType.
	 *
	 * @param id the id of the fundingTypeDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the fundingTypeDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/funding-types/{id}")
	@Timed
	public ResponseEntity<FundingTypeDTO> getFundingType(@PathVariable Long id) {
		log.debug("REST request to get FundingType : {}", id);
		FundingType fundingType = fundingTypeRepository.findOne(id);
		FundingTypeDTO fundingTypeDTO = fundingTypeMapper.fundingTypeToFundingTypeDTO(fundingType);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fundingTypeDTO));
	}

	/**
	 * DELETE  /funding-types/:id : delete the "id" fundingType.
	 *
	 * @param id the id of the fundingTypeDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/funding-types/{id}")
	@Timed
	@PreAuthorize("hasAuthority('reference:delete:entities')")
	public ResponseEntity<Void> deleteFundingType(@PathVariable Long id) {
		log.debug("REST request to delete FundingType : {}", id);
		fundingTypeRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
