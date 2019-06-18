package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.TitleDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.Title;
import com.transformuk.hee.tis.reference.service.repository.TitleRepository;
import com.transformuk.hee.tis.reference.service.service.impl.TitleServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.TitleMapper;
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

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the TitleResource REST controller.
 *
 * @see TitleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TitleResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Title";

  @Autowired
  private TitleRepository titleRepository;

  @Autowired
  private TitleMapper titleMapper;

  @Autowired
  private TitleServiceImpl titleService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restTitleMockMvc;

  private Title title;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Title createEntity(EntityManager em) {
    Title title = new Title()
        .code(DEFAULT_CODE)
        .label(DEFAULT_LABEL);
    return title;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    TitleResource titleResource = new TitleResource(titleRepository, titleMapper, titleService);
    this.restTitleMockMvc = MockMvcBuilders.standaloneSetup(titleResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    title = createEntity(em);
  }

  @Test
  @Transactional
  public void createTitle() throws Exception {
    int databaseSizeBeforeCreate = titleRepository.findAll().size();

    // Create the Title
    TitleDTO titleDTO = titleMapper.titleToTitleDTO(title);
    restTitleMockMvc.perform(post("/api/titles")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(titleDTO)))
        .andExpect(status().isCreated());

    // Validate the Title in the database
    List<Title> titleList = titleRepository.findAll();
    assertThat(titleList).hasSize(databaseSizeBeforeCreate + 1);
    Title testTitle = titleList.get(titleList.size() - 1);
    assertThat(testTitle.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testTitle.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createTitleWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = titleRepository.findAll().size();

    // Create the Title with an existing ID
    title.setId(1L);
    TitleDTO titleDTO = titleMapper.titleToTitleDTO(title);

    // An entity with an existing ID cannot be created, so this API call must fail
    restTitleMockMvc.perform(post("/api/titles")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(titleDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Title> titleList = titleRepository.findAll();
    assertThat(titleList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = titleRepository.findAll().size();
    // set the field null
    title.setCode(null);

    // Create the Title, which fails.
    TitleDTO titleDTO = titleMapper.titleToTitleDTO(title);

    restTitleMockMvc.perform(post("/api/titles")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(titleDTO)))
        .andExpect(status().isBadRequest());

    List<Title> titleList = titleRepository.findAll();
    assertThat(titleList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = titleRepository.findAll().size();
    // set the field null
    title.setLabel(null);

    // Create the Title, which fails.
    TitleDTO titleDTO = titleMapper.titleToTitleDTO(title);

    restTitleMockMvc.perform(post("/api/titles")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(titleDTO)))
        .andExpect(status().isBadRequest());

    List<Title> titleList = titleRepository.findAll();
    assertThat(titleList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllTitles() throws Exception {
    // Initialize the database
    titleRepository.saveAndFlush(title);

    // Get all the titleList
    restTitleMockMvc.perform(get("/api/titles?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(title.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }
  
  @Test
  @Transactional
  public void getTitlesWithQuery() throws Exception {
    // Initialize the database
    Title unencodedTitle = new Title()
        .code(UNENCODED_CODE)
        .label(UNENCODED_LABEL);
    titleRepository.saveAndFlush(unencodedTitle);
    
    // Get all the titleList
    restTitleMockMvc.perform(get("/api/titles?searchQuery=\"Te%24t\"&sort=id,desc"))
    .andExpect(status().isOk())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    .andExpect(jsonPath("$.[*].id").value(unencodedTitle.getId().intValue()))
    .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
    .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getTitle() throws Exception {
    // Initialize the database
    titleRepository.saveAndFlush(title);

    // Get the title
    restTitleMockMvc.perform(get("/api/titles/{id}", title.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(title.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingTitle() throws Exception {
    // Get the title
    restTitleMockMvc.perform(get("/api/titles/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateTitle() throws Exception {
    // Initialize the database
    titleRepository.saveAndFlush(title);
    int databaseSizeBeforeUpdate = titleRepository.findAll().size();

    // Update the title
    Title updatedTitle = titleRepository.findOne(title.getId());
    updatedTitle
        .code(UPDATED_CODE)
        .label(UPDATED_LABEL);
    TitleDTO titleDTO = titleMapper.titleToTitleDTO(updatedTitle);

    restTitleMockMvc.perform(put("/api/titles")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(titleDTO)))
        .andExpect(status().isOk());

    // Validate the Title in the database
    List<Title> titleList = titleRepository.findAll();
    assertThat(titleList).hasSize(databaseSizeBeforeUpdate);
    Title testTitle = titleList.get(titleList.size() - 1);
    assertThat(testTitle.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testTitle.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingTitle() throws Exception {
    int databaseSizeBeforeUpdate = titleRepository.findAll().size();

    // Create the Title
    TitleDTO titleDTO = titleMapper.titleToTitleDTO(title);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restTitleMockMvc.perform(put("/api/titles")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(titleDTO)))
        .andExpect(status().isCreated());

    // Validate the Title in the database
    List<Title> titleList = titleRepository.findAll();
    assertThat(titleList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteTitle() throws Exception {
    // Initialize the database
    titleRepository.saveAndFlush(title);
    int databaseSizeBeforeDelete = titleRepository.findAll().size();

    // Get the title
    restTitleMockMvc.perform(delete("/api/titles/{id}", title.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Title> titleList = titleRepository.findAll();
    assertThat(titleList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Title.class);
  }
}
