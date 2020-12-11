package com.transformuk.hee.tis.reference.service.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.AssessmentType;
import com.transformuk.hee.tis.reference.service.repository.AssessmentTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.AssessmentTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.AssessmentTypeMapper;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the AssessmentType REST controller.
 *
 * @see AssessmentTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AssessmentTypeResourceIntTest {

  private static final String LOCATION_HEADER = "Location";
  private static final Long DEFAULT_ID = 1L;
  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";
  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Assessment";
  @InjectMocks
  private AssessmentTypeResource testObj;

  @MockBean
  private AssessmentTypeRepository assessmentTypeRepositoryMock;

  @MockBean
  private AssessmentTypeServiceImpl assessmentTypeServiceMock;

  @Captor
  private ArgumentCaptor<Pageable> pageableArgumentCaptor;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private MockMvc testObjMockMvc;
  private AssessmentType assessmentType;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static AssessmentType createEntity() {
    AssessmentType assessmentType = new AssessmentType();
    assessmentType.setId(DEFAULT_ID);
    assessmentType.setCode(DEFAULT_CODE);
    assessmentType.setLabel(DEFAULT_LABEL);
    return assessmentType;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    AssessmentTypeMapper mapper = Mappers.getMapper(AssessmentTypeMapper.class);
    AssessmentTypeResource testObj = new AssessmentTypeResource(assessmentTypeRepositoryMock,
        assessmentTypeServiceMock, mapper);
    this.testObjMockMvc = MockMvcBuilders.standaloneSetup(testObj)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    assessmentType = createEntity();
  }

  @Test
  @Transactional
  public void createAssessmentTypeWithNoIdShouldReturnThePersistedAssessmentType()
      throws Exception {
    assessmentType.setId(null);
    when(assessmentTypeRepositoryMock.save(assessmentType)).thenReturn(createEntity());

    testObjMockMvc.perform(post("/api/assessment-types")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(assessmentType)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(DEFAULT_ID))
        .andExpect(jsonPath("$.code").value(equalTo(DEFAULT_CODE)))
        .andExpect(jsonPath("$.label").value(equalTo(DEFAULT_LABEL)))
        .andExpect(header().string(LOCATION_HEADER, "/api/assessment-types/" + DEFAULT_CODE));

    verify(assessmentTypeRepositoryMock).save(assessmentType);
  }

  @Test
  @Transactional
  public void createAssessmentTypeWithIdShouldReturnBadRequest() throws Exception {
    testObjMockMvc.perform(post("/api/assessment-types")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(assessmentType)))
        .andExpect(status().isBadRequest());

    verify(assessmentTypeRepositoryMock, never()).save(anyCollectionOf(AssessmentType.class));
  }

  @Test
  @Transactional
  public void bulkCreateAssessmentTypesWithNoIdsShouldReturnThePersistedAssessmentTypes()
      throws Exception {
    assessmentType.setId(null);
    AssessmentType assessmentType2 = new AssessmentType();
    assessmentType2.setId(null);
    assessmentType2.setCode("ABC");
    assessmentType2.setLabel("ABC_123");
    List<AssessmentType> assessmentTypes = Arrays.asList(assessmentType, assessmentType2);

    when(assessmentTypeRepositoryMock.save(assessmentTypes)).thenAnswer(invocation -> {
      List<AssessmentType> argument = invocation.getArgumentAt(0, List.class);
      argument.get(0).setId(DEFAULT_ID);
      argument.get(1).setId(2L);
      return argument;
    });

    testObjMockMvc.perform(post("/api/bulk-assessment-types")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(assessmentTypes)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].id").value(hasItems(DEFAULT_ID.intValue(), 2)))
        .andExpect(jsonPath("$.[*].code").value(hasItems(DEFAULT_CODE, "ABC")))
        .andExpect(jsonPath("$.[*].label").value(hasItems(DEFAULT_LABEL, "ABC_123")));

    verify(assessmentTypeRepositoryMock).save(any(Iterable.class));
  }

  @Test
  @Transactional
  public void bulkCreateAssessmentTypesWithIdShouldReturnBadRequest() throws Exception {
    List<AssessmentType> assessmentTypes = Arrays.asList(assessmentType, createEntity());

    testObjMockMvc.perform(post("/api/bulk-assessment-types")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(assessmentTypes)))
        .andExpect(status().isBadRequest());

    verify(assessmentTypeRepositoryMock, never()).save(any(AssessmentType.class));
  }

  @Test
  @Transactional
  public void updateAssessmentTypeShouldReturnUpdateAssessmentType() throws Exception {
    when(assessmentTypeRepositoryMock.findOne(assessmentType.getId())).thenReturn(assessmentType);
    when(assessmentTypeRepositoryMock.save(assessmentType)).thenReturn(assessmentType);

    testObjMockMvc.perform(MockMvcRequestBuilders.put("/api/assessment-types")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(assessmentType)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(DEFAULT_ID))
        .andExpect(jsonPath("$.code").value(equalTo(DEFAULT_CODE)))
        .andExpect(jsonPath("$.label").value(equalTo(DEFAULT_LABEL)));

    verify(assessmentTypeRepositoryMock).save(assessmentType);
  }

  @Test
  @Transactional
  public void updateAssessmentTypeShouldReturnCreateAssessmentTypeWhenItDoesntExist()
      throws Exception {
    assessmentType.setId(null);
    when(assessmentTypeRepositoryMock.findOne(assessmentType.getId())).thenReturn(null);
    when(assessmentTypeRepositoryMock.save(assessmentType)).thenReturn(createEntity());

    testObjMockMvc.perform(MockMvcRequestBuilders.put("/api/assessment-types")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(assessmentType)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(DEFAULT_ID))
        .andExpect(jsonPath("$.code").value(equalTo(DEFAULT_CODE)))
        .andExpect(jsonPath("$.label").value(equalTo(DEFAULT_LABEL)));

    verify(assessmentTypeRepositoryMock).save(assessmentType);
  }

  @Test
  @Transactional
  public void updateAssessmentTypeWithNoCodeShouldReturnBadRequest() throws Exception {
    assessmentType.setCode(null);

    testObjMockMvc.perform(MockMvcRequestBuilders.put("/api/assessment-types")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(assessmentType)))
        .andExpect(status().isBadRequest());

    verify(assessmentTypeRepositoryMock, never()).save(any(AssessmentType.class));
  }

  @Test
  @Transactional
  public void getAllAssessmentTypes() throws Exception {
    AssessmentType assessmentType1 = createEntity();
    AssessmentType assessmentType2 = new AssessmentType();
    assessmentType2.setId(2L);
    assessmentType2.setCode("ABC");
    assessmentType2.setLabel("ABC_123");

    List<AssessmentType> allAssessmentTypes = Lists.newArrayList(assessmentType1, assessmentType2);
    Page<AssessmentType> pageResults = new PageImpl<>(allAssessmentTypes);
    when(assessmentTypeRepositoryMock.findAll(pageableArgumentCaptor.capture()))
        .thenReturn(pageResults);

    testObjMockMvc.perform(get("/api/assessment-types?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItems(DEFAULT_ID.intValue(), 2)))
        .andExpect(jsonPath("$.[*].code").value(hasItems(DEFAULT_CODE, "ABC")))
        .andExpect(jsonPath("$.[*].label").value(hasItems(DEFAULT_LABEL, "ABC_123")));

    Pageable captorValue = pageableArgumentCaptor.getValue();
    Sort expectedSort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
    Assert.assertEquals(expectedSort, captorValue.getSort());
  }

  @Test
  @Transactional
  public void getAssessmentTypesForSearchQuery() throws Exception {
    AssessmentType unencodedAssessmentType = new AssessmentType();
    unencodedAssessmentType.setId(DEFAULT_ID);
    unencodedAssessmentType.setLabel(UNENCODED_LABEL);
    unencodedAssessmentType.setCode(UNENCODED_CODE);

    List<AssessmentType> allAssessmentTypes = Lists.newArrayList(unencodedAssessmentType);
    Page<AssessmentType> pageResults = new PageImpl<>(allAssessmentTypes);
    when(assessmentTypeServiceMock.advancedSearch(eq(UNENCODED_LABEL), any(), any()))
        .thenReturn(pageResults);

    testObjMockMvc
        .perform(get("/api/assessment-types?searchQuery=\"Te%24t%20Assessment\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(DEFAULT_ID.intValue()))
        .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getAssessmentTypeByCodeShouldReturnSingleAssessmentType() throws Exception {
    when(assessmentTypeRepositoryMock.findOneByCode(assessmentType.getCode()))
        .thenReturn(assessmentType);

    testObjMockMvc.perform(get("/api/assessment-types/{code}", assessmentType.getCode()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(DEFAULT_ID))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));

    verify(assessmentTypeRepositoryMock).findOneByCode(assessmentType.getCode());
  }

  @Test
  @Transactional
  public void getAssessmentTypeByCodeShouldReturn404() throws Exception {
    when(assessmentTypeRepositoryMock.findOneByCode(assessmentType.getCode())).thenReturn(null);

    testObjMockMvc.perform(get("/api/assessment-types/{code}", assessmentType.getCode()))
        .andExpect(status().isNotFound());

    verify(assessmentTypeRepositoryMock).findOneByCode(assessmentType.getCode());
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(AssessmentType.class);
  }
}
