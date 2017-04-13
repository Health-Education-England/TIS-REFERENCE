package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.service.model.TariffRate;
import com.transformuk.hee.tis.reference.service.repository.TariffRateRepository;
import com.transformuk.hee.tis.reference.service.api.dto.TariffRateDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.TariffRateMapper;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
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
 * REST controller for managing TariffRate.
 */
@RestController
@RequestMapping("/api")
public class TariffRateResource {

	private static final String ENTITY_NAME = "tariffRate";
	private final Logger log = LoggerFactory.getLogger(TariffRateResource.class);
	private final TariffRateRepository tariffRateRepository;

	private final TariffRateMapper tariffRateMapper;

	public TariffRateResource(TariffRateRepository tariffRateRepository, TariffRateMapper tariffRateMapper) {
		this.tariffRateRepository = tariffRateRepository;
		this.tariffRateMapper = tariffRateMapper;
	}

	/**
	 * POST  /tariff-rates : Create a new tariffRate.
	 *
	 * @param tariffRateDTO the tariffRateDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new tariffRateDTO, or with status 400 (Bad Request) if the tariffRate has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/tariff-rates")
	@Timed
	public ResponseEntity<TariffRateDTO> createTariffRate(@Valid @RequestBody TariffRateDTO tariffRateDTO) throws URISyntaxException {
		log.debug("REST request to save TariffRate : {}", tariffRateDTO);
		if (tariffRateDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tariffRate cannot already have an ID")).body(null);
		}
		TariffRate tariffRate = tariffRateMapper.tariffRateDTOToTariffRate(tariffRateDTO);
		tariffRate = tariffRateRepository.save(tariffRate);
		TariffRateDTO result = tariffRateMapper.tariffRateToTariffRateDTO(tariffRate);
		return ResponseEntity.created(new URI("/api/tariff-rates/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /tariff-rates : Updates an existing tariffRate.
	 *
	 * @param tariffRateDTO the tariffRateDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated tariffRateDTO,
	 * or with status 400 (Bad Request) if the tariffRateDTO is not valid,
	 * or with status 500 (Internal Server Error) if the tariffRateDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/tariff-rates")
	@Timed
	public ResponseEntity<TariffRateDTO> updateTariffRate(@Valid @RequestBody TariffRateDTO tariffRateDTO) throws URISyntaxException {
		log.debug("REST request to update TariffRate : {}", tariffRateDTO);
		if (tariffRateDTO.getId() == null) {
			return createTariffRate(tariffRateDTO);
		}
		TariffRate tariffRate = tariffRateMapper.tariffRateDTOToTariffRate(tariffRateDTO);
		tariffRate = tariffRateRepository.save(tariffRate);
		TariffRateDTO result = tariffRateMapper.tariffRateToTariffRateDTO(tariffRate);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tariffRateDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /tariff-rates : get all the tariffRates.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of tariffRates in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/tariff-rates")
	@Timed
	public ResponseEntity<List<TariffRateDTO>> getAllTariffRates(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of TariffRates");
		Page<TariffRate> page = tariffRateRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tariff-rates");
		return new ResponseEntity<>(tariffRateMapper.tariffRatesToTariffRateDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /tariff-rates/:id : get the "id" tariffRate.
	 *
	 * @param id the id of the tariffRateDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the tariffRateDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/tariff-rates/{id}")
	@Timed
	public ResponseEntity<TariffRateDTO> getTariffRate(@PathVariable Long id) {
		log.debug("REST request to get TariffRate : {}", id);
		TariffRate tariffRate = tariffRateRepository.findOne(id);
		TariffRateDTO tariffRateDTO = tariffRateMapper.tariffRateToTariffRateDTO(tariffRate);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tariffRateDTO));
	}

	/**
	 * DELETE  /tariff-rates/:id : delete the "id" tariffRate.
	 *
	 * @param id the id of the tariffRateDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/tariff-rates/{id}")
	@Timed
	public ResponseEntity<Void> deleteTariffRate(@PathVariable Long id) {
		log.debug("REST request to delete TariffRate : {}", id);
		tariffRateRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
