package com.transformuk.hee.tis.reference.service.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.model.LocalOffice;
import com.transformuk.hee.tis.reference.service.model.Site;
import com.transformuk.hee.tis.reference.service.model.Trust;
import com.transformuk.hee.tis.reference.service.repository.LocalOfficeRepository;
import com.transformuk.hee.tis.reference.service.repository.SiteRepository;
import com.transformuk.hee.tis.reference.service.repository.TrustRepository;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class SitesTrustsServiceTest {

  public static final String SEARCH_STRING = "search me";
  public static final Pageable LIMIT = PageRequest.of(0, 100);
  public static final List<Site> EMPTY_SITE_LIST = new ArrayList<>();
  public static final List<Trust> EMPTY_TRUST_LIST = new ArrayList<>();
  public static final String TRUST_CODE = "trust code";
  public static final String LOCAL_OFFICE_ABBREVIATION = "abbreviation";
  public static final List<LocalOffice> EMPTY_LO_LIST = new ArrayList<>();

  @Mock
  private SiteRepository siteRepository;

  @Mock
  private TrustRepository trustRepository;

  @Mock
  private LocalOfficeRepository localOfficeRepository;

  @InjectMocks
  private SitesTrustsService service =
      new SitesTrustsService(siteRepository, trustRepository, localOfficeRepository, 100);

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
    given(siteRepository.findBySearchStringAndTrustCode(TRUST_CODE, SEARCH_STRING, LIMIT))
        .willReturn(EMPTY_SITE_LIST);

    // when
    List<Site> sites = service.searchSitesWithinTrust(TRUST_CODE, SEARCH_STRING);

    // then
    assertEquals(EMPTY_SITE_LIST, sites);
  }

  @Test
  public void shouldSearchSitesWithInATrustForEmptyOrNullSearchString() {
    // given
    given(siteRepository.findByTrustCode(TRUST_CODE, LIMIT)).willReturn(EMPTY_SITE_LIST);

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
  public void shouldGetTrustsWithGivenCodeAndStatus() {
    // given
    Trust trust = new Trust();
    given(trustRepository.findByCodeAndStatus(TRUST_CODE, Status.CURRENT)).willReturn(
        Lists.newArrayList(trust));

    // when
    List<Trust> actualTrusts = service.getTrustsByCodeAndStatus(TRUST_CODE, Status.CURRENT);

    // then
    assertEquals(trust, actualTrusts.get(0));
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
    // when
    List<Trust> trusts = service.searchTrusts("");

    // then
    assertEquals(EMPTY_SITE_LIST, trusts);
    verify(trustRepository).findAll();
  }

  @Test
  public void shouldGetLocalOfficeWithGivenAbbreviation() {
    // given
    LocalOffice localOffice = new LocalOffice();
    given(localOfficeRepository.findByAbbreviation(LOCAL_OFFICE_ABBREVIATION))
        .willReturn(localOffice);

    // when
    LocalOffice actualLocalOffice = service.getLocalOfficeByAbbreviation(LOCAL_OFFICE_ABBREVIATION);

    // then
    assertEquals(localOffice, actualLocalOffice);
  }

  @Test
  public void shouldSearchLocalOffices() {
    // given
    Page<LocalOffice> emptyPage = new PageImpl<>(EMPTY_LO_LIST);
    given(localOfficeRepository.findBySearchString(SEARCH_STRING, LIMIT)).willReturn(emptyPage);

    // when
    List<LocalOffice> localOffices = service.searchLocalOffices(SEARCH_STRING);

    // then
    assertEquals(EMPTY_LO_LIST, localOffices);
  }

  @Test
  public void shouldReturnAllLocalOfficesIfSearchStringIsNullOrEmpty() {
    // when
    List<LocalOffice> localOffices = service.searchLocalOffices("");

    // then
    assertEquals(EMPTY_LO_LIST, localOffices);
    verify(localOfficeRepository).findAll();
  }
}
