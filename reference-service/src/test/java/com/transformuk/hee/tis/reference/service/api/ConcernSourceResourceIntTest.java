package com.transformuk.hee.tis.reference.service.api;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.model.ConcernSource;
import com.transformuk.hee.tis.reference.service.repository.ConcernSourceRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.ConcernSourceMapper;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ConcernSourceResourceIntTest {

  private static final String DEFAULT_SOURCE_NAME = "HET";

  @Autowired
  private ConcernSourceRepository concernSourceRepository;

  @Autowired
  private ConcernSourceMapper concernSourceMapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;

  @Autowired
  private EntityManager entityManager;

  private MockMvc restConcernSourceMockMvc;

  private ConcernSource concernSource;

  public static ConcernSource createEntity(EntityManager entityManager) {
    ConcernSource concernSource = new ConcernSource();
    concernSource.setName(DEFAULT_SOURCE_NAME);
    return concernSource;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ConcernSourceResource concernSourceResource = new ConcernSourceResource(
        concernSourceRepository, concernSourceMapper);
    this.restConcernSourceMockMvc = MockMvcBuilders.standaloneSetup(concernSourceResource)
        .setMessageConverters(jackson2HttpMessageConverter).build();
  }

  @Before
  public void initTest() {
    concernSource = createEntity(entityManager);
  }

  @Test
  public void getAllSources() throws Exception {
    concernSourceRepository.saveAndFlush(concernSource);

    restConcernSourceMockMvc.perform(get("/api/sources"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(concernSource.getId().intValue())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_SOURCE_NAME)));
  }

}
