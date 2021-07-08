package com.transformuk.hee.tis.reference.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.IsNot.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.DBC;
import com.transformuk.hee.tis.reference.service.repository.DBCRepository;
import com.transformuk.hee.tis.reference.service.service.impl.DBCServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.DBCMapper;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the DBCResource REST controller.
 *
 * @see DBCResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DBCResourceIntTest {

  private static final String DEFAULT_DBC = "AAAAAAAAAA";
  private static final String UPDATED_DBC = "BBBBBBBBBB";
  private static final String UNENCODED_DBC = "TV-Code";

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";
  private static final String UNENCODED_NAME = "Te$ting & Validation Body";

  private static final String DEFAULT_ABBR = "AAAAAAAAAA";
  private static final String UPDATED_ABBR = "BBBBBBBBBB";
  private static final String UNENCODED_ABBR = "T&V Body";

  private static final String HENE_DBC_CODE = "1-AIIDSI";
  private static final String HENWL_DBC_CODE = "1-AIIDWA";
  private static final String HEKSS_DBC_CODE = "1-AIIDR8";

  private static String[] dbcArray = new String[] {HENE_DBC_CODE, HENWL_DBC_CODE, HEKSS_DBC_CODE};

  @Autowired
  private DBCRepository dBCRepository;

  @Autowired
  private DBCMapper dBCMapper;

  @Autowired
  private DBCServiceImpl dbcService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restDBCMockMvc;

  private DBC dBC;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static DBC createEntity() {
    DBC dbc = new DBC();
    dbc.setDbc(DEFAULT_DBC);
    dbc.setName(DEFAULT_NAME);
    dbc.setAbbr(DEFAULT_ABBR);
    return dbc;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    DBCResource dBCResource = new DBCResource(dBCRepository, dBCMapper, dbcService);
    this.restDBCMockMvc = MockMvcBuilders.standaloneSetup(dBCResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
    dBC = createEntity();
    dBCRepository.deleteAllInBatch();
    TestUtil.mockUserProfile("jamesh", HENWL_DBC_CODE, HEKSS_DBC_CODE);
  }

  @Test
  @Transactional
  public void createDBC() throws Exception {
    // Create the DBC
    DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(dBC);
    restDBCMockMvc.perform(post("/api/dbcs")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(dBCDTO)))
        .andExpect(status().isCreated());

    // Validate the DBC in the database
    List<DBC> dBCList = dBCRepository.findAll();
    assertThat(dBCList).hasSize(1);
    DBC testDBC = dBCList.get(dBCList.size() - 1);
    assertThat(testDBC.getDbc()).isEqualTo(DEFAULT_DBC);
    assertThat(testDBC.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testDBC.getAbbr()).isEqualTo(DEFAULT_ABBR);
  }

  @Test
  @Transactional
  public void createDBCWithExistingId() throws Exception {
    // Create the DBC with an existing ID
    dBC.setId(1L);
    DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(dBC);

    // An entity with an existing ID cannot be created, so this API call must fail
    restDBCMockMvc.perform(post("/api/dbcs")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(dBCDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<DBC> dBCList = dBCRepository.findAll();
    assertThat(dBCList).hasSize(0);
  }

  @Test
  @Transactional
  public void checkDbcIsRequired() throws Exception {
    // set the field null
    dBC.setDbc(null);

    // Create the DBC, which fails.
    DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(dBC);

    restDBCMockMvc.perform(post("/api/dbcs")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(dBCDTO)))
        .andExpect(status().isBadRequest());

    List<DBC> dBCList = dBCRepository.findAll();
    assertThat(dBCList).hasSize(0);
  }

  @Test
  @Transactional
  public void checkNameIsRequired() throws Exception {
    // set the field null
    dBC.setName(null);

    // Create the DBC, which fails.
    DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(dBC);

    restDBCMockMvc.perform(post("/api/dbcs")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(dBCDTO)))
        .andExpect(status().isBadRequest());

    List<DBC> dBCList = dBCRepository.findAll();
    assertThat(dBCList).hasSize(0);
  }

  @Test
  @Transactional
  public void checkAbbrIsRequired() throws Exception {
    // set the field null
    dBC.setAbbr(null);

    // Create the DBC, which fails.
    DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(dBC);

    restDBCMockMvc.perform(post("/api/dbcs")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(dBCDTO)))
        .andExpect(status().isBadRequest());

    List<DBC> dBCList = dBCRepository.findAll();
    assertThat(dBCList).hasSize(0);
  }

  @Test
  @Transactional
  public void getAllDBCS() throws Exception {
    // Initialize the database
    dBCRepository.saveAndFlush(dBC);

    // Get all the dBCList
    restDBCMockMvc.perform(get("/api/dbcs?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(dBC.getId().intValue())))
        .andExpect(jsonPath("$.[*].dbc").value(hasItem(DEFAULT_DBC.toString())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].abbr").value(hasItem(DEFAULT_ABBR.toString())));
  }

  @Test
  @Transactional
  public void getDBCWithEncodedQuery() throws Exception {
    DBC encDbc = new DBC();
    encDbc.setDbc(UNENCODED_DBC);
    encDbc.setName(UNENCODED_NAME);
    encDbc.setAbbr(UNENCODED_ABBR);
    ArrayList<DBC> dbcs = Lists.newArrayList(dBC, encDbc);
    // Initialize the database
    dBCRepository.saveAll(dbcs);
    dBCRepository.flush();

    // Get all the dBCList
    restDBCMockMvc.perform(get("/api/dbcs?searchQuery=\"Te%24ting%20%26%20\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(encDbc.getId().intValue()))
        .andExpect(jsonPath("$.[*].dbc").value(UNENCODED_DBC))
        .andExpect(jsonPath("$.[*].name").value(UNENCODED_NAME))
        .andExpect(jsonPath("$.[*].abbr").value(UNENCODED_ABBR));
  }

  @Test
  @Transactional
  public void getDBC() throws Exception {
    // Initialize the database
    dBCRepository.saveAndFlush(dBC);

    // Get the dBC
    restDBCMockMvc.perform(get("/api/dbcs/{id}", dBC.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(dBC.getId().intValue()))
        .andExpect(jsonPath("$.dbc").value(DEFAULT_DBC.toString()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.abbr").value(DEFAULT_ABBR.toString()));
  }

  @Test
  @Transactional
  public void getDBCByCode() throws Exception {
    // Initialize the database
    dBCRepository.saveAndFlush(dBC);

    // Get the dBC
    restDBCMockMvc.perform(get("/api/dbcs/code/" + DEFAULT_DBC))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.dbc").value(DEFAULT_DBC))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
        .andExpect(jsonPath("$.abbr").value(DEFAULT_ABBR));
  }

  @Test
  @Transactional
  public void getNonExistingDBC() throws Exception {
    // Get the dBC
    restDBCMockMvc.perform(get("/api/dbcs/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateDBC() throws Exception {
    // Initialize the database
    dBCRepository.saveAndFlush(dBC);

    // Update the dBC
    DBC updatedDBC = dBCRepository.findById(dBC.getId()).get();
    updatedDBC.setDbc(UPDATED_DBC);
    updatedDBC.setName(UPDATED_NAME);
    updatedDBC.setAbbr(UPDATED_ABBR);
    DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(updatedDBC);

    restDBCMockMvc.perform(put("/api/dbcs")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(dBCDTO)))
        .andExpect(status().isOk());

    // Validate the DBC in the database
    List<DBC> dBCList = dBCRepository.findAll();
    assertThat(dBCList).hasSize(1);
    DBC testDBC = dBCList.get(dBCList.size() - 1);
    assertThat(testDBC.getDbc()).isEqualTo(UPDATED_DBC);
    assertThat(testDBC.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testDBC.getAbbr()).isEqualTo(UPDATED_ABBR);
  }

  @Test
  @Transactional
  public void updateNonExistingDBC() throws Exception {
    // Create the DBC
    DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(dBC);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restDBCMockMvc.perform(put("/api/dbcs")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(dBCDTO)))
        .andExpect(status().isCreated());

    // Validate the DBC in the database
    List<DBC> dBCList = dBCRepository.findAll();
    assertThat(dBCList).hasSize(1);
  }

  @Test
  @Transactional
  public void getDbcsUserAndCheckOnlyAllowedDbcsAreReturned() throws Exception {
    // Given
    // Create some variables for DBCRepository size comparison
    int dbcRepositorySize;

    // Add the static DBC object
    dBCRepository.saveAndFlush(dBC);

    // Create some more DBCs - with real Codes etc
    // Use counter to change abbreviation to satisfy check constraint on table

    Integer count = 1;
    for (String dbc : dbcArray) {
      DBC dbcReal = new DBC();
      dbcReal.setDbc(dbc);
      dbcReal.setName(dbc);
      dbcReal.setAbbr("AAA" + count.toString());
      dBCRepository.saveAndFlush(dbcReal);
      count++;
    }

    // Check the dbcRepository contains the expected values
    assertThat(dBCRepository.findAll()).extracting("dbc")
        .contains(HEKSS_DBC_CODE, HENE_DBC_CODE, HEKSS_DBC_CODE);
    // Check that the dbcRepository now has the correct number of additions
    dbcRepositorySize = dBCRepository.findAll().size();
    assertThat(dbcRepositorySize == 4);

    // Get a valid ID that the user has access to from the dbcRepository
    int testID = 0; // initialise it in case no dbc is found
    List<DBC> allDbcs = dBCRepository.findAll();
    for (DBC dbc : allDbcs) {
      if (dbc.getDbc().equals(HENWL_DBC_CODE)) {
        testID = dbc.getId().intValue();
        break;
      }
    }
    // When & Then
    // Get the local offices
    restDBCMockMvc.perform(get("/api/dbcs/user"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(testID)))
        .andExpect(jsonPath("$.[*].dbc").value(hasItem(HEKSS_DBC_CODE)))
        .andExpect(jsonPath("$.[*].dbc").value(hasItem(HENWL_DBC_CODE)))
        .andExpect(jsonPath("$.[*].dbc").value(not(contains(HENE_DBC_CODE))));
  }

  @Test
  @Transactional
  public void deleteDBC() throws Exception {
    // Initialize the database
    dBCRepository.saveAndFlush(dBC);

    // Get the dBC
    restDBCMockMvc.perform(delete("/api/dbcs/{id}", dBC.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<DBC> dBCList = dBCRepository.findAll();
    assertThat(dBCList).hasSize(0);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(DBC.class);
  }
}
