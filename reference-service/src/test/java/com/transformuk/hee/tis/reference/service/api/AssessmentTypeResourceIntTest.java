package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.AssessmentType;
import com.transformuk.hee.tis.reference.service.repository.AssessmentTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.AssessmentTypeServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
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

/**
 * Test class for the AssessmentType REST controller.
 *
 * @see AssessmentTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AssessmentTypeResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Assessment";
  
  public static final String LOCATION_HEADER = "Location";

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
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static AssessmentType createEntity() {
    AssessmentType assessmentType = new AssessmentType();
    assessmentType.setCode(DEFAULT_CODE);
    assessmentType.setLabel(DEFAULT_LABEL);
    return assessmentType;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    AssessmentTypeResource testObj = new AssessmentTypeResource(assessmentTypeRepositoryMock, assessmentTypeServiceMock);
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
  public void createAssessmentTypeShouldReturnThePersistedAssessmentType() throws Exception {
    when(assessmentTypeRepositoryMock.save(assessmentType)).thenReturn(assessmentType);

    testObjMockMvc.perform(post("/api/assessment-types")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(assessmentType)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.code").value(equalTo(DEFAULT_CODE)))
        .andExpect(jsonPath("$.label").value(equalTo(DEFAULT_LABEL)))
        .andExpect(header().string(LOCATION_HEADER, "/api/assessment-types/" + DEFAULT_CODE));

    verify(assessmentTypeRepositoryMock).save(assessmentType);
  }

  @Test
  @Transactional
  public void createAssessmentTypeWithNoCodeShouldShouldReturnBadRequest() throws Exception {
    assessmentType.setCode(null);

    testObjMockMvc.perform(post("/api/assessment-types")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(assessmentType)))
        .andExpect(status().isBadRequest());

    verify(assessmentTypeRepositoryMock, never()).save(any(AssessmentType.class));
  }

  @Test
  @Transactional
  public void updateAssessmentTypeShouldReturnUpdateAssessmentType() throws Exception {
    when(assessmentTypeRepositoryMock.findOne(assessmentType.getCode())).thenReturn(assessmentType);
    when(assessmentTypeRepositoryMock.save(assessmentType)).thenReturn(assessmentType);

    testObjMockMvc.perform(MockMvcRequestBuilders.put("/api/assessment-types")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(assessmentType)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(equalTo(DEFAULT_CODE)))
        .andExpect(jsonPath("$.label").value(equalTo(DEFAULT_LABEL)));

    verify(assessmentTypeRepositoryMock).save(assessmentType);
  }

  @Test
  @Transactional
  public void updateAssessmentTypeShouldReturnCreateAssessmentTypeWhenItDoesntExist() throws Exception {
    when(assessmentTypeRepositoryMock.findOne(assessmentType.getCode())).thenReturn(null);
    when(assessmentTypeRepositoryMock.save(assessmentType)).thenReturn(assessmentType);

    testObjMockMvc.perform(MockMvcRequestBuilders.put("/api/assessment-types")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(assessmentType)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.code").value(equalTo(DEFAULT_CODE)))
        .andExpect(jsonPath("$.label").value(equalTo(DEFAULT_LABEL)));

    verify(assessmentTypeRepositoryMock).save(assessmentType);
  }

  @Test
  @Transactional
  public void updateAssessmentTypeWithNoCodeShouldShouldReturnBadRequest() throws Exception {
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
    assessmentType2.setCode("ABC");
    assessmentType2.setLabel("ABC_123");

    List<AssessmentType> allAssessmentTypes = Lists.newArrayList(assessmentType1, assessmentType2);
    Page<AssessmentType> pageResults = new PageImpl<>(allAssessmentTypes);
    when(assessmentTypeRepositoryMock.findAll(pageableArgumentCaptor.capture())).thenReturn(pageResults);

    testObjMockMvc.perform(get("/api/assessment-types?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].code").value(hasItems(DEFAULT_CODE, "ABC")))
        .andExpect(jsonPath("$.[*].label").value(hasItems(DEFAULT_LABEL, "ABC_123")));

    Pageable captorValue = pageableArgumentCaptor.getValue();
    Sort expectedSort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
    Assert.assertEquals(expectedSort, captorValue.getSort());
  }

  @Test
  @Transactional
  public void getAssessmentTypesForSearchQuery() throws Exception {
    AssessmentType unencodedAssessmentType = new AssessmentType()
        .name(UNENCODED_LABEL);
    unencodedAssessmentType.setCode(UNENCODED_CODE);
    
    List<AssessmentType> allAssessmentTypes = Lists.newArrayList(unencodedAssessmentType);
    Page<AssessmentType> pageResults = new PageImpl<>(allAssessmentTypes);
    when(assessmentTypeServiceMock.advancedSearch(eq(UNENCODED_LABEL), any(), any())).thenReturn(pageResults);

    testObjMockMvc.perform(get("/api/assessment-types?searchQuery=%22Te%24t%20Assessment%22&sort=id,desc"))
    .andExpect(status().isOk())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
    .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getAssessmentTypeByCodeShouldReturnSingleAssessmentType() throws Exception {
    when(assessmentTypeRepositoryMock.findOne(assessmentType.getCode())).thenReturn(assessmentType);

    testObjMockMvc.perform(get("/api/assessment-types/{code}", assessmentType.getCode()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));

    verify(assessmentTypeRepositoryMock).findOne(assessmentType.getCode());
  }

  @Test
  @Transactional
  public void getAssessmentTypeByCodeShouldReturn404() throws Exception {
    when(assessmentTypeRepositoryMock.findOne(assessmentType.getCode())).thenReturn(null);

    testObjMockMvc.perform(get("/api/assessment-types/{code}", assessmentType.getCode()))
        .andExpect(status().isNotFound());

    verify(assessmentTypeRepositoryMock).findOne(assessmentType.getCode());
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(AssessmentType.class);
  }
}
