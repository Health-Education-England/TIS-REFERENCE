package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.OrganisationalEntity;
import com.transformuk.hee.tis.reference.service.repository.OrganisationalEntityRepository;
import com.transformuk.hee.tis.reference.service.service.impl.OrganisationalEntityServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.OrganisationalEntityMapper;
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

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Test class for the OrganisationalEntityResource REST controller.
 *
 * @see OrganisationalEntityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class OrganisationalEntityResourceIntTest {

    private static final String DEFAULT_NAME = "HEE";

    @Autowired
    private OrganisationalEntityRepository organisationalEntityRepository;

    @Autowired
    private OrganisationalEntityMapper organisationalEntityMapper;

    @Autowired
    private OrganisationalEntityServiceImpl organisationalEntityService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restOrganisationalEntityMockMvc;

    private OrganisationalEntity organisationalEntity;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it, if they test an entity
     * which requires the current entity.
     */
    public static OrganisationalEntity createEntity() {
        OrganisationalEntity organisationalEntity = new OrganisationalEntity()
                .name(DEFAULT_NAME);
        return organisationalEntity;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrganisationalEntityResource organisationalEntityResource = new OrganisationalEntityResource(organisationalEntityRepository, organisationalEntityMapper, organisationalEntityService);
        this.restOrganisationalEntityMockMvc = MockMvcBuilders.standaloneSetup(organisationalEntityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
        organisationalEntity = createEntity();
        organisationalEntityRepository.deleteAllInBatch();
    }

    @Test
    @Transactional
    public void getAllOrganisationalEntities() throws Exception {
        // Initialize the database
        organisationalEntityRepository.saveAndFlush(organisationalEntity);

        // Get all the organisationalEntityList
        restOrganisationalEntityMockMvc.perform(get("/api/organisational-entities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisationalEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getOrganisationalEntity() throws Exception {
        // Initialize the database
        organisationalEntityRepository.saveAndFlush(organisationalEntity);

        // Get the organisationalEntity
        restOrganisationalEntityMockMvc.perform(get("/api/organisational-entities/{id}", organisationalEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(organisationalEntity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }
}
