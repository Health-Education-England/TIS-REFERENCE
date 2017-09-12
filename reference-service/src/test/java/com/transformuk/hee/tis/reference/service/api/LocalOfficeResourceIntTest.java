package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.LocalOfficeDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.LocalOffice;
import com.transformuk.hee.tis.reference.service.repository.LocalOfficeRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.LocalOfficeMapper;
import net.sf.cglib.core.Local;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the LocalOfficeResource REST controller.
 *
 * @see LocalOfficeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class LocalOfficeResourceIntTest {

  private static final String DEFAULT_ABBREVIATION = "AAAAAAAAAA";
  private static final String UPDATED_ABBREVIATION = "BBBBBBBBBB";

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String HENE_NAME = "Health Education England North East";
  private static final String HENWL_NAME = "Health Education England North West London";
  private static final String HEKSS_NAME = "Health Education England Kent, Surrey and Sussex";

  private static String[] localOfficeArray = new String[]{HENE_NAME,HENWL_NAME,HEKSS_NAME};


  @Autowired
  private LocalOfficeRepository localOfficeRepository;

  @Autowired
  private LocalOfficeMapper localOfficeMapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restLocalOfficeMockMvc;

  private LocalOffice localOffice;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static LocalOffice createEntity(EntityManager em) {
    LocalOffice localOffice = new LocalOffice()
        .abbreviation(DEFAULT_ABBREVIATION)
        .name(DEFAULT_NAME);
    return localOffice;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    LocalOfficeResource localOfficeResource = new LocalOfficeResource(localOfficeRepository, localOfficeMapper);
    this.restLocalOfficeMockMvc = MockMvcBuilders.standaloneSetup(localOfficeResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
		TestUtil.mockUserProfile("jamesh", "1-AIIDR8", "1-AIIDWA");
		// jamesh has dbcs for North West London and Kent, Surrey & Sussex
  }

  @Before
  public void initTest() {
    localOffice = createEntity(em);
  }

  @Test
  @Transactional
  public void createLocalOffice() throws Exception {
    int databaseSizeBeforeCreate = localOfficeRepository.findAll().size();

    // Create the LocalOffice
    LocalOfficeDTO localOfficeDTO = localOfficeMapper.localOfficeToLocalOfficeDTO(localOffice);
    restLocalOfficeMockMvc.perform(post("/api/local-offices")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(localOfficeDTO)))
        .andExpect(status().isCreated());

    // Validate the LocalOffice in the database
    List<LocalOffice> localOfficeList = localOfficeRepository.findAll();
    assertThat(localOfficeList).hasSize(databaseSizeBeforeCreate + 1);
    LocalOffice testLocalOffice = localOfficeList.get(localOfficeList.size() - 1);
    assertThat(testLocalOffice.getAbbreviation()).isEqualTo(DEFAULT_ABBREVIATION);
    assertThat(testLocalOffice.getName()).isEqualTo(DEFAULT_NAME);
  }

  @Test
  @Transactional
  public void createLocalOfficeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = localOfficeRepository.findAll().size();

    // Create the LocalOffice with an existing ID
    localOffice.setId(1L);
    LocalOfficeDTO localOfficeDTO = localOfficeMapper.localOfficeToLocalOfficeDTO(localOffice);

    // An entity with an existing ID cannot be created, so this API call must fail
    restLocalOfficeMockMvc.perform(post("/api/local-offices")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(localOfficeDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<LocalOffice> localOfficeList = localOfficeRepository.findAll();
    assertThat(localOfficeList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkAbbreviationIsRequired() throws Exception {
    int databaseSizeBeforeTest = localOfficeRepository.findAll().size();
    // set the field null
    localOffice.setAbbreviation(null);

    // Create the LocalOffice, which fails.
    LocalOfficeDTO localOfficeDTO = localOfficeMapper.localOfficeToLocalOfficeDTO(localOffice);

    restLocalOfficeMockMvc.perform(post("/api/local-offices")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(localOfficeDTO)))
        .andExpect(status().isBadRequest());

    List<LocalOffice> localOfficeList = localOfficeRepository.findAll();
    assertThat(localOfficeList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = localOfficeRepository.findAll().size();
    // set the field null
    localOffice.setName(null);

    // Create the LocalOffice, which fails.
    LocalOfficeDTO localOfficeDTO = localOfficeMapper.localOfficeToLocalOfficeDTO(localOffice);

    restLocalOfficeMockMvc.perform(post("/api/local-offices")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(localOfficeDTO)))
        .andExpect(status().isBadRequest());

    List<LocalOffice> localOfficeList = localOfficeRepository.findAll();
    assertThat(localOfficeList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllLocalOffices() throws Exception {
    // Initialize the database
    localOfficeRepository.saveAndFlush(localOffice);

    // Get all the localOfficeList
    restLocalOfficeMockMvc.perform(get("/api/local-offices?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(localOffice.getId().intValue())))
        .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION.toString())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
  }

	@Test
	@Transactional
	public void getAllLocalOfficesUserAndCheckOnlyAllowedLOsAreReturned() throws Exception {
	// Given
  	// Create some variables for localOfficeRepository size comparison
		int localOfficeRepositoryPreSize, localOfficeRepositoryPostSize;
		// Find the initial size of the localOfficeRepository
		localOfficeRepositoryPreSize = localOfficeRepository.findAll().size();

		// Add the static localOffice object
		localOfficeRepository.saveAndFlush(localOffice);

		// Create some more localOffices - with real Codes etc
		// Use counter to change abbreviation to satisfy check constraint on table

		Integer count = 1;
		for (String lo : localOfficeArray) {
			LocalOffice localOfficeReal = new LocalOffice()
					.abbreviation(DEFAULT_ABBREVIATION.substring(1, 9) + count.toString())
					.name(lo);
			localOfficeRepository.saveAndFlush(localOfficeReal);
			count++;
		}

// Check the localRepository contains the expected values
		assertThat(localOfficeRepository.findAll()).extracting("name").contains(HEKSS_NAME,HENE_NAME,HENWL_NAME,DEFAULT_NAME);
		// Check that the localOfficeRepository now has 4 more rows
		localOfficeRepositoryPostSize = localOfficeRepository.findAll().size();
		assertThat(localOfficeRepositoryPostSize-localOfficeRepositoryPreSize == 4);

// Get a valid ID that the user has access to from the localOfficeRepository
		int testID = 0; // initialise it in case no local office is found
		List<LocalOffice> allLocalOffices = localOfficeRepository.findAll();
		for (LocalOffice lo : allLocalOffices) {
			if (lo.getName().equals("Health Education England North West London")) {
				testID = lo.getId().intValue();
				break;
			}
		}
		// When & Then
		// Get the local offices
		ResultActions resultActions = restLocalOfficeMockMvc.perform(get("/api/local-offices/user"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(testID)))
				.andExpect(jsonPath("$.[*].abbreviation").value(hasItem("AAAAAAAA3")))
				.andExpect(jsonPath("$.[*].abbreviation").value(hasItem("AAAAAAAA2")))
				.andExpect(jsonPath("$.[*].name").value(hasItem(HEKSS_NAME)))
				.andExpect(jsonPath("$.[*].name").value(hasItem(HENWL_NAME)))
				.andExpect(jsonPath("$.[*].name").value(not(contains(HENE_NAME))));
	}

  @Test
  @Transactional
  public void getLocalOffice() throws Exception {
    // Initialize the database
    localOfficeRepository.saveAndFlush(localOffice);

    // Get the localOffice
    restLocalOfficeMockMvc.perform(get("/api/local-offices/{id}", localOffice.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(localOffice.getId().intValue()))
        .andExpect(jsonPath("$.abbreviation").value(DEFAULT_ABBREVIATION.toString()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingLocalOffice() throws Exception {
    // Get the localOffice
    restLocalOfficeMockMvc.perform(get("/api/local-offices/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateLocalOffice() throws Exception {
    // Initialize the database
    localOfficeRepository.saveAndFlush(localOffice);
    int databaseSizeBeforeUpdate = localOfficeRepository.findAll().size();

    // Update the localOffice
    LocalOffice updatedLocalOffice = localOfficeRepository.findOne(localOffice.getId());
    updatedLocalOffice
        .abbreviation(UPDATED_ABBREVIATION)
        .name(UPDATED_NAME);
    LocalOfficeDTO localOfficeDTO = localOfficeMapper.localOfficeToLocalOfficeDTO(updatedLocalOffice);

    restLocalOfficeMockMvc.perform(put("/api/local-offices")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(localOfficeDTO)))
        .andExpect(status().isOk());

    // Validate the LocalOffice in the database
    List<LocalOffice> localOfficeList = localOfficeRepository.findAll();
    assertThat(localOfficeList).hasSize(databaseSizeBeforeUpdate);
    LocalOffice testLocalOffice = localOfficeList.get(localOfficeList.size() - 1);
    assertThat(testLocalOffice.getAbbreviation()).isEqualTo(UPDATED_ABBREVIATION);
    assertThat(testLocalOffice.getName()).isEqualTo(UPDATED_NAME);
  }

  @Test
  @Transactional
  public void updateNonExistingLocalOffice() throws Exception {
    int databaseSizeBeforeUpdate = localOfficeRepository.findAll().size();

    // Create the LocalOffice
    LocalOfficeDTO localOfficeDTO = localOfficeMapper.localOfficeToLocalOfficeDTO(localOffice);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restLocalOfficeMockMvc.perform(put("/api/local-offices")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(localOfficeDTO)))
        .andExpect(status().isCreated());

    // Validate the LocalOffice in the database
    List<LocalOffice> localOfficeList = localOfficeRepository.findAll();
    assertThat(localOfficeList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteLocalOffice() throws Exception {
    // Initialize the database
    localOfficeRepository.saveAndFlush(localOffice);
    int databaseSizeBeforeDelete = localOfficeRepository.findAll().size();

    // Get the localOffice
    restLocalOfficeMockMvc.perform(delete("/api/local-offices/{id}", localOffice.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<LocalOffice> localOfficeList = localOfficeRepository.findAll();
    assertThat(localOfficeList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(LocalOffice.class);
  }
}
