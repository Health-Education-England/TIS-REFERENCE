package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.Site;
import com.transformuk.hee.tis.reference.service.model.Trust;
import com.transformuk.hee.tis.reference.service.repository.SiteRepository;
import com.transformuk.hee.tis.reference.service.repository.TrustRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class SitesTrustsServiceTest {

  public static final String SEARCH_STRING = "search me";
  public static final Pageable LIMIT = new PageRequest(0, 100);
  public static final List<Site> EMPTY_SITE_LIST = new ArrayList<>();
  public static final List<Trust> EMPTY_TRUST_LIST = new ArrayList<>();
  public static final String TRUST_CODE = "trust code";

  @Mock
  private SiteRepository siteRepository;

  @Mock
  private TrustRepository trustRepository;

  @InjectMocks
  private SitesTrustsService service =
      new SitesTrustsService(siteRepository, trustRepository, 100);

  @Test
  public void shouldSearchSites() {
    // given
    Page<Site> emptyPage = new PageImpl<>(EMPTY_SITE_LIST);
    given(siteRepository.findBySearchString(SEARCH_STRING, LIMIT)).willReturn(emptyPage);

    // when
    List<Site> sites = service.searchSites(SEARCH_STRING);

    // then
    assertEquals(EMPTY_SITE_LIST, sites);
  }

  @Test
  public void shouldReturnAllSitesIfSearchStringIsNullOrEmpty() {
    // given
    given(siteRepository.findAll()).willReturn(EMPTY_SITE_LIST);

    // when
    List<Site> sites = service.searchSites("");

    // then
    assertEquals(EMPTY_SITE_LIST, sites);
  }

  @Test
  public void shouldSearchSitesWithInATrust() {
    // given
    given(siteRepository.findBySearchStringAndTrustCode(SEARCH_STRING, TRUST_CODE, LIMIT)).willReturn(EMPTY_SITE_LIST);

    // when
    List<Site> sites = service.searchSitesWithinTrust(TRUST_CODE, SEARCH_STRING);

    // then
    assertEquals(EMPTY_SITE_LIST, sites);
  }

  @Test
  public void shouldSearchSitesWithInATrustForEmptyOrNullSearchString() {
    // given
    given(siteRepository.findBySearchStringAndTrustCode("", TRUST_CODE, LIMIT)).willReturn(EMPTY_SITE_LIST);

    // when
    List<Site> sites = service.searchSitesWithinTrust(TRUST_CODE, "");

    // then
    assertEquals(EMPTY_SITE_LIST, sites);
  }

  @Test
  public void shouldGetTrustWithGivenCode() {
    // given
    Trust trust = new Trust();
    given(trustRepository.findByCode(TRUST_CODE)).willReturn(trust);

    // when
    Trust actualTrust = service.getTrustByCode(TRUST_CODE);

    // then
    assertEquals(trust, actualTrust);
  }

  @Test
  public void shouldSearchTrusts() {
    // given
    Page<Trust> emptyPage = new PageImpl<>(EMPTY_TRUST_LIST);
    given(trustRepository.findBySearchString(SEARCH_STRING, LIMIT)).willReturn(emptyPage);

    // when
    List<Trust> trusts = service.searchTrusts(SEARCH_STRING);

    // then
    assertEquals(EMPTY_SITE_LIST, trusts);
  }

  @Test
  public void shouldReturnAllTrustsIfSearchStringIsNullOrEmpty() {
    // given
    Page<Trust> emptyPage = new PageImpl<>(EMPTY_TRUST_LIST);
    given(trustRepository.findBySearchString("", LIMIT)).willReturn(emptyPage);

    // when
    List<Trust> trusts = service.searchTrusts("");

    // then
    assertEquals(EMPTY_SITE_LIST, trusts);
  }
}
