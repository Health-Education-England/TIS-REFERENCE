package com.transformuk.hee.tis.reference.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.api.dto.LocalOfficeContactDto;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.LocalOffice;
import com.transformuk.hee.tis.reference.service.model.LocalOfficeContact;
import com.transformuk.hee.tis.reference.service.model.LocalOfficeContactType;
import com.transformuk.hee.tis.reference.service.repository.LocalOfficeContactRepository;
import com.transformuk.hee.tis.reference.service.repository.LocalOfficeContactTypeRepository;
import com.transformuk.hee.tis.reference.service.repository.LocalOfficeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.LocalOfficeContactServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.LocalOfficeContactMapper;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the LocalOfficeContactResource REST controller.
 *
 * @see LocalOfficeContactResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class LocalOfficeContactResourceIntTest {

  private static final String DEFAULT_CONTACT = "contact@example.com";
  private static final String UPDATED_CONTACT = "new-contact@example.com";
  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "Te$t Code";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Label";

  @Autowired
  private LocalOfficeContactRepository repository;

  @Autowired
  private LocalOfficeContactTypeRepository contactTypeRepository;

  @Autowired
  private LocalOfficeRepository localOfficeRepository;

  @Autowired
  private LocalOfficeContactMapper mapper;

  @Autowired
  private LocalOfficeContactServiceImpl service;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc mockMvc;

  private LocalOfficeContact entity;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static LocalOfficeContact createEntity(EntityManager em) {
    LocalOfficeContact entity = new LocalOfficeContact();
    entity.setLocalOffice(LocalOfficeResourceIntTest.createEntity(em));
    entity.setContactType(LocalOfficeContactTypeResourceIntTest.createEntity(em));
    entity.setContact(DEFAULT_CONTACT);
    return entity;
  }

  /**
   * Create an entity for this test.
   *
   * @param em                      The entity manager.
   * @param contactTypeCode         An override for the created contact type's code.
   * @param localOfficeAbbreviation An override for the created local office's abbreviation.
   * @return The created LocalOfficeContact.
   */
  private static LocalOfficeContact createEntity(EntityManager em, String contactTypeCode,
      String localOfficeAbbreviation) {
    LocalOfficeContact entity = createEntity(em);

    if (contactTypeCode != null) {
      entity.getContactType().setCode(contactTypeCode);
    }

    if (localOfficeAbbreviation != null) {
      entity.getLocalOffice().setAbbreviation(localOfficeAbbreviation);
    }

    return entity;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    LocalOfficeContactResource controller = new LocalOfficeContactResource(service, mapper);
    this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    entity = createEntity(em);
    entity.setContactType(contactTypeRepository.saveAndFlush(entity.getContactType()));
    entity.setLocalOffice(localOfficeRepository.saveAndFlush(entity.getLocalOffice()));
  }

  @Test
  @Transactional
  public void createLocalOfficeContact() throws Exception {
    int databaseSizeBeforeCreate = repository.findAll().size();

    // Create the LocalOfficeContact.
    LocalOfficeContactDto dto = mapper.toDto(entity);
    mockMvc.perform(post("/api/local-office-contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isCreated());

    // Validate the LocalOfficeContact in the database.
    List<LocalOfficeContact> entityList = repository.findAll();
    assertThat(entityList).hasSize(databaseSizeBeforeCreate + 1);
    LocalOfficeContact testLocalOfficeContact = entityList.get(entityList.size() - 1);
    assertThat(testLocalOfficeContact.getContact()).isEqualTo(DEFAULT_CONTACT);
    assertThat(testLocalOfficeContact.getLocalOffice()).isEqualTo(entity.getLocalOffice());
    assertThat(testLocalOfficeContact.getContactType()).isEqualTo(entity.getContactType());
  }

  @Test(expected = DataIntegrityViolationException.class)
  @Transactional
  public void createLocalOfficeContactWithDuplicateIndex() throws Exception {
    // Create the LocalOfficeContactType.
    LocalOfficeContactDto dto = mapper.toDto(entity);
    mockMvc.perform(post("/api/local-office-contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isCreated());

    mockMvc.perform(post("/api/local-office-contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isCreated());

    // The above insert will not fail until flushed.
    repository.flush();
  }

  @Test
  @Transactional
  public void createLocalOfficeContactWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = repository.findAll().size();

    // Create the LocalOfficeContact with an existing ID.
    entity.setId(UUID.randomUUID());
    LocalOfficeContactDto dto = mapper.toDto(entity);

    // An entity with an existing ID cannot be created, so this API call must fail.
    mockMvc.perform(post("/api/local-office-contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database.
    List<LocalOfficeContact> entityList = repository.findAll();
    assertThat(entityList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkContactTypeIsRequired() throws Exception {
    int databaseSizeBeforeTest = repository.findAll().size();
    // Set the field null.
    entity.setContactType(null);

    // Create the LocalOfficeContact, which fails.
    LocalOfficeContactDto dto = mapper.toDto(entity);

    mockMvc.perform(post("/api/local-office-contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isBadRequest());

    List<LocalOfficeContact> entityList = repository.findAll();
    assertThat(entityList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLocalOfficeIsRequired() throws Exception {
    int databaseSizeBeforeTest = repository.findAll().size();
    // Set the field null.
    entity.setLocalOffice(null);

    // Create the LocalOfficeContact, which fails.
    LocalOfficeContactDto dto = mapper.toDto(entity);

    mockMvc.perform(post("/api/local-office-contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isBadRequest());

    List<LocalOfficeContact> entityList = repository.findAll();
    assertThat(entityList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllLocalOfficeContacts() throws Exception {
    // Initialize the database.
    repository.saveAndFlush(entity);

    // Get all the entityList.
    mockMvc.perform(get("/api/local-office-contacts?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(entity.getId().toString())))
        .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
        .andExpect(jsonPath("$.[*].contactType.id").value(
            hasItem(entity.getContactType().getId().toString())))
        .andExpect(jsonPath("$.[*].localOffice.uuid").value(
            hasItem(entity.getLocalOffice().getUuid().toString())));
  }

  @Test
  @Transactional
  public void getLocalOfficeContactsWithColumnFilter() throws Exception {
    // Initialize the database.
    repository.saveAndFlush(entity);

    LocalOfficeContact entity2 = createEntity(em, UUID.randomUUID().toString(),
        UUID.randomUUID().toString());
    LocalOfficeContactType contactType = entity2.getContactType();
    contactType.setCode("FILTCODE");
    entity2.setContactType(contactTypeRepository.saveAndFlush(contactType));
    entity2.setLocalOffice(localOfficeRepository.saveAndFlush(entity.getLocalOffice()));
    entity2.setContact("column.filter@example.com");
    repository.saveAndFlush(entity2);

    // Get all the matching entities.
    mockMvc.perform(
            get("/api/local-office-contacts?columnFilters=%7B\"contact\":[\"column.filter@example.com\"]%7D"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$.[*].id").value(hasItem(entity2.getId().toString())))
        .andExpect(jsonPath("$.[*].contact").value(hasItem("column.filter@example.com")))
        .andExpect(jsonPath("$.[*].contact").value(not(hasItem(DEFAULT_CONTACT))))
        .andExpect(jsonPath("$.[*].contactType.code").value(hasItem("FILTCODE")));
  }

  @Test
  @Transactional
  public void getLocalOfficeContactsWithQueryMatchingContact() throws Exception {
    // Initialize the database.
    LocalOfficeContact entity = createEntity(em, UUID.randomUUID().toString(),
        UUID.randomUUID().toString());
    entity.setContactType(contactTypeRepository.saveAndFlush(entity.getContactType()));
    entity.setLocalOffice(localOfficeRepository.saveAndFlush(entity.getLocalOffice()));

    entity.setContact("query@example.com");
    repository.saveAndFlush(entity);

    // Get all the matching entities.
    mockMvc.perform(
            get("/api/local-office-contacts?searchQuery=\"query@example.com\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.[*].id").value(entity.getId().toString()))
        .andExpect(jsonPath("$.[*].contact").value(hasItem("query@example.com")))
        .andExpect(jsonPath("$.[*].contactType.id").value(
            hasItem(entity.getContactType().getId().toString())))
        .andExpect(jsonPath("$.[*].localOffice.uuid").value(
            hasItem(entity.getLocalOffice().getUuid().toString())));
  }

  @Test
  @Transactional
  public void getLocalOfficeContactsWithQueryMatchingLocalOfficeAbbreviation() throws Exception {
    // Initialize the database.
    LocalOfficeContact entity = createEntity(em, UUID.randomUUID().toString(), "TSTABR");
    entity.setContactType(contactTypeRepository.saveAndFlush(entity.getContactType()));
    entity.setLocalOffice(localOfficeRepository.saveAndFlush(entity.getLocalOffice()));
    repository.saveAndFlush(entity);

    // Get all the matching entities.
    mockMvc.perform(get("/api/local-office-contacts?searchQuery=\"TST\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.[*].id").value(entity.getId().toString()))
        .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
        .andExpect(jsonPath("$.[*].contactType.id").value(
            hasItem(entity.getContactType().getId().toString())))
        .andExpect(jsonPath("$.[*].localOffice.uuid").value(
            hasItem(entity.getLocalOffice().getUuid().toString())))
        .andExpect(jsonPath("$.[*].localOffice.abbreviation").value("TSTABR"));
  }

  @Test
  @Transactional
  public void getLocalOfficeContactsWithQueryMatchingLocalOfficeName() throws Exception {
    // Initialize the database.
    LocalOfficeContact entity = createEntity(em, UUID.randomUUID().toString(),
        UUID.randomUUID().toString());
    entity.setContactType(contactTypeRepository.saveAndFlush(entity.getContactType()));
    LocalOffice localOffice = entity.getLocalOffice();
    localOffice.setName("TST Name");
    entity.setLocalOffice(localOfficeRepository.saveAndFlush(localOffice));
    repository.saveAndFlush(entity);

    // Get all the matching entities.
    mockMvc.perform(get("/api/local-office-contacts?searchQuery=\"TST\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.[*].id").value(entity.getId().toString()))
        .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
        .andExpect(jsonPath("$.[*].contactType.id").value(
            hasItem(entity.getContactType().getId().toString())))
        .andExpect(jsonPath("$.[*].localOffice.uuid").value(
            hasItem(entity.getLocalOffice().getUuid().toString())))
        .andExpect(jsonPath("$.[*].localOffice.name").value("TST Name"));
  }

  @Test
  @Transactional
  public void getLocalOfficeContactsWithQueryMatchingContactTypeCode() throws Exception {
    // Initialize the database.
    LocalOfficeContact entity = createEntity(em, "TSTCODE", UUID.randomUUID().toString());
    entity.setContactType(contactTypeRepository.saveAndFlush(entity.getContactType()));
    entity.setLocalOffice(localOfficeRepository.saveAndFlush(entity.getLocalOffice()));
    repository.saveAndFlush(entity);

    // Get all the matching entities.
    mockMvc.perform(get("/api/local-office-contacts?searchQuery=\"TST\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.[*].id").value(entity.getId().toString()))
        .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
        .andExpect(jsonPath("$.[*].contactType.id").value(
            hasItem(entity.getContactType().getId().toString())))
        .andExpect(jsonPath("$.[*].contactType.code").value("TSTCODE"))
        .andExpect(jsonPath("$.[*].localOffice.uuid").value(
            hasItem(entity.getLocalOffice().getUuid().toString())));
  }

  @Test
  @Transactional
  public void getLocalOfficeContactsWithQueryMatchingContactTypeLabel() throws Exception {
    // Initialize the database.
    LocalOfficeContact entity = createEntity(em, UUID.randomUUID().toString(),
        UUID.randomUUID().toString());
    LocalOfficeContactType contactType = entity.getContactType();
    contactType.setLabel("TST Label");
    entity.setContactType(contactTypeRepository.saveAndFlush(contactType));
    entity.setLocalOffice(localOfficeRepository.saveAndFlush(entity.getLocalOffice()));
    repository.saveAndFlush(entity);

    String contactTypeLabel = entity.getContactType().getLabel();

    // Get all the matching entities.
    mockMvc.perform(get("/api/local-office-contacts?searchQuery=\"TST\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.[*].id").value(entity.getId().toString()))
        .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
        .andExpect(jsonPath("$.[*].contactType.id").value(
            hasItem(entity.getContactType().getId().toString())))
        .andExpect(jsonPath("$.[*].contactType.label").value("TST Label"))
        .andExpect(jsonPath("$.[*].localOffice.uuid").value(
            hasItem(entity.getLocalOffice().getUuid().toString())));
  }

  @Test
  @Transactional
  public void getLocalOfficeContact() throws Exception {
    // Initialize the database.
    repository.saveAndFlush(entity);

    // Get the contact.
    mockMvc.perform(get("/api/local-office-contacts/{id}", entity.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(entity.getId().toString()))
        .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT))
        .andExpect(jsonPath("$.contactType.id").value(entity.getContactType().getId().toString()))
        .andExpect(
            jsonPath("$.localOffice.uuid").value(entity.getLocalOffice().getUuid().toString()));
  }

  @Test
  @Transactional
  public void getNonExistingLocalOfficeContact() throws Exception {
    // Get the contact.
    mockMvc.perform(get("/api/local-office-contacts/{id}", UUID.randomUUID()))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateLocalOfficeContact() throws Exception {
    // Initialize the database.
    repository.saveAndFlush(entity);
    int databaseSizeBeforeUpdate = repository.findAll().size();

    // Update the contact.
    LocalOfficeContact updatedEntity = repository.findById(entity.getId()).get();
    updatedEntity.setContact(UPDATED_CONTACT);

    LocalOfficeContactType updatedContactType = LocalOfficeContactTypeResourceIntTest.createEntity(
        em);
    updatedContactType.setCode("UPDCODE");
    updatedContactType = contactTypeRepository.saveAndFlush(updatedContactType);
    updatedEntity.setContactType(updatedContactType);

    LocalOffice updatedLocalOffice = LocalOfficeResourceIntTest.createEntity(em);
    updatedLocalOffice.setAbbreviation("UPDABR");
    updatedLocalOffice = localOfficeRepository.saveAndFlush(updatedLocalOffice);
    updatedEntity.setLocalOffice(updatedLocalOffice);

    LocalOfficeContactDto dto = mapper.toDto(updatedEntity);

    mockMvc.perform(put("/api/local-office-contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isOk());

    // Validate the LocalOfficeContact in the database.
    List<LocalOfficeContact> entityList = repository.findAll();
    assertThat(entityList).hasSize(databaseSizeBeforeUpdate);
    LocalOfficeContact testContact = entityList.get(entityList.size() - 1);
    assertThat(testContact.getContact()).isEqualTo(UPDATED_CONTACT);
    assertThat(testContact.getContactType()).isEqualTo(updatedContactType);
    assertThat(testContact.getLocalOffice()).isEqualTo(updatedLocalOffice);
  }

  @Test
  @Transactional
  public void updateNonExistingLocalOfficeContact() throws Exception {
    int databaseSizeBeforeUpdate = repository.findAll().size();

    // Create the LocalOfficeContact.
    LocalOfficeContactDto dto = mapper.toDto(entity);

    // If the entity doesn't have an ID, it will cause a bad request.
    mockMvc.perform(put("/api/local-office-contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isBadRequest());

    // Validate the LocalOfficeContact in the database.
    List<LocalOfficeContact> entityList = repository.findAll();
    assertThat(entityList).hasSize(databaseSizeBeforeUpdate);
  }
}
