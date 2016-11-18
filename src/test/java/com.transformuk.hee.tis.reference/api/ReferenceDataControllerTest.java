package com.transformuk.hee.tis.reference.api;

import com.transformuk.hee.tis.reference.service.ReferenceDataService;
import com.transformuk.hee.tis.reference.exception.ExceptionHandlerController;
import com.transformuk.hee.tis.reference.model.Grade;
import com.transformuk.hee.tis.reference.model.Site;
import com.transformuk.hee.tis.reference.model.Trust;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ReferenceDataControllerTest {

	public static final String DBC = "DBC";
	public static final String USER_ID = "James H";
	public static final String SEARCH_STRING = "search me";
	public static final String TRUST_CODE = "TS007";
	public static final String TRUST_NAME = "NHS Staffordshire";

	@Mock
	private ReferenceDataService service;

	@InjectMocks
	private ReferenceDataController controller = new ReferenceDataController(service, 100);
	private MockMvc mockMvc;

	@Before
	public void setup() {
		initMocks(this);
		mockMvc = standaloneSetup(controller).setControllerAdvice(new ExceptionHandlerController()).build();
		TestUtils.mockUserprofile(USER_ID, DBC);
	}

	@Test
	public void shouldGetAllGrades() throws Exception {
		// given
		Grade grade = new Grade();
		grade.setAbbreviation("abbr");
		grade.setLabel("label");
		grade.setPlacementGrade(true);
		grade.setPostGrade(false);
		grade.setTrainingGrade(true);
		given(service.getAllGrades()).willReturn(newArrayList(grade));


		// when & then
		mockMvc.perform(get("/api/grades")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(content()
						.string("[{\"label\":\"label\",\"abbreviation\":\"abbr\",\"trainingGrade\":true,\"postGrade\":false,\"placementGrade\":true}]"));
	}

	@Test
	public void shouldSearchSites() throws Exception {
		// given
		Site site = new Site();
		site.setAddress("www.blah");
		site.setPostCode("IG11 6TY");
		site.setSiteCode("ST1");
		site.setSiteName("Middle england");
		site.setTrustCode("TS007");
		given(service.searchSites(SEARCH_STRING)).willReturn(newArrayList(site));


		// when & then
		mockMvc.perform(get("/api/sites")
				.param("searchString", "search me")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(content()
						.string("{\"total\":1,\"messageCode\":null,\"list\":[{\"siteCode\":\"ST1\",\"trustCode\":\"TS007\",\"siteName\":\"Middle england\",\"address\":\"www.blah\",\"postCode\":\"IG11 6TY\"}]}"));
	}

	@Test
	public void shouldSearchSitesWithInATrustCode() throws Exception {
		// given
		Site site = new Site();
		site.setAddress("www.blah");
		site.setPostCode("IG11 6TY");
		site.setSiteCode("ST1");
		site.setSiteName("Middle england");
		site.setTrustCode(TRUST_CODE);
		given(service.searchSitesWithinTrust(TRUST_CODE, SEARCH_STRING)).willReturn(newArrayList(site));


		// when & then
		mockMvc.perform(get("/api/sites/" + TRUST_CODE)
				.param("searchString", "search me")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(content()
						.string("{\"total\":1,\"messageCode\":null,\"list\":[{\"siteCode\":\"ST1\",\"trustCode\":\"TS007\",\"siteName\":\"Middle england\",\"address\":\"www.blah\",\"postCode\":\"IG11 6TY\"}]}"));
	}

	@Test
	public void shouldGetTrustByCode() throws Exception {
		// given
		Trust trust = new Trust();
		trust.setCode(TRUST_CODE);
		trust.setName(TRUST_NAME);
		given(service.getTrustWithCode(TRUST_CODE)).willReturn(trust);


		// when & then
		mockMvc.perform(get("/api/trusts/" + TRUST_CODE)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(content()
						.string("{\"code\":\"TS007\",\"name\":\"NHS Staffordshire\"}"));
	}

	@Test
	public void shouldSearchTrustsBySearchString() throws Exception {
		// given
		Trust trust = new Trust();
		trust.setCode(TRUST_CODE);
		trust.setName(TRUST_NAME);
		given(service.searchTrusts(SEARCH_STRING)).willReturn(newArrayList(trust));


		// when & then
		mockMvc.perform(get("/api/trusts")
				.param("searchString", SEARCH_STRING)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(content()
						.string("{\"total\":1,\"messageCode\":null,\"list\":[{\"code\":\"TS007\",\"name\":\"NHS Staffordshire\"}]}"));
	}
}
