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

import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.api.validation.FundingSubTypeValidator;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.FundingSubType;
import com.transformuk.hee.tis.reference.service.model.FundingType;
import com.transformuk.hee.tis.reference.service.repository.FundingSubTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.FundingSubTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.FundingSubTypeMapper;
import java.util.Arrays;
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
 * Test class for the FundingSubTypeResource REST controller.
 *
 * @see FundingSubTypeResource
 */
@SpringBootTest(classes = Application.class)
class FundingSubTypeResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "Te$t Code";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Label";

  private static final Long FUNDING_TYPE_ID = 1L;

  @Autowired
  private FundingSubTypeMapper fundingSubTypeMapper;

  @Autowired
  private FundingSubTypeServiceImpl fundingSubTypeService;

  @Autowired
  private FundingSubTypeValidator fundingSubTypeValidator;

  @Autowired
  private FundingSubTypeRepository fundingSubTypeRepository;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private MockMvc restFundingSubTypeMockMvc;

  private FundingSubType fundingSubType;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    FundingSubTypeResource fundingSubTypeResource = new FundingSubTypeResource(fundingSubTypeMapper,
        fundingSubTypeService, fundingSubTypeValidator);
    this.restFundingSubTypeMockMvc = MockMvcBuilders.standaloneSetup(fundingSubTypeResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @BeforeEach
  void initTestData() {
    FundingType fundingType = new FundingType();
    fundingType.setId(FUNDING_TYPE_ID);
    fundingSubType = new FundingSubType();
    fundingSubType.setCode(DEFAULT_CODE);
    fundingSubType.setLabel(DEFAULT_LABEL);
    fundingSubType.setStatus(Status.CURRENT);
    fundingSubType.setFundingType(fundingType);
  }

  @Test
  @Transactional
  void createFundingSubType() throws Exception {
    int dbSizeBeforeCreate = fundingSubTypeRepository.findAll().size();
    FundingSubTypeDto fundingSubTypeDto = fundingSubTypeMapper.toDto(fundingSubType);

    restFundingSubTypeMockMvc.perform(post("/api/funding-sub-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundingSubTypeDto)))
        .andExpect(status().isCreated());

    List<FundingSubType> fundingSubTypeList = fundingSubTypeRepository.findAll();
    assertThat(fundingSubTypeList).hasSize(dbSizeBeforeCreate + 1);
    FundingSubType testFundingSubType = fundingSubTypeList.get(fundingSubTypeList.size() - 1);
    assertThat(testFundingSubType.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testFundingSubType.getLabel()).isEqualTo(DEFAULT_LABEL);
    assertThat(testFundingSubType.getFundingType().getId()).isEqualTo(FUNDING_TYPE_ID);
  }

  @Test
  @Transactional
  void createFundingTypeWithExistingId() throws Exception {
    fundingSubType = fundingSubTypeRepository.saveAndFlush(fundingSubType);
    int dbSizeBeforeCreate = fundingSubTypeRepository.findAll().size();

    // Create the FundingSubType with an existing ID
    FundingSubTypeDto fundingSubTypeDto = fundingSubTypeMapper.toDto(fundingSubType);

    // An entity with an existing id cannot be created, so this API call must fail
    restFundingSubTypeMockMvc.perform(post("/api/funding-sub-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundingSubTypeDto)))
        .andExpect(status().isBadRequest());

    // Validate in the database
    List<FundingSubType> fundingSubTypeList = fundingSubTypeRepository.findAll();
    assertThat(fundingSubTypeList).hasSize(dbSizeBeforeCreate);
  }

  @Test
  @Transactional
  void updateFundingSubType() throws Exception {
    // Initialize the database
    fundingSubType = fundingSubTypeRepository.saveAndFlush(fundingSubType);
    int dbSizeBeforeUpdate = fundingSubTypeRepository.findAll().size();

    // Update the fundingSubType
    FundingSubTypeDto fundingSubTypeDto = fundingSubTypeMapper.toDto(fundingSubType);
    fundingSubTypeDto.setCode(UPDATED_CODE);
    fundingSubTypeDto.setLabel(UPDATED_LABEL);

    restFundingSubTypeMockMvc.perform(put("/api/funding-sub-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundingSubTypeDto)))
        .andExpect(status().isOk());

    // Validate the FundingSubType in the database
    List<FundingSubType> fundingSubTypeList = fundingSubTypeRepository.findAll();
    assertThat(fundingSubTypeList).hasSize(dbSizeBeforeUpdate);
    FundingSubType testFundingSubType = fundingSubTypeList.get(fundingSubTypeList.size() - 1);
    assertThat(testFundingSubType.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testFundingSubType.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  void updateNonExistingFundingSubType() throws Exception {
    int dbSizeBeforeUpdate = fundingSubTypeRepository.findAll().size();

    // Create the FundingSubType
    FundingSubTypeDto fundingSubTypeDto = fundingSubTypeMapper.toDto(fundingSubType);

    // If the entity doesn't have an id, it will be created instead of just being updated
    restFundingSubTypeMockMvc.perform(put("/api/funding-sub-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundingSubTypeDto)))
        .andExpect(status().isCreated());

    // Validate in the database
    List<FundingSubType> fundingSubTypeList = fundingSubTypeRepository.findAll();
    assertThat(fundingSubTypeList).hasSize(dbSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  void deleteFundingSubType() throws Exception {
    // Initialize the database
    fundingSubTypeRepository.saveAndFlush(fundingSubType);
    int dbSizeBeforeDelete = fundingSubTypeRepository.findAll().size();

    // Get the fundingSubType
    restFundingSubTypeMockMvc.perform(
            delete("/api/funding-sub-types/{id}", fundingSubType.getId())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database
    List<FundingSubType> fundingSubTypeList = fundingSubTypeRepository.findAll();
    assertThat(fundingSubTypeList).hasSize(dbSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  void getAllFundingSubTypes() throws Exception {
    // Initialize the database
    fundingSubTypeRepository.saveAndFlush(fundingSubType);

    // Get all the fundingSubTypeList
    restFundingSubTypeMockMvc.perform(get("/api/funding-sub-types?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(fundingSubType.getId().toString())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
        .andExpect(jsonPath("$.[*].fundingType.id").value(hasItem(FUNDING_TYPE_ID.intValue())));
  }

  @Test
  @Transactional
  void getFundingSubTypesWithQuery() throws Exception {
    // Initialize the database
    FundingSubType unencodedFundingSubType = new FundingSubType();
    unencodedFundingSubType.setCode(UNENCODED_CODE);
    unencodedFundingSubType.setLabel(UNENCODED_LABEL);
    FundingType fundingType = new FundingType();
    fundingType.setId(FUNDING_TYPE_ID);
    unencodedFundingSubType.setFundingType(fundingType);
    fundingSubTypeRepository.saveAndFlush(unencodedFundingSubType);

    // Get all the fundingSubTypeList
    restFundingSubTypeMockMvc.perform(
            get("/api/funding-sub-types?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[0].id").value(unencodedFundingSubType.getId().toString()))
        .andExpect(jsonPath("$.[0].code").value(UNENCODED_CODE))
        .andExpect(jsonPath("$.[0].label").value(UNENCODED_LABEL))
        .andExpect(jsonPath("$.[0].fundingType.id").value(FUNDING_TYPE_ID.intValue()));
  }

  @Test
  @Transactional
  void getFundingSubType() throws Exception {
    // Initialize the database
    fundingSubTypeRepository.saveAndFlush(fundingSubType);

    // Get the fundingSubType
    restFundingSubTypeMockMvc.perform(get("/api/funding-sub-types/{id}", fundingSubType.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(fundingSubType.getId().toString()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
  }

  @Test
  @Transactional
  void getNonExistingFundingSubType() throws Exception {
    restFundingSubTypeMockMvc.perform(get("/api/funding-sub-types/{id}", UUID.randomUUID()))
        .andExpect(status().isNotFound());
  }
}