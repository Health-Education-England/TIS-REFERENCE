package com.transformuk.hee.tis.reference.service.api;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.model.ConcernType;
import com.transformuk.hee.tis.reference.service.repository.ConcernTypeRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.ConcernTypeMapper;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ConcernTypeResourceIntTest {

  private static final String DEFAULT_TYPE_CODE = "DEFAULT_TYPE_CODE";
  private static final String DEFAULT_TYPE_LABEL = "DEFAULT_TYPE_LABEL";


  @Autowired
  private ConcernTypeRepository concernTypeRepository;

  @Autowired
  private ConcernTypeMapper concernTypeMapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;

  @Autowired
  private EntityManager entityManager;

  private MockMvc restConcernTypeMockMvc;

  private ConcernType concernType;

  public static ConcernType createEntity(EntityManager entityManager) {
    ConcernType concernType = new ConcernType();
    concernType.setCode(DEFAULT_TYPE_CODE);
    concernType.setLabel(DEFAULT_TYPE_LABEL);
    return concernType;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ConcernTypeResource concernTypeResource = new ConcernTypeResource(
        concernTypeRepository, concernTypeMapper);
    this.restConcernTypeMockMvc = MockMvcBuilders.standaloneSetup(concernTypeResource)
        .setMessageConverters(jackson2HttpMessageConverter).build();
  }

  @Before
  public void initTest() {
    concernType = createEntity(entityManager);
  }

  @Test
  public void getAllTypes() throws Exception {
    concernTypeRepository.saveAndFlush(concernType);

    restConcernTypeMockMvc.perform(get("/api/concern-types"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(concernType.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_TYPE_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_TYPE_LABEL.toString())));
  }
}
