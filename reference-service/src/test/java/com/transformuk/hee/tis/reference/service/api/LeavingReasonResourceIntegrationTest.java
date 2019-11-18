package com.transformuk.hee.tis.reference.service.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.model.LeavingReason;
import com.transformuk.hee.tis.reference.service.repository.LeavingReasonRepository;
import com.transformuk.hee.tis.reference.service.service.LeavingReasonService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * The integration tests for {@link LeavingReasonResource}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class LeavingReasonResourceIntegrationTest {

  @Autowired
  private LeavingReasonRepository repository;

  @Autowired
  LeavingReasonService service;

  private MockMvc mockMvc;

  @Before
  public void setUp() {
    LeavingReasonResource resource = new LeavingReasonResource(service);
    mockMvc = MockMvcBuilders.standaloneSetup(resource).build();
  }

  /**
   * Test that an empty array is returned when there are no leaving reasons.
   */
  @Test
  public void testGetAllLeavingReasons_noLeavingReasons_emptyList() throws Exception {
    // Set up test data.
    repository.deleteAll();

    // Call the code under test and perform assertions.
    mockMvc.perform(get("/api/leaving-reasons"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }

  /**
   * Test that the CURRENT leaving reasons are returned when there are CURRENT leaving reasons.
   */
  @Test
  public void testGetAllLeavingReasons_currentLeavingReasons_currentLeavingReasons()
      throws Exception {
    // Set up test data.
    long leavingReasonCount = repository.count();
    repository.save(createLeavingReason("code one", "label one", Status.CURRENT));
    repository.save(createLeavingReason("code two", "label two", Status.CURRENT));

    // Call the code under test and perform assertions.
    mockMvc.perform(get("/api/leaving-reasons"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.length()").value(leavingReasonCount + 2))
        .andExpect(jsonPath(
            "$.[?(@.code == \"code one\" && @.label == \"label one\" && @.status == \"CURRENT\")]")
            .exists())
        .andExpect(jsonPath(
            "$.[?(@.code == \"code two\" && @.label == \"label two\" && @.status == \"CURRENT\")]")
            .exists());
  }

  /**
   * Test that the INACTIVE leaving reasons are returned when there are INACTIVE leaving reasons.
   */
  @Test
  public void testGetAllLeavingReasons_inactiveLeavingReasons_inactiveLeavingReasons()
      throws Exception {
    // Set up test data.
    long leavingReasonCount = repository.count();
    repository.save(createLeavingReason("code one", "label one", Status.INACTIVE));
    repository.save(createLeavingReason("code two", "label two", Status.INACTIVE));

    // Call the code under test and perform assertions.
    mockMvc.perform(get("/api/leaving-reasons"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.length()").value(leavingReasonCount + 2))
        .andExpect(jsonPath(
            "$.[?(@.code == \"code one\" && @.label == \"label one\" && @.status == \"INACTIVE\")]")
            .exists())
        .andExpect(jsonPath(
            "$.[?(@.code == \"code two\" && @.label == \"label two\" && @.status == \"INACTIVE\")]")
            .exists());
  }

  /**
   * Test that both CURRENT and INACTIVE leaving reasons are returned when there are CURRENT and
   * INACTIVE leaving reasons.
   */
  @Test
  public void testGetAllLeavingReasons_mixedStatusLeavingReasons_mixedStatusLeavingReasons()
      throws Exception {
    // Set up test data.
    long leavingReasonCount = repository.count();
    repository.save(createLeavingReason("code one", "label one", Status.CURRENT));
    repository.save(createLeavingReason("code two", "label two", Status.INACTIVE));

    // Call the code under test and perform assertions.
    mockMvc.perform(get("/api/leaving-reasons"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.length()").value(leavingReasonCount + 2))
        .andExpect(jsonPath(
            "$.[?(@.code == \"code one\" && @.label == \"label one\" && @.status == \"CURRENT\")]")
            .exists())
        .andExpect(jsonPath(
            "$.[?(@.code == \"code two\" && @.label == \"label two\" && @.status == \"INACTIVE\")]")
            .exists());
  }

  /**
   * Test that a 404 Not Found status is returned when no leaving reason with the given ID exists.
   */
  @Test
  public void testDeleteLeavingReason_idNotExists_notFound() throws Exception {
    // Set up test data.
    repository.deleteAll();

    // Call the code under test and perform assertions.
    mockMvc.perform(delete("/api/leaving-reasons/1"))
        .andExpect(status().isNotFound());
  }

  /**
   * Test that the leaving reason is deleted when a leaving reason with the given ID exists.
   */
  @Test
  public void testDeleteLeavingReason_idExists_leavingReason() throws Exception {
    // Set up test data.
    LeavingReason leavingReason = createLeavingReason("code one", "label one", Status.CURRENT);
    leavingReason = repository.save(leavingReason);
    Long id = leavingReason.getId();

    long preDeleteCount = repository.count();

    // Call the code under test and perform assertions.
    mockMvc.perform(delete("/api/leaving-reasons/" + id))
        .andExpect(status().isOk())
        .andExpect(header().string("X-referenceApp-alert", "referenceApp.LeavingReason.deleted"))
        .andExpect(header().string("X-referenceApp-params", id.toString()));

    long postDeleteCount = repository.count();
    assertThat("The number of leaving reasons did not match the expected value.", postDeleteCount,
        is(preDeleteCount - 1));

    leavingReason = repository.findOne(id);
    assertThat("The leaving reason did not match the expected value.", leavingReason, nullValue());
  }

  /**
   * Create a {@link LeavingReason}.
   *
   * @param code   The code to set.
   * @param label  The label to set.
   * @param status The status to set.
   * @return The created, non-persisted, {@code LeavingReason}.
   */
  private static LeavingReason createLeavingReason(String code, String label, Status status) {
    LeavingReason leavingReason = new LeavingReason();
    leavingReason.setCode(code);
    leavingReason.setLabel(label);
    leavingReason.setStatus(status);

    return leavingReason;
  }
}
