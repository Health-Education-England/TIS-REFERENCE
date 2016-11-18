package com.transformuk.hee.tis.reference.service;

import com.transformuk.hee.tis.reference.model.Site;
import com.transformuk.hee.tis.reference.model.Trust;
import com.transformuk.hee.tis.reference.repository.GradeRepository;
import com.transformuk.hee.tis.reference.repository.SiteRepository;
import com.transformuk.hee.tis.reference.repository.TrustRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.util.Lists.emptyList;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceDataServiceTest {

	public static final String SEARCH_STRING = "search me";
	public static final Pageable LIMIT = new PageRequest(0, 100);
	public static final List<Site> EMPTY_LIST = new ArrayList<>();
	public static final String TRUST_CODE = "trust code";
	@Mock
	private GradeRepository gradeRepository;

	@Mock
	private SiteRepository siteRepository;

	@Mock
	private TrustRepository trustRepository;

	@InjectMocks
	private ReferenceDataService service =
			new ReferenceDataService(gradeRepository, siteRepository, trustRepository, 100);

	@Test
	public void shouldGetAllGrades() {
		// given

		// when
		service.getAllGrades();

		// then
		verify(gradeRepository).findAll();
	}

	@Test
	public void shouldSearchSites() {
		// given
		given(siteRepository.findBySearchString(SEARCH_STRING, LIMIT)).willReturn(EMPTY_LIST);

		// when
		List<Site> sites = service.searchSites(SEARCH_STRING);

		// then
		assertEquals(EMPTY_LIST, sites);
	}

	@Test
	public void shouldReturnAllSitesIfSearchStringIsNullOrEmpty() {
		// given
		given(siteRepository.findAll()).willReturn(EMPTY_LIST);

		// when
		List<Site> sites = service.searchSites("");

		// then
		assertEquals(EMPTY_LIST, sites);
	}

	@Test
	public void shouldSearchSitesWithInATrust() {
		// given
		given(siteRepository.findBySearchStringAndTrustCode(SEARCH_STRING, TRUST_CODE, LIMIT)).willReturn(EMPTY_LIST);

		// when
		List<Site> sites = service.searchSitesWithinTrust(TRUST_CODE, SEARCH_STRING);

		// then
		assertEquals(EMPTY_LIST, sites);
	}

	@Test
	public void shouldSearchSitesWithInATrustForEmptyOrNullSearchString() {
		// given
		given(siteRepository.findBySearchStringAndTrustCode("", TRUST_CODE, LIMIT)).willReturn(EMPTY_LIST);

		// when
		List<Site> sites = service.searchSitesWithinTrust(TRUST_CODE, "");

		// then
		assertEquals(EMPTY_LIST, sites);
	}

	@Test
	public void shouldGetTrustWithGivenCode() {
		// given
		Trust trust = new Trust();
		given(trustRepository.findOne(TRUST_CODE)).willReturn(trust);

		// when
		Trust actualTrust = service.getTrustWithCode(TRUST_CODE);

		// then
		assertEquals(trust, actualTrust);
	}

	@Test
	public void shouldSearchTrusts() {
		// given
		given(trustRepository.findBySearchString(SEARCH_STRING, LIMIT)).willReturn(emptyList());

		// when
		List<Trust> trusts = service.searchTrusts(SEARCH_STRING);

		// then
		assertEquals(EMPTY_LIST, trusts);
	}

	@Test
	public void shouldReturnAllTrustsIfSearchStringIsNullOrEmpty() {
		// given
		given(trustRepository.findBySearchString("", LIMIT)).willReturn(emptyList());

		// when
		List<Trust> trusts = service.searchTrusts("");

		// then
		assertEquals(EMPTY_LIST, trusts);
	}
}
