package com.transformuk.hee.tis.reference.service.api;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.model.RoleCategory;
import com.transformuk.hee.tis.reference.service.repository.RoleCategoryRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.RoleCategoryMapper;
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

/**
 * Test class for the RoleResource REST controller.
 *
 * @see RoleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RoleCategoryResourceIntTest {

  @Autowired
  private RoleCategoryRepository repository;

  @Autowired
  private RoleCategoryMapper mapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.openMocks(this);
    RoleCategoryResource resource = new RoleCategoryResource(repository, mapper);
    this.mockMvc = MockMvcBuilders.standaloneSetup(resource)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Test
  @Transactional
  public void shouldGetAllRoleCategories() throws Exception {
    // Initialize the database
    RoleCategory roleCategory = new RoleCategory();
    roleCategory.setName("categoryName");
    repository.saveAndFlush(roleCategory);

    // Get all the roleList
    mockMvc.perform(get("/api/role-categories"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(roleCategory.getId().intValue())))
        .andExpect(jsonPath("$.[*].name").value(hasItem("categoryName")));
  }
}
