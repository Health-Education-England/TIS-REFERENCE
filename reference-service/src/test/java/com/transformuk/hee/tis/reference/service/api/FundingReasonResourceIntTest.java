package com.transformuk.hee.tis.reference.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.api.dto.FundingReasonDto;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.FundingReason;
import com.transformuk.hee.tis.reference.service.repository.FundingReasonRepository;
import com.transformuk.hee.tis.reference.service.service.impl.FundingReasonServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.FundingReasonMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the FundingReasonResource REST controller.
 *
 * @see FundingReasonResource
 */
@SpringBootTest(classes = Application.class)
class FundingReasonResourceIntTest {

  private static final String DEFAULT_REASON = "Redistribution";
  private static final String UPDATED_REASON = "Expansion Posts";
  private static final String UNENCODED_REASON = "Te$t Reason";

  private static final Long FUNDING_REASON_ID = 1L;

  @Autowired
  private FundingReasonMapper fundingReasonMapper;

  @Autowired
  private FundingReasonServiceImpl fundingReasonService;

  @Autowired
  private FundingReasonRepository fundingReasonRepository;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private MockMvc restFundingReasonMockMvc;

  private FundingReason fundingReason;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    FundingReasonResource fundingReasonResource = new FundingReasonResource(fundingReasonMapper,
        fundingReasonService);
    this.restFundingReasonMockMvc = MockMvcBuilders.standaloneSetup(fundingReasonResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @BeforeEach
  void initTestData() {
    fundingReason = new FundingReason();
    fundingReason.setReason(DEFAULT_REASON);
    fundingReason.setStatus(Status.CURRENT);
  }

  @Test
  @Transactional
  void createFundingReason() throws Exception {
    int dbSizeBeforeCreate = fundingReasonRepository.findAll().size();
    FundingReasonDto fundingReasonDto = fundingReasonMapper.toDto(fundingReason);

    restFundingReasonMockMvc.perform(post("/api/funding-reason")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundingReasonDto)))
        .andExpect(status().isCreated());

    List<FundingReason> fundingReasonList = fundingReasonRepository.findAll();
    assertThat(fundingReasonList).hasSize(dbSizeBeforeCreate + 1);
    FundingReason testFundingReason = fundingReasonList.get(fundingReasonList.size() - 1);
    assertThat(testFundingReason.getReason()).isEqualTo(DEFAULT_REASON);
  }

  @Test
  @Transactional
  void createFundingTypeWithExistingId() throws Exception {
    fundingReason = fundingReasonRepository.saveAndFlush(fundingReason);
    int dbSizeBeforeCreate = fundingReasonRepository.findAll().size();

    // Create the FundingReason with an existing ID
    FundingReasonDto fundingReasonDto = fundingReasonMapper.toDto(fundingReason);

    // An entity with an existing id cannot be created, so this API call must fail
    restFundingReasonMockMvc.perform(post("/api/funding-reason")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundingReasonDto)))
        .andExpect(status().isBadRequest());

    // Validate in the database
    List<FundingReason> fundingReasonList = fundingReasonRepository.findAll();
    assertThat(fundingReasonList).hasSize(dbSizeBeforeCreate);
  }

  @Test
  @Transactional
  void updateFundingReason() throws Exception {
    // Initialize the database
    fundingReason = fundingReasonRepository.saveAndFlush(fundingReason);
    int dbSizeBeforeUpdate = fundingReasonRepository.findAll().size();

    // Update the fundingReason
    FundingReasonDto fundingReasonDto = fundingReasonMapper.toDto(fundingReason);
    fundingReasonDto.setReason(UPDATED_REASON);

    restFundingReasonMockMvc.perform(put("/api/funding-reason")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundingReasonDto)))
        .andExpect(status().isOk());

    // Validate the FundingReason in the database
    List<FundingReason> fundingReasonList = fundingReasonRepository.findAll();
    assertThat(fundingReasonList).hasSize(dbSizeBeforeUpdate);
    FundingReason testFundingReason = fundingReasonList.get(fundingReasonList.size() - 1);
    assertThat(testFundingReason.getReason()).isEqualTo(UPDATED_REASON);
  }

  @Test
  @Transactional
  void updateNonExistingFundingReason() throws Exception {
    int dbSizeBeforeUpdate = fundingReasonRepository.findAll().size();

    // Create the FundingReason
    FundingReasonDto fundingReasonDto = fundingReasonMapper.toDto(fundingReason);

    // If the entity doesn't have an id, it will be created instead of just being updated
    restFundingReasonMockMvc.perform(put("/api/funding-reason")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundingReasonDto)))
        .andExpect(status().isCreated());

    // Validate in the database
    List<FundingReason> fundingReasonList = fundingReasonRepository.findAll();
    assertThat(fundingReasonList).hasSize(dbSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  void deleteFundingReason() throws Exception {
    // Initialize the database
    fundingReasonRepository.saveAndFlush(fundingReason);
    int dbSizeBeforeDelete = fundingReasonRepository.findAll().size();

    // Get the fundingReason
    restFundingReasonMockMvc.perform(
            delete("/api/funding-reason/{id}", fundingReason.getId())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database
    List<FundingReason> fundingReasonList = fundingReasonRepository.findAll();
    assertThat(fundingReasonList).hasSize(dbSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  void getAllFundingReasons() throws Exception {
    // Initialize the database
    fundingReasonRepository.saveAndFlush(fundingReason);

    // Get all the fundingReasonList
    restFundingReasonMockMvc.perform(get("/api/funding-reason?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(fundingReason.getId().toString())))
        .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));
  }

  @Test
  @Transactional
  void getFundingReasonsWithQuery() throws Exception {
    // Initialize the database
    FundingReason unencodedFundingReason = new FundingReason();
    unencodedFundingReason.setReason(UNENCODED_REASON);
    fundingReasonRepository.saveAndFlush(unencodedFundingReason);

    // Get all the fundingReasonList
    restFundingReasonMockMvc.perform(
            get("/api/funding-reason?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[0].id").value(unencodedFundingReason.getId().toString()))
        .andExpect(jsonPath("$.[0].reason").value(UNENCODED_REASON));
  }

  @Test
  @Transactional
  void getFundingReason() throws Exception {
    // Initialize the database
    fundingReasonRepository.saveAndFlush(fundingReason);

    // Get the fundingReason
    restFundingReasonMockMvc.perform(get("/api/funding-reason/{id}", fundingReason.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(fundingReason.getId().toString()))
        .andExpect(jsonPath("$.reason").value(DEFAULT_REASON));
  }

  @Test
  @Transactional
  void getNonExistingFundingReason() throws Exception {
    restFundingReasonMockMvc.perform(get("/api/funding-reason/{id}", UUID.randomUUID()))
        .andExpect(status().isNotFound());
  }
}