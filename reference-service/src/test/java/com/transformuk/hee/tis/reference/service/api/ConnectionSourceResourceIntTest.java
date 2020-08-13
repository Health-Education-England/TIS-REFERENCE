package com.transformuk.hee.tis.reference.service.api;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.model.ConnectionSource;
import com.transformuk.hee.tis.reference.service.repository.ConnectionSourceRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.ConnectionSourceMapper;
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
public class ConnectionSourceResourceIntTest {

  private static final String DEFAULT_SOURCE_NAME = "HET";

  @Autowired
  private ConnectionSourceRepository connectionSourceRepository;

  @Autowired
  private ConnectionSourceMapper connectionSourceMapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;

  @Autowired
  private EntityManager entityManager;

  private MockMvc restConnectionSourceMockMvc;

  private ConnectionSource connectionSource;

  public static ConnectionSource createEntity(EntityManager entityManager) {
    ConnectionSource connectionSource = new ConnectionSource();
    connectionSource.setName(DEFAULT_SOURCE_NAME);
    return connectionSource;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ConnectionSourceResource connectionSourceResource = new ConnectionSourceResource(
        connectionSourceRepository, connectionSourceMapper);
    this.restConnectionSourceMockMvc = MockMvcBuilders.standaloneSetup(connectionSourceResource)
        .setMessageConverters(jackson2HttpMessageConverter).build();
  }

  @Before
  public void initTest() {
    connectionSource = createEntity(entityManager);
  }

  @Test
  public void getAllSources() throws Exception {
    connectionSourceRepository.saveAndFlush(connectionSource);

    restConnectionSourceMockMvc.perform(get("/api/sources"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(connectionSource.getId().intValue())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_SOURCE_NAME)));
  }

}
